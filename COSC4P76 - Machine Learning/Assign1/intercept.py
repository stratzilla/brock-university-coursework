#!/usr/bin/env python3

import socket

SERVER = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
DGRAM_SIZE = 512 # DNS queries typically are under 512 bytes
PORT = 53
IP_ADDRESS = "127.0.0.1"

def server_setup():
	print("\nSetting up server...", end=' ')
	try:
		SERVER.bind((IP_ADDRESS, PORT))
	except OSError as e:
		print("failed!\n")
		print(f"Error received was: {e}.\n")
		exit(0)
	print("done!\n")

def server_loop():
	try:
		while True:
			data, addr = SERVER.recvfrom(DGRAM_SIZE)
			print(str(data))
	except KeyboardInterrupt:
		print("\nServer closing...", end=' ')
		SERVER.close()
		print("done!\n")

if __name__ == '__main__':
	server_setup()
	server_loop()
	exit(1)