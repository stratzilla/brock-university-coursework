#!/usr/bin/env python3

from sys import exit, argv
from math import exp

try: # attempt to import dependencies
	import numpy as np # random, unique
	import pandas as pd # containerize CSV
except ImportError: # if not found
	print("\nModules could not be loaded.")
	print("Ensure `numpy` and `pandas` are installed before execution.\n")
	exit(1)

def invalid_arguments():
	"""
	Reminds user on correct execution. Provides instructions to use.
	"""
	print("\nExecute the script as the below:\n")
	print(" $ ./backprop_nn.py <Type> <H> <LR> <MR> <E> <S>\n")
	print("Where the arguments are as below:")
	print(" <Type> -- type of activation function:")
	print("        -- \"LOG\" - logistic sigmoid")
	print("        -- \"TANH\" - hyperbolic tanget")
	print(" <H> -- hidden layer structure")
	print("     -- eg. \"15-10\" means two layers of 15 and 10 neurons each")
	print(" <LR> -- learning rate [0.00..10.00]")
	print(" <MR> -- momentum rate [0.00..10.00]")
	print(" <E> -- the number of epochs")
	print(" <S> -- proportion of examples to use [0.00..1.00]\n")
	exit(1)

# default values, will be overwritten by args
L_RATE, M_RATE = 0.00, 0.00
EPOCHS, SLICES = 0, 1
A_TYPE, H_STRU = None, None
H_S = ""

if len(argv) != 7: # if inappropriate arguments
	invalid_arguments()
else:
	try: # try to init globals
		L_RATE, M_RATE = float(argv[3]), float(argv[4])
		EPOCHS, SLICES = int(argv[5]), float(argv[6])
		H_STRU = [int(i) for i in argv[2].split('-')]
		H_S, A_TYPE = argv[2], argv[1].upper()
		if 0.00 > L_RATE > 10.00 \
		or 0.00 > M_RATE > 10.00: # if globals not in range
			raise Error
		if A_TYPE not in ["LOG", "TANH"]:
			raise Error
	except:
		invalid_arguments()
	
def sgd(nn, c, tr, te):
	"""
	Stochastic gradient descent.
	Performs both the forward and backward passes to generate new weights for
	each synapse. This function trains the network on training data to generate
	a trained model.
	
	Parameter:
	nn -- the neural network structure
	c -- number of classes (potential outputs)
	tr -- a matrix of training examples
	te -- a matrix of testing examples
	"""
	print("Epoch, MSE, Accuracy (Train), Accuracy (Test)")
	for i in range(1, EPOCHS+1): # iterate over epochs
		first = True # first example flag
		total_epoch_error = 0 # to get MSE later
		for x in tr: # for each vector in training matrix
			"""
			Momentum relies on temporal delta, or the prior iteration's delta
			for each neuron. Keep a copy of the deltas from the previous example
			to reference when processing the next example.
			"""
			vd = [n['d'] for l in nn for n in l] if not first else None
			ex = [0 for i in range(c)] # create class output vector
			ex[int(x[-1])] = 1 # denote correct classification
			"""
			Calling `loss()` below will also trigger `forward_pass()` which will
			mutate the network (intended).
			"""
			total_epoch_error += loss(ex, forward_pass(nn, x)) # get loss for ep
			backward_pass(nn, ex) # propagate error through network
			update_weights(nn, x, vd) # initialize new weights
			reset_neurons(nn) # reset each neuron
			first = False # denote no longer first example
		trp = performance_measure(nn, tr) # classification performance on train
		tep = performance_measure(nn, te) # same for test
		print(f"{i}, {total_epoch_error/len(tr):.4f}, {trp:.2f}, {tep:.2f}")
	print(f"\nSystem accuracy against training data was {trp/100:.2%}.")
	print(f"System accuracy against testing data was {tep/100:.2%}.\n")

