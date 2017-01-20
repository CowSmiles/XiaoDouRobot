cmake_minimum_required(VERSION 3.6)
project(jni)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_BUILD_TYPE Release)

set(SOURCE_FILES
        speechrecognizer.c
        SpeechRecognizer.h
        Iat.h
        iat.c
        formats.h
        functions.c
        linuxrec.h
        linuxrec.c
        speech_recognizer.h
        speech_recognizer.c
        )

SET(LIBRARY_OUTPUT_PATH ${CMAKE_CURRENT_SOURCE_DIR}/../../../libs/xunfei)


include_directories(${CMAKE_CURRENT_SOURCE_DIR}/include)
include_directories(/usr/lib/jvm/java-8-jdk/include)
include_directories(/usr/lib/jvm/java-8-jdk/include/linux)

find_library(ALSA_LIBRARY NAMES asound
        DOC "The ALSA (asound) library"
        )

if (NOT ALSA_LIBRARY)
    message(FATAL_ERROR "ALSA library not found")
endif ()

find_library(MSC_LIBRARY NAMES msc PATHS $LIBRARY_OUTPUT_PATH)
if (NOT MSC_LIBRARY)
    message(FATAL_ERROR "msc library not found")
endif ()

SET(CMAKE_SHARED_LINKER_FLAGS '-Wl,-rpath,/usr/local/lib -L/usr/local/lib -lasound -lmsc')

ADD_LIBRARY(jnimsc SHARED ${SOURCE_FILES})
TARGET_LINK_LIBRARIES(jnimsc ${ALSA_LIBRARY} ${MSC_LIBRARY})

