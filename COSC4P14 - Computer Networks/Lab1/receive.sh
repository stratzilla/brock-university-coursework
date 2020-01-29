#!/bin/sh

nc localhost 1234 -q 0 > received.txt
base64 -d received.txt > decoded.txt
rm received.txt encoded.txt
echo "Sent:"
md5sum original.txt
echo "Received:"
md5sum decoded.txt
rm decoded.txt original.txt