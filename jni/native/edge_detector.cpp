
#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>
#include <vector>
#include "native_bridge.h"

using namespace cv;

bool processNV21ToEdges(const unsigned char* nv21, int width, int height, bool doCanny, std::vector<unsigned char>& outRGBA) {
	// NV21 to BGR
	Mat nv21(height + height / 2, width, CV_8UC1, (void*)nv21);
	Mat bgr;
	cvtColor(nv21, bgr, COLOR_YUV2BGR_NV21);

	// BGR to Grayscale
	Mat gray;
	cvtColor(bgr, gray, COLOR_BGR2GRAY);

	// Canny edge detection (optional)
	Mat edges;
	if (doCanny) {
		Canny(gray, edges, 100, 200);
	} else {
		edges = gray;
	}

	// Convert edges to RGBA
	Mat rgba;
	if (doCanny) {
		// For Canny, show edges as white on black
		Mat edgeMask;
		threshold(edges, edgeMask, 0, 255, THRESH_BINARY);
		Mat edgeRGBA;
		cvtColor(edgeMask, edgeRGBA, COLOR_GRAY2RGBA);
		rgba = edgeRGBA;
	} else {
		cvtColor(edges, rgba, COLOR_GRAY2RGBA);
	}

	// Copy RGBA bytes to output
	outRGBA.resize(rgba.total() * rgba.elemSize());
	memcpy(outRGBA.data(), rgba.data, outRGBA.size());
	return true;
}
