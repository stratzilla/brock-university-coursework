#!/usr/bin/env python3

from sys import argv, exit

if len(argv) != 2:
	print("\nExecute the script as the below:")
	print(" $ ./encode_one.py <File>\n")
	print("Where <File> is the filename of the plaintext file to encode.\n")
	exit(1)

def load_file(input):
	"""Loads the argumented file as bytearray.
	
	Parameters:
		input : the filename to open.
	
	Returns:
		A bytearray from the input.
	"""
	print(f'\nLoading {input}...', end=' ')
	try:
		file = bytearray(open(input, 'rb').read())
		print('success!')
		return file
	except:
		print('failed!\n')
		exit(1)

def encode_file(input, key):
	"""Encodes the file using the key.
	
	Parameters:
		input : the bytearray of the file.
		key : the key used to encode it.
	"""
	print('\nSaving encoded file as \'out.answer.A.enc\'...', end=' ')
	for i in range(len(input)):
		input[i] ^= key
	open('out.answer.A.enc', 'wb').write(input)
	print('done!\n')

if __name__ == '__main__':
	FILE = load_file(argv[1])
	KEY = 100 + 42
	encode_file(FILE, KEY)
	exit(0)