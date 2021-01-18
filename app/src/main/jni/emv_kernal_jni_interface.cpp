#include <fcntl.h>
#include <dlfcn.h>
#include <unistd.h>
#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include "emv_kernal_interface.h"
#include "emv_kernal_jni_interface.h"
#include <android/log.h>
#include "hal_sys_log.h"


EMV_KERNEL_INSTANCE* g_emv_kernel_instance = NULL;

JavaVM *g_jvm2 = NULL;
jobject g_obj2 = NULL;
jclass g_cls = NULL;

static void detachThread() {
	//Detach主线程
	if (g_jvm2->DetachCurrentThread() != JNI_OK) {
		hal_sys_error("DetachCurrentThread() failed");
	}
	hal_sys_error("DetachCurrentThread() OK");
}

jbyte native_load(JNIEnv * env, jclass obj)
{

	char *pError = NULL;
	if(g_emv_kernel_instance == NULL)
	{
		void* pHandle = dlopen("/data/data/com.mpos.newthree/lib/libEMVKernal.so", RTLD_LAZY);
		if (!pHandle)
		{
			hal_sys_error("can't open emv kernel: %s\n", dlerror());
			return -2;
		}

		g_emv_kernel_instance = new EMV_KERNEL_INSTANCE();

		g_emv_kernel_instance->pHandle = pHandle;

		// card functions
		g_emv_kernel_instance->open_reader = (OPEN_READER)dlsym(pHandle, "open_reader");
		if(g_emv_kernel_instance->open_reader == NULL)
		{
			hal_sys_error("can't open open_reader: %s\n", pError);
			return -1;
		}

		g_emv_kernel_instance->close_reader = (CLOSE_READER)dlsym(pHandle, "close_reader");
		if(g_emv_kernel_instance->close_reader == NULL)
		{
			hal_sys_error("can't open close_reader: %s\n", pError);
			return -1;
		}

		g_emv_kernel_instance->poweron_card = (POWERON_CARD)dlsym(pHandle, "poweron_card");
		if(g_emv_kernel_instance->poweron_card == NULL)
		{
			hal_sys_error("can't open poweron_card: %s\n", pError);
			return -1;
		}

		g_emv_kernel_instance->get_card_type = (GET_CARD_TYPE)dlsym(pHandle, "get_card_type");
		if(g_emv_kernel_instance->get_card_type == NULL)
		{
			hal_sys_error("can't open get_card_type: %s\n", pError);
			return -1;
		}

		g_emv_kernel_instance->get_card_atr = (GET_CARD_ATR)dlsym(pHandle, "get_card_atr");
		if(g_emv_kernel_instance->get_card_atr == NULL)
		{
			hal_sys_error("can't open get_card_atr: %s\n", pError);
			return -1;
		}

		g_emv_kernel_instance->transmit_card = (TRANSMIT_CARD)dlsym(pHandle, "transmit_card");
		if(g_emv_kernel_instance->transmit_card == NULL)
		{
			hal_sys_error("can't open transmit_card: %s\n", pError);
			return -1;
		}

		// EMV Functions
		// 0
		g_emv_kernel_instance->emv_kernel_initialize = (EMV_KERNEL_INITIALIZE)dlsym(pHandle, "emv_kernel_initialize");
		if(g_emv_kernel_instance->emv_kernel_initialize == NULL)
		{
			hal_sys_error("can't open emv_kernel_initialize: %s\n", pError);
			return -1;
		}

		// 1
		g_emv_kernel_instance->emv_is_tag_present = (EMV_IS_TAG_PRESENT)dlsym(pHandle, "emv_is_tag_present");
		if(g_emv_kernel_instance->emv_is_tag_present == NULL)
		{
			hal_sys_error("can't open emv_is_tag_present: %s\n", pError);
			return -1;
		}

		// 2
		g_emv_kernel_instance->emv_get_tag_data = (EMV_GET_TAG_DATA)dlsym(pHandle, "emv_get_tag_data");
		if(g_emv_kernel_instance->emv_get_tag_data == NULL)
		{
			hal_sys_error("can't open emv_get_tag_data: %s\n", pError);
			return -1;
		}

		// 3
		g_emv_kernel_instance->emv_get_tag_list_data = (EMV_GET_TAG_LIST_DATA)dlsym(pHandle, "emv_get_tag_list_data");
		if(g_emv_kernel_instance->emv_get_tag_list_data == NULL)
		{
			hal_sys_error("can't open emv_get_tag_list_data: %s\n", pError);
			return -1;
		}

		// 4
		g_emv_kernel_instance->emv_set_tag_data = (EMV_SET_TAG_DATA)dlsym(pHandle, "emv_set_tag_data");
		if(g_emv_kernel_instance->emv_set_tag_data == NULL)
		{
			hal_sys_error("can't open emv_set_tag_data: %s\n", pError);
			return -1;
		}

		// 5
		g_emv_kernel_instance->emv_preprocess_qpboc = (EMV_PREPROCESS_QPBOC)dlsym(pHandle, "emv_preprocess_qpboc");
		if(g_emv_kernel_instance->emv_preprocess_qpboc == NULL)
		{
			hal_sys_error("can't open emv_preprocess_qpboc: %s\n", pError);
			return -1;
		}

		// 6
		g_emv_kernel_instance->emv_trans_initialize = (EMV_TRANS_INITIALIZE)dlsym(pHandle, "emv_trans_initialize");
		if(g_emv_kernel_instance->emv_trans_initialize == NULL)
		{
			hal_sys_error("can't open emv_trans_initialize: %s\n", pError);
			return -1;
		}

		// 7
		g_emv_kernel_instance->emv_get_version_string = (EMV_GET_VERSION_STRING)dlsym(pHandle, "emv_get_version_string");
		if(g_emv_kernel_instance->emv_get_version_string == NULL)
		{
			hal_sys_error("can't open emv_get_version_string: %s\n", pError);
			return -1;
		}

		// 8
		g_emv_kernel_instance->emv_set_trans_amount = (EMV_SET_TRANS_AMOUNT)dlsym(pHandle, "emv_set_trans_amount");
		if(g_emv_kernel_instance->emv_set_trans_amount == NULL)
		{
			hal_sys_error("can't open emv_set_trans_amount: %s\n", pError);
			return -1;
		}

		// 9
		g_emv_kernel_instance->emv_set_other_amount = (EMV_SET_OTHER_AMOUNT)dlsym(pHandle, "emv_set_other_amount");
		if(g_emv_kernel_instance->emv_set_other_amount == NULL)
		{
			hal_sys_error("can't open emv_set_other_amount: %s\n", pError);
			return -1;
		}

		// 10
		g_emv_kernel_instance->emv_set_trans_type = (EMV_SET_TRANS_TYPE)dlsym(pHandle, "emv_set_trans_type");
		if(g_emv_kernel_instance->emv_set_trans_type == NULL)
		{
			hal_sys_error("can't open emv_set_trans_type: %s\n", pError);
			return -1;
		}

		// 11
		g_emv_kernel_instance->emv_set_kernel_type = (EMV_SET_KERNEL_TYPE)dlsym(pHandle, "emv_set_kernel_type");
		if(g_emv_kernel_instance->emv_set_kernel_type == NULL)
		{
			hal_sys_error("can't open emv_set_kernel_type: %s\n", pError);
			return -1;
		}

		// 12
		g_emv_kernel_instance->emv_process_next = (EMV_PROCESS_NEXT)dlsym(pHandle, "emv_process_next");
		if(g_emv_kernel_instance->emv_process_next == NULL)
		{
			hal_sys_error("can't open emv_process_next: %s\n", pError);
			return -1;
		}

		// 13
		g_emv_kernel_instance->emv_is_need_advice = (EMV_IS_NEED_ADVICE)dlsym(pHandle, "emv_is_need_advice");
		if(g_emv_kernel_instance->emv_is_need_advice == NULL)
		{
			hal_sys_error("can't open emv_is_need_advice: %s\n", pError);
			return -1;
		}

		// 14
		g_emv_kernel_instance->emv_is_need_signature = (EMV_IS_NEED_SIGNATURE)dlsym(pHandle, "emv_is_need_signature");
		if(g_emv_kernel_instance->emv_is_need_signature == NULL)
		{
			hal_sys_error("can't open emv_is_need_signature: %s\n", pError);
			return -1;
		}

		// 15
		g_emv_kernel_instance->emv_set_force_online = (EMV_SET_FORCE_ONLINE)dlsym(pHandle, "emv_set_force_online");
		if(g_emv_kernel_instance->emv_set_force_online == NULL)
		{
			hal_sys_error("can't open emv_set_force_online: %s\n", pError);
			return -1;
		}

		// 16
		g_emv_kernel_instance->emv_get_card_record = (EMV_GET_CARD_RECORD)dlsym(pHandle, "emv_get_card_record");
		if(g_emv_kernel_instance->emv_get_card_record == NULL)
		{
			hal_sys_error("can't open emv_get_card_record: %s\n", pError);
			return -1;
		}

		// 17
		g_emv_kernel_instance->emv_get_candidate_list = (EMV_GET_CANDIDATE_LIST)dlsym(pHandle, "emv_get_candidate_list");
		if(g_emv_kernel_instance->emv_get_candidate_list == NULL)
		{
			hal_sys_error("can't open emv_get_candidate_list: %s\n", pError);
			return -1;
		}

		// 18
		g_emv_kernel_instance->emv_set_candidate_list_result = (EMV_SET_CANDIDATE_LIST_RESULT)dlsym(pHandle, "emv_set_candidate_list_result");
		if(g_emv_kernel_instance->emv_set_candidate_list_result == NULL)
		{
			hal_sys_error("can't open emv_set_candidate_list_result: %s\n", pError);
			return -1;
		}

		// 19
		g_emv_kernel_instance->emv_set_id_check_result = (EMV_SET_ID_CHECK_RESULT)dlsym(pHandle, "emv_set_id_check_result");
		if(g_emv_kernel_instance->emv_set_id_check_result == NULL)
		{
			hal_sys_error("can't open emv_set_id_check_result: %s\n", pError);
			return -1;
		}

		// 20
		g_emv_kernel_instance->emv_set_online_pin_entered = (EMV_SET_ONLINE_PIN_ENTERED)dlsym(pHandle, "emv_set_online_pin_entered");
		if(g_emv_kernel_instance->emv_set_online_pin_entered == NULL)
		{
			hal_sys_error("can't open emv_set_online_pin_entered: %s\n", pError);
			return -1;
		}

		// 21
		g_emv_kernel_instance->emv_set_pin_bypass_confirmed = (EMV_SET_PIN_BYPASS_CONFIRMED)dlsym(pHandle, "emv_set_pin_bypass_confirmed");
		if(g_emv_kernel_instance->emv_set_pin_bypass_confirmed == NULL)
		{
			hal_sys_error("can't open emv_set_pin_bypass_confirmed: %s\n", pError);
			return -1;
		}

		// 22
		g_emv_kernel_instance->emv_set_online_result = (EMV_SET_ONLINE_RESULT)dlsym(pHandle, "emv_set_online_result");
		if(g_emv_kernel_instance->emv_set_online_result == NULL)
		{
			hal_sys_error("can't open emv_set_online_result: %s\n", pError);
			return -1;
		}

		// 23
		g_emv_kernel_instance->emv_aidparam_clear = (EMV_AIDPARAM_CLEAR)dlsym(pHandle, "emv_aidparam_clear");
		if(g_emv_kernel_instance->emv_aidparam_clear == NULL)
		{
			hal_sys_error("can't open emv_aidparam_clear: %s\n", pError);
			return -1;
		}

		// 24
		g_emv_kernel_instance->emv_aidparam_add = (EMV_AIDPARAM_ADD)dlsym(pHandle, "emv_aidparam_add");
		if(g_emv_kernel_instance->emv_aidparam_add == NULL)
		{
			hal_sys_error("can't open emv_aidparam_add: %s\n", pError);
			return -1;
		}

		// 25
		g_emv_kernel_instance->emv_capkparam_clear = (EMV_CAPKPARAM_CLEAR)dlsym(pHandle, "emv_capkparam_clear");
		if(g_emv_kernel_instance->emv_capkparam_clear == NULL)
		{
			hal_sys_error("can't open emv_capkparam_clear: %s\n", pError);
			return -1;
		}

		// 26
		g_emv_kernel_instance->emv_capkparam_add = (EMV_CAPKPARAM_ADD)dlsym(pHandle, "emv_capkparam_add");
		if(g_emv_kernel_instance->emv_capkparam_add == NULL)
		{
			hal_sys_error("can't open emv_capkparam_add: %s\n", pError);
			return -1;
		}

		// 27
		g_emv_kernel_instance->emv_terminal_param_set = (EMV_TERMINAL_PARAM_SET)dlsym(pHandle, "emv_terminal_param_set");
		if(g_emv_kernel_instance->emv_terminal_param_set == NULL)
		{
			hal_sys_error("can't open emv_terminal_param_set: %s\n", pError);
			return -1;
		}

		g_emv_kernel_instance->emv_terminal_param_set2 = (EMV_TERMINAL_PARAM_SET2)dlsym(pHandle, "emv_terminal_param_set2");
		if(g_emv_kernel_instance->emv_terminal_param_set2 == NULL)
		{
			hal_sys_error("can't open emv_terminal_param_set2: %s\n", pError);
			return -1;
		}

		// 28
		g_emv_kernel_instance->emv_exception_file_clear = (EMV_EXCEPTION_FILE_CLEAR)dlsym(pHandle, "emv_exception_file_clear");
		if(g_emv_kernel_instance->emv_exception_file_clear == NULL)
		{
			hal_sys_error("can't open emv_exception_file_clear: %s\n", pError);
			return -1;
		}

		// 29
		g_emv_kernel_instance->emv_exception_file_add = (EMV_EXCEPTION_FILE_ADD)dlsym(pHandle, "emv_exception_file_add");
		if(g_emv_kernel_instance->emv_exception_file_add == NULL)
		{
			hal_sys_error("can't open emv_exception_file_add: %s\n", pError);
			return -1;
		}

		// 30
		g_emv_kernel_instance->emv_revoked_cert_clear = (EMV_REVOKED_CERT_CLEAR)dlsym(pHandle, "emv_revoked_cert_clear");
		if(g_emv_kernel_instance->emv_revoked_cert_clear == NULL)
		{
			hal_sys_error("can't open emv_revoked_cert_clear: %s\n", pError);
			return -1;
		}

		// 31
		g_emv_kernel_instance->emv_revoked_cert_add = (EMV_REVOKED_CERT_ADD)dlsym(pHandle, "emv_revoked_cert_add");
		if(g_emv_kernel_instance->emv_revoked_cert_add == NULL)
		{
			hal_sys_error("can't open emv_revoked_cert_add: %s\n", pError);
			return -1;
		}

		// 32
		g_emv_kernel_instance->emv_log_file_clear = (EMV_LOG_FILE_CLEAR)dlsym(pHandle, "emv_log_file_clear");
		if(g_emv_kernel_instance->emv_log_file_clear == NULL)
		{
			hal_sys_error("can't open emv_log_file_clear: %s\n", pError);
			return -1;
		}

		g_emv_kernel_instance->emv_set_kernel_attr = (EMV_SET_KERNEL_ATTR)dlsym(pHandle, "emv_set_kernel_attr");
        if(g_emv_kernel_instance->emv_set_kernel_attr == NULL)
        {
            hal_sys_error("can't open emv_set_kernel_attr: %s\n", pError);
            return -1;
        }
	}
	g_emv_kernel_instance->g_jni_env = env;
	g_emv_kernel_instance->g_jni_obj = obj;
	env->GetJavaVM(&g_jvm2);
	g_obj2 = env->NewGlobalRef(obj);
	//com.mpos.sdk.
	jclass cls = env->FindClass("com/mpos/newthree/wizarpos/emvsample/activity/FuncActivity");
	g_cls = reinterpret_cast<jclass>(env->NewGlobalRef(cls));
	//g_cls = (jclass)env->NewGlobalRef(cls);
	if (g_cls == NULL)
	{
		hal_sys_error("FindClass() Error.....");
	}
	env->DeleteLocalRef(cls);
	return 0;
}

