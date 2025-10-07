cmake_minimum_required(VERSION 3.10)
project(edge_detector)

add_library(native-lib SHARED
			native_bridge.cpp
			edge_detector.cpp)

find_library(log-lib log)

# Adjust path if needed
set(OpenCV_DIR ${CMAKE_SOURCE_DIR}/../../third_party/opencv-android-sdk/sdk/native/jni)
find_package(OpenCV REQUIRED core imgproc highgui)

include_directories(${OpenCV_INCLUDE_DIRS})
target_link_libraries(native-lib ${log-lib} ${OpenCV_LIBS})
