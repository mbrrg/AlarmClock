#include <jni.h>
/* Header for class axomI2C */
#include <stdio.h>
#include <android/log.h>
#include <fcntl.h>
#include <memory.h>
#include <malloc.h>

#ifndef _Included_ad7150rd
#define _Included_ad7150rd
#ifdef __cplusplus
  extern "C" {
#endif
/*
 * Class:     axon_I2C
 * Method:    open
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ad7150rd_test
  (JNIEnv *, jobject, jstring);

#ifdef __cplusplus
}
#endif
#endif
