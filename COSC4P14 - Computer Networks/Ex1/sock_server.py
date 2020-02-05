#!/usr/bin/env python3

from socket import AF_INET, socket, SOCK_STREAM
from threading import Thread
import sys

# in case of inappropriate arguments, remind user how to execute
if len(sys.argv) != 3:
	print("\nExecute the script as below:")
	print(" $ ./sock_server.py <IP Address> <Port>")
	print("For example: `./sock_server.py 127.0.0.1 8008`\n")
	sys.exit(1) # exit with code denoting abnormality

SERVER = socket(AF_INET, SOCK_STREAM) # TCP
IP_ADDRESS = str(sys.argv[1]) # IP address to connect to
PORT = int(sys.argv[2]) # port to connect to
MAX_CLIENTS = 100 # maximum number of connections
RECV_SIZE = 4096 # receiving buffer size

CLIENT_LIST = [] # an array of connected clients
ADDRESS_LIST = [] # an array of connected IP addresses

def set_up_server():
	"""
	Binds server to specific IP/host and port.
	"""
	print("Attempting to bind server...", end='')
	SERVER.bind((IP_ADDRESS, PORT))
	print(" Server bound!")

def listen_incoming():
	"""
	Enables server to listen for incoming connections.
	"""
	SERVER.listen(MAX_CLIENTS)
	print("Currently listening for %i clients..." % MAX_CLIENTS)
	while True:
		# wait on connection, then output details to server
		(client, address) = SERVER.accept()
		print("%s has connected." % address)
		ADDRESS_LIST[client] = address
		Thread(target=client_thread, args=(client, )).start()
		
def client_thread(c):
	"""
	Creates a thread per each client.
	
	Parameters:
	c -- socket connection 
	"""
	username = c.recv(RECV_SIZE).decode("utf8")

if __name__ == "__main__":
	print("")
	set_up_server()
	listen_incoming()
	# todo
