#!/usr/bin/env python3

import csv
import random
from math import floor, ceil, exp
import matplotlib.pyplot as plt
import copy

def genetic_network(el_p, to_p, dim, epochs):
	"""Genetic Neural Network training function.
	
	Parameters:
		el_p : the proportion of elites
		to_p : the proportion of tournament
		dim : dimensionality of network.
		epochs : how many generations to run.
	
	Returns:
		A trained neural network.
	"""
	# initialize network as initially random
	population = initialize_population(POP_SIZE, dim)
	for e in range(1, epochs+1):
		# sort the population by fitness
		population = sort_population(population)
		error = mse(population[0], TRAIN)
		tra_p = performance_measure(population[0], TRAIN)
		tes_p = performance_measure(population[0], TRAIN)
		print(f'{e}, {error:.4f}, {tra_p:.2f}, {tes_p:.2f}')
		# get fitness of network
		#MSE.append(mse(population[0], TRAIN))
		# training accuracy of network
		#TRP.append(performance_measure(population[0], TRAIN))
		# testing accuracy of network
		#TEP.append(performance_measure(population[0], TEST))
		mating_pool = [] # init mating pool
		# get elites from population
		elites = elite_selection(population, el_p)
		del population[:len(elites)] # remove elites
		# find tournament and winner
		t_winner = tournament_selection(population, to_p)
		# add tournament victor and elites to mating pool
		mating_pool.extend(elites)
		mating_pool.append(t_winner)
		# generate a new population based on mating pool
		population = evolve(mating_pool, elites)
		mating_pool.clear() # clear mating pool for next gen
	population = sort_population(population)
	return population[0]

def evolve(mating_pool, elites):
	"""Evolves population based on genetic operators.
	
	Parameters:
		mating_pool : where to select parents from.
		elites : previously found elites.
	
	Returns:
		A new population of offspring from mating pool.
	"""
	new_population = [] # store new population as list
	new_population += elites # add elites verbatim
	# while population isn't at max size
	while len(new_population) < POP_SIZE:
		# get both parents indices
		p_a_idx = random.randrange(len(mating_pool))
		p_b_idx = random.randrange(len(mating_pool))
		# we don't mind parents having identical genes but we don't
		# want the parents to use the same index. Parent A can be
		# equal to Parent B, but Parent A cannot be Parent B
		if p_a_idx == p_b_idx:
			continue
		# get the parents from indices
		parent_a = mating_pool[p_a_idx]
		parent_b = mating_pool[p_b_idx]
		# find children using crossover
		child_a, child_b = crossover(parent_a, parent_b)
		# mutate each child
		child_a = mutation(child_a)
		child_b = mutation(child_b)
		# add children to population
		new_population += [child_a, child_b]
	return [initialize_network(FEATURES, HIDDEN_SIZE, CLASSES, \
		new_population[i]) for i in range(len(new_population))]

def crossover(parent_a, parent_b):
	"""Two-point crossover operator.
	
	Parameters:
		parent_a : the first parent.
		parent_b : the second parent.
	
	Returns:
		Two child chromosomes as a product of both parents.
	"""
	# only perform crossover based on the crossover rate
	if random.uniform(0.00, 1.00) >= CROSS_RATE:
		return copy.deepcopy([parent_a, parent_b])
	child_a, child_b = [], []
	# find pivot points at random 1..n-1
	pivot_a = random.randint(1, len(parent_a)-1)
	# second pivot is between pivot_a..n-1
	pivot_b = random.randint(pivot_a, len(parent_a)-1)
	for i in range(0, len(parent_a)):
		# before first pivot, use genes from one parent
		if i < pivot_a:
			child_a.append(parent_a[i])
			child_b.append(parent_b[i])
		# before second pivot, use genes from second parent
		elif i < pivot_b:
			child_a.append(parent_b[i])
			child_b.append(parent_a[i])
		# after second pivot, use genes from first parent again
		else:
			child_a.append(parent_a[i])
			child_b.append(parent_b[i])
	return child_a, child_b

def mutation(child):
	"""Mutation operator.
	
	Parameters:
		child : the chromosome to mutate.
	
	Returns:
		A mutated child.
	"""
	for i in range(len(child)):
		# only perform mutation based on the mutation rate
		if random.uniform(0.00, 1.00) <= MUTAT_RATE:
			# update that axes with random position
			child[i] = random.uniform(-1.00, 1.00)
	return child

