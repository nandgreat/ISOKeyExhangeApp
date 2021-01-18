LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := $(TARGET_ARCH_ABI)\libEMVKernal.so
LOCAL_SRC_FILES := $(LOCAL_MODULE)
LOCAL_LDLIBS    := -lm -llog
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := wizarpos_printer
LOCAL_SRC_FILES := hal_sys_log.c
LOCAL_SRC_FILES += printer_jni_register.cpp
LOCAL_SRC_FILES += printer_jni_interface.cpp
LOCAL_LDLIBS := -lm -llog
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := wizarpos_pinpad
LOCAL_SRC_FILES := hal_sys_log.c
LOCAL_SRC_FILES += pinpad_jni_register.cpp
LOCAL_SRC_FILES += pinpad_jni_interface.cpp
LOCAL_LDLIBS := -lm -llog
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := wizarpos_contact_ic_card
LOCAL_SRC_FILES := hal_sys_log.c
LOCAL_SRC_FILES += smart_card_jni_register.cpp 
LOCAL_SRC_FILES += smart_card_event.cpp
LOCAL_SRC_FILES += smart_card_jni_interface.cpp
LOCAL_LDLIBS := -lm -llog
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := wizarpos_contactless_ic_card
LOCAL_SRC_FILES := hal_sys_log.c
LOCAL_SRC_FILES += contactless_jni_register.cpp 
LOCAL_SRC_FILES += contactless_card_event.cpp 
LOCAL_SRC_FILES += contactless_jni_interface.cpp
LOCAL_LDLIBS := -lm -llog
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := wizarpos_emv_kernel
LOCAL_SRC_FILES := hal_sys_log.c
LOCAL_SRC_FILES += emv_kernal_jni_register.cpp
LOCAL_SRC_FILES += emv_kernal_jni_interface.cpp
LOCAL_LDLIBS := -lm -llog
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := access_config
LOCAL_SRC_FILES := access_config.cpp
LOCAL_LDLIBS := -lm -llog
include $(BUILD_SHARED_LIBRARY)