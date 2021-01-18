#include <stdio.h>
#include <dlfcn.h>
#include <jni.h>
#include <pthread.h>

#include "hal_sys_log.h"
#include "pinpad_jni_interface.h"
#include "pinpad_interface.h"

#define DEBUG	1

const char* g_pJNIREG_CLASS = "com/mpos/newthree/wizarpos/jni/PinPadInterface";

static jmethodID mmid;
static JavaVM *g_jvm = NULL;
static jclass mcls = NULL;

static pthread_mutex_t pinpad_mutex = PTHREAD_MUTEX_INITIALIZER;

typedef struct pinpad_hal_interface
{
	pinpad_open                open;
	pinpad_close               close;
	pinpad_show_text           set_text;
	pinpad_select_key          set_key;
	pinpad_set_pin_length      set_pin_length;
	pinpad_encrypt_string      encrypt;
	pinpad_calculate_pin_block input_pin;
	pinpad_update_user_key     update_user_key;
	pinpad_update_master_key   update_master_key;
	pinpad_set_pinblock_callback	set_pinblock_callback;
	void* pSoHandle;
}PINPAD_HAL_INSTANCE;

static PINPAD_HAL_INSTANCE* g_pPinpadInstance = NULL;

int native_pinpad_open(JNIEnv* env, jclass obj)
{
	pthread_mutex_lock(&pinpad_mutex);

	int nResult = 0;
	hal_sys_info("native_pinpad_open() is called");
	env->GetJavaVM(&g_jvm);

	if(g_pPinpadInstance == NULL)
	{
        hal_sys_info("Do pinpad open");
		void* pHandle = dlopen("libwizarposHAL.so", RTLD_LAZY);
		if (!pHandle)
		{
			hal_sys_error("%s\n", dlerror());

			pthread_mutex_unlock(&pinpad_mutex);
			return -1;
		}

		g_pPinpadInstance = new PINPAD_HAL_INSTANCE();

		g_pPinpadInstance->open = (pinpad_open)dlsym(pHandle, "pinpad_open");
		if(g_pPinpadInstance->open == NULL)
		{
			hal_sys_error("can't find pinpad_open");
			goto pinpad_init_clean;
		}

		g_pPinpadInstance->close = (pinpad_close)dlsym(pHandle, "pinpad_close");
		if(g_pPinpadInstance->close == NULL)
		{
			hal_sys_error("can't find pinpad_close");
			goto pinpad_init_clean;
		}

		g_pPinpadInstance->set_text = (pinpad_show_text)dlsym(pHandle, "pinpad_show_text");
		if(g_pPinpadInstance->set_text == NULL)
		{
			hal_sys_error("can't find pinpad_show_text");
			goto pinpad_init_clean;
		}

		g_pPinpadInstance->set_key = (pinpad_select_key)dlsym(pHandle, "pinpad_select_key");
		if(g_pPinpadInstance->set_key == NULL)
		{
			hal_sys_error("can't find pinpad_select_key");
			goto pinpad_init_clean;
		}

		g_pPinpadInstance->encrypt = (pinpad_encrypt_string)dlsym(pHandle, "pinpad_encrypt_string");
		if(g_pPinpadInstance->encrypt == NULL)
		{
			hal_sys_error("can't find pinpad_encrypt_string");
			goto pinpad_init_clean;
		}

		g_pPinpadInstance->input_pin = (pinpad_calculate_pin_block)dlsym(pHandle, "pinpad_calculate_pin_block");
		if(g_pPinpadInstance->input_pin == NULL)
		{
			hal_sys_error("can't find pinpad_calculate_pin_block");
			goto pinpad_init_clean;
		}

		g_pPinpadInstance->update_user_key = (pinpad_update_user_key)dlsym(pHandle, "pinpad_update_user_key");
		if(g_pPinpadInstance->update_user_key == NULL)
		{
			hal_sys_error("can't find pinpad_update_user_key");
			goto pinpad_init_clean;
		}

		g_pPinpadInstance->update_master_key = (pinpad_update_master_key)dlsym(pHandle, "pinpad_update_master_key");
		if(g_pPinpadInstance->update_master_key == NULL)
		{
			hal_sys_error("can't find pinpad_update_master_key");
			goto pinpad_init_clean;
		}

		g_pPinpadInstance->set_pin_length = (pinpad_set_pin_length)dlsym(pHandle, "pinpad_set_pin_length");
		if(g_pPinpadInstance->set_pin_length == NULL)
		{
			hal_sys_error("can't find pinpad_set_pin_length");
			goto pinpad_init_clean;
		}

		g_pPinpadInstance->set_pinblock_callback = (pinpad_set_pinblock_callback)dlsym(pHandle, "pinpad_set_pinblock_callback");

		g_pPinpadInstance->pSoHandle = pHandle;
		nResult = g_pPinpadInstance->open();
	}

	pthread_mutex_unlock(&pinpad_mutex);
	return nResult;
pinpad_init_clean:
	if(g_pPinpadInstance != NULL)
	{
		delete g_pPinpadInstance;
		g_pPinpadInstance = NULL;
	}

	pthread_mutex_unlock(&pinpad_mutex);
	return -1;
}

