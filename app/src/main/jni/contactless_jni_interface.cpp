#include <fcntl.h>

#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <semaphore.h>
#include <errno.h>
#include <dlfcn.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <jni.h>
#include "hal_sys_log.h"
#include "contactless_jni_interface.h"
#include "contactless_card_interface.h"
#include "contactless_card_event.h"
#include "event_queue.h"

typedef struct contactless_card_hal_interface
{
	contactless_card_open 					open;
	contactless_card_close 					close;
	contactless_card_search_target_begin	search_target_begin;
	contactless_card_search_target_end		search_target_end;
	contactless_card_attach_target			attach_target;
	contactless_card_detach_target			detach_target;
	contactless_card_transmit				transmit;
	contactless_card_send_control_command	send_control_command;

	CEventQueue<CContactlessCardEvent>* pEventQueue;

	int pCardHandle;
	void* pSoHandle;
	//sem_t sem;
}CONTACTLESS_CARD_HAL_INSTANCE;

static CONTACTLESS_CARD_HAL_INSTANCE* g_pContactlessCard = NULL;

static void contactless_card_callback(void* pUserData, int nEvent, unsigned char* pEventData, int nEventDataLength)
{
	int nResult = -1;
	CONTACTLESS_CARD_HAL_INSTANCE* pContactlessCard = (CONTACTLESS_CARD_HAL_INSTANCE*)pUserData;
	CContactlessCardEvent event(nEvent, pEventData, nEventDataLength);
	nResult = pContactlessCard->pEventQueue->push_back(event);
	return;
}

int native_contactless_card_init (JNIEnv * env, jclass obj)
{
	void* pSoHandle = NULL;
	void* pCardHandle = NULL;
	int nErrorCode = -1;

	if(g_pContactlessCard == NULL)
	{
		pSoHandle = dlopen("libwizarposHAL.so", RTLD_LAZY);
		if (!pSoHandle)
		{
			hal_sys_error("can't find libhal_contactless_card.so, error is : %s\n", dlerror());
		    return -1;
		}

		g_pContactlessCard = new CONTACTLESS_CARD_HAL_INSTANCE();
		g_pContactlessCard->pEventQueue = new CEventQueue<CContactlessCardEvent>();

		g_pContactlessCard->open = (contactless_card_open)dlsym(pSoHandle, "contactless_card_open");
		if(g_pContactlessCard->open == NULL)
		{
			hal_sys_error("can't open contactless_card_open.\n");
			goto card_open_clean;
		}

		g_pContactlessCard->close = (contactless_card_close)dlsym(pSoHandle, "contactless_card_close");
		if(g_pContactlessCard->close == NULL)
		{
			hal_sys_error("can't open contactless_card_close.\n");
			goto card_open_clean;
		}

		g_pContactlessCard->search_target_begin = (contactless_card_search_target_begin)dlsym(pSoHandle, "contactless_card_search_target_begin");
		if(g_pContactlessCard->search_target_begin == NULL)
		{
			hal_sys_error("can't open contactless_card_search_target_begin.\n");
			goto card_open_clean;
		}

		g_pContactlessCard->search_target_end = (contactless_card_search_target_end)dlsym(pSoHandle, "contactless_card_search_target_end");
		if(g_pContactlessCard->search_target_end == NULL)
		{
			hal_sys_error("can't open contactless_card_search_target_end.\n");
			goto card_open_clean;
		}

		g_pContactlessCard->attach_target = (contactless_card_attach_target)dlsym(pSoHandle, "contactless_card_attach_target");
		if(g_pContactlessCard->attach_target == NULL)
		{
			hal_sys_error("can't open contactless_card_attach_target.\n");
			goto card_open_clean;
		}

		g_pContactlessCard->detach_target = (contactless_card_detach_target)dlsym(pSoHandle, "contactless_card_detach_target");
		if(g_pContactlessCard->detach_target == NULL)
		{
			hal_sys_error("can't open contactless_card_detach_target.\n");
			goto card_open_clean;
		}

		g_pContactlessCard->transmit = (contactless_card_transmit)dlsym(pSoHandle, "contactless_card_transmit");
		if(g_pContactlessCard->transmit == NULL)
		{
			hal_sys_error("can't open contactless_card_transmit.\n");
			goto card_open_clean;
		}

		g_pContactlessCard->send_control_command = (contactless_card_send_control_command)dlsym(pSoHandle, "contactless_card_send_control_command");
		if(g_pContactlessCard->send_control_command == NULL)
		{
			hal_sys_error("can't open contactless_card_send_control_command.\n");
			goto card_open_clean;
		}

		g_pContactlessCard->pSoHandle = pSoHandle;

	}

	return 0;
card_open_clean:
	if(g_pContactlessCard)
	{
		if(g_pContactlessCard->pEventQueue)
			delete g_pContactlessCard->pEventQueue;
		delete g_pContactlessCard;
		g_pContactlessCard = NULL;
	}
	return -1;
}

int native_contactless_card_open (JNIEnv * env, jclass obj)
{
	int nErrorCode = -1;
	void* pCardHandle = NULL;
	pCardHandle = g_pContactlessCard->open(contactless_card_callback, g_pContactlessCard, &nErrorCode);
	if(pCardHandle == NULL || nErrorCode < 0)
		return -1;
	g_pContactlessCard->pCardHandle = (int)pCardHandle;
	return g_pContactlessCard->pCardHandle;

}

int native_contactless_card_close (JNIEnv * env, jclass obj)
{
	if(g_pContactlessCard == NULL)
		return -1;
	g_pContactlessCard->close(g_pContactlessCard->pCardHandle);
	g_pContactlessCard->pEventQueue->queue_clear();
	return 0;
}