def update_weights(nn, x, vd):
	"""
	Update weights.
	For each neuron, update the weights based on network parameters. This is
	both learning rate and momentum, where learning rate determines how much
	of the error is corrected as a function of weight and error and momentum
	is how much inertia this update has as a function of prior error deltas.
	
	Parameter:
	nn -- the neural network structure
	x -- a vector of inputs
	vd -- velocity vector (to get temporal deltas)
	"""
	for i in range(len(nn)): # for each layer
		t = [n['o'] for n in nn[i - 1]] if i != 0 else x[:-1]
		for n, d in zip(nn[i], range(0, len(nn[i]))):
			for k in range(len(t)):
				# for the first layer iterated over, there is no delta
				n['w'][k] += (L_RATE * t[k] * n['d'])
				n['w'][k] += ((M_RATE * vd[d]) if vd is not None else 0)
			# also update bias
			n['w'][-1] += (L_RATE * n['d'])

def forward_pass(nn, x):
	"""
	Forward pass through network layers.
	Passes data through each layer via the network synapses. Determines outputs
	of each neuron within the network layers.
	Output = activation(sum(weight * inputs)) for each neuron
	
	Parameter:
	nn -- the neural network structure
	x -- a vector of inputs
	"""
	# to not clobber x, save copy of inputs, init list for outputs
	l_in, l_out = x, []
	for nl in nn: # for each neural layer
		for n in nl: # for each neuron in layer
			n['o'] = activation_function(summing_function(n['w'], l_in))
			l_out.append(n['o']) # append to list of outputs
		# to propagate into further layers, also reset to default
		l_in, l_out = l_out, []
	return l_in

def backward_pass(nn, t):
	"""
	Backwards pass through network layers.
	Propagates error through the layers to find deltas over t.
	
	Parameter:
	nn -- the neural network structure
	t -- a vector of outputs
	"""
	nn_size = len(nn) # layer count
	for i in range(nn_size-1, -1, -1): # for each layer
		for j in range(len(nn[i])): # for each neuron in layer
			"""
			Depending on which layer, find the error as a function of expected
			output and neural output (Output Layer) or as a function of neural
			deltas and synapse weights (Inner Layers)
			Outer: d = f'act(n) * (t - y) (y = n_ij[o])
			Inner: d = f'act(n) * sum(d * w)
			"""
			err = (t[j] - nn[i][j]['o'] if i == nn_size-1 else \
				sum((n['w'][j] * n['d']) for n in nn[i+1]))
			nn[i][j]['d'] = activation_function(nn[i][j]['o'], True) * err

def summing_function(w, x):
	"""
	Sums the synapse weights with inputs and bias.
	u = \sum_i^n{w_i * x_i} + b	
	
	Parameter:
	w -- a vector of synaptic weights
	x -- a vector of inputs
	"""
	return w[-1] + sum((w[i]*x[i]) for i in range(len(w)-1)) # bias plus sum

def activation_function(z, t=False):
	"""
	Activation function selector.
	Signifies a difference in neural activation/firing. Typically, as is the
	case with rectified linear activations, it will be a binary difference.
	However, sigmoid, tanh, etc non-linear activation functions can signify the
	probability of neural firing.
	
	Parameter:
	z -- summing output
	t -- type (F = Normal, T = Derivative)
	"""
	if A_TYPE == "LOG":
		return logistic_function(z) if not t else logistic_function(z, True)
	elif A_TYPE == "TANH":
		return tanh_function(z) if not t else tanh_function(z, True)

def logistic_function(z, derivative=False):
	"""
	Logistic sigmoid activation function.
	log(z) = 1 / (1 + e^-z)
	dlog(z)/dz = log(z) * (1 - log(z))
	Since log(z) was already found in the forward pass, dlog(z)/dz does not
	incorporate log(z) as log(z) existed in the neural output.
	"""
	return (1 / (1 + exp(-z)) if not derivative else z * (1 - z))

def tanh_function(z, derivative=False):
	"""
	Hyperbolic tangent activation function.
	tanh(z) = 2 / (1 + e^-2z) - 1
	dtanh(z)/dz = 1 - tanh^2(z)
	Since tanh(z) was already found in the forward pass, dtanh(z)/dz does not
	incorporate tanh(z) as tanh(z) existed in the neural output.
	"""
	return ((2 / (1 + exp(-2 * z))) - 1 if not derivative else 1 - (z**2))

