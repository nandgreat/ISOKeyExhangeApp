/*This file is defined for Test JNI interface */

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
#include "smart_card_jni_interface.h"
#include "smart_card_interface.h"

#include "event_queue.h"
#include "smart_card_event.h"


typedef struct smart_card_hal_interface
{
	smart_card_init             init;
	smart_card_terminate        terminate;
	smart_card_query_max_number query_max_number;
	smart_card_query_presence   query_presence;
	smart_card_open             open;
	smart_card_close            close;
	smart_card_power_on         power_on;
	smart_card_power_off        power_off;
	smart_card_set_slot_info    set_slot_info;
	smart_card_transmit         transmit;
	smart_card_mc_read          sle4442_read;
	smart_card_mc_write         sle4442_write;
	smart_card_mc_verify_data   sle4442_verify;

	CEventQueue<CSmartCardEvent>* pEventQueue;

	void* pSoHandle;
}SMART_CARD_HAL_INSTANCE;

static SMART_CARD_HAL_INSTANCE* g_pSmartCardInstance = NULL;


static void smart_card_event_notify(void* pUserData, int nSlotIndex, int nEvent)
{
	int nResult = -1;
	hal_sys_info("nSlotIndex = %d nEvent  = %d\n", nSlotIndex, nEvent);
	SMART_CARD_HAL_INSTANCE* pSmartCardInstance = (SMART_CARD_HAL_INSTANCE*)pUserData;
	CSmartCardEvent event(nEvent, nSlotIndex);
	nResult = pSmartCardInstance->pEventQueue->push_back(event);
	hal_sys_info("push_back return %d", nResult);
	return;
}


int native_smart_card_init (JNIEnv * env, jclass obj)
{

	hal_sys_info("enter native_smart_card_init!");
	int nResult = -1;
	char *pError = NULL;

	if(g_pSmartCardInstance == NULL)
	{
		hal_sys_info("native_smart_card_init!");
		void* pHandle = dlopen("libwizarposHAL.so", RTLD_LAZY);
		if (!pHandle)
		{
			hal_sys_error("%s\n", dlerror());
			return -1;
		}

		g_pSmartCardInstance = new SMART_CARD_HAL_INSTANCE();
		g_pSmartCardInstance->pEventQueue = new CEventQueue<CSmartCardEvent>();

		g_pSmartCardInstance->init = (smart_card_init)dlsym(pHandle, "smart_card_init");
		if(g_pSmartCardInstance->init == NULL)
		{
			hal_sys_info("can't find smart_card_init!");
			goto smart_card_module_init_clean;
		}

		g_pSmartCardInstance->terminate = (smart_card_terminate)dlsym(pHandle, "smart_card_terminate");
		if(g_pSmartCardInstance->terminate == NULL)
		{
			hal_sys_info("can't find smart_card_terminate!");
			goto smart_card_module_init_clean;
		}

		g_pSmartCardInstance->query_max_number = (smart_card_query_max_number)dlsym(pHandle, "smart_card_query_max_number");
		if(g_pSmartCardInstance->query_max_number == NULL)
		{
			hal_sys_info("can't find smart_card_query_max_number!");
			goto smart_card_module_init_clean;
		}

		g_pSmartCardInstance->query_presence = (smart_card_query_presence)dlsym(pHandle, "smart_card_query_presence");
		if(g_pSmartCardInstance->query_presence == NULL)
		{
			hal_sys_info("can't find smart_card_query_presence!");
			goto smart_card_module_init_clean;
		}

		g_pSmartCardInstance->open = (smart_card_open)dlsym(pHandle, "smart_card_open");
		if(g_pSmartCardInstance->open == NULL)
		{
			hal_sys_info("can't find smart_card_open!");
			goto smart_card_module_init_clean;
		}

		g_pSmartCardInstance->close = (smart_card_close)dlsym(pHandle, "smart_card_close");
		if(g_pSmartCardInstance->close == NULL)
		{
			hal_sys_info("can't find smart_card_close!");
			goto smart_card_module_init_clean;
		}

		g_pSmartCardInstance->power_on = (smart_card_power_on)dlsym(pHandle, "smart_card_power_on");
		if(g_pSmartCardInstance->power_on == NULL)
		{
			hal_sys_info("can't find smart_card_power_on!");
			goto smart_card_module_init_clean;
		}

		g_pSmartCardInstance->power_off = (smart_card_power_off)dlsym(pHandle, "smart_card_power_off");
		if(g_pSmartCardInstance->power_off == NULL)
		{
			hal_sys_info("can't find smart_card_power_off!");
			goto smart_card_module_init_clean;
		}

		g_pSmartCardInstance->set_slot_info = (smart_card_set_slot_info)dlsym(pHandle, "smart_card_set_slot_info");
		if(g_pSmartCardInstance->set_slot_info == NULL)
		{
			hal_sys_info("can't find smart_card_set_slot_info!");
			goto smart_card_module_init_clean;
		}

		g_pSmartCardInstance->transmit = (smart_card_transmit)dlsym(pHandle, "smart_card_transmit");
		if(g_pSmartCardInstance->transmit == NULL)
		{
			hal_sys_info("can't find smart_card_transmit!");
			goto smart_card_module_init_clean;
		}

		g_pSmartCardInstance->sle4442_read = (smart_card_mc_read)dlsym(pHandle, "smart_card_mc_read");
		if(g_pSmartCardInstance->sle4442_read == NULL)
		{
			hal_sys_info("can't find smart_card_mc_read!");
			goto smart_card_module_init_clean;
		}

		g_pSmartCardInstance->sle4442_write = (smart_card_mc_write)dlsym(pHandle, "smart_card_mc_write");
		if(g_pSmartCardInstance->sle4442_write == NULL)
		{
			hal_sys_info("can't find smart_card_mc_write!");
			goto smart_card_module_init_clean;
		}

		g_pSmartCardInstance->sle4442_verify = (smart_card_mc_verify_data)dlsym(pHandle, "smart_card_mc_verify_data");
		if(g_pSmartCardInstance->sle4442_verify == NULL)
		{
			hal_sys_info("can't find smart_card_mc_verify_data!");
			goto smart_card_module_init_clean;
		}

		g_pSmartCardInstance->pSoHandle = pHandle;

		nResult = g_pSmartCardInstance->init();
	}
	return 0;

smart_card_module_init_clean:
	delete g_pSmartCardInstance->pEventQueue;
	delete g_pSmartCardInstance;
	g_pSmartCardInstance = NULL;
	return -1;
}


