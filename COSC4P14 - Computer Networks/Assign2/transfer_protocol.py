#!/usr/bin/env python3

from hashlib import md5
from random import random
from sys import argv, exit

def execution_instructions():
	"""Reminds user how to execute on error."""
	print("\nExecute the script as the below:")
	print(" $ ./transfer_protocol.py <Type>\n")
	print("Where the following are the arguments:")
	print(" <Type> -- the type of data transformation to use:")
	print("        -- 1 - Bits could be randomly flipped")
	print("        -- 2 - Messages could be dropped")
	print("        -- 3 - no transformation\n")
	exit(1)

if len(argv) != 2: # if inappropriate amount of args
	execution_instructions()
else:
	try:
		# if arg is valid and within range
		if int(argv[1]) and 3 >= int(argv[1]) >= 1:
			TYPE = int(argv[1]) # init global
		else: raise ValueError
	except ValueError:
		execution_instructions()

TYPE = int(argv[1])

class Message:
	"""Message class.
	
	Containerizes message with checksum.
	
	Attributes:
		bit_data : bit string of message.
		checksum : md5 checksum of message.
	"""

	def __init__(self, b, c):
		"""Message constructor."""
		self.bit_data, self.checksum = self.random_transformation(b), c
	
	def random_transformation(self, b):
		"""Perform some data transformation.
		
		Parameter:
			b : the bits to manipulate.
		"""
		m = list(b) # split into a list
		if TYPE == 1: # randomly flipping bits
			# randomly flipping bits
			for i in range(3, len(m)):
				if random() < 0.005 * (1/8): # small chance per bit in char
					m[i] = '0' if m[i] == '1' else '1'
		else: # dropped messages
			if random() < 0.50: # 50% chance to drop message
				for i in range(3, len(m)):
					m[i] = '' # erase each bit
		return ''.join(m) # concatenate back into bit string

	def get_bit_data(self):	return self.bit_data # bit_data accessor	
	def get_checksum(self):	return self.checksum # checksum accessor

class Sender:
	"""Sender class.
	
	One end of the connection between sender/recver, this one sends data.
	
	Attributes:
		data : raw data of message to send.
		bit_data : bit string of message.
	"""
	
	def __init__(self, file):
		"""Sender constructor."""
		self.data = self.load_data(file).encode('iso-8859-1')
		self.bit_data = bin(int.from_bytes(self.data, 'big'))
	
	def load_data(self, f):
		"""Loads data from file into message.
		
		Parameter:
			f : the filename of the file to load.
		"""
		return open(f, 'r').read()
	
	def send_message(self):
		"""Sends message between sender and receiver.
		
		Returns:
			A message container of data and its checksum.
		"""
		return Message(self.get_bit_data(), md5(self.data))
	
	def get_data(self):	return self.data # data accessor
	def get_bit_data(self):	return self.bit_data # bit_data accessor

class Receiver:
	"""Receiver class.
	
	The other end of the connection between sender/recver, this one recv data.
	
	Attributes:
		data : raw data of message received (decoded).
		bit_data : bit string of received message.
	"""
	
	def __init__(self):
		"""Receiver constructor."""
		self.data, self.bit_data = None, None
	
	def receive_message(self, m):
		"""Receives message from connection to sender.
		
		Parameter:
			m : the received message.
		"""
		self.bit_data = m.get_bit_data()
		self.data = int(self.bit_data, 2).to_bytes((int(self.bit_data, 2)\
			.bit_length()+7) // 8, 'big')
	
	def get_data(self):	return self.data # data accessor
	def get_bit_data(self): return self.bit_data # bit_data accessor

if __name__ == '__main__':
	sender = Sender('file.txt') # init sender
	receiver = Receiver() # init receiver
	print("\nMessage to send: \n")
	print(sender.get_data().decode('iso-8859-1'), '\n')
	count = 1
	while True: # continually send/receive data until correct
		message = sender.send_message() # sender sends message
		receiver.receive_message(message) # receiver receives message
		sent_checksum = message.get_checksum().hexdigest()
		recv_checksum = md5(receiver.data).hexdigest()
		print(f'Checksum of sent: {sent_checksum}')
		print(f'Checksum of recv: {recv_checksum}\n')
		if recv_checksum == sent_checksum:
			print("Message was received correctly.\n")
			break
		else:
			count += 1
			print("Message was not received correctly, resending message.\n")
	print("Message received: \n")
	print(receiver.get_data().decode('iso-8859-1'), '\n')
	print(f"There was {count} attempt(s) to correctly send the data.\n")
	exit(0)
	