#undef __cplusplus
#include <string.h>
#include <fcntl.h>
#include <stdlib.h>
#include <unistd.h>
#include <jni.h>
#include <android/log.h>
#include <stdio.h>
#include <errno.h>
#include <poll.h>

#define  LOG_TAG    		"SensorInputNative"
#define  LOGD(...)  		__android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  		__android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGW(...)  		__android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)

#define GPIO_PATH	 		"/sys/class/gpio/gpio1_pb4/"
#define I2C_PATH			"/sys/bus/i2c/devices/2-0048/device0/"

int open_file(char *path, int write)
{
	int fd;
	int mode;

	if (write) {
		LOGD("About to open %s for write access.", path);
		mode = O_WRONLY;
	} else {
		LOGD("About to open %s for read access.", path);
		mode = O_RDONLY | O_NONBLOCK;
	}

	fd = open(path, mode);

	if (fd != -1) {
		LOGD("Succeeded in opening file.");
	}
	else {
		LOGE("Failed to open file - %s.", strerror(errno));
	}

	return fd;
}

int write_file(char *filename, char *value)
{
	int fd;

	fd = open_file(filename, 1);

	if (fd != -1) {
		if (write(fd, value, strlen(value) + 1) != -1) {
			LOGD("Succeeded in writing \"%s\" to file.", value);
		}
		else {
			LOGE("Failed to write to file - %s.", strerror(errno));
		}

		close(fd);
	}

	return fd;
}

int read_file(char *path)
{
	int fd, ret, len;
	char buf[32];

	ret = -1;

	fd = open_file(path, 0);

	if (fd != -1) {
		len = read(fd, buf, sizeof(buf));

		if (len >= 0) {
			buf[len] = '\0';
			ret = atoi(buf);
		} else {
			LOGE("Failed to read from file - %s.", strerror(errno));
		}

		close(fd);
	}

	return ret;
}

JNIEXPORT jint JNICALL Java_com_plushware_hardware_SensorInput_setThreshold(JNIEnv *env, jobject obj, jint threshold)
{
	char buf[32];

	snprintf(buf, sizeof(buf), "%d", threshold);

	return write_file(I2C_PATH "ch1_threshold", buf);
}

JNIEXPORT jint Java_com_plushware_hardware_SensorInput_getThreshold(JNIEnv *env, jobject obj)
{
	return read_file(I2C_PATH "ch1_threshold");
}

JNIEXPORT jint Java_com_plushware_hardware_SensorInput_getValue(JNIEnv *env, jobject obj)
{
	return read_file(GPIO_PATH "value");
}

JNIEXPORT jint Java_com_plushware_hardware_SensorInput_getRawValue(JNIEnv *env, jobject obj)
{
	return read_file(I2C_PATH "ch1_value");
}

JNIEXPORT jint JNICALL Java_com_plushware_hardware_SensorInput_poll(JNIEnv *env, jobject obj)
{
    int fd, len, ret;
    struct pollfd fdset[1];
    char buf[32];

    write_file(GPIO_PATH "direction", "in");
    write_file(GPIO_PATH "edge", "rising");

    fd = open_file(GPIO_PATH "value", 0);

    if (fd == -1) {
    	return -1;
    }

	while (1) {
		memset((void*)fdset, 0, sizeof(fdset));

		fdset[0].fd = fd;
		fdset[0].events = POLLPRI | POLLERR;

		len = read(fd, buf, 1);

		LOGD("Read %d bytes from file.", len);
		LOGD("Starting poll operation, infinite timeout.");

		ret = poll(fdset, 1, -1);

		if (ret > 0) {
			LOGD("poll call completed successfully.");

			if (fdset[0].revents & POLLPRI) {
				LOGD("GPIO interrupt!");

				lseek(fd, 0, SEEK_SET);
				len = read(fdset[0].fd, buf, sizeof(buf));

				LOGD("Read %d bytes.", len);

				ret = 0;

				break;
			} else {
				LOGW("No GPIO interrupt detected, weird.");
			}
		} else if (ret == 0) {
			LOGE("poll call timed out, should not be possible!");
		} else {
			LOGE("poll call failed - %s.", strerror(errno));

			break;
		}
	}

    LOGD("Closing GPIO file descriptor.");
	close(fd);

	return ret;
}
