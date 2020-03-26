#!/usr/bin/env python3

from sys import argv, exit
from time import time

if len(argv) != 2:
	print("\nExecute the script as the below:")
	print(" $ ./decode_one.py <File>\n")
	print("Where <File> is the filename of the encoded file to decode.\n")
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

def determine_key(input):
	"""Determine the key used to encrypt the file.
	
	Parameters:
		input : the bytearray of the file
		
	Returns:
		The key used to encrypt the file.
	"""
	print('\nBrute forcing key...\n')
	best_key = 0
	best_lfcr = 0
	start_t = time()
	for potential_key in range(256): # brute force
		if potential_key % 32 == 0:
			print(f'Trying keys [{potential_key}, {potential_key+32}]...')
		count_lfcr = 0
		for i in range(len(input)-1): # every byte
			# xor byte and next byte with key
			# if this equals CR+LF, add to count
			if input[i] ^ potential_key == 13 \
			and input[i+1] ^ potential_key == 10:
				count_lfcr += 1
			if count_lfcr > best_lfcr:
				# keep track of the best count
				best_lfcr = count_lfcr
				best_key = potential_key
	end_t = time()
	print(f'\nBest key was {best_key}, found in {end_t-start_t:.2f}s.')
	return best_key

def decode_file(input, key):
	"""Decodes the file using the key.
	
	Parameters:
		input : the bytearray of the file.
		key : the key used to decode it.
	"""
	print('\nSaving decoded file as \'decodedA.txt\'...', end=' ')
	for i in range(len(input)):
		input[i] ^= key
	open('decodedA.txt', 'wb').write(input)
	print('done!\n')

if __name__ == '__main__':
	FILE = load_file(argv[1])
	KEY = determine_key(FILE)
	decode_file(FILE, KEY)
	exit(0)