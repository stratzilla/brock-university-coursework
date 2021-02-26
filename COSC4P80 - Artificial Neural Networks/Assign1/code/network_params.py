"""
General Network Parameters
"""

def get_epochs():
	num_epochs = 25
	return num_epochs

def get_hidden_size(type):
	if type == 1:
		hidden_size = 5
	elif type == 2:
		hidden_size = 10
	elif type == 3:
		hidden_size = 15
	else:
		hidden_size = 20
	return hidden_size

def get_holdout():
	training_portion = 0.7
	# testing portion will be adjusted as 1 - training_portion
	return training_portion

def get_rand_range():
	rand_min = -0.50
	rand_max = 0.50
	return rand_min, rand_max

"""
BP-NN Specific Parameters
"""

def get_bp_params(type_1, type_2):
	if type_1 == 1:
		learning_rate = 0.001
	elif type_1 == 2:
		learning_rate = 0.010
	elif type_1 == 3:
		learning_rate = 0.100
	else:
		learning_rate = 1.000
	if type_2 == 1:
		momentum_rate = 0.001
	elif type_2 == 2:
		momentum_rate = 0.010
	elif type_2 == 3:
		momentum_rate = 0.100
	else:
		momentum_rate = 1.000
	return learning_rate, momentum_rate

"""
PSO-NN Specific Parameters
"""

def get_swarm_size(type):
	if type == 1:
		swarm_size = 10
	elif type == 2:
		swarm_size = 25
	elif type == 3:
		swarm_size = 50
	elif type == 4:
		swarm_size = 75
	else:
		swarm_size = 100
	return swarm_size

def get_pso_params(type):
	if type == 1:
		inertial_weight = 0.25
		cognitive_coefficient = 1.35
		social_coefficient = 1.05
		boundary = 10
	elif type == 2:
		inertial_weight = 0.35
		cognitive_coefficient = 1.45
		social_coefficient = 1.15
		boundary = 10
	elif type == 3:
		inertial_weight = 0.45
		cognitive_coefficient = 1.55
		social_coefficient = 1.25
		boundary = 10
	elif type == 4:
		inertial_weight = 0.55
		cognitive_coefficient = 1.65
		social_coefficient = 1.35
		boundary = 10
	else:
		inertial_weight = 0.65
		cognitive_coefficient = 1.75
		social_coefficient = 1.45
		boundary = 10
	return inertial_weight, cognitive_coefficient, social_coefficient, \
		boundary