jbyte native_close(JNIEnv * env, jclass obj)
{
	if(g_emv_kernel_instance == NULL)
		return -1;
	dlclose(g_emv_kernel_instance->pHandle);
	delete g_emv_kernel_instance;
	g_emv_kernel_instance = NULL;
	return 0;
}

// 回调函数
static void emvProcessCallback(uchar* data)
{
	JNIEnv *env;
	jclass cls;

	if (g_jvm2->AttachCurrentThread(&env, NULL) != JNI_OK)
	{
		hal_sys_error("%s: AttachCurrentThread() failed", __FUNCTION__);
		return;
	}
	jmethodID method = env->GetStaticMethodID(g_cls, "emvProcessCallback", "([B)V");
	if (env->ExceptionCheck()) {
		hal_sys_error("jni can't find java emvProcessCallback");
	   return;
	}

	jbyteArray aryBuffer = env->NewByteArray(2);
	char * aryChar = (char*)env->GetByteArrayElements(aryBuffer, 0);
	aryChar[0] = data[0];
	aryChar[1] = data[1];
	env->ReleaseByteArrayElements(aryBuffer, (jbyte*) aryChar, 0);

	env->CallStaticVoidMethod( g_cls, method, aryBuffer);
	if (env->ExceptionCheck()) {
		hal_sys_error("jni can't call java emvProcessCallback");
		return;
	}
}

