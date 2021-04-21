#!/usr/bin/env python3

from socket import AF_INET, socket, SOCK_STREAM
from threading import Thread
from sys import exit, argv

def setup_server():
	"""Server setup function."""
	print('\nAttempting to bind server... ', end='')
	try:
		SERVER.bind((IP_ADDRESS, PORT)) # try to make server on IP:Port
	except OSError: # if failed
		print('failed!\n') # return for exit
		return
	print('done!')
	print(f'Connect Four server live on {IP_ADDRESS}:{PORT}.')
	# start a server thread to listen for connections
	server = Thread(target=listen_incoming_connections)
	server.start()
	server.join()

def listen_incoming_connections():
	"""Server listen function."""
	SERVER.listen() # listen for connections repeatedly
	print(f'Currently listening for client connections...\n')
	while True:
		(client1, address1) = SERVER.accept() # wait until first of pair joins
		print(f'{address1[1]} has connected.')
		(client2, address2) = SERVER.accept() # wait until second of pair joins
		print(f'{address2[1]} has connected.')
		print(f'\nConnecting {address1[1]} to {address2[1]}... ', end='')
		# send them off in their own thread to play a game
		Thread(target=game_thread, args=(client1, client2, )).start()
		print('done!\n')

def game_thread(client1, client2):
	"""Server game thread function.
	
	Params:
		client1 : the first of the pair of players.
		client2 : the second of the pair of players.
	"""
	client1.send(bytes('0', 'utf8')) # let first player know they are 'o'
	client2.send(bytes('1', 'utf8')) # let second player know they are 'x'
	while True: # continuously listen for moves and send to other player
		client2.send(client1.recv(RECV_SIZE)) # first's move is sent to second
		client1.send(client2.recv(RECV_SIZE)) # second's move is sent to first
		
if __name__ == '__main__':
	SERVER = socket(AF_INET, SOCK_STREAM) # create a server
	IP_ADDRESS = str(argv[1]) # IP to connect to
	PORT = int(argv[2]) # port to connect to
	RECV_SIZE = 4096 # packet size
	setup_server() # setup and bind the server
	SERVER.close() # closer server when finished
	exit(0)