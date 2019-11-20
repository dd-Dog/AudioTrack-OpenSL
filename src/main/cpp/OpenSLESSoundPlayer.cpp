//
// Created by bian on 2019/11/20.
//
#include <android/log.h>
#include <sound_service.h>
#include "include/com_flyscale_chapter_4_OpenSLESSoundPlayer.h"

extern "C" {
#include "libcommon/CommonTools.h"
#include <SLES/OpenSLES.h>
}

#define LOG_TAG "OpenSLESAPI"
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jstring JNICALL Java_com_flyscale_chapter_14_OpenSLESSoundPlayer_getStringFromJNI
        (JNIEnv *env, jclass clazz, jstring str) {
    const char *utfStr = env->GetStringUTFChars(str, NULL);
    LOGI("JNI got message from Java : %s", utfStr);
    return env->NewStringUTF("Welcome to JNI");
}


static void ThrowException(JNIEnv* env, const char* className, const char* message) {
    // Get the exception class
    jclass clazz = env->FindClass(className);

    // If exception class is found
    if (0 != clazz) {
        // Throw exception
        env->ThrowNew(clazz, message);

        // Release local class reference
        env->DeleteLocalRef(clazz);
    }
}

static const char* JAVA_LANG_IOEXCEPTION = "java/lang/IOException";
static const char* JAVA_LANG_OUTOFMEMORYERROR = "java/lang/OutOfMemoryError";

/**
 * 检查SLResult是否返回错误
 */
static bool CheckError(JNIEnv* env, SLresult result) {
    bool isError = false;
    // If an error occurred
    if (SL_RESULT_SUCCESS != result) {
        // Throw IOException
        const char* msg = ResultToString(result);
        LOGI("msg is %s", msg);
        ThrowException(env, JAVA_LANG_IOEXCEPTION, msg);

        isError = true;
    }

    return isError;
}

SoundService* soundService = NULL;
JNIEXPORT jboolean JNICALL Java_com_flyscale_chapter_14_OpenSLESSoundPlayer_setAudioDataSource
        (JNIEnv *env, jobject obj, jstring dataSource, jfloat percent) {
    const char *dataPath = env->GetStringUTFChars(dataSource, NULL);
    soundService = SoundService::GetInstance();
    //设置播放完成的回调
    JavaVM *g_jvm = NULL;
    env->GetJavaVM(&g_jvm);
    jobject g_obj = env->NewGlobalRef(obj);
    soundService->setOnCompletionCallback(g_jvm, g_obj);

    //设置播放文件路径以及解码buffer的1秒钟的百分比
    soundService->initSongDecoder(dataPath, percent);
    SLresult result = soundService->initSoundTrack();
    env->ReleaseStringUTFChars(dataSource, dataPath);
    bool isError = CheckError(env, result);
    return isError;
}

JNIEXPORT jint JNICALL Java_com_flyscale_chapter_14_OpenSLESSoundPlayer_getAccompanySampleRate
        (JNIEnv *env, jobject obj){
    if (NULL != soundService) {
        return soundService->getAccompanySampleRate();
    }
    return -1;
}

JNIEXPORT void JNICALL Java_com_flyscale_chapter_14_OpenSLESSoundPlayer_play
        (JNIEnv *env, jobject obj){
    if(NULL != soundService) {
        soundService->play();
    }
}

JNIEXPORT jint JNICALL Java_com_flyscale_chapter_14_OpenSLESSoundPlayer_getCurrentTimeMills
        (JNIEnv *env, jobject obj){
    if (NULL != soundService) {
        return soundService->getCurrentTimeMills();
    }
    return 0;
}

JNIEXPORT void JNICALL Java_com_flyscale_chapter_14_OpenSLESSoundPlayer_stop
        (JNIEnv *env, jobject obj){
    if (NULL != soundService) {
        soundService->stop();
        soundService = NULL;
    }
}
#ifdef __cplusplus
}
#endif
