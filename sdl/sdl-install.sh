sudo docker build -t sdl-install-x86_64-w64-mingw32 -f sdl-install-x86_64-w64-mingw32.dockerfile .
sudo docker build -t sdl-install-x86_64-linux-gnu -f sdl-install-x86_64-linux-gnu.dockerfile .

sudo docker run --rm --mount type=bind,source="$PWD/SDL",target=/SDL sdl-install-x86_64-w64-mingw32
sudo docker run --rm --mount type=bind,source="$PWD/SDL",target=/SDL sdl-install-x86_64-linux-gnu

