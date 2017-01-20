#include "SpeechRecognizer.h"
#include "speech_recognizer.h"
#include <string.h>
#include <stdlib.h>
#include "include/msp_cmn.h"
#include "include/msp_errors.h"
#include "include/msp_types.h"
#include "functions.c"

#define FRAME_LEN    640
#define    BUFFER_SIZE    4096
static struct speech_rec iat;
jobject g_listener;
jclass g_listener_class;
JNIEnv *g_env;

static char *g_result = NULL;
static unsigned int g_buffersize = BUFFER_SIZE;

void on_result(const char *result, char is_last) {
    if (result) {
        size_t left = g_buffersize - 1 - strlen(g_result);
        size_t size = strlen(result);
        if (left < size) {
            g_result = (char *) realloc(g_result, g_buffersize + BUFFER_SIZE);
            if (g_result)
                g_buffersize += BUFFER_SIZE;
            else {
                printf("mem alloc failed\n");
                return;
            }
        }
        strncat(g_result, result, size);
        // call listen same name function
        jmethodID methodID = (*g_env)->GetMethodID(g_env, g_listener_class, "onResult", "(Ljava/lang/String;Z)V");
        if (methodID == 0) {
            printf("could not get method id!\n");
            return;
        }
        (*g_env)->CallVoidMethod(g_env, g_listener, methodID, stoJstring(g_env, result), (jboolean) is_last);
    }
}

void on_speech_begin() {
    if (g_result) {
        free(g_result);
    }
    g_result = (char *) malloc(BUFFER_SIZE);
    g_buffersize = BUFFER_SIZE;
    memset(g_result, 0, g_buffersize);

    // call listen same name function
    jmethodID methodID = (*g_env)->GetMethodID(g_env, g_listener_class, "onBeginOfSpeech", "()V");
    if (methodID == 0) {
        printf("could not get method id!\n");
        return;
    }
    (*g_env)->CallVoidMethod(g_env, g_listener, methodID);
}

void on_speech_end(int reason) {
    // call listen same name function
    jmethodID methodID = (*g_env)->GetMethodID(g_env, g_listener_class, "onEndOfSpeech", "()V");
    if (methodID == 0) {
        printf("could not get method id!\n");
        return;
    }
    (*g_env)->CallVoidMethod(g_env, g_listener, methodID);
}

/*
 * Class:     com_klniu_xiaoyi_XFJNI_SpeechRecognizer
 * Method:    init
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT jint JNICALL Java_com_klniu_xiaoyi_XFJNI_SpeechRecognizer_init
        (JNIEnv *env, jobject obj, jstring javaString) {
    int ret = MSP_SUCCESS;
    char login_params[50];

    const char *appID = (*env)->GetStringUTFChars(env, javaString, 0);
    sprintf(login_params, "appid = %s, work_dir = .", appID);
    // release
    (*env)->ReleaseStringUTFChars(env, javaString, appID);

    /* Login first. the 1st arg is username, the 2nd arg is password
     * just set them as NULL. the 3rd arg is login paramertes
     * */
    return MSPLogin(NULL, NULL, login_params);
}

/*
 * Class:     com_klniu_xiaoyi_XFJNI_SpeechRecognizer
 * Method:    startListening
 * Signature: (Lcom/iflytek/cloud/speech/RecognizerListener;)V
 */
JNIEXPORT void JNICALL Java_com_klniu_xiaoyi_XFJNI_SpeechRecognizer_startListening
        (JNIEnv *env, jobject obj, jobject listener) {
    const char *session_begin_params = "sub = iat, domain = iat, language = zh_cn, "
            "accent = mandarin, sample_rate = 16000, "
            "result_type = plain, result_encoding = gb2312";
    int errcode;
    int i = 0;

    struct speech_rec_notifier recnotifier = {
            on_result,
            on_speech_begin,
            on_speech_end
    };
    // set listener to g_env
    g_listener = listener;
    g_listener_class = (*env)->GetObjectClass(env, listener);
    g_env = env;

    errcode = sr_init(&iat, session_begin_params, SR_MIC, &recnotifier);
    if (errcode) {
        printf("speech recognizer init failed\n");
        return;
    }
    errcode = sr_start_listening(&iat);
    if (errcode) {
        printf("start listen failed %d\n", errcode);
    }
}

/*
 * Class:     com_klniu_xiaoyi_XFJNI_SpeechRecognizer
 * Method:    stopListening
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_klniu_xiaoyi_XFJNI_SpeechRecognizer_stopListening
        (JNIEnv *env, jobject obj) {
    int errcode = sr_stop_listening(&iat);
    if (errcode) {
        printf("stop listening failed %d\n", errcode);
    }

    sr_uninit(&iat);
}

/*
 * Class:     com_klniu_xiaoyi_XFJNI_SpeechRecognizer
 * Method:    isListening
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_klniu_xiaoyi_XFJNI_SpeechRecognizer_isListening
        (JNIEnv *env, jobject obj) {
    if (iat.state < SR_STATE_STARTED) {
        return 0;
    }
    return 1;
}

/*
 * Class:     com_klniu_xiaoyi_XFJNI_SpeechRecognizer
 * Method:    destroy
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_klniu_xiaoyi_XFJNI_SpeechRecognizer_destroy
        (JNIEnv *env, jobject obj) {
    // destory iat
    sr_uninit(&iat);
    // logout
    MSPLogout();
}