static void cardEventOccured(int eventType)
{
	JNIEnv *env;
	if (g_jvm2->AttachCurrentThread(&env, NULL) != JNI_OK)
	{
		hal_sys_error("%s: AttachCurrentThread() failed", __FUNCTION__);
		return;
	}
	jmethodID method = env->GetStaticMethodID(g_cls, "cardEventOccured", "(I)V");
	if (env->ExceptionCheck()) {
		hal_sys_error("jni can't find java cardEventOccured");
		detachThread();
	   return;
	}
	env->CallStaticVoidMethod( g_cls, method,eventType);
	if (env->ExceptionCheck()) {
		hal_sys_error("jni can't call java cardEventOccured");
		detachThread();
		return;
	}
	detachThread();
}

// card functions
jint native_open_reader(JNIEnv * env, jclass obj, jint reader)
{
	if(g_emv_kernel_instance == NULL)
	{
		if(g_emv_kernel_instance->open_reader == NULL)
		{
			hal_sys_error("jni invoke g_emv_kernel_instance->open_reader null\n");
		}
		return 0;
	}
	return g_emv_kernel_instance->open_reader(reader);
}

void native_close_reader(JNIEnv *env, jclass obj, jint reader)
{
	g_emv_kernel_instance->close_reader(reader);
}