int native_smart_card_terminate (JNIEnv * env, jclass obj)
{
	int nResult = -1;
	hal_sys_info("enter native_smart_card_terminate!");
	if(g_pSmartCardInstance == NULL)
		return -1;

	nResult = g_pSmartCardInstance->terminate();
	dlclose(g_pSmartCardInstance->pSoHandle);

	if(g_pSmartCardInstance->pEventQueue)
		delete g_pSmartCardInstance->pEventQueue;
	delete g_pSmartCardInstance;

	g_pSmartCardInstance = NULL;
	return nResult;
}

int native_smart_card_poll_event (JNIEnv * env, jclass obj, jint nTimeout_MS, jobject objEvent)
{
	int nResult = -1;
	CSmartCardEvent event;

	if(g_pSmartCardInstance == NULL)
		return -1;

	nResult = g_pSmartCardInstance->pEventQueue->pop_front(event, nTimeout_MS);
	hal_sys_info("pop_front return %d", nResult);
	if(nResult >= 0)
	{

		jclass clazz = env->GetObjectClass(objEvent);
		if(0 == clazz)
			return -1;

		jfieldID fidEventID = env->GetFieldID(clazz, "nEventID", "I");
		env->SetIntField(objEvent, fidEventID, event.m_nEventID);

		jfieldID fidSlotIndex = env->GetFieldID(clazz, "nSlotIndex", "I");
		env->SetIntField(objEvent, fidSlotIndex, event.m_nSlotIndex);
	}
	return nResult;
}


int native_smart_card_query_max_number (JNIEnv * env, jclass obj)
{
	if(g_pSmartCardInstance == NULL)
		return -1;
	return g_pSmartCardInstance->query_max_number();
}

int native_smart_card_query_presence (JNIEnv * env, jclass obj, jint nSlotIndex)
{
	if(g_pSmartCardInstance == NULL)
		return -1;
	int nResult = g_pSmartCardInstance->query_presence(nSlotIndex);
	hal_sys_info("g_pSmartCardInstance->query_presence(nSlotIndex) return value = %d\n", nResult);
	return nResult;
}

int native_smart_card_open (JNIEnv * env, jclass obj, jint nSlotIndex)
{
	if(g_pSmartCardInstance == NULL)
		return -1;

	g_pSmartCardInstance->pEventQueue->queue_clear();

	int nResult = g_pSmartCardInstance->open(nSlotIndex, smart_card_event_notify, g_pSmartCardInstance);
	return nResult;
}

