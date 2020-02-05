#!/bin/sh

# test file for transmit
echo "hello world" > original.txt
# encrypt using base64
base64 original.txt > encoded.txt
# open listener server, connect to peers and send encoded text
cat encoded.txt | nc -l -p 1234 -q 0
