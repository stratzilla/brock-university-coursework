#!/usr/bin/env python3

from math import log, exp
from random import uniform, randint
from sys import exit, argv

try: # attempt to import dependency
	import matplotlib.pyplot as plt
except ImportError: # if not found
	print("\nModules could not be loaded.")
	print("Ensure `matplotlib` is installed before execution.\n")
	exit(1)

try: # initialization of constants from command line
	if len(argv) != 8: raise Error # if inappropriate number of arguments
	WIDTH, HEIGHT = int(argv[1]), int(argv[2]) # dimensions of NxN network
	NUM_VECTORS = int(argv[3]) # number of input vectors
	MAX_EPOCH = int(argv[4]) # number of training epochs
	LEARNING_RATE = float(argv[5]) # learning rate for training
	MAX_NEIGHBORHOOD_RAD = int(argv[6]) # maximum neighborhood radius around BMU
	METRIC_SPACE = int(argv[7]) # metric space for distance calculation
	RANGE_MIN, RANGE_MAX = 0, 1 # color range; hardcoded by default
	DIMENSIONS = 3 # color channels; hardcoded by default
	# if any argument is outside valid range, raise an error
	if WIDTH < 5 or HEIGHT < 5 or NUM_VECTORS < 2 or MAX_EPOCH < 2 \
		or not (0.00 < LEARNING_RATE < 1.00) or MAX_NEIGHBORHOOD_RAD < 1 \
		or not (1 <= METRIC_SPACE <= 3):
			raise Error
except:	# remind user of proper execution instructions
	print("\nExecute the script as the below:\n")
	print(" $ ./color_som.py <args 1..7>\n")
	print("Where the arguments are as below:\n")
	print(" <arg1> - width of the network [5, \u221E)")
	print(" <arg2> - height of the network [5, \u221E)")
	print(" <arg3> - number of input vectors [2, \u221E)")
	print(" <arg4> - number of training epochs [2, \u221E)")
	print(" <arg5> - learning rate [0.00, 1.00]")
	print(" <arg6> - fraction of network width to use for neighborhood")
	print("          represented as a denominator (1/n); n = [1, \u221E)")
	print(" <arg7> - metric space to use for distance calculations")
	print("          (1 - Euclidean, 2 - Manhattan, 3 - Chebyshev)\n")
	exit(1)
finally: # on successful initialization, display chosen parameters in console
	print(f"\nNetwork dimensions: {WIDTH}x{HEIGHT}")
	print(f"Number of input color vectors: {NUM_VECTORS}")
	print(f"Number of training epochs: {MAX_EPOCH}")
	print(f"Learning rate: {LEARNING_RATE}")
	print(f"Size of neighborhood proportion: 1/{MAX_NEIGHBORHOOD_RAD}")
	ms_string = ["Euclidean", "Manhattan", "Chebyshev"]
	print(f"Metric space used: {ms_string[METRIC_SPACE-1]}")

def self_organizing_map():
	"""Self organizing map main driver.
	Initializes random network lattice, random set of input vectors, and trains.
	"""
	# create the NxN lattice of neurons
	lattice = generate_lattice()
	# create a set of random input vectors (random colors)
	input_vectors = generate_input_vectors()
	show_image(lattice, 0) # display initial random NxN lattice
	input("\nPress Enter to Begin Training\n")
	for e in range(MAX_EPOCH+1):
		# get random input vector
		random_idx = randint(0, NUM_VECTORS-1)
		random_input = input_vectors[random_idx]
		# find node in lattice which most closely resembles input vector
		bmu = find_best_matching_unit(lattice, random_input)
		# find some radius around the most similar node to then shift
		neighborhood = find_neighborhood(lattice, bmu, e)
		# update bmu and bmu neighborhood to closer resemble input vector
		weight_update(lattice, bmu, neighborhood, random_input, e)
		show_image(lattice, e) # display current epoch's NxN lattice

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
	for x in range(len(lattice)): # for each column
		for y in range(len(lattice[0])): # for each row
			# find distance of (x,y) to the BMU
			bmu_distance = distance([x,y], best_matching_unit)
			# if (x,y) is within neighborhood radius of BMU
			if bmu_distance < neighborhood:
				# neighborhood multiplier to update based on distance
				n_mult = exp(-1 * ((bmu_distance**2)/(2*(neighborhood**2))))
				for i in range(len(lattice[x][y])): # for each weight
					# update weight based on hyperparameters and input vector
					lattice[x][y][i] += (n_mult * l_rate * \
						(vec[i] - lattice[x][y][i]))

def find_neighborhood(lattice, best_matching_unit, curr_epoch):
	"""Finds neighborhood about best matching unit.
	Determines how large the neighborhood is based on a radius.

	Parameters:
		lattice : the network to find neighborhood around BMU in.
		best_matching_unit : the BMU.
		curr_epoch : the current epoch number.
	Returns:
		The neighborhood radius.
	"""
	# the largest possible radius is a proportion of largest frame dimensions
	max_radius = (max(len(lattice), len(lattice[0])) / (MAX_NEIGHBORHOOD_RAD*2))
	# some time constant to decay neighborhood radius
	time_constant = MAX_EPOCH/log(max_radius)
	# return a decayed radius based on epoch and maximum radius
	return (max_radius * exp(-1 * (curr_epoch/time_constant)))

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
		range(DIMENSIONS)], [RANGE_MAX for _ in range(DIMENSIONS)])
	# best co-ordinates set outside of range so guaranteed overwrite
	best_coordinate = [-1, -1]
	for x in range(len(lattice)): # for each column
		for y in range(len(lattice[x])): # for each row
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
		return ((sum(((p[i] - q[i])**2) for i in range(len(p))))**(0.5))
	elif METRIC_SPACE == 2: # manhattan
		return (sum(abs(p[i] - q[i]) for i in range(len(p))))
	else: # chebyshev
		distances = [abs(p[i] - q[i]) for i in range(len(p))]
		return max(distances)

def rand_weights():
	"""Random weight generator.
	Generates a weight within range (default 0.00 .. 1.00).

	Returns:
		A random weight within the specified range.
	"""
	return [uniform(RANGE_MIN, RANGE_MAX) for _ in range(DIMENSIONS)]

def generate_input_vectors():
	"""Input vectors initialization.
	Generates a set of random input vectors (colors).

	Returns:
		A set of random input vectors.
	"""
	return [rand_weights() for _ in range(NUM_VECTORS)]

def generate_lattice():
	"""Network initialization.
	Generates a lattice of NxN nodes of random weights.

	Returns:
		An NxN network of random weights.
	"""
	return [[rand_weights() for _ in range(WIDTH)] for _ in range(HEIGHT)]

def show_image(data, curr_epoch):
	"""Image displayer for animation of training.
	Interfaces `matplotlib` to illustrate how SOM trains over time.

	Parameters:
		data : the NxN array of 3-tuples of color data (pixels).
		curr_epoch : the current training epoch number.
	"""
	plt.imshow(data) # load data into frame
	plt.title(f"Epoch {curr_epoch}") # denote epoch number
	plt.axis('off') # remove axes
	plt.pause(0.01) # delay between frames
	if curr_epoch < MAX_EPOCH: # clear frame for redraw if non-terminal frame
		plt.clf()
	else: # maintain window if terminal frame
		plt.show()

if __name__ == '__main__':
	self_organizing_map()
	exit(0)
