#!/usr/bin/env python3

from sys import argv, exit
from time import time

# if inappropriate argument(s), remind user on how to execute
if len(argv) != 2:
	print('\nExecute the script as the below:')
	print(' $ ./decode_two.py <File>\n')
	print('Where <File> is the filename of the encoded file to decode.\n')
	exit(1)

def determine_key(ba):
	"""Determine the key used to encrypt the file.
	
	Parameters:
		ba : the bytearray of the file
		
	Returns:
		The key used to encrypt the file.
	"""
	print('\nBrute forcing key...\n')
	best_key, best_e = 0, 0 # keep track of the best key and best e count
	start_t = time() # time how long it took
	for potential_key in range(32768): # brute force
		if potential_key % 4096 == 0:
			print(f'Trying keys [{potential_key}, {potential_key+4095}]...')
		count_e = 0 # to count occurrences of 'e'/'E'
		# since the file is English text, it is safe to assume that it also
		# follows English letter frequency. The most abundant letter in the
		# English language is 'E' and 'e', accounting for roughly 11% of all
		# letters used. Try to decode the file based on the currently iterated
		# key, and then count the occurrences of 'E' or 'e'
		for i in range(len(ba)): # for every byte
			key = key_generator(potential_key, i) # generate next key
			# xor byte with key, if this equals `e` or `E`, add to count
			if ba[i] ^ key == 69 or ba[i] ^ key == 101:
				count_e += 1
		if count_e > best_e: # if better than previously found
			best_e = count_e # reinitialize as new highest
			best_key = potential_key # keep track of key that gave highest
	end_t = time() # finish timing
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
	# this works like so: imagine a 15-bit number:
	#  010011100011001
	# this will shift the bits such that if you were to index
	# the 16th bit, it will loop around and catch the 1st bit
	# visualize it as an infinitely long bit string repetition:
	#  010011100011001 010011100011001 010011100011001 ...
	# this function returns segments of 8 bits from the repetition
	for _ in range(idx % 15):
		# if the string repeats every 15th position, you can use mod
		# to reduce the number of shifts, so instead of doing 17 shifts
		# only do two, et cetera. Without this, the algorithm takes 75min
		# and with this, it takes 85 seconds.
		bin_num = bin_num[8:] + bin_num[:8] # shift
	return int('0b' + bin_num[:8], 2) # convert back

def decode_file(ba, key):
	"""Decodes the file using the key.
	
	Parameters:
		ba : the bytearray of the file.
		key : the key used to decode it.
	"""
	print('\nSaving decoded file as \'../outputs/decodedB.txt\'...', end=' ')
	for i in range(len(ba)): # for each byte in bytearray
		the_key = key_generator(key, i) # generate next key
		ba[i] ^= the_key # xor it with found key
	open('../outputs/decodedB.txt', 'wb').write(ba)
	print('done!\n')

def load_file(filename):
	"""Loads the argumented file as bytearray.
	
	Parameters:
		filename : the filename to open.
	
	Returns:
		A bytearray from the input.
	"""
	print(f'\nLoading {filename}...', end=' ')
	try: # attempt to load file
		file = bytearray(open(filename, 'rb').read()) # load as bytearray
		print('success!')
		return file
	except: # catch IOErrors, FileNotFoundErrors, etc
		print('failed!\n')
		exit(1)

if __name__ == '__main__':
	FILE = load_file(argv[1])
	KEY = determine_key(FILE)
	decode_file(FILE, KEY)
	exit(0)