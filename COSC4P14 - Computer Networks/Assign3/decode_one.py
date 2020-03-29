#!/usr/bin/env python3

from sys import argv, exit
from time import time

# if inappropriate argument(s), remind user on how to execute
if len(argv) != 2:
	print('\nExecute the script as the below:')
	print(' $ ./decode_one.py <File>\n')
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
	best_key, best_lfcr = 0, 0 # keep track of the best key and best lfcr count
	start_t = time() # time how long it took
	for potential_key in range(256): # brute force
		if potential_key % 32 == 0:
			print(f'Trying keys [{potential_key}, {potential_key+31}]...')
		count_lfcr = 0 # to count occurrences of CR+LF
		# since the file is Windows encoded, it is safe to assume that the
		# occurrence of CR followed by LF is high. Try to decode file based on
		# the currently iterated key, and then count the occurrences of CR+LF
		for i in range(len(ba)-1): # for every byte
			# xor byte and next byte with key, if this equals CR+LF, count++
			if ba[i] ^ potential_key == 13 and ba[i+1] ^ potential_key == 10:
				count_lfcr += 1
		if count_lfcr > best_lfcr: # if better than previously found
			best_lfcr = count_lfcr # reinitialize as new highest
			best_key = potential_key # keep track of key that gave highest
	end_t = time() # finish timing
	print(f'\nBest key was {best_key}, found in {end_t-start_t:.2f}s.')
	return best_key

def decode_file(ba, key):
	"""Decodes the file using the key.
	
	Parameters:
		ba : the bytearray of the file.
		key : the key used to decode it.
	"""
	print('\nSaving decoded file as \'../outputs/decodedA.txt\'...', end=' ')
	for i in range(len(ba)): # for each byte in bytearray
		ba[i] ^= key # xor it with found key
	open('../outputs/decodedA.txt', 'wb').write(ba)
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