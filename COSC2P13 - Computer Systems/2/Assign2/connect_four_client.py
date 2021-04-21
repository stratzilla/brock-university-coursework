#!/usr/bin/env python3

from socket import AF_INET, socket, SOCK_STREAM
from threading import Thread
from sys import exit, argv

def check_winner(board, win_condition, player):
	"""Board evaluation function.
	
	Determines if a winner exists by search for continuous sequences of 
	identical pieces in all directions.
	
	Parameters:
		board : the game board to evaluate.
		win_condition : how long the continuous sequence needs to be.
		player : the player to check.
	
	Returns:
		T/F if `player` has a sequence of moves that's considered a win.
	"""
	sequence = 0 # to keep track of how long continuous sequences are
	# check horizontally
	for i in range(len(board[0])-3):
		for j in range(len(board)):
			for k in range(win_condition):
				if board[j][i+k] == player:
					sequence += 1
				if sequence == win_condition:
					return True
			sequence = 0
	# check vertically
	for i in range(len(board[0])):
		for j in range(len(board)-3):
			for k in range(win_condition):
				if board[j+k][i] == player:
					sequence += 1
				if sequence == win_condition:
					return True
			sequence = 0
	# check diagonally (like /)
	for i in range(len(board[0])-3):
		for j in range(3, len(board), 1):
			for k in range(win_condition):
				if board[j-k][i+k] == player:
					sequence += 1
				if sequence == win_condition:
					return True
			sequence = 0
	# check diagonally (like \)
	for i in range(len(board[0])-3):
		for j in range(len(board)-3):
			for k in range(win_condition):
				if board[j+k][i+k] == player:
					sequence += 1
				if sequence == win_condition:
					return True
			sequence = 0
	return False

def make_move(board, move, player):
	"""Function to place piece on board.
	
	Parameters:
		board : the game board to place piece on.
		move : the position of the piece to place.
		player : which player made this move.
	"""
	# check for first open position in column
	for i in range(len(board)-1, -1, -1):
		if board[i][move] == -1: # check if row/col is empty
			board[i][move] = player # if so, place the piece
			break

def prompt_move(board):
	"""Function to prompt user for move to make.
	
	Parameters:
		board : the game board to make move on.
	
	Returns:
		A column in which to place a piece.
	"""
	while(True): # a valid move will leave loop
		print("What move would you like to make? (Aa-Gg) : ", end='')
		user_move = ord(input()) # prompt user for move
		if user_move == 113 or user_move == 81: # if Qq, quit game
			print('\nYou\'ve forfeited the game.\n')
			CLIENT.send(bytes(str(user_move), 'utf8')) # let server know
			CLIENT.close() # close the client
			exit(0) # exit execution
		if not 97 <= user_move <= 103 \
		and not 65 <= user_move <= 71: # if move is not within A-G or a-g
			print("This is an invalid move.\n")
			continue # restart loop
		if user_move <= 71: # if A-G
			user_move -= 65 # convert to 0-6
		elif user_move <= 103: # if a-g
			user_move -= 97 # convert to 0-6
		if board[0][user_move] == -1: # if move can be made
			return user_move # break loop and return the column number
		else: # if column cannot fit a new piece
			print("This column is full.\n")

def print_board(board):
	"""Board displayer function.
	
	Parameters:
		board : the game board to display to console.
	"""
	print()
	for i in range(len(board)):
		print(' ', end='')
		for _ in range(len(board[0])):
			print('+---', end='')
		print('+\n |', end='')
		for j in range(len(board[0])):
			piece = ''
			if board[i][j] == 1:
				piece = ' x '
			elif board[i][j] == 0:
				piece = ' o '
			else:
				piece = '   '
			print(piece, end='|')
		print()
	print(' ', end='')
	for _ in range(len(board[0])):
		print('+---', end='')
	print('+\n  ', end='')
	for i in range(len(board[0])):
		print(f' {chr(65+i)} ', end=' ')
	print('\n')

def initialize_board(rows, cols):
	"""Board initialization function.
	
	Parameters:
		rows : how many rows in the board.
		cols : how many columns in the board.
	
	Returns:
		A rows x cols game board.
	"""
	return [[-1 for _ in range(cols)] for _ in range(rows)]

def setup_client():
	"""Client setup function."""
	print('\nAttempting to connect to server... ', end='')
	try: # attempt to connect to IP and port
		CLIENT.connect((IP_ADDRESS, PORT))
	except ConnectionRefusedError: # if cannot connect, return back to main
		print('failed!\n')
		return
	print('done!')
	print(f'Connected to server at {IP_ADDRESS}:{PORT}.')
	GAME_LOOP = Thread(target=game_thread) # otherwise begin the game loop
	GAME_LOOP.start() # start thread
	GAME_LOOP.join() # rejoin main thread

def game_thread():
	"""Main gameplay thread."""
	# wait for server to assign you a player number
	player = int(CLIENT.recv(RECV_SIZE).decode('utf8')) # get the player number
	print(f'You are player {PIECE_SHAPES[player]}.')
	current_player = 0 # 0th player begins first
	board = initialize_board(NUM_ROWS, NUM_COLS) # initialize the game board
	while True: # until game is over
		print_board(board) # display board to player
		if player == current_player: # if your turn
			move = prompt_move(board) # get your move via io
			make_move(board, move, current_player) # place the piece on board
			CLIENT.send(bytes(str(move), 'utf8')) # let other player know
		else: # if opponent's turn
			print('Waiting for opponent to make a move...')
			# receive move from server
			move = int(CLIENT.recv(RECV_SIZE).decode('utf8'))
			if move == 113: # if quitting
				print('\nYou\'ve won by forfeit.\n')
				CLIENT.close() # close client
				exit(0)
			# otherwise place the piece for your opponent on your board
			make_move(board, move, current_player)
		# check if the latest move is a winning move
		if check_winner(board, WIN_CONDITION, current_player):
			print_board(board) # if so, print the final board state
			print(f'Player {PIECE_SHAPES[current_player]} has won!\n')
			break # break out to return to main
		else:
			# alternate which player's turn it is
			current_player = 0 if current_player == 1 else 1

if __name__ == '__main__':
	NUM_ROWS = 6 # how many rows for game board
	NUM_COLS = 7 # how many columns for game board
	WIN_CONDITION = 4 # length of sequence to win Connect Four
	PIECE_SHAPES = ['o', 'x'] # the player pieces
	CLIENT = socket(AF_INET, SOCK_STREAM) # create client
	IP_ADDRESS = str(argv[1]) # IP to connect to
	PORT = int(argv[2]) # port to connect to
	RECV_SIZE = 4096 # packet size
	setup_client() # set up client
	CLIENT.close() # close client
	exit(0)