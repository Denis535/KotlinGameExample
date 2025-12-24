#!/usr/bin/env bash
set -e

BUILD_DIR="build/x86_64-linux-gnu"
INSTALL_DIR="install/x86_64-linux-gnu"

export CC=x86_64-linux-gnu-gcc
export CXX=x86_64-linux-gnu-g++

cmake -S . -B "${BUILD_DIR}" \
  -DCMAKE_BUILD_TYPE=Release \
  -DSDL_SHARED=ON \
  -DSDL_STATIC=OFF \
  -DSDL_ENABLE_PCH=OFF \
  -DSDL_VIDEO_WINDOWS=OFF \
  -DSDL_VIDEO_X11=ON \
  -DSDL_VIDEO_WAYLAND=OFF \
  -DSDL_RENDER_OPENGL=ON \
  -DSDL_RENDER_VULKAN=ON \
  -DCMAKE_INSTALL_PREFIX="${INSTALL_DIR}"

cmake --build "${BUILD_DIR}" -- -j$(nproc)

cmake --install "${BUILD_DIR}"

