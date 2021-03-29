#!/usr/bin/env python3

from math import log, exp, ceil
from random import uniform, randint
from sys import exit, argv
import os.path
import io
import pickle

try: # attempt to import dependencies
	import matplotlib.pyplot as plt
	from PIL import Image
	from numpy import array
except ImportError: # if not found
	print("\nModules could not be loaded.")
	depend = "`matplotlib`, `PIL`, `numpy`"
	print(f"Ensure {depend} are installed before execution.\n")
	exit(1)

try: # initialization of constants from command line
	if len(argv) != 8 and len(argv) != 2: # if inappropriate number of arguments
		raise ValueError
	if len(argv) == 8: # if training
		NETWORK_SIZE = int(argv[1]) # dimensions of NxN network
		FRAME_SIZE = int(argv[2]) # dimensions of frame slices
		MAX_EPOCH = int(argv[3]) # number of training epochs
		# greyscale trange; hardcoded by default
		RANGE_MIN, RANGE_MAX = 0.00, 1.00
		LEARNING_RATE = float(argv[4]) # learning rate for training
		# maximum neighborhood radius around BMU
		MAX_NEIGHBORHOOD_RAD = int(argv[5])
		METRIC_SPACE = int(argv[6]) # metric space for distance calculation
		FILENAME = f"./images/{argv[7]}"
		if not os.path.isfile(FILENAME): # check if file exists
			raise ValueError
		# if any argument is outside valid range, raise an error
		if (NETWORK_SIZE & (NETWORK_SIZE-1) != 0) \
			or (FRAME_SIZE & (FRAME_SIZE-1) != 0) or MAX_EPOCH < 2 \
			or not (0.00 <= LEARNING_RATE <= 1.00) or MAX_NEIGHBORHOOD_RAD < 1 \
			or not (1 <= METRIC_SPACE <= 3):
				raise ValueError
	elif len(argv) == 2: # if decompressing
		FILENAME = f"./{argv[1]}"
		if not os.path.isfile(FILENAME):
			raise ValueError
		MAX_EPOCH = 1 # just since some functions need it
except ValueError: # remind user of proper execution instructions
	print("\nExecute the script as the below:\n")
	print(" $ ./image_som.py <args 1..7>\n")
	print("Where the arguments are as below:\n")
	print(" <arg1> - dimensions of the network")
	print("          (an exponent of 2, eg. 4, 8, 16, 32)")
	print(" <arg2> - dimensions of the slices")
	print("          (an exponent of 2, eg. 4, 8, 16, 32)")
	print(" <arg3> - number of training epochs [2, \u221E)")
	print(" <arg4> - learning rate [0.00, 1.00]")
	print(" <arg5> - fraction of network width to use for neighborhood")
	print("          (represented as a denominator (1/n); n = [1, \u221E))")
	print(" <arg6> - metric space to use for distance calculations")
	print("          (1 - Euclidean, 2 - Manhattan, 3 - Chebyshev)")
	print(" <arg7> - filename of image to used (located in /images)\n")
	print("Alternatively, you can execute the script as the below:\n")
	print(" $ ./image_som.py <arg1>\n")
	print("Where <arg1> is the filename for binary file to decompress.\n")
	exit(1)

def self_organizing_map():
	"""Self organizing map main driver.
	Loads the input image, initialized a set of input vectors from it, then
	initializes a random network, followed by training using the input.
	"""
	# create the NxN lattice of neurons
	lattice = generate_lattice()
	# load original image
	original_image = load_image()
	# create a set of slices from image to make input vectors
	input_vectors = image_to_input_vector(original_image)
	plot_title = "Initial conditions"
	# display initial random NxN lattice
	show_image(lattice_to_image(lattice), 0, plot_title)
	input("\nPress `Enter` to Begin Training\n")
	print(f"Training network for {MAX_EPOCH} epochs... ", end="", flush=True)
	for e in range(MAX_EPOCH+1):
		# get random input vector
		random_idx = randint(0, len(input_vectors)-1)
		random_input = input_vectors[random_idx]
		# find node in lattice which most closely resembles input vector
		bmu = find_best_matching_unit(lattice, random_input)
		# find some radius around the most similar node to then shift
		neighborhood = find_neighborhood(lattice, e)
		# update bmu and bmu neighborhood to closer resemble input vector
		weight_update(lattice, bmu, neighborhood, random_input, e)
		plot_title = f"Epoch {e} / {MAX_EPOCH}"
		# display current epoch's NxN lattice
		show_image(lattice_to_image(lattice), e+1, plot_title)
	print("Reconstructing image... ", end="", flush=True)
	plot_title = "Reconstructed Image"
	reconstructed, indices = recreate_image(input_vectors, lattice)
	print("done!\n")
	similar = similarity(reconstructed)
	print(f"The reconstructed image is {similar}% similar to the original.\n")
	print("Saving image to binary file in parent directory... ", \
		end="", flush=True)
	out_binary_file(indices, lattice)
	print("done!")
	# display final reconstructed image from feature map
	show_image(reconstructed, MAX_EPOCH+2, plot_title)
	# save to compressed file