int native_contactless_card_search_target_begin(JNIEnv * env, jclass obj, jint nCardMode, jint nFlagSearchAll, jint nTimeout_MS)
{
	if(g_pContactlessCard == NULL)
		return -1;

	return g_pContactlessCard->search_target_begin(g_pContactlessCard->pCardHandle, nCardMode, nFlagSearchAll, nTimeout_MS);
}


int native_contactless_card_search_target_end (JNIEnv * env, jclass obj)
{
	if(g_pContactlessCard == NULL)
		return -1;
	return g_pContactlessCard->search_target_end(g_pContactlessCard->pCardHandle);
}


int native_contactless_card_attach_target (JNIEnv * env, jclass obj, jbyteArray arryATR)
{
	int nResult = -1;
	if(g_pContactlessCard == NULL)
		return -1;
	jbyte* pData = env->GetByteArrayElements(arryATR, NULL);
	jint nATRBufferLength = env->GetArrayLength(arryATR);
	nResult = g_pContactlessCard->attach_target(g_pContactlessCard->pCardHandle, (unsigned char*)pData, nATRBufferLength);
	env->ReleaseByteArrayElements(arryATR, pData, 0);
	return nResult;
}


int native_contactless_card_detach_target (JNIEnv * env, jclass obj)
{
	if(g_pContactlessCard == NULL)
		return -1;
	return g_pContactlessCard->detach_target(g_pContactlessCard->pCardHandle);
}


int native_contactless_card_transmit (JNIEnv * env, jclass obj, jbyteArray arryAPDU, jint nAPDULength, jbyteArray arryResponse)
{
	int nResult = -1;
	if(g_pContactlessCard == NULL)
		return -1;
	jbyte* pData = env->GetByteArrayElements(arryAPDU, NULL);
	jbyte* pResponse = env->GetByteArrayElements(arryResponse, NULL);
	unsigned int nResponseBufferLength = (unsigned int)(env->GetArrayLength(arryResponse));
	nResult = g_pContactlessCard->transmit(g_pContactlessCard->pCardHandle, (unsigned char*)pData, nAPDULength, (unsigned char*)pResponse, &nResponseBufferLength);
	env->ReleaseByteArrayElements(arryAPDU, pData, 0);
	env->ReleaseByteArrayElements(arryResponse, pResponse, 0);
	return nResult >= 0 ? nResponseBufferLength : nResult;
}


int native_contactless_card_send_control_command (JNIEnv * env, jclass obj, jint nCmdID, jbyteArray arryCmdData, jint nDataLength)
{
	int nResult = -1;

	if(g_pContactlessCard == NULL)
		return -1;
	jbyte* pData = env->GetByteArrayElements(arryCmdData, NULL);
	nResult = g_pContactlessCard->send_control_command(g_pContactlessCard->pCardHandle, (unsigned int)nCmdID, (unsigned char*)pData, (unsigned int)nDataLength);
	env->ReleaseByteArrayElements(arryCmdData, pData, 0);
	return nResult;
}


int native_contactless_card_poll_event (JNIEnv * env, jclass obj, jint nTimeout_MS, jobject objEvent)
{
	int nResult = -1;
	CContactlessCardEvent event;
	if(g_pContactlessCard == NULL)
		return -1;
	nResult = g_pContactlessCard->pEventQueue->pop_front(event, nTimeout_MS);

	if(nResult >= 0)
	{
		event.explore();

		jclass clazz = env->GetObjectClass(objEvent);
		if(0 == clazz)
			return -1;

		jfieldID fidEventID = env->GetFieldID(clazz, "nEventID", "I");
		env->SetIntField(objEvent, fidEventID, event.m_nEventID);

		jfieldID fidEventDataLength = env->GetFieldID(clazz, "nEventDataLength", "I");
		env->SetIntField(objEvent, fidEventDataLength, event.m_nEventDataLength);

		jfieldID fidEventData = env->GetFieldID(clazz, "arryEventData", "[B");
		jobject mvdata = env->GetObjectField(objEvent, fidEventData);

		jbyteArray* pArray = reinterpret_cast<jbyteArray*>(&mvdata);
		jbyte* data = env->GetByteArrayElements(*pArray, NULL);
		memcpy(data, event.m_strEventData, event.m_nEventDataLength);
		env->ReleaseByteArrayElements(*pArray, data, 0);
	}
	return nResult;
}

const char* g_pJNIREG_CLASS = "com/mpos/newtwo/wizarpos/jni/ContactlessICCardReaderInterface";

static JNINativeMethod g_Methods[] =
{
	{"init",					"()I",														(void*)native_contactless_card_init},
	{"open",					"()I",														(void*)native_contactless_card_open},
	{"close",					"()I",														(void*)native_contactless_card_close},
	{"pollEvent",				"(ILcom/wizarpos/jni/ContactlessEvent;)I",			        (void*)native_contactless_card_poll_event},
	{"searchTargetBegin",		"(III)I",													(void*)native_contactless_card_search_target_begin},
	{"searchTargetEnd",			"()I",														(void*)native_contactless_card_search_target_end},
	{"attachTarget",			"([B)I",													(void*)native_contactless_card_attach_target},
	{"detachTarget",			"()I",														(void*)native_contactless_card_detach_target},
	{"transmit",				"([BI[B)I",													(void*)native_contactless_card_transmit},
	{"sendControlCommand",		"(I[BI)I",													(void*)native_contactless_card_send_control_command},
};

const char* contactless_get_class_name()
{
	return g_pJNIREG_CLASS;
}

JNINativeMethod* contactless_get_methods(int* pCount)
{
	*pCount = sizeof(g_Methods) /sizeof(g_Methods[0]);
	return g_Methods;
}