def elite_selection(population, percent):
	"""Elite selection function.
	Stores elites to bring into the next generation and mating pool.
	
	Parameters:
		population : the population to take elites from.
		percent : the proportion of the population to consider elites.
	
	Returns:
		A list of elite solutions.
	"""
	elites = []
	# grab percent% best individuals
	for i in range(ceil(len(population)*percent)):
		elites.append(population[i]) # and append to elites
	return [decode_chromosome(elites[i]) for i in range(len(elites))]

def tournament_selection(population, percent):
	"""Tournament selection function.
	Creates a tournament of random individuals and returns the best.
	
	Parameters:
		population : the population to take tournament from.
		percent : the proportion of the population in the tournament.
	
	Returns:
		Best fit individual from tournament.
	"""
	tournament = []
	# grab percent% random individuals
	for i in range(ceil(len(population)*percent)):
		random_idx = random.randint(0, len(population)-1)
		tournament.append(population.pop(random_idx))
	tournament = sort_population(tournament) # sort by fitness
	# return best fit from tournament
	return decode_chromosome(tournament[0])

def sort_population(population):
	"""Sort population by fitness.
	
	Parameters:
		population : the population to sort.
		
	Returns:
		The sorted population.
	"""
	return sorted(population, key=lambda x: mse(x, TRAIN))

def initialize_population(size, dim):
	"""Initializes a random population.
	
	Parameters:
		size : the size of the population.
		dim : the dimensionality of the problem
	
	Returns:
		A random population of that many points.
	"""
	population = [] # population stored as a list
	for i in range(size): # for the size of the population
		point = [] # point as well as a list
		for j in range(dim): # to fill each axes
			# generate a random position for that axes
			point.append(random.uniform(-0.50, 0.50))
		population.append(point) # add to population
	return [initialize_network(FEATURES, HIDDEN_SIZE, CLASSES, \
		population[i]) for i in range(len(population))]

def decode_chromosome(candidate):
	"""Decodes a network candidate into a 1D chromosome.
	
	Parameters:
		candidate : the network to decode.
	
	Returns:
		A 1D list of weights as a chromosome of genes.
	"""
	chromosome = [] # store chromosome as list
	for layer in candidate:
		for neuron in layer:
			for weight in neuron:
				# add weight to chromosome as gene
				chromosome.append(weight)
	return chromosome

def sse(actual, target):
	"""Sum of Squared Error.
	
	Parameters:
		actual : network output.
		target : example target output.
	
	Returns:
		The sum of squared error of the network for an example.
	"""
	summ = 0.00
	for i in range(len(actual)):
		summ += (actual[i] - target[i])**2
	return summ

def mse(network, training):
	"""Mean Squared Error.
	
	Parameters:
		network : the neural network to test.
		training : a set of training data to test with.
	"""
	summ = 0.00
	# for each training example
	for example in training:
		# populate a target vector
		target = [0 for _ in range(CLASSES)]
		# denote correct classification
		target[int(example[-1])] = 1
		# get actual output by feeding example through network
		actual = feed_forward(network, example)
		# sum up the sum of squared error
		summ += sse(actual, target)
	# MSE is just sum(sse)/number of examples
	return summ / len(training)
	
def feed_forward(network, example):
	"""Feedforward method. Feeds data forward through network.
	
	Parameters:
		network : the neural network.
		example : an example of data to feed forward.
	
	Returns:
		The output of the forward pass.
	"""
	layer_input, layer_output = example, []
	for layer in network:
		for neuron in layer:
			# sum the weight with inputs
			summ = summing_function(neuron, layer_input)
			# activate the sum, append output to outputs
			layer_output.append(activation_function(summ))
		# inputs become outputs of previous layer
		layer_input, layer_output = layer_output, []
	return layer_input # return the final output

def summing_function(weights, inputs):
	"""Sums the synapse weights with inputs and bias.
	
	Parameters:
		weights : synaptic weights.
		inputs : a vector of inputs.
	
	Returns:
		The aggregate of inputs times weights, plus bias.
	"""
	# bias is the final value in the weight vector
	bias = weights[-1]
	summ = 0.00 # to sum
	for i in range(len(weights)-1):
		# aggregate the weights with input values
		summ += (weights[i] * float(inputs[i]))
	return summ + bias

