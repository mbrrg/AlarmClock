#undef __cplusplus
#include <string.h>
#include <fcntl.h>
#include <stdlib.h>
#include <unistd.h>
#include "ad7150rd.h"
#include <jni.h>
#define  LOG_TAG    "axoni2c"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

//***************************************************************************
// Open the I2C device
//***************************************************************************

JNIEXPORT jint JNICALL Java_com_plushware_alarmclock_ad7150rd_test(JNIEnv *env, jobject obj, jstring file)
{
/*    char fileName[64];
    const jbyte *str;

    str = (*env)->GetStringUTFChars(env, file, NULL);
    if (str == NULL) {
        LOGI("Can't get file name!");
        return -1;
    }
    sprintf(fileName, "%s", str);
    LOGI("will open i2c device node %s", fileName);

    (*env)->ReleaseStringUTFChars(env, file, str);
    return open(fileName, O_RDWR);
*/
		return 0;
}