jint native_poweron_card(JNIEnv *env, jclass obj)
{
	return g_emv_kernel_instance->poweron_card();
}

jint native_get_card_type(JNIEnv *env, jclass obj)
{
	return g_emv_kernel_instance->get_card_type();
}

jint native_get_card_atr(JNIEnv *env, jclass obj, jbyteArray atr)
{
	jbyte* bData = env->GetByteArrayElements(atr, NULL);
	jint iResult = g_emv_kernel_instance->get_card_atr((uchar *)bData);
	env->ReleaseByteArrayElements(atr, bData, 0);
	return iResult;
}

jint native_transmit_card(JNIEnv *env, jclass obj, jbyteArray cmd, jint cmdLength, jbyteArray respData, jint respDataLength)
{
	jbyte* bCmd = env->GetByteArrayElements(cmd, NULL);
	jbyte* bRespData = env->GetByteArrayElements(respData, NULL);
	jint iResult = g_emv_kernel_instance->transmit_card((uchar *)bCmd, cmdLength, (uchar*)bRespData, respDataLength);
	env->ReleaseByteArrayElements(cmd, bCmd, 0);
	env->ReleaseByteArrayElements(respData, bRespData, 0);
	return iResult;
}

// emv functions
void native_emv_kernel_initialize(JNIEnv *env, jclass obj)
{
	EMV_INIT_PARAM initParam;
	memset(&initParam,0,sizeof(initParam));
	// 初始化回调函数
	initParam.pCardEventOccured = (CARD_EVENT_OCCURED)cardEventOccured;
	initParam.pEMVProcessCallback = (EMV_PROCESS_CALLBACK)emvProcessCallback;
	g_emv_kernel_instance->emv_kernel_initialize(&initParam);
	return;
}

