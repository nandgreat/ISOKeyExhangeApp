#ifndef SMART_CARD_JNI_INTERFACE_H_
#define SMART_CARD_JNI_INTERFACE_H_

const char* smart_card_get_class_name();
JNINativeMethod* smart_card_get_methods(int* pCount);

#endif /* SMART_CARD_JNI_INTERFACE_H_ */