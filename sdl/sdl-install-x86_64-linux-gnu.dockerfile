FROM ubuntu:24.04

ENV DEBIAN_FRONTEND=noninteractive

# https://wiki.libsdl.org/SDL3/README-linux#build-dependencies
RUN apt update && apt install -y \
       build-essential git make \
       pkg-config cmake ninja-build gnome-desktop-testing libasound2-dev libpulse-dev \
       libaudio-dev libfribidi-dev libjack-dev libsndio-dev libx11-dev libxext-dev \
       libxrandr-dev libxcursor-dev libxfixes-dev libxi-dev libxss-dev libxtst-dev \
       libxkbcommon-dev libdrm-dev libgbm-dev libgl1-mesa-dev libgles2-mesa-dev \
       libegl1-mesa-dev libdbus-1-dev libibus-1.0-dev libudev-dev libthai-dev \
       libpipewire-0.3-dev libwayland-dev libdecor-0-dev liburing-dev \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /SDL

ENTRYPOINT ["/usr/bin/env", "bash", "-e", "-c", \
    " \
    BUILD_DIR='build/x86_64-linux-gnu'; \
    INSTALL_DIR='x86_64-linux-gnu'; \
    export CC=x86_64-linux-gnu-gcc; \
    export CXX=x86_64-linux-gnu-g++; \
    cmake -S . -B \"$BUILD_DIR\" \
        -DCMAKE_BUILD_TYPE=Release \
        -DSDL_SHARED=ON \
        -DSDL_STATIC=OFF \
        -DSDL_ENABLE_PCH=OFF \
        -DSDL_VIDEO_WINDOWS=OFF \
        -DSDL_VIDEO_X11=ON \
        -DSDL_VIDEO_WAYLAND=OFF \
        -DSDL_RENDER_OPENGL=ON \
        -DSDL_RENDER_VULKAN=ON \
        -DCMAKE_INSTALL_PREFIX=\"$INSTALL_DIR\"; \
    cmake --build \"$BUILD_DIR\" -- -j$(nproc); \
    cmake --install \"$BUILD_DIR\" \
    "]
