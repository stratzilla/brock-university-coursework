#!/bin/sh

# connect to listener server, take output as "received.txt"
nc localhost 1234 -q 0 > received.txt
# decrypt using base64, save as new file
base64 -d received.txt > decoded.txt
# no longer need these files
rm received.txt encoded.txt
# output md5 hashes of original and decoded received
echo "Sent:"
md5sum original.txt
echo "Received:"
md5sum decoded.txt
# no longer need those either
rm decoded.txt original.txt