int native_smart_card_close (JNIEnv * env, jclass obj, jint Handle)
{
	if(g_pSmartCardInstance == NULL)
		return -1;

	return g_pSmartCardInstance->close(Handle);
}

	/*
	 * return value : >= 0 : ATR length
	 * 				  < 0 : error code
	 */
static const char* strSlotField_Short[] =
{
		"FIDI", "EGT", "WI", "WTX", "EDC", "protocol", "power", "conv", "IFSC",
};
static const char* strSlotField_Long[] =
{
		"cwt", "bwt", "nSlotInfoItem",
};

int native_smart_card_power_on (JNIEnv * env, jclass obj, jint Handle, jbyteArray byteArrayATR, jobject objSlotInfo)
{
	jint i = 0;
	jint nResult = -1;
	SMART_CARD_SLOT_INFO slot_info;
	if(g_pSmartCardInstance == NULL)
		return -1;
	memset(&slot_info, 0, sizeof(SMART_CARD_SLOT_INFO));
	jbyte* pElements = env->GetByteArrayElements(byteArrayATR, 0);
	unsigned int nLength = (unsigned int)env->GetArrayLength(byteArrayATR);
	nResult = g_pSmartCardInstance->power_on(Handle, (unsigned char*)pElements, &nLength, &slot_info);
	if(nResult >= 0)
	{
		jclass clazz = env->GetObjectClass(objSlotInfo);
		if(0 == clazz)
		{
			env->ReleaseByteArrayElements(byteArrayATR, pElements, 0);
			return -1;
		}

		unsigned char* pTemp = (unsigned char*)&slot_info;
		for(i = 0; i < sizeof(strSlotField_Short)/sizeof(strSlotField_Short[0]); i++)
		{
			jfieldID fid = env->GetFieldID(clazz, strSlotField_Short[i], "S");
			env->SetShortField(objSlotInfo, fid, (jshort)(*pTemp++));
		}
		unsigned int* pTempInt = (unsigned int*)&slot_info;
		pTempInt += 3;
		for(i = 0; i < sizeof(strSlotField_Long) / sizeof(strSlotField_Long[0]); i++)
		{
			jfieldID fid = env->GetFieldID(clazz, strSlotField_Long[i], "J");
			env->SetLongField(objSlotInfo, fid, (unsigned int)(*pTempInt++));
		}
	}
	hal_sys_info("power on result = %d\n", nResult);
	env->ReleaseByteArrayElements(byteArrayATR, pElements, 0);
	return nResult >= 0 ? nLength : nResult;
}

int native_smart_card_power_off (JNIEnv * env, jclass obj, jint Handle)
{
	if(g_pSmartCardInstance == NULL)
		return -1;
	return g_pSmartCardInstance->power_off(Handle);
}

int native_smart_card_set_slot_info (JNIEnv * env, jclass obj, jint Handle, jobject objSlotInfo)
{
	jint i = 0;
	jint nResult = -1;
	SMART_CARD_SLOT_INFO slot_info;
	if(g_pSmartCardInstance == NULL)
		return -1;

	jclass clazz = env->GetObjectClass(objSlotInfo);
	if(0 == clazz)
		return -1;

	unsigned char* pTemp = (unsigned char*)&slot_info;
	for(i = 0; i < sizeof(strSlotField_Short)/sizeof(strSlotField_Short[0]); i++)
	{
		jfieldID fid = env->GetFieldID(clazz, strSlotField_Short[i], "S");
		*pTemp++ = (unsigned char)env->GetShortField(objSlotInfo, fid);
		//env->SetShortField(objSlotInfo, fid, (jshort)(*pTemp++));
	}
	unsigned int* pTempInt = (unsigned int*)&slot_info;
	pTempInt += 3;
	for(i = 0; i < sizeof(strSlotField_Long) / sizeof(strSlotField_Long[0]); i++)
	{
		jfieldID fid = env->GetFieldID(clazz, strSlotField_Long[i], "J");
		*pTempInt++ = (unsigned int)env->GetLongField(objSlotInfo, fid);
		//env->SetLongField(objSlotInfo, fid, (unsigned int)(*pTempInt++));
	}
	nResult = g_pSmartCardInstance->set_slot_info(Handle, &slot_info);
	return nResult;
}


