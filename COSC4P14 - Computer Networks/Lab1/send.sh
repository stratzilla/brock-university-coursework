#!/bin/sh

echo "hello world" > original.txt
base64 original.txt > encoded.txt
cat encoded.txt | nc -l -p 1234 -q 0