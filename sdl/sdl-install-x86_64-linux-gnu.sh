sudo docker build -t sdl-install-x86_64-linux-gnu -f sdl-install-x86_64-linux-gnu.dockerfile .

sudo docker run --rm --mount type=bind,source="$PWD/SDL",target=/SDL sdl-install-x86_64-linux-gnu