int native_smart_card_transmit (JNIEnv * env, jclass obj, jint Handle, jbyteArray byteArrayAPDU, jint nAPDULength, jbyteArray byteArrayResponse)
{
	int nResult = -1;
	if(g_pSmartCardInstance == NULL)
		return -1;

	jbyte* strAPDUCmd = env->GetByteArrayElements(byteArrayAPDU, 0);
	unsigned int nAPDUCmdLength = (unsigned int)env->GetArrayLength(byteArrayAPDU);

	jbyte* strResponse = env->GetByteArrayElements(byteArrayResponse, 0);
	unsigned int nResponseBufferLength = (unsigned int)env->GetArrayLength(byteArrayResponse);


	nResult = g_pSmartCardInstance->transmit(Handle, (unsigned char*)strAPDUCmd, nAPDUCmdLength, (unsigned char*)strResponse, &nResponseBufferLength);
	env->ReleaseByteArrayElements(byteArrayAPDU, strAPDUCmd, 0);
	env->ReleaseByteArrayElements(byteArrayResponse, strResponse, 0);

	return nResult < 0 ? nResult : nResponseBufferLength;
}

int native_smart_card_mc_read(JNIEnv * env, jclass obj, jint Handle, jint nAreaType, jbyteArray byteDataBuffer, jint nDataLength, jbyte cStartAddress)
{
	int nResult = -1;
	if(g_pSmartCardInstance == NULL)
		return -1;
	jbyte* strDataBuffer = env->GetByteArrayElements(byteDataBuffer, 0);
	nResult = g_pSmartCardInstance->sle4442_read(Handle, nAreaType, (unsigned char*)strDataBuffer, nDataLength, cStartAddress);
	env->ReleaseByteArrayElements(byteDataBuffer, strDataBuffer, 0);
	return nResult;
}

int native_smart_card_mc_write(JNIEnv * env, jclass obj, jint Handle, jint nAreaType, jbyteArray byteDataBuffer, jint nDataLength, jbyte cStartAddress)
{
	int nResult = -1;
	if(g_pSmartCardInstance == NULL)
		return -1;
	jbyte* strDataBuffer = env->GetByteArrayElements(byteDataBuffer, 0);
	nResult = g_pSmartCardInstance->sle4442_write(Handle, nAreaType, (unsigned char*)strDataBuffer, nDataLength, cStartAddress);
	env->ReleaseByteArrayElements(byteDataBuffer, strDataBuffer, 0);
	return nResult;
}

int native_smart_card_mc_verify_data(JNIEnv * env, jclass obj, jint Handle, jbyteArray byteDataBuffer, jint nDataLength)
{
	int nResult = -1;
	if(g_pSmartCardInstance == NULL)
		return -1;
	jbyte* strDataBuffer = env->GetByteArrayElements(byteDataBuffer, 0);
	nResult = g_pSmartCardInstance->sle4442_verify(Handle, (unsigned char*)strDataBuffer, nDataLength);
	env->ReleaseByteArrayElements(byteDataBuffer, strDataBuffer, 0);
	return nResult;
}

const char* g_pJNIREG_CLASS = "com/mpos/newthree/wizarpos/jni/ContactICCardReaderInterface";

/*
 * Table of methods associated with a single class
 * Please pay attention to the signature string of the function, don't forget the ';'
 */
static JNINativeMethod g_Methods[] =
{
	{"init",			"()I",												(void*)native_smart_card_init},
	{"terminate",		"()I",												(void*)native_smart_card_terminate},
    {"pollEvent",		"(ILcom/mpos/newthree/wizarpos/jni/SmartCardEvent;)I",			(void*)native_smart_card_poll_event},
	{"queryMaxNumber",	"()I",												(void*)native_smart_card_query_max_number},
	{"queryPresence",	"(I)I",												(void*)native_smart_card_query_presence},
	{"open",			"(I)I",												(void*)native_smart_card_open},
	{"close",			"(I)I",												(void*)native_smart_card_close},
	{"powerOn",		    "(I[BLcom/mpos/newthree/wizarpos/jni/ContactICCardSlotInfo;)I",	(void*)native_smart_card_power_on},
	{"powerOff",		"(I)I",												(void*)native_smart_card_power_off},
	{"setSlotInfo",	    "(ILcom/mpos/newthree/wizarpos/jni/ContactICCardSlotInfo;)I",		(void*)native_smart_card_set_slot_info},
	{"transmit",		"(I[BI[B)I",										(void*)native_smart_card_transmit}
};

const char* smart_card_get_class_name()
{
	return g_pJNIREG_CLASS;
}

JNINativeMethod* smart_card_get_methods(int* pCount)
{
	*pCount = sizeof(g_Methods) /sizeof(g_Methods[0]);
	return g_Methods;
}