// 1
jint native_emv_is_tag_present(JNIEnv *env, jclass obj, jint tag)
{
	return g_emv_kernel_instance->emv_is_tag_present(tag);
}

// 2
jint native_emv_get_tag_data(JNIEnv *env, jclass obj, jint tag, jbyteArray data, jint dataLength)
{
	jbyte* bData = env->GetByteArrayElements(data, NULL);
	jint iResult = g_emv_kernel_instance->emv_get_tag_data(tag, (uchar*)bData,dataLength);
	env->ReleaseByteArrayElements(data, bData, 0);
	return iResult;
}

// 3
jint native_emv_get_tag_list_data(JNIEnv *env, jclass obj, jintArray tagList, jint tagCount, jbyteArray data, jint dataLength)
{
	jint* iTagList = env->GetIntArrayElements(tagList, NULL);
	jbyte* bData = env->GetByteArrayElements(data, NULL);
	jint iResult = g_emv_kernel_instance->emv_get_tag_list_data((int*)iTagList, tagCount, (uchar*)bData, dataLength);
	env->ReleaseIntArrayElements(tagList, iTagList, 0);
	env->ReleaseByteArrayElements(data, bData, 0);
	return iResult;
}

// 4
jint native_emv_set_tag_data(JNIEnv *env, jclass obj, jint tag, jbyteArray data, jint dataLength)
{
	jbyte* bData = env->GetByteArrayElements(data, NULL);
	jint iResult = g_emv_kernel_instance->emv_set_tag_data(tag, (uchar*)bData, dataLength);
	env->ReleaseByteArrayElements(data, bData, 0);
	return iResult;
}

// 5
jint native_emv_preprocess_qpboc(JNIEnv *env, jclass obj)
{
	return g_emv_kernel_instance->emv_preprocess_qpboc();
}

