#!/usr/bin/env python3

from socket import AF_INET, socket, SOCK_STREAM
from threading import Thread
import sys

# in case of inappropriate arguments, remind user how to execute
if len(sys.argv) != 3:
	print("\nExecute the script as below:")
	print(" $ ./sock_server.py <IP Address> <Port>")
	print("For example: `./sock_server.py 127.0.0.1 8008`.\n")
	sys.exit(1) # exit with code denoting abnormality

SERVER = socket(AF_INET, SOCK_STREAM) # TCP
IP_ADDRESS = str(sys.argv[1]) # IP address to connect to
PORT = int(sys.argv[2]) # port to connect to
MAX_CLIENTS = 100 # maximum number of connections
RECV_SIZE = 4096 # receiving buffer size

CLIENT_LIST = {} # an array of connected clients

ACTIVE = True # whether thread should continue or not
SERVER_LOOP = None # thread to poll for connections

def set_up_server():
	"""
	Binds server to specific IP/host and port.
	"""
	global ACTIVE
	print("Attempting to bind server...", end=' ')
	try:
		SERVER.bind((IP_ADDRESS, PORT))
	except OSError: # error in binding
		print("failed!")
		ACTIVE = False
		return
	print("bound!")
	print(f"Server live on {IP_ADDRESS}:{PORT}.")
	SERVER_LOOP = Thread(target=listen_incoming)
	SERVER_LOOP.start()
	SERVER_LOOP.join()

def listen_incoming():
	"""
	Enables server to listen for incoming connections.
	"""
	SERVER.listen(MAX_CLIENTS)
	print(f"Currently listening for up to {MAX_CLIENTS} clients...\n")
	while ACTIVE:
		# wait on connection, then output details to server
		(client, address) = SERVER.accept()
		print(f" * {address} has connected.")
		Thread(target=client_thread, args=(client, )).start()
		
def client_thread(c):
	"""
	Creates a thread per each client.
	
	Parameter:
	c -- client socket connection 
	"""
	username = c.recv(RECV_SIZE).decode("utf8") # get username
	CLIENT_LIST[c] = username # add to client list
	user_string = "<" + username + "> " # special format for chat
	welcome_message = f" * You have connected to the server at {IP_ADDRESS}."
	c.send(bytes(welcome_message, "utf8"))
	join_message = f" * {username} has joined the server."
	send_all(join_message)
	while ACTIVE:
		# decode message for inplace actions depending on text
		message = c.recv(RECV_SIZE).decode("utf8")
		if message == "/quit": # quit handler
			quit_message = f" * {username} has left the server."
			send_all(quit_message)
			c.close()
			del CLIENT_LIST[c]
			break;
		elif message.startswith("/me"): # action handler
			send_all(message.replace("/me", " * " + username, 1))
		else: # vanilla message
			message = user_string + message
			send_all(message)
	
def send_all(m):
	"""
	Sends a message to all connected clients.
	
	Parameters:
	m -- the message to send
	"""
	for c in CLIENT_LIST:
		# for each client, send message
		c.send(bytes(m, "utf8"))
	print(m)

if __name__ == "__main__":
	print()
	set_up_server()
	SERVER.close()
	exit(0)