def reset_neurons(nn):
	"""
	Neuron resetter.
	Resets the neural outputs to zero. Deltas are reseted in new init of deltas
	Prevents some overflow issue with activation functions and aggregate outputs
	
	Parameter:
	nn -- the neural network structure
	"""
	for nl in nn: # for each layer 
		for n in nl: # for each neuron in layer
			n['o'] = 0 # reset output

def loss(e, o):
	"""
	Loss function.
	Compares the squared error between expected and output of neurons.
	
	Parameter:
	e -- expected output
	o -- actual output
	
	Return:
	The sum of squared error between expected and actual.
	"""
	return sum([(e[i]-o[i])**2 for i in range(len(e))])

def setup_network(n, h, o):
	"""
	Neural network initializer.
	The network will be structured as nested data structures, namely a list of
	lists of dicts. As the algorithm continues, not only the weights will be
	stored but also deltas, outputs, errors.
	
	Parameter:
	n -- number of input neurons
	h -- hidden layer structure
	o -- number of output neurons
	
	Return:
	nn -- the neural network structure
	"""
	def r(): # inline, return random -0.5..0.5
		return np.random.uniform(-0.50,0.50) # nonparameterized is random seed
	"""
	Weights, outputs, deltas, etc are all stored within the network structure
	which is a list of list of dictionaries. in order, it is a list of layers
	which is a list of neurons, which is a dict of various characteristics
	"""
	nn = [] # neural network in list structure
	# weights signify synapse connections between HL_1 and IL
	nn.append([{'w':[r() for i in range(n + 1)]} for j in range(h[0])])
	"""
	h denotes hidden layer structure as an array. The structure and values in
	this array describes how the hidden layers are formed. The length of the
	array is the number of hidden layers whereas the values themselves are their
	sizes. For example, an h = [2, 3] is two hidden layers, 2 and 3 neurons each
	"""
	for i in range(1, len(h)): # each layer i = 1..n
		# likewise connections between HL_i and HL_i-1
		nn.append([{'w':[r() for i in range(h[i-1] + 1)]} for j in range(h[i])])
	# likewise connections between HL_n and OL
	nn.append([{'w':[r() for i in range(h[len(h)-1] + 1)]} for j in range(o)])
	return nn

def check_output(nn, x):
	"""
	Compares network output to actual output.
	
	Parameter:
	nn -- the neural network
	x - a tuple
	"""
	out = forward_pass(nn, x) # pass tuple thru IL, HL1, HL2, ..., OL
	return out.index(max(out)) # return class from that output

def performance_measure(nn, d):
	"""
	Determine how accurate the model is.
	
	Parameter:
	nn -- the neural network
	d -- the data to test on
	"""
	correct, total = 0, 0
	for x in d: # for each tuple in data
		if check_output(nn, x) == x[-1]: # if correct guess
			correct += 1
		total += 1
	return 100*(correct/total)

def load_csv(f1, f2):
	"""
	Loads CSV for reading.
	
	Parameter:
	f1 -- training filename
	f2 -- testing filename
	"""
	train = pd.read_csv(f1, header=None).sample(frac=SLICES).values.tolist()
	test = pd.read_csv(f2, header=None).sample(frac=SLICES).values.tolist()
	return train, test
	
def print_params(f1, f2, tr, te, il, hl, ol):
	print("\nNetwork Parameters:")
	print(f"Training data: {f1} ({len(tr)} examples)")
	print(f"Testing data: {f2} ({len(te)} examples)")
	print(f"Network structure: {il}-{hl}-{ol}")
	print(f"Learning rate: {L_RATE}")
	print(f"Momentum rate: {M_RATE}")
	print(f"Epochs: {EPOCHS}")
	print(f"Activation function: {A_TYPE}\n")

if __name__ == '__main__':
	# get .CSV files
	train_filename, test_filename = './data/train.csv', './data/test.csv'
	train, test = load_csv(train_filename, test_filename)
	# get IL and OL neurons
	feat, clas = len(train[0][:-1]), len(np.unique([c[-1] for c in train]))
	# print to console params
	print_params(train_filename, test_filename, train, test, feat, H_S, clas)
	nn = setup_network(feat, H_STRU, clas) # set up the network
	sgd(nn, clas, train, test) # make the network and perform sgd on it
	exit(0)