// 6
void native_emv_trans_initialize(JNIEnv *env, jclass obj)
{
	EMV_INIT_PARAM initParam;
	memset(&initParam,0,sizeof(initParam));
	// 初始化回调函数
	initParam.pEMVProcessCallback = (EMV_PROCESS_CALLBACK)emvProcessCallback;
	g_emv_kernel_instance->emv_trans_initialize();
	return;
}

// 7
jint native_emv_get_version_string(JNIEnv *env, jclass obj, jbyteArray data, jint dataLength)
{
	jbyte* bData = env->GetByteArrayElements(data, NULL);
	jint iResult = g_emv_kernel_instance->emv_get_version_string((uchar*)bData, dataLength);
	env->ReleaseByteArrayElements(data, bData, 0);
	return iResult;
}

// 8
jint native_emv_set_trans_amount(JNIEnv *env, jclass obj, jbyteArray amount)
{
	jbyte* bAmount = env->GetByteArrayElements(amount, NULL);
	jint iResult = g_emv_kernel_instance->emv_set_trans_amount((uchar*)bAmount);
	env->ReleaseByteArrayElements(amount, bAmount, 0);
	return iResult;
}

// 9
jint native_emv_set_other_amount(JNIEnv *env, jclass obj, jbyteArray amount)
{
	jbyte* bAmount = env->GetByteArrayElements(amount, NULL);
	jint iResult = g_emv_kernel_instance->emv_set_other_amount((uchar*)bAmount);
	env->ReleaseByteArrayElements(amount, bAmount, 0);
	return iResult;
}

// 10
jint native_emv_set_trans_type(JNIEnv *env, jclass obj, jbyte transType)
{
	return g_emv_kernel_instance->emv_set_trans_type(transType);
}

// 11
jint native_emv_set_kernel_type(JNIEnv *env, jclass obj, jbyte kernelType)
{
	return g_emv_kernel_instance->emv_set_kernel_type(kernelType);
}

// 12
jint native_emv_process_next(JNIEnv *env, jclass obj)
{
	return g_emv_kernel_instance->emv_process_next();
}

// 13
jint native_emv_is_need_advice(JNIEnv *env, jclass obj)
{
	return g_emv_kernel_instance->emv_is_need_advice();
}

// 14
jint native_emv_is_need_signature(JNIEnv *env, jclass obj)
{
	return g_emv_kernel_instance->emv_is_need_signature();
}

// 15
jint native_emv_set_force_online(JNIEnv *env, jclass obj, jint flag)
{
	return g_emv_kernel_instance->emv_set_force_online(flag);
}

// 16
jint native_emv_get_card_record(JNIEnv *env, jclass obj, jbyteArray data, jint dataLength)
{
	jbyte* bData = env->GetByteArrayElements(data,NULL);
	jint iResult = g_emv_kernel_instance->emv_get_card_record((uchar*)bData, dataLength);
	env->ReleaseByteArrayElements(data,bData,0);

	return iResult;
}

// 17
jint native_emv_get_candidate_list(JNIEnv *env, jclass obj, jbyteArray data, jint dataLength)
{
	jbyte* bData = env->GetByteArrayElements(data,NULL);
	jint iResult = g_emv_kernel_instance->emv_get_candidate_list((uchar*)bData, dataLength);
	env->ReleaseByteArrayElements(data,bData,0);
	return iResult;
}

// 18
jint native_emv_set_candidate_list_result(JNIEnv *env, jclass obj, jint index)
{
	return g_emv_kernel_instance->emv_set_candidate_list_result(index);
}

// 19
jint native_emv_set_id_check_result(JNIEnv *env, jclass obj, jint result)
{
	return g_emv_kernel_instance->emv_set_id_check_result(result);
}

// 20
jint native_emv_set_online_pin_entered(JNIEnv *env, jclass obj, jint result)
{
	return g_emv_kernel_instance->emv_set_online_pin_entered(result);
}

// 21
jint native_emv_set_pin_bypass_confirmed(JNIEnv *env, jclass obj, jint result)
{
	return g_emv_kernel_instance->emv_set_pin_bypass_confirmed(result);
}

