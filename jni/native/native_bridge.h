#ifndef NATIVE_BRIDGE_H
#define NATIVE_BRIDGE_H

#include <vector>
#include <jni.h>

bool processNV21ToEdges(const unsigned char* nv21, int width, int height, bool doCanny, std::vector<unsigned char>& outRGBA);

#endif // NATIVE_BRIDGE_H