def weight_update(lattice, best_matching_unit, neighborhood, vec, curr_epoch):
	"""Network weight update function.
	Updates weights of all nodes in network based on their distance from the
	best matching unit and neighborhood size as well as hyperparameters.

	Parameters:
		lattice : the network to update weights within.
		best_matching_unit : the BMU.
		neighborhood : the radius of the neighborhood around BMU.
		vec : an input vector.
		curr_epoch : the current epoch number.
	"""
	# learning rate decays as a function of current epoch and total epochs
	l_rate = (LEARNING_RATE * exp(-1 * (curr_epoch/MAX_EPOCH)))
	for x, _ in enumerate(lattice): # for each column
		for y, _ in enumerate(lattice[0]): # for each row
			# find distance of (x,y) to the BMU
			bmu_distance = distance([x, y], best_matching_unit)
			# if (x,y) is within neighborhood radius of BMU
			if bmu_distance < neighborhood:
				# neighborhood multiplier to update based on distance
				n_mult = exp(-1 * ((bmu_distance**2)/(2*(neighborhood**2))))
				for i, _ in enumerate(lattice[x][y]): # for each weight
					# update weight based on hyperparameters and input vector
					lattice[x][y][i] += (n_mult * l_rate * \
						(vec[i] - lattice[x][y][i]))
					# clamp just in case limitation on learning rate is removed
					# leading to invalid RGB values (eg. below 0 or above 1)
					lattice[x][y][i] = max(min(lattice[x][y][i], \
						RANGE_MAX), RANGE_MIN)

def find_neighborhood(lattice, curr_epoch):
	"""Finds neighborhood about best matching unit.
	Determines how large the neighborhood is based on a radius.

	Parameters:
		lattice : the network to find neighborhood around BMU in.
		curr_epoch : the current epoch number.
	Returns:
		The neighborhood radius.
	"""
	# the largest possible radius is a proportion of largest frame dimensions
	max_radius = (max(len(lattice), len(lattice[0])) / (MAX_NEIGHBORHOOD_RAD*2))
	# some time constant to decay neighborhood radius
	time_constant = MAX_EPOCH/log(max_radius)
	# return a decayed radius based on epoch and maximum radius
	return max_radius * exp(-1 * (curr_epoch/time_constant))

def find_best_matching_unit(lattice, vec):
	"""Finds the best matching unit node in the network.
	Using an input vector, determines which node most resembles the input.

	Parameters:
		lattice : the network to find BMU in.
		vec : an input vector.
	Returns:
		The best matching unit for the input vector.
	"""
	# the maximal distance is the distance between highest and lowest possible
	best_distance = distance([RANGE_MIN for _ in \
		range(FRAME_SIZE**2)], [RANGE_MAX for _ in range(FRAME_SIZE**2)])
	# best co-ordinates set outside of range so guaranteed overwrite
	best_coordinate = [-1, -1]
	for x, _ in enumerate(lattice): # for each column
		for y, _ in enumerate(lattice[x]): # for each row
			# find distance between (x,y) and input vector
			vector_distance = distance(lattice[x][y], vec)
			if vector_distance < best_distance: # if new best found
				# overwrite best distance and position
				best_distance = vector_distance
				best_coordinate = [x, y]
	return best_coordinate

def distance(p, q):
	"""Distance calculation.
	Finds the distance between two points using different metric spaces.

	d_eucl = sqrt((p_1 - q_1)^2 + (p_2 - q_2)^2 + ...)
	d_manh = |p_1 - q_1| + |p_2 - q_2| + ...
	d_cheb = max(|p_1 - q_1|, |p_2 - q_2|, ...)

	Parameters:
		p : the first point.
		q : the second point.
	Returns:
		The distance between p and q in the decided metric space.
	"""
	if METRIC_SPACE == 1: # euclidean
		return (sum(((p[i] - q[i])**2) for i in range(len(p))))**(0.5)
	if METRIC_SPACE == 2: # manhattan
		return sum(abs(p[i] - q[i]) for i in range(len(p)))
	# otherwise chebyshev
	distances = [abs(p[i] - q[i]) for i in range(len(p))]
	return max(distances)

