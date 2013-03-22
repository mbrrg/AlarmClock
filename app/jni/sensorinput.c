#undef __cplusplus
#include <string.h>
#include <fcntl.h>
#include <stdlib.h>
#include <unistd.h>
#include <jni.h>
#include <android/log.h>
#include <stdio.h>
#include <errno.h>

#define  LOG_TAG    		"SensorInputNative"
#define  LOGI(...)  		__android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  		__android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  		__android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

#define GPIO_PATH	 		"/sys/class/gpio"
#define BUF_SIZE			256

int gpio_open(char *port)
{
	int fd;
	char buf[BUF_SIZE];

	snprintf(buf, sizeof(buf), GPIO_PATH "/gpio%s/value", port);

 	LOGD("About to open %s for read access.", buf);

	fd = open(buf, O_RDONLY | O_NONBLOCK);

	if (fd != -1) {
		LOGD("Succeeded in opening file.");
	}
	else {
		LOGE("Failed to open file - %s.", strerror(errno));
	}

	return fd;
}

int gpio_write_file(char *port, char *file, char *value)
{
	int fd;
	char buf[BUF_SIZE];

	snprintf(buf, sizeof(buf), GPIO_PATH "/gpio%s/%s", port, file);

	LOGD("About to open %s for write access.", buf);

	fd = open(buf, O_WRONLY);

	if (fd != -1) {
		LOGD("Succeeded in opening file.");

		if (write(fd, value, strlen(value) + 1) != -1) {
			LOGD("Succeeded in writing \"%s\" to file.", value);
		}
		else {
			LOGE("Failed to write to file - %s.", strerror(errno));
		}

		close(fd);
	}
	else {
		LOGE("Failed to open file - %s.", strerror(errno));
	}

	return fd;
}

JNIEXPORT jint JNICALL Java_com_plushware_hardware_SensorInput_poll(JNIEnv *env, jobject obj, jstring portparam)
{
    char port[64];
    const jbyte *str;
    int fd, len, ret;
    struct pollfd fdset[1];
    char buf[BUF_SIZE];

    str = (*env)->GetStringUTFChars(env, portparam, NULL);

    if (str == NULL) {
        LOGE("Failed to get port name!");

        return -1;
    }

    sprintf(port, "%s", str);
    LOGD("Using port %s.", port);

    (*env)->ReleaseStringUTFChars(env, portparam, str);

    gpio_write_file(port, "direction", "in");
    gpio_write_file(port, "edge", "rising");

    fd = gpio_open(port);

    if (fd == -1) {
    	return -1;
    }

	while (1) {
		memset((void*)fdset, 0, sizeof(fdset));

		fdset[0].fd = fd;
		fdset[0].events = POLLPRI | POLLERR;

		read(fd, buf, 1);

		ret = poll(fdset, 1, -1);

		if (ret < 0) {
			LOGE("Poll failed - %s.", strerror(errno));

			return -1;
		}

		if (fdset[1].revents & POLLPRI) {
			LOGD("GPIO %s interrupt!", port);

			lseek(fd, 0, SEEK_SET);
			len = read(fdset[0].fd, buf, BUF_SIZE);

			LOGD("Read %d bytes.", len);
		}
	}

    LOGD("Closing GPIO file descriptor.");
	close(fd);

	return 0;
}
