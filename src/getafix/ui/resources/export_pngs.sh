#!/bin/bash
array=( 16 24 32 48 64 96 128 256 )
for i in "${array[@]}"
do
	inkscape -f icon.svg --export-png=icon_$i"x"$i.png --export-width=$i --export-height=$i
done