def similarity(reconstructed):
	"""Function to determine similarity between images.
	Compares original image to reconstructed image from codebook.

	Parameters:
		reconstructed : the reconstructed image.
	Returns:
		A percentage of similarity.
	"""
	original = array(Image.open(FILENAME).convert("L"))/255.
	inverted = abs(original - 1)
	original, inverted = original.tolist(), inverted.tolist()
	worst_distance = distance([i for j in original for i in j], \
		[i for j in inverted for i in j])
	actual_distance = distance([i for j in original for i in j], \
		[i for j in reconstructed for i in j])
	return round((((actual_distance-worst_distance)*100)/(0-worst_distance)), 2)

def recreate_image_from_file(image_characteristics):
	"""Function to recreate image from binary file.
	Using the file's codebook and indices, recreates the image and displays
	it to the user. As well, it saves the image.

	Parameters:
		image_characteristics : the contents of the binary file.
	"""
	[img_frames, frame_size, net_size, book, indices] = image_characteristics
	image = [[0.00 for _ in range(img_frames*frame_size)] \
		for _ in range(img_frames*frame_size)]
	idx = 0
	for i in range(0, len(image), frame_size):
		for j in range(0, len(image[0]), frame_size):
			for k in range(frame_size):
				for l in range(frame_size):
					image[i+k][j+l] = book[indices[idx]][(k*frame_size)+l]
			idx += 1
	show_image(image, MAX_EPOCH+3, "Reconstructed from File")

def recreate_image(input_vectors, lattice):
	"""Recreates image from codebook.
	Uses SOM lattice and input vectors to map codebook to new image.

	Parameters:
		input_vectors : the original image's input vectors/slices.
		lattice : the final training lattice.
	Returns:
		A reconstructed image using the codebook and BMU for each input vector.
		A list of indices of codebook that comprises new image.
	"""
	# make empty input vector set
	reconstructed = [[0 for _ in range(len(input_vectors[0]))] \
		for _ in range(len(input_vectors))]
	list_indices = [] # empty list for indices
	for i in range(len(input_vectors)):
		# find BMU and place it into new input vectors
		(x, y) = find_best_matching_unit(lattice, input_vectors[i])
		reconstructed[i] = lattice[x][y]
		# also take the index of the BMU and add to indices to return later
		list_indices.append((x*len(lattice))+y)
	# convert found vectors into an image
	return input_vector_to_image(reconstructed), list_indices

def lattice_to_image(lattice):
	"""Lattice to image function.
	Converts a lattice into an image.

	Parameters:
		lattice : the lattice to convert.
	Returns:
		An image corresponding to the lattice.
	"""
	# make an empty image to fill later
	image = [[0 for _ in range(len(lattice)*FRAME_SIZE)] \
		for _ in range(len(lattice[0])*FRAME_SIZE)]
	for i in range(0, len(image), FRAME_SIZE):
		for j in range(0, len(image[0]), FRAME_SIZE):
			for k in range(FRAME_SIZE):
				for l in range(FRAME_SIZE):
					image[i+k][j+l] = lattice[int(i/FRAME_SIZE)]\
						[int(j/FRAME_SIZE)][(k*FRAME_SIZE)+l]
	return image

def input_vector_to_image(input_vectors):
	"""Input vectors to image function.
	Converts a set of input vectors into an image.

	Parameters:
		input_vectors : the input vectors to convert.
	Returns:
		An image corresponding to the input vectors.
	"""
	# make an empty image to later fill
	image = [[0 for _ in range(int((len(input_vectors) * \
		len(input_vectors[0]))**(0.5)))] for _ in \
		range(int((len(input_vectors) * len(input_vectors[0]))**(0.5)))]
	idx = 0
	for i in range(0, len(image), FRAME_SIZE):
		for j in range(0, len(image[0]), FRAME_SIZE):
			for k in range(FRAME_SIZE):
				for l in range(FRAME_SIZE):
					image[i+k][j+l] = input_vectors[idx][(k*FRAME_SIZE)+l]
			idx += 1
	return image

def image_to_input_vector(image):
	"""Image to Input vectors function.
	Converts an image into a set of input vectors.

	Parameters:
		image : the image to convert.
	Returns:
		A set of input vectors corresponding to the image.
	"""
	input_vectors = []
	for i in range(0, len(image), FRAME_SIZE):
		for j in range(0, len(image[0]), FRAME_SIZE):
			vector = []
			for k in range(FRAME_SIZE):
				for l in range(FRAME_SIZE):
					vector.append(image[i+k][j+l])
			input_vectors.append(vector)
	return input_vectors

