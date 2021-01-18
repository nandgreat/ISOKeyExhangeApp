#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <dlfcn.h>
#include <semaphore.h>
#include <unistd.h>
#include <errno.h>

#include <jni.h>

#include "hal_sys_log.h"
#include "printer_jni_interface.h"
#include "printer_interface.h"
const char* g_pJNIREG_CLASS = "com/mpos/newthree/wizarpos/jni/PrinterInterface";

typedef struct printer_hal_interface
{
	printer_open 		open;
	printer_close	 	close;
	printer_begin 		begin;
	printer_end			end;
	printer_write		write;
	void* pSoHandle;
}PRINTER_HAL_INSTANCE;

static PRINTER_HAL_INSTANCE* g_pPrinterInstance = NULL;

int native_printer_open (JNIEnv * env, jclass obj)
{
	int nResult = 0;
	hal_sys_info("native_printer_open() is called");
	if(g_pPrinterInstance == NULL)
	{
		void* pHandle = dlopen("libwizarposHAL.so", RTLD_LAZY);
		if (!pHandle)
		{
			hal_sys_error("%s\n", dlerror());
			return -1;
		}

		g_pPrinterInstance = new PRINTER_HAL_INSTANCE();

		g_pPrinterInstance->open = (printer_open)dlsym(pHandle, "printer_open");
		if(g_pPrinterInstance->open == NULL)
		{
			hal_sys_info("can't find printer_open");
			goto printer_init_clean;
		}

		g_pPrinterInstance->close = (printer_close)dlsym(pHandle, "printer_close");
		if(g_pPrinterInstance->close == NULL)
		{
			hal_sys_info("can't find printer_close");
			goto printer_init_clean;
		}

		g_pPrinterInstance->begin = (printer_begin)dlsym(pHandle, "printer_begin");
		if(g_pPrinterInstance->begin == NULL)
		{
			hal_sys_info("can't find printer_begin");
			goto printer_init_clean;
		}

		g_pPrinterInstance->end = (printer_end)dlsym(pHandle, "printer_end");
		if(g_pPrinterInstance->end == NULL)
		{
			hal_sys_info("can't find printer_end");
			goto printer_init_clean;
		}

		g_pPrinterInstance->write = (printer_write)dlsym(pHandle, "printer_write");
		if(g_pPrinterInstance->write == NULL)
		{
			hal_sys_info("can't find printer_write");
			goto printer_init_clean;
		}

		g_pPrinterInstance->pSoHandle = pHandle;
		nResult = g_pPrinterInstance->open();
	}
	return nResult;
printer_init_clean:
	if(g_pPrinterInstance != NULL)
	{
		delete g_pPrinterInstance;
		g_pPrinterInstance = NULL;
	}
	return -1;
}

int native_printer_close (JNIEnv * env, jclass obj)
{
	hal_sys_info("native_printer_close() is called");
	if(g_pPrinterInstance != NULL)
	{
		dlclose(g_pPrinterInstance->pSoHandle);
		delete g_pPrinterInstance;
		g_pPrinterInstance = NULL;
	}
	return 0;
}

int native_printer_begin (JNIEnv * env, jclass obj)
{
	if(g_pPrinterInstance == NULL)
		return -1;
	return g_pPrinterInstance->begin();
}

int native_printer_end (JNIEnv * env, jclass obj)
{
	if(g_pPrinterInstance == NULL)
		return -1;
	return g_pPrinterInstance->end();
}

int native_printer_write (JNIEnv * env, jclass obj, jbyteArray arryData, jint nDataLength)
{
	int nResult = -1;
	if(g_pPrinterInstance == NULL)
		return -1;

	jbyte* pData = env->GetByteArrayElements(arryData, NULL);
	nResult = g_pPrinterInstance->write((unsigned char*)pData, nDataLength);
	env->ReleaseByteArrayElements(arryData, pData, 0);
	hal_sys_info("native_printer_write() return value = %d\n", nResult);
	return nResult;
}


/*
 * Maybe, this table should be defined in the file contactless_card_jni_interface.cpp
 * and then, try to get the pointer by a public method!
 */
static JNINativeMethod g_Methods[] =
{
	{"open",				"()I",		(void*)native_printer_open},
	{"close",				"()I",		(void*)native_printer_close},
	{"begin",				"()I",		(void*)native_printer_begin},
	{"end",					"()I",		(void*)native_printer_end},
	{"write",				"([BI)I",	(void*)native_printer_write},
};

const char* printer_get_class_name()
{
	return g_pJNIREG_CLASS;
}

JNINativeMethod* printer_get_methods(int* pCount)
{
	*pCount = sizeof(g_Methods) /sizeof(g_Methods[0]);
	return g_Methods;
}