int native_pinpad_close(JNIEnv* env, jclass obj)
{
	pthread_mutex_lock(&pinpad_mutex);

    hal_sys_info("native_pinpad_close() is called");
	int nResult = -1;
	if(g_pPinpadInstance == NULL)
    {
        pthread_mutex_unlock(&pinpad_mutex);
        return -1;
    }
	nResult = g_pPinpadInstance->close();
	dlclose(g_pPinpadInstance->pSoHandle);
	delete g_pPinpadInstance;
	g_pPinpadInstance = NULL;

	pthread_mutex_unlock(&pinpad_mutex);
    hal_sys_info("native_pinpad_close():%d", nResult);
	return nResult;
}

int native_pinpad_show_text(JNIEnv* env, jclass obj, jint nLineIndex, jbyteArray arryText, jint nLength, jint nFlagSound)
{
	int nResult = -1;
	if(g_pPinpadInstance == NULL)
		return -1;

	//typedef int (*pinpad_show_text)(int nLineIndex, char* strText, int nLength, int nFlagSound);
	if(arryText == NULL)
		nResult = g_pPinpadInstance->set_text(nLineIndex, NULL, 0, nFlagSound);
	else
	{
		jbyte* pText = env->GetByteArrayElements(arryText, 0);
		nResult = g_pPinpadInstance->set_text(nLineIndex, (char*)pText, nLength, nFlagSound);
		env->ReleaseByteArrayElements(arryText, pText, 0);
	}
	return nResult;
}

int native_pinpad_select_key(JNIEnv* env, jclass obj, jint nKeyType, jint nMasterKeyID, jint nUserKeyID, jint nAlgorith)
{
	int nResult = -1;
	if(g_pPinpadInstance == NULL)
		return -1;

	nResult = g_pPinpadInstance->set_key(nKeyType, nMasterKeyID, nUserKeyID, nAlgorith);
	return nResult;
}

int native_pinpad_encrypt_string(JNIEnv* env, jclass obj, jbyteArray arryPlainText, jint nTextLength, jbyteArray arryCipherTextBuffer)
{
	int nResult = -1;
	if(g_pPinpadInstance == NULL)
		return -1;

	if(arryPlainText == NULL || arryCipherTextBuffer == NULL)
		return -1;

	//typedef int (*pinpad_encrypt_string)(unsigned char* pPlainText, int nTextLength, unsigned char* pCipherTextBuffer, int nCipherTextBufferLength);

	jbyte* pPlainText = env->GetByteArrayElements(arryPlainText, 0);
	jbyte* pCipherTextBuffer = env->GetByteArrayElements(arryCipherTextBuffer, 0);
	jint nCipherTextBufferLength = env->GetArrayLength(arryCipherTextBuffer);

	nResult = g_pPinpadInstance->encrypt((unsigned char*)pPlainText, nTextLength, (unsigned char*)pCipherTextBuffer, nCipherTextBufferLength);

	env->ReleaseByteArrayElements(arryPlainText, pPlainText, 0);
	env->ReleaseByteArrayElements(arryCipherTextBuffer, pCipherTextBuffer, 0);
	return nResult;
}

int native_pinpad_calculate_pin_block(JNIEnv* env, jclass obj, jbyteArray arryASCIICardNumber, jint nCardNumberLength, jbyteArray arryPinBlockBuffer, jint nTimeout_MS, jint nFlagSound)
{
	int nResult = -1;
	if(g_pPinpadInstance == NULL)
		return -1;

	if(arryASCIICardNumber == NULL || arryPinBlockBuffer == NULL)
		return -1;

	jbyte* pASCIICardNumber = env->GetByteArrayElements(arryASCIICardNumber, 0);
	jbyte* pPinBlockBuffer = env->GetByteArrayElements(arryPinBlockBuffer, 0);
	jint nPinBlockBufferLength = env->GetArrayLength(arryPinBlockBuffer);

	nResult = g_pPinpadInstance->input_pin((unsigned char*)pASCIICardNumber, nCardNumberLength,
			(unsigned char*)pPinBlockBuffer, nPinBlockBufferLength, nTimeout_MS, nFlagSound);

	env->ReleaseByteArrayElements(arryASCIICardNumber, pASCIICardNumber, 0);
	env->ReleaseByteArrayElements(arryPinBlockBuffer, pPinBlockBuffer, 0);

	return nResult;
}

