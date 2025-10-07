#include "native_bridge.h"

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_yourpackage_NativeBridge_processNV21(
	JNIEnv* env, jobject /* this */, jbyteArray nv21Data, jint width, jint height, jboolean doCanny)
{
	jsize len = env->GetArrayLength(nv21Data);
	std::vector<jbyte> buf(len);
	env->GetByteArrayRegion(nv21Data, 0, len, buf.data());

	std::vector<unsigned char> outRGBA;
	bool ok = processNV21ToEdges((const unsigned char*)buf.data(), width, height, doCanny, outRGBA);
	if(!ok) return nullptr;

	jbyteArray result = env->NewByteArray(outRGBA.size());
	env->SetByteArrayRegion(result, 0, outRGBA.size(), (jbyte*)outRGBA.data());
	return result;
}
