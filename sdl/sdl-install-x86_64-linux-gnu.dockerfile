FROM ubuntu:24.04

ENV DEBIAN_FRONTEND=noninteractive

RUN apt update \
    && apt install -y \
    build-essential \
    cmake \
    ninja-build \
    pkg-config \
    ca-certificates \
    libx11-dev \
    libxrandr-dev \
    libxinerama-dev \
    libxcursor-dev \
    libxi-dev \
    libgl1-mesa-dev \
    libvulkan-dev \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /SDL

ENTRYPOINT ["/usr/bin/env", "bash", "-e", "-c", \
    " \
    BUILD_DIR=\"build/x86_64-linux-gnu\"; \
    INSTALL_DIR=\"install/x86_64-linux-gnu\"; \
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

