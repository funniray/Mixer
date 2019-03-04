#!/bin/sh
for i in "1.9" "1.9.4" "1.10.2" "1.11.2" "1.12.2" "1.13" "1.13.2"
do
  echo "Downloading version $i"
  wget https://funniray.com/spigot/spigot-$i.jar -O libs/spigot-$i.jar
done