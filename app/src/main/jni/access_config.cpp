#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_evregebillspay_activities_MainActivity_getKey(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "accessis4life";
    return env->NewStringUTF(hello.c_str());
}