// 22
jint native_emv_set_online_result(JNIEnv *env, jclass obj, jint result, jbyteArray respCode, jbyteArray issuerRespData, jint issuerRespDataLength)
{
	jint iResult = -1;
	jbyte* bRespCode = env->GetByteArrayElements(respCode, NULL);
	if(issuerRespData == NULL || issuerRespDataLength == 0)
	{
		iResult = g_emv_kernel_instance->emv_set_online_result(result, (uchar *)bRespCode, NULL, 0);
	}
	else{
		jbyte* bIssuerRespData = env->GetByteArrayElements(issuerRespData, NULL);
		iResult = g_emv_kernel_instance->emv_set_online_result(result, (uchar *)bRespCode, (uchar *)bIssuerRespData, issuerRespDataLength);
		env->ReleaseByteArrayElements(issuerRespData, bIssuerRespData, 0);
	}

	env->ReleaseByteArrayElements(respCode, bRespCode, 0);
	return iResult;
}

// 23
jint native_emv_aidparam_clear(JNIEnv *env, jclass obj)
{
	return g_emv_kernel_instance->emv_aidparam_clear();
}

// 24
jint native_emv_aidparam_add(JNIEnv *env, jclass obj, jbyteArray data, jint dataLength)
{
	jbyte* bData = env->GetByteArrayElements(data, NULL);
	jint iResult = g_emv_kernel_instance->emv_aidparam_add((uchar *)bData, dataLength);
	env->ReleaseByteArrayElements(data, bData, 0);
	return iResult;
}

// 25
jint native_emv_capkparam_clear(JNIEnv *env, jclass obj)
{
	return g_emv_kernel_instance->emv_capkparam_clear();
}

// 26
jint native_emv_capkparam_add(JNIEnv *env, jclass obj, jbyteArray data, jint dataLength)
{
	jbyte* bData = env->GetByteArrayElements(data, NULL);
	jint iResult = g_emv_kernel_instance->emv_capkparam_add((uchar *)bData, dataLength);
	env->ReleaseByteArrayElements(data, bData, 0);
	return iResult;
}

// 27
jint native_emv_terminal_param_set(JNIEnv *env, jclass obj, jbyteArray data)
{
	jbyte* bData = env->GetByteArrayElements(data, NULL);
	jint iResult = g_emv_kernel_instance->emv_terminal_param_set((uchar *)bData);
	env->ReleaseByteArrayElements(data, bData, 0);
	return iResult;
}

jint native_emv_terminal_param_set2(JNIEnv *env, jclass obj, jbyteArray data, jint dataLength)
{
	jbyte* bData = env->GetByteArrayElements(data, NULL);
	jint iResult = g_emv_kernel_instance->emv_terminal_param_set2((uchar *)bData, dataLength);
	env->ReleaseByteArrayElements(data, bData, 0);
	return iResult;
}

// 28
jint native_emv_exception_file_clear(JNIEnv *env, jclass obj)
{
	return g_emv_kernel_instance->emv_exception_file_clear();
}

// 29
jint native_emv_exception_file_add(JNIEnv *env, jclass obj, jbyteArray data)
{
	jbyte* bData = env->GetByteArrayElements(data, NULL);
	jint iResult = g_emv_kernel_instance->emv_exception_file_add((uchar *)bData);
	env->ReleaseByteArrayElements(data, bData, 0);
	return iResult;
}

// 30
jbyte native_emv_revoked_cert_clear(JNIEnv *env, jclass obj)
{
	return g_emv_kernel_instance->emv_revoked_cert_clear();
}

// 31
jbyte native_emv_revoked_cert_add(JNIEnv *env, jclass obj, jbyteArray data)
{
	jbyte* bData = env->GetByteArrayElements(data, NULL);
	jint iResult = g_emv_kernel_instance->emv_revoked_cert_add((uchar *)bData);
	env->ReleaseByteArrayElements(data, bData, 0);
	return iResult;
}

// 32
jint native_emv_log_file_clear(JNIEnv *env, jclass obj)
{
	return g_emv_kernel_instance->emv_log_file_clear();
}

