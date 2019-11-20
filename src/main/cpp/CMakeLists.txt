
project(OpenSL-ES)

cmake_minimum_required(VERSION 3.4.1)

include_directories(include/)
include_directories(libcommon/)
include_directories(libmusicdecoder)
include_directories(libmedia)
include_directories(3rdparty/ffmpeg/include)



add_library(soundtrack SHARED
        OpenSLESSoundPlayer.cpp
        libmedia/opensl_es_context.cpp
        libmedia/sound_service.cpp
        libmusicdecoder/accompany_decoder_controller.cpp
        libmusicdecoder/accompany_decoder.cpp
        libcommon/packet_pool.cpp
        libcommon/packet_queue.cpp
        )

#添加FFmpeg静态链接库
add_library(avformat STATIC IMPORTED)
add_library(avcodec STATIC IMPORTED)
add_library(postproc STATIC IMPORTED)
add_library(avfilter STATIC IMPORTED)
add_library(avutil STATIC IMPORTED)
add_library(swresample STATIC IMPORTED)
add_library(swscale STATIC IMPORTED)
add_library(fdk-aac STATIC IMPORTED)
add_library(x264 STATIC IMPORTED)
add_library(vo-aacenc STATIC IMPORTED)

#设置FFmpeg静态库的路径属性
set(STATIC_LIBS_DIR ${CMAKE_CURRENT_SOURCE_DIR}/3rdparty/prebuilt/armv7-a)
set_target_properties(avformat
        PROPERTIES IMPORTED_LOCATION
        ${STATIC_LIBS_DIR}/libavformat.a)
set_target_properties(avcodec
        PROPERTIES IMPORTED_LOCATION
        ${STATIC_LIBS_DIR}/libavcodec.a)
set_target_properties(postproc
        PROPERTIES IMPORTED_LOCATION
        ${STATIC_LIBS_DIR}/libpostproc.a)
set_target_properties(avfilter
        PROPERTIES IMPORTED_LOCATION
        ${STATIC_LIBS_DIR}/libavfilter.a)
set_target_properties(avutil
        PROPERTIES IMPORTED_LOCATION
        ${STATIC_LIBS_DIR}/libavutil.a)
set_target_properties(swresample
        PROPERTIES IMPORTED_LOCATION
        ${STATIC_LIBS_DIR}/libswresample.a)
set_target_properties(swscale
        PROPERTIES IMPORTED_LOCATION
        ${STATIC_LIBS_DIR}/libswscale.a)
set_target_properties(fdk-aac
        PROPERTIES IMPORTED_LOCATION
        ${STATIC_LIBS_DIR}/libfdk-aac.a)
set_target_properties(x264
        PROPERTIES IMPORTED_LOCATION
        ${STATIC_LIBS_DIR}/libx264.a)
set_target_properties(vo-aacenc
        PROPERTIES IMPORTED_LOCATION
        ${STATIC_LIBS_DIR}/libvo-aacenc.a)

target_link_libraries(soundtrack z) #链接系统libz库,不知道为什么，必须有
#链接FFmpeg静态库
target_link_libraries(soundtrack avformat)
target_link_libraries(soundtrack avcodec)
target_link_libraries(soundtrack postproc)
target_link_libraries(soundtrack avfilter)
target_link_libraries(soundtrack avutil)
target_link_libraries(soundtrack swresample)
target_link_libraries(soundtrack swscale)
target_link_libraries(soundtrack fdk-aac)
target_link_libraries(soundtrack x264)
target_link_libraries(soundtrack vo-aacenc)

find_library(log-lib log .)

target_link_libraries(soundtrack OpenSLES ${log-lib})