def activation_function(z):
	"""ReLU activation function.
	
	Parameters:
		z : summed output of neuron.
	
	Returns:
		The neuron activation based on the summed output.
	"""
	return z if z >= 0 else 0

def initialize_network(n, h, o, c):
	"""Neural network initializer.
	
	Parameters:
		n : the number of input neurons.
		h : the number of hidden neurons.
		o : the number of output neurons.
		c : the chromosome to encode into the network.
	
	Returns:
		The n-h-o neural network.
	"""
	chr = iter(c) # make iterator from c
	neural_network = [] # initially an empty list
	# there are (n * h) connections between input layer and hidden layer
	neural_network.append([[next(chr) for i in range(n+1)] for j in range(h)])
	# there are (h * o) connections between hidden layer and output layer
	neural_network.append([[next(chr) for i in range(h+1)] for j in range(o)])
	return neural_network

def performance_measure(network, data):
	"""Measures accuracy of the network using classification error.
	
	Parameters:
		network : the neural network.
		data : a set of data examples.
	Returns:
		A percentage of correct classifications.
	"""
	correct, total = 0, 0
	for example in data:
		# check to see if the network output matches target output
		if check_output(network, example) == float(example[-1]):
			correct += 1
		total += 1
	return 100*(correct / total)

def check_output(network, example):
	"""Compares network output to actual output.
	
	Parameters:
		network : the neural network.
		example : an example of data.
	Returns:
		The class the example belongs to (based on network guess).
	"""
	output = feed_forward(network, example)
	return output.index(max(output))

def load_data(filename):
	"""Loads CSV for splitting into training and testing data.
	
	Parameters:
		filename : the filename of the file to load.
	
	Returns:
		Two lists, each corresponding to training and testing data.
	"""
	with open(filename, newline='\n') as csv_file: # open CSV
		data = [] # store data as a master list
		rows = csv.reader(csv_file, delimiter=',') # get each row
		for row in rows: # for each row
			data.append(row) # add to data list
	random.shuffle(data) # randomize data order
	training_data = data[:floor(len(data)*0.70)] # 70% split train
	testing_data = data[-ceil(len(data)*0.30):] # 30% split test
	return training_data, testing_data

def plot_data():
	"""Plots data.
	Displays MSE, training accuracy, and testing accuracy over time.
	"""
	x = range(0, EPOCHS)
	fig, ax2 = plt.subplots()
	ax2.set_xlabel('Epoch')
	ax2.set_ylabel('MSE', color='blue')
	line, = ax2.plot(x, MSE, '-', c='blue', lw='1', label='MSE')
	ax1 = ax2.twinx()
	ax1.set_ylabel('Accuracy (%)', color='green')
	line2, = ax1.plot(x, TRP, '-', c='green', lw='1', label='Training')
	line3, = ax1.plot(x, TEP, ':', c='green', lw='1', label='Testing')
	fig.tight_layout()
	fig.legend(loc='center')
	ax1.set_ylim(0, 101)
	plt.show()
	plt.clf()

if __name__ == '__main__':
	# for storing measurements from training
	MSE, TRP, TEP = [], [], []
	# get training/testing data
	TRAIN, TEST = load_data('../data/wheat.csv')
	# count number of attributes
	FEATURES = len(TRAIN[0][:-1])
	# count number of unique classifications
	CLASSES = len(list(set([c[-1] for c in TRAIN])))
	HIDDEN_SIZE = 8 # number of hidden neurons
	# chromosome size
	CHROMOSOME_SIZE = (HIDDEN_SIZE * (FEATURES+1)) + \
		(CLASSES * (HIDDEN_SIZE+1))
	ELITE_PROPORTION = 0.05 # proportion of elites
	TOURN_PROPORTION = 0.03 # proportion of tournament
	EPOCHS = 100 # how many generations
	CROSS_RATE = 0.90 # crossover rate
	MUTAT_RATE = 0.05 # mutation rate
	POP_SIZE = 100 # how large a population is
	# initialize network as trained from genetic algorithm
	NETWORK = genetic_network(ELITE_PROPORTION, TOURN_PROPORTION, \
		CHROMOSOME_SIZE, EPOCHS)
	#plot_data()