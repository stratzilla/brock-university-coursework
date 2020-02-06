#!/usr/bin/env python3

from socket import AF_INET, socket, SOCK_STREAM
from threading import Thread
import sys

# in case of inappropriate arguments, remind user how to execute
if len(sys.argv) != 3:
	print("\nExecute the script as below:")
	print(" $ ./sock_client.py <IP Address> <Port>")
	print("For example: `./sock_server.py 127.0.0.1 8008`.")
	print("Additionally, ensure the socket server is live beforehand.\n")
	sys.exit(1)

CLIENT = socket(AF_INET, SOCK_STREAM) # TCP
IP_ADDRESS = str(sys.argv[1]) # IP address to connect to
PORT = int(sys.argv[2]) # port to connect to
RECV_SIZE = 4096 # receiving buffer size

ACTIVE = True # whether threads should continue or not
RECEIVE_THREAD = None # thread to receive messages
SEND_THREAD = None # thread to send messages

def set_up_client():
	"""
	Binds client to specific IP/host and port.
	"""
	global ACTIVE
	print("Attempting to connect to server...", end=' ')
	try:
		CLIENT.connect((IP_ADDRESS, PORT))
	except ConnectionRefusedError: # if no server active
		print("failed!\n")
		ACTIVE = False
		return		
	print("connected!")
	print(f"Client connected to server at {IP_ADDRESS}.")
	
def start_io_loop():
	"""
	Initializes send/receive loop threads.
	"""
	if ACTIVE: # as long as threads allowed to run
		RECEIVE_THREAD = Thread(target=receive_message)
		SEND_THREAD = Thread(target=send_message)
		RECEIVE_THREAD.start()
		SEND_THREAD.start()
		# wait for threads to finish before rejoining main thread
		RECEIVE_THREAD.join()
		SEND_THREAD.join()
	
def send_message():
	"""
	Sending message loop.
	"""
	global ACTIVE
	print("\nEnter a username: ", end='')
	username = input() # first get username
	print()
	CLIENT.send(bytes(username, "utf8"))
	while ACTIVE: # then look for inputs continuously
		message = input()
		if message == "/quit": # if client has quit
			ACTIVE = False
		CLIENT.send(bytes(message, "utf8"))

def receive_message():
	"""
	Receiving message loop.
	"""
	global ACTIVE
	while ACTIVE: # while connection established
		try: # attempt to receive data
			message = CLIENT.recv(RECV_SIZE).decode("utf8")
			print(f"{message}") # print the message to client
		except OSError: # if failed
			ACTIVE = False # terminate all threads

if __name__ == "__main__":
	print()
	set_up_client() # initialize client connection
	start_io_loop() # begin send/receive loops
	CLIENT.close() # close client connection when threads done
	exit(0)