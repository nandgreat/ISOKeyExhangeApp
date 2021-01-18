#ifndef CONTACTLESS_JNI_INTERFACE_H_
#define CONTACTLESS_JNI_INTERFACE_H_

const char* contactless_get_class_name();
JNINativeMethod* contactless_get_methods(int* pCount);

#endif /* CONTACTLESS_JNI_INTERFACE_H_ */