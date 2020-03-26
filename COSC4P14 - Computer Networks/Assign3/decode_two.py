#!/usr/bin/env python3

from sys import argv, exit
from time import time

if len(argv) != 2:
	print("\nExecute the script as the below:")
	print(" $ ./decode_two.py <File>\n")
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
	best_e = 0
	start_t = time()
	for potential_key in range(32768): # brute force
		if potential_key % 2048 == 0:
			print(f'Trying keys [{potential_key}, {potential_key+2048}]...')
		count_e = 0
		for i in range(len(input)): # every byte
			# xor byte with key
			# if this equals `e` or `E`, add to count
			key = key_generator(potential_key, i)
			if input[i] ^ key == 69 \
			or input[i] ^ key == 101:
				count_e += 1
			if count_e > best_e:
				# keep track of the best count
				best_e = count_e
				best_key = potential_key
	end_t = time()
	print(f'\nBest key was {best_key}, found in {end_t-start_t:.2f}s.')
	return best_key

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

def decode_file(input, key):
	"""Decodes the file using the key.
	
	Parameters:
		input : the bytearray of the file.
		key : the key used to decode it.
	"""
	print('\nSaving decoded file as \'decodedB.txt\'...', end=' ')
	for i in range(len(input)):
		the_key = key_generator(key, i)
		input[i] ^= the_key
	open('decodedB.txt', 'wb').write(input)
	print('done!\n')

if __name__ == '__main__':
	FILE = load_file(argv[1])
	KEY = determine_key(FILE)
	decode_file(FILE, KEY)
	exit(0)