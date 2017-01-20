#include "Iat.h"
#include <stdlib.h>
#include "include/msp_cmn.h"
#include "include/msp_errors.h"
#include "functions.c"

/*
 * Class:     com_klniu_xiaoyi_XFJNI_Iat
 * Method:    uploadHotWords
 * Signature: (Ljava/lang/String;Lcom/iflytek/cloud/speech/GrammarListener;)I
 */
JNIEXPORT jint JNICALL Java_com_klniu_xiaoyi_XFJNI_Iat_uploadHotWords
        (JNIEnv *env, jobject obj, jstring content) {
    const char *words = (*env)->GetStringUTFChars(env, content, 0);
    size_t len = strlen(words);
    int ret = -1;

    MSPUploadData("userwords", words, len, "sub = uup, dtt = userword", &ret);
    if (MSP_SUCCESS != ret) {
        printf("\nMSPUploadData failed ! errorCode: %d \n", ret);
        goto upload_exit;
    }

upload_exit:
    // release
    (*env)->ReleaseStringUTFChars(env, content, words);

    return ret;
}