int native_pinpad_update_user_key(JNIEnv* env, jclass obj, jint nMasterKeyID, jint nUserKeyID, jbyteArray arryCipherNewUserKey, jint nCipherNewUserKeyLength)
{
	int nResult = -1;
	if(g_pPinpadInstance == NULL)
		return -1;

	if(arryCipherNewUserKey == NULL)
		return -1;

	//typedef int (*pinpad_update_user_key)(int nMasterKeyID, int nUserKeyID, unsigned char* pCipherNewUserKey, int nCipherNewUserKeyLength);
	jbyte* pCipherNewUserKey = env->GetByteArrayElements(arryCipherNewUserKey, 0);
	nResult = g_pPinpadInstance->update_user_key(nMasterKeyID, nUserKeyID, (unsigned char*)pCipherNewUserKey, nCipherNewUserKeyLength);
	env->ReleaseByteArrayElements(arryCipherNewUserKey, pCipherNewUserKey, 0);

	return nResult;
}

int native_pinpad_update_master_key(JNIEnv* env, jclass obj, jint nMasterKeyID, jbyteArray arrayOldKey, jint nOldKeyLength, jbyteArray arrayNewKey, jint nNewKeyLength)
{
	int nResult = -1;
	if(g_pPinpadInstance == NULL)
		return -1;

	jbyte* pOldKey = env->GetByteArrayElements(arrayOldKey, 0);
	jbyte* pNewKey = env->GetByteArrayElements(arrayNewKey, 0);

	hal_sys_error("Do update_master_key\n");
	nResult = g_pPinpadInstance->update_master_key(nMasterKeyID, (unsigned char*)pOldKey, nOldKeyLength, (unsigned char*)pNewKey, nNewKeyLength);
	hal_sys_info((char*)nResult);
	env->ReleaseByteArrayElements(arrayOldKey, pOldKey, 0);
	env->ReleaseByteArrayElements(arrayNewKey, pNewKey, 0);
	return nResult;
}

int native_pinpad_set_pin_length(JNIEnv* env, jclass obj, jint nLength, jint nFlag)
{
	int nResult = -1;
	if(g_pPinpadInstance == NULL)
		return -1;
	nResult = g_pPinpadInstance->set_pin_length(nLength, nFlag);
	return nResult;
}

void keyevent_notifier(int nCount, int nExtra)
{
	JNIEnv *env = NULL;
//	callback_ID = pthread_self();

	bool needDetach = false;
	if (g_jvm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK) {
		g_jvm->AttachCurrentThread(&env, NULL);
		needDetach = true;
	} else {
		hal_sys_error("Callback is running in java thread!!!");
	}

    jbyteArray elemt = env->NewByteArray(2);

    jbyte data[2];
    data[0] = (jbyte)nCount;
    data[1] = (jbyte)nExtra;

    env->SetByteArrayRegion(elemt, 0, 2, (const jbyte *)data);
    env->CallStaticVoidMethod(mcls, mmid, elemt);

    env->DeleteLocalRef(elemt);
    if (needDetach) {
        g_jvm->DetachCurrentThread();
    }
}

//return value: -1 has not find lib; -2 has not find pinpad_set_pinblock_callback in lib; -3 has not find PinpadCallback in Java code
int native_pinpad_set_pinblock_callback(JNIEnv* env, jclass obj)
{
    if (g_pPinpadInstance == NULL)
        return -1;

    if (g_pPinpadInstance->set_pinblock_callback == NULL)
        return -2;

    if(mcls == NULL)
        mcls = (jclass) env->NewGlobalRef(env->FindClass(g_pJNIREG_CLASS));
    mmid = env->GetStaticMethodID(mcls, "pinpadCallback","([B)V");
    if (mmid == NULL)
        return -3;
    hal_sys_error("Do pinpad set_pinblock_callback");
    int nResult = g_pPinpadInstance->set_pinblock_callback(keyevent_notifier);
    if (nResult < 0) {
        hal_sys_error("error in set callback\n");
        nResult = -1;
    }

    return nResult;
}

static JNINativeMethod g_Methods[] =
{
	{"open",			"()I",									(void*)native_pinpad_open},
	{"close",			"()I",									(void*)native_pinpad_close},
	{"setText",			"(I[BII)I",								(void*)native_pinpad_show_text},
	{"setKey",			"(IIII)I",								(void*)native_pinpad_select_key},
	{"setPinLength",	"(II)I",								(void*)native_pinpad_set_pin_length},
	{"encrypt",			"([BI[B)I",								(void*)native_pinpad_encrypt_string},
	{"inputPIN",		"([BI[BII)I",							(void*)native_pinpad_calculate_pin_block},
	{"updateUserKey",	"(II[BI)I",								(void*)native_pinpad_update_user_key},
	{"updateMasterKey",	"(I[BI[BI)I",							(void*)native_pinpad_update_master_key},
	{"setPinblockCallback",		"()I",							(void*)native_pinpad_set_pinblock_callback}
};

const char* pinpad_get_class_name()
{
	return g_pJNIREG_CLASS;
}

JNINativeMethod* pinpad_get_methods(int* pCount)
{
	*pCount = sizeof(g_Methods) /sizeof(g_Methods[0]);
	return g_Methods;
}
