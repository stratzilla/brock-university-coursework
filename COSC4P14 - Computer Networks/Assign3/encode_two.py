#!/usr/bin/env python3

from sys import argv, exit

if len(argv) != 2:
	print("\nExecute the script as the below:")
	print(" $ ./encode_two.py <File>\n")
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
	print('\nSaving encoded file as \'rs09co.answer.B.enc\'...', end=' ')
	for i in range(len(input)):
		input[i] ^= key_generator(key, i)
	open('rs09co.answer.B.enc', 'wb').write(input)
	print('done!\n')

def key_generator(num, idx):
	"""Generates keys from stream.
	
	Parameters:
		num : the 15-bit word to generate 8-bit word from.
		idx : how many cyclic bit shifts to perform.
	
	Returns:
		The 15-bit word bit shifted idx times, sliced to 8-bits.
	"""
	bin_num = bin(num)[2:] # convert to bit string
	for _ in range(15 - len(bin_num)):
		bin_num = '0' + bin_num # pad so 15-bits
	for _ in range(idx):
		bin_num = bin_num[8:] + bin_num[:8] # shift
	out_num = bin_num[:8] # get first 8 bits
	return int('0b' + out_num, 2) # convert back

if __name__ == '__main__':
	FILE = load_file(argv[1])
	KEY = 4582
	encode_file(FILE, KEY)
	exit(0)