jint native_emv_set_kernel_attr(JNIEnv *env, jclass obj, jbyteArray data, jint dataLen)
{
	jbyte *bData = env->GetByteArrayElements(data, NULL);
	jint iResult = g_emv_kernel_instance->emv_set_kernel_attr((uchar *)bData, dataLen);
	env->ReleaseByteArrayElements(data, bData, 0);
	return iResult;
}
//com.mpos.sdk.wizarpos.emvsample.activity
const char* g_pJNIREG_CLASS = "com/mpos/newthree/wizarpos/emvsample/activity/FuncActivity";
static JNINativeMethod g_Methods[] =
{
	{"loadEMVKernel",					"()B",				(void*)native_load},
	{"exitEMVKernel",					"()B",				(void*)native_close},
	// card functions
	{"open_reader",   					"(I)I",			    (void*)native_open_reader},
	{"close_reader",					"(I)V",				(void*)native_close_reader},
	{"poweron_card",					"()I",				(void*)native_poweron_card},
	{"get_card_type",					"()I",				(void*)native_get_card_type},
	{"get_card_atr",					"([B)I",	        (void*)native_get_card_atr},
	{"transmit_card",					"([BI[BI)I",	    (void*)native_transmit_card},
	// emv Functions
	{"emv_kernel_initialize",		   	"()V",	       	    (void*)native_emv_kernel_initialize},   // 0
	{"emv_is_tag_present",				"(I)I",				(void*)native_emv_is_tag_present},		// 1
	{"emv_get_tag_data",				"(I[BI)I",			(void*)native_emv_get_tag_data},		// 2
	{"emv_get_tag_list_data",			"([II[BI)I",		(void*)native_emv_get_tag_list_data},	// 3
	{"emv_set_tag_data",				"(I[BI)I",			(void*)native_emv_set_tag_data},		// 4
	{"emv_preprocess_qpboc",			"()I",	        	(void*)native_emv_preprocess_qpboc},    // 5
	{"emv_trans_initialize",		   	"()V",	       	    (void*)native_emv_trans_initialize},    // 6
	{"emv_get_version_string",			"([BI)I",			(void*)native_emv_get_version_string},  // 7
	{"emv_set_trans_amount",			"([B)I",			(void*)native_emv_set_trans_amount},    // 8
	{"emv_set_other_amount",			"([B)I",			(void*)native_emv_set_other_amount},    // 9
	{"emv_set_trans_type",				"(B)I",				(void*)native_emv_set_trans_type},      // 10
	{"emv_set_kernel_type",				"(B)I",				(void*)native_emv_set_kernel_type},     // 11
	{"emv_process_next",				"()I",	            (void*)native_emv_process_next},        // 12
	{"emv_is_need_advice",		    	"()I",				(void*)native_emv_is_need_advice},      // 13
	{"emv_is_need_signature",			"()I",				(void*)native_emv_is_need_signature},   // 14
	{"emv_set_force_online",			"(I)I",				(void*)native_emv_set_force_online},    // 15
	{"emv_get_card_record",		    	"([BI)I",			(void*)native_emv_get_card_record},     // 16
	{"emv_get_candidate_list",	    	"([BI)I",	        (void*)native_emv_get_candidate_list},  // 17
	{"emv_set_candidate_list_result",	"(I)I",	            (void*)native_emv_set_candidate_list_result},  // 18
	{"emv_set_id_check_result",		    "(I)I",				(void*)native_emv_set_id_check_result},        // 19
	{"emv_set_online_pin_entered",	    "(I)I",	            (void*)native_emv_set_online_pin_entered},     // 20
	{"emv_set_pin_bypass_confirmed",	"(I)I",	            (void*)native_emv_set_pin_bypass_confirmed},   // 21
	{"emv_set_online_result",	        "(I[B[BI)I",	    (void*)native_emv_set_online_result},          // 22
	{"emv_aidparam_clear",				"()I",				(void*)native_emv_aidparam_clear},             // 23
	{"emv_aidparam_add",				"([BI)I",			(void*)native_emv_aidparam_add},               // 24
	{"emv_capkparam_clear",				"()I",				(void*)native_emv_capkparam_clear},            // 25
	{"emv_capkparam_add",				"([BI)I",			(void*)native_emv_capkparam_add},              // 26
	{"emv_terminal_param_set",			"([B)I",	        (void*)native_emv_terminal_param_set},         // 27
	{"emv_terminal_param_set2",			"([BI)I",	        (void*)native_emv_terminal_param_set2},        //
	{"emv_exception_file_clear",		"()I",				(void*)native_emv_exception_file_clear},       // 28
	{"emv_exception_file_add",			"([B)I",		    (void*)native_emv_exception_file_add},         // 29
	{"emv_revoked_cert_clear",			"()I",				(void*)native_emv_revoked_cert_clear},         // 30
	{"emv_revoked_cert_add",			"([B)I",		    (void*)native_emv_revoked_cert_add},           // 31
	{"emv_log_file_clear",		        "()I",				(void*)native_emv_log_file_clear},             // 32
	{"emv_set_kernel_attr",             "([BI)I",           (void*)native_emv_set_kernel_attr}
};

const char* emv_kernal_get_class_name()
{
	return g_pJNIREG_CLASS;
}

JNINativeMethod* emv_kernal_get_methods(int* pCount)
{
	*pCount = sizeof(g_Methods) /sizeof(g_Methods[0]);
	return g_Methods;
}
