#!/usr/bin/env bash
set -e

SDL_NAME="SDL"
SDL_VERSION="3.2.28"
SDL_FULLNAME="${SDL_NAME}-${SDL_VERSION}"
SDL_URL="https://github.com/libsdl-org/SDL/archive/refs/tags/release-${SDL_VERSION}.tar.gz"

curl -L -s "${SDL_URL}" -o "${SDL_FULLNAME}.tar.gz"

tar -xf "${SDL_FULLNAME}.tar.gz"

mv ${SDL_NAME}-release-${SDL_VERSION} $SDL_NAME

