#include <jni.h>
#include <string.h>

static jstring stoJstring(JNIEnv *env, const char *pat) {
    jclass strClass = (*env)->FindClass(env, "Ljava/lang/String;");
    jmethodID ctorID = (*env)->GetMethodID(env, strClass, "<init>", "([BLjava/lang/String;)V");
    jbyteArray bytes = (*env)->NewByteArray(env, strlen(pat));
    (*env)->SetByteArrayRegion(env, bytes, 0, strlen(pat), (jbyte *) pat);
    jstring encoding = (*env)->NewStringUTF(env, "utf-8");
    return (jstring) (*env)->NewObject(env, strClass, ctorID, bytes, encoding);
}