def show_image(data, curr_epoch, title):
	"""Image displayer for animation of training.
	Interfaces `matplotlib` to illustrate how SOM trains over time.

	Parameters:
		data : the image to display.
		curr_epoch : the current training epoch number.
		title : title of the image being display.
	"""
	plt.imshow(data, cmap='gray', vmin=0.00, vmax=1.00) # load data into frame
	plt.title(title) # denote epoch number
	plt.axis('off') # remove axes
	plt.pause(0.01) # delay between frames
	if curr_epoch <= MAX_EPOCH: # clear frame for redraw if non-terminal frame
		plt.clf()
	elif curr_epoch == MAX_EPOCH+1: # show user finished codebook
		print("done!")
		# save just figure after closing shown window
		plt.title("")
		print("Saving feature map to parent directory... ", end="", flush=True)
		save_destination = f"./feature_map.png"
		plt.savefig(save_destination, bbox_inches='tight', pad_inches=0.1)
		print("done!\n")
		print("Close figure window to reconstruct image.\n")
		plt.title("Finished training")
		plt.show() # maintain window
	elif curr_epoch == MAX_EPOCH+2: # show user recreated image from memory
		plt.title("")
		print("Saving reconstructed image to parent directory... ", \
			end="", flush=True)
		save_destination = f"./reconstructed.png"
		plt.savefig(save_destination, bbox_inches='tight', pad_inches=0.1)
		print("done!\n")
		print("Close figure window to leave program.\n")
		plt.title("Reconstructed Image")
		plt.show()
	else: # show user recreated image from binary file
		plt.title("")
		print("Saving reconstructed image to parent directory... ", \
			end="", flush=True)
		save_destination = f"./reconstructed_from_file.png"
		plt.savefig(save_destination, bbox_inches='tight', pad_inches=0.1)
		print("done!\n")
		print("Close figure window to leave program.\n")
		plt.show()

def in_binary_file(filename):
	"""Binary file reading.
	Makes a list of image characteristics taken from binary file.

	Parameters:
		filename : the binary file's name.
	Returns:
		A list of image characteristics.
	"""
	infile = open(filename, 'rb')
	from_binary = pickle.load(infile)
	infile.close()
	return from_binary

def out_binary_file(indices, lattice):
	"""Binary file creation.
	Stores the image characteristics to binary file.

	Parameters:
		indices : the indices of BMUs for codebook.
		lattice : the trained network.
	"""
	to_binary = [int(((len(indices)**(0.5)))), # how many frames per dim of img
				 int(len(lattice[0][0])**(0.5)), # dim of frame
				 int(len(lattice)), # dim of network
				 # codebook
				 [lattice[i][j] for i in range(len(lattice)) \
					for j in range(len(lattice[0]))],
				 indices] # BMU indices
	outfile = open('./binary_output', 'wb') # open file to save
	pickle.dump(to_binary, outfile) # save to file
	outfile.close()

def rand_weights():
	"""Random weight generator.
	Generates a weight within range (default 0.00 .. 1.00).

	Returns:
		A random weight within the specified range.
	"""
	return [round(uniform(RANGE_MIN, RANGE_MAX),2) \
		for _ in range(FRAME_SIZE**2)]

def generate_lattice():
	"""Network initialization.
	Generates a lattice of NxN nodes of random weights.

	Returns:
		An NxN network of random weights.
	"""
	return [[rand_weights() for _ in range(NETWORK_SIZE)] \
		for _ in range(NETWORK_SIZE)]

def load_image():
	"""Loads an image into an array.
	Interfaces both `PIL` and `numpy` to load image, convert to grayscale,
	then finally convert to a list for use.

	Returns:
		A list comprising of grayscale values of the input image.
	"""
	return (array(Image.open(FILENAME).convert("L"))/255.).tolist()

if __name__ == '__main__':
	if len(argv) == 8: # if training
		# on successful initialization, display chosen parameters in console
		print(f"\nNetwork dimensions: {NETWORK_SIZE}x{NETWORK_SIZE}")
		print(f"Frame slice dimensions: {FRAME_SIZE}x{FRAME_SIZE}")
		print(f"Number of training epochs: {MAX_EPOCH}")
		print(f"Learning rate: {LEARNING_RATE}")
		print(f"Size of neighborhood proportion: 1/{MAX_NEIGHBORHOOD_RAD}")
		print(f"Image used: {FILENAME}")
		MS_STRING = ["Euclidean", "Manhattan", "Chebyshev"]
		print(f"Metric space used: {MS_STRING[METRIC_SPACE-1]}")
		self_organizing_map() # begin training
	elif len(argv) == 2: # if recreating from binary file
		print(f"\nRecreating {FILENAME}... ", end="", flush=True)
		image_characteristics = in_binary_file(FILENAME)
		print("done!")
		recreate_image_from_file(image_characteristics)
	exit(0)
