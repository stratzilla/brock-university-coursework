#!/usr/bin/env python3

from sys import argv, exit
from math import sqrt, inf
import os.path

try:
	import pandas as pd # some containerization for input data
	import numpy as np # random
	import matplotlib.pyplot as plt # plotting
	import matplotlib.patches as mpat # for matplot legends
except ImportError:
	print("\nModules could not be loaded.")
	print("Ensure module dependencies are installed before execution.\n")
	exit(1)

def execution_instructions(): # remind user how to use program
	print("\nExecute the script as the below:")
	print(" $ ./k-means.py <File> <Type> <K> <Epochs> <Save>\n")
	print("Where the following are the arguments:")
	print(" <File> -- the data to perform k-means clustering upon")
	print(" <Type> -- the type of metric space to use:")
	print("        -- 1 - Euclidean Metric")
	print("        -- 2 - Manhattan Metric")
	print("        -- 3 - Chebyshev Metric")
	print(" <K> -- k-parameter, or how many clusters")
	print(" <Epochs> -- how many iterations of k-means to perform")
	print(" <Save> -- path to save plot to\n")
	print("Ensure <File> and <Save> point to valid files and directories.\n")
	exit(1)

try:
	if len(argv) != 6:
		raise Error
	FILENAME = argv[1]
	TYPE = int(argv[2])
	K_PARAM = int(argv[3])
	MAX_EPOCH = int(argv[4])
	OUTPUT = argv[5]
	if not 3 >= TYPE >= 1 or K_PARAM < 1:
		raise Error
	if not os.path.isfile(FILENAME):
		raise Error
	if not os.path.isdir('/'.join(OUTPUT.split('/')[:-1])):
		raise Error
except:
	execution_instructions()

class Point:
	"""
	Point class.
	Containerizes an x,y-coordinate as a class object.
	"""	
	def __init__(self, x, y):
		"""
		Point class constructor.
		
		Parameter:
		x -- the x-coordinate of the point
		y -- the y-coordinate of the point
		"""
		self.x, self.y = x, y

	def find_cluster(self, sc):
		"""
		Method to determine which cluster the point belongs to.
		
		Parameter:
		sc -- a set of clusters to check
		"""
		best_distance = inf # initially max_value
		best_cluster = None # initially no cluster is set
		for c in sc: # for each cluster centroid in set of clusters
			d = distance(self, c) # find the distance between self and centroid
			if d < best_distance: # if better distance
				best_distance = d # make new distance
				best_cluster = c # make new best centroid
		self.set_cluster(best_cluster) # set the centroid as cluster ownership

	def set_coord(self, x, y): # mutator method for x, y attributes
		self.x, self.y = x, y
	def get_coord(self): # accessor method for x, y attributes
		return self.x, self.y
	def set_cluster(self, c): # mutator method for c attribute
		self.c = c		
	def get_cluster(self): # accessor method for c attribute
		return self.c	
	def __str__(self): # print overloading
		return str(int(self.x)) + ", " + str(int(self.y))
	def __eq__(self, other): # equality overloading
		return isinstance(other, self__class__) and \
			self.x == other.x and self.y == other.y
	def __neq__(self, other): # inequality overloading
		return not self.__eq__(other)
		
def k_means(pf, cf, e=MAX_EPOCH):
	"""
	Performs the k-means clustering on the data.
	
	Parameter:
	pf -- a collection of points to find clusters on
	cf -- a collection of clusters to "evolve" over epochs
	e -- how many epochs/iterations
	"""
	for i in range(1, e+1): # perform the clustering algorithm e times
		find_clusters(pf, cf) # find clusters
		move_centroids(pf, cf) # move centroids to middle of new clusters
		#print(f"\nEpoch {i}:")
		#for c in cf:
			#print(c)

def distance(p, q, t=TYPE):
	"""
	Distance calculation driver.
	Determine the distance between points based on CLI argument.
	
	Parameter:
	p -- point p
	q -- point q
	t -- the type of metric to use
	
	Return:
	Distance between p and q based on metric argument.
	"""
	if t is 1: return euclidean_distance(p, q)
	elif t is 2: return manhattan_distance(p, q)
	elif t is 3: return chebyshev_distance(p, q)

def euclidean_distance(p, q):
	"""
	Euclidean Distance measure.
	Determines the distance between points p and q using euclidean metrics.
	d(p, q) = sqrt((q_1 - p_2)^2 + (q_2 - p_2)^2)
	
	Parameter:
	p -- point p
	q -- point q
	
	Return:
	The distance between p and q using euclidean metrics.
	"""
	return sqrt((q.x - p.x)**2 + (q.y - p.y)**2)

def manhattan_distance(p, q):
	"""
	Manhattan Distance measure.
	Determines the distance between points p and q using manhattan metrics.
	d(p, q) = |q_1 - p_1| + |q_2 - p_2|
	
	Parameter:
	p -- point p
	q -- point q
	
	Return:
	The distance between p and q using manhattan metrics.
	"""
	return abs(q.x - p.x) + abs(q.y - p.y)

def chebyshev_distance(p, q):
	"""
	Chebyshev Distance measure.
	Determines the distance between points p and q using chebyshev metrics.
	d(p, q) = max(|q_1 - p_1|, |q_2 - p_2|)
	
	Parameter:
	p -- point p
	q -- point q
	
	Return:
	The distance between p and q using chebyshev metrics.
	"""
	return max(abs(q.x - p.x), abs(q.y - p.y))

def find_clusters(pf, cf):
	"""
	Function to get each point to find a cluster.
	Finds cluster based on distance between point and cluster centroid.
	
	Parameter:
	pf -- an array of points to find clusters on
	cf -- an array of centroids to find clusters with
	"""
	for p in pf: p.find_cluster(cf)

def generate_centroids(pf, x_min, x_max, y_min, y_max, k=K_PARAM):
	"""
	Generates centroids within the dataset at random.
	
	Parameter:
	pf -- a collection of points
	x_min, x_max -- the lower/upper bounds for centroid x-coordinate
	y_min, y_max -- the lower/upper bounds for centroid y-coordinate
	
	Return:
	cf -- an array of k centroids
	"""
	def rx(): return np.random.uniform(x_min, x_max)
	def ry(): return np.random.uniform(y_min, y_max)
	cf = []
	# find centroids and append to array
	for _ in range(k): cf.append(Point(rx(), ry()))
	while(check_div_zero(pf, cf)):
		"""
		If a centroid were to have no "member" points, it will result in
		division-by-zero errors later. This is random and depends on centroid
		initial position. If a div-by-zero error were to occur, catch it here
		and reinitialize the centroids at new random positions. Repeat until no
		possibility of div-by-zero errors.
		"""
		cf = []
		for _ in range(k): cf.append(Point(rx(), ry()))
	return cf

def check_div_zero(pf, cf):
	"""
	Pre-emptively catch a division-by-zero error later.
	Determines if a centroid has no member points.
	
	Parameter:
	pf -- a collection of points
	cf -- a collection of centroids
	
	Return:
	Whether there exists a centroid without any member points
	"""
	find_clusters(pf, cf)
	for c in cf:
		count = 0
		for p in pf:
			if p.get_cluster() is c:
				count += 1
		if count is 0:
			return True
		count = 0
	return False

def move_centroids(pf, cf):
	"""
	Moves centroids to middle of clusters.
	
	Parameter:
	pf -- a collection of points
	cf -- a collection of clusters
	"""
	for c in cf: # for each cluster centroid
		mean_x, mean_y, count = 0, 0, 0
		for p in pf: # for each point
			if p.get_cluster() is c: # if point belongs to cluster
				mean_x += p.get_coord()[0]
				mean_y += p.get_coord()[1]
				count += 1
		# move centroid to mean position of points in cluster
		c.set_coord(mean_x/count, mean_y/count)

def load_data(f=FILENAME):
	"""
	Loads data from file and inserts into array of Points.
	
	Parameter:
	f -- the filename to grab data from
	
	Return:
	arr_points -- an array of Points corresponding to tuples in data
	df_minmax['x'] -- a container of min-max values for x column
	df_minmax['y'] -- a container of min-max values for y column
	"""
	def min_max(t): # allows grabbing min/max values per column
		return pd.Series(index=['min', 'max'], data=[t.min(), t.max()])
	df = pd.read_csv(f, names=['x', 'y'], delimiter=r'\s+')
	# find minimum and maximum of each column
	df_minmax = df.apply(min_max)
	arr_points = []
	# convert from pd df to points, append to array
	for _, d in df.iterrows(): arr_points.append(Point(d['x'], d['y']))
	return arr_points, df_minmax['x'], df_minmax['y']

def setup_plot(f=FILENAME):
	"""
	Sets up the plot with some attributes.
	"""
	plt.figure(f'K-Means Clustering ({f})')
	plt.xlabel('x')
	plt.xticks([])
	plt.ylabel('y')
	plt.yticks([])
	legend_data = mpat.Patch(color='black', label='data')
	legend_cent = mpat.Patch(color='red', label='centroid')
	plt.legend(handles=[legend_data, legend_cent])

def plot_data(pf, cf, o=OUTPUT):
	"""
	Plots data and cluster centroids.
	
	Parameter:
	pf -- the points to plot
	cf -- the centroids to plot
	o -- where to save plot
	"""
	setup_plot()
	for p in pf: # plot each point
		x, y = p.get_coord()
		plt.scatter(x, y, marker='o', color='k', s=2, linewidths=1)
	for c in cf: # plot each centroid
		x, y = c.get_coord()
		plt.scatter(x, y, marker='x', color='r', s=25, linewidths=1)
	plt.savefig(o) # save file

if __name__ == '__main__': # main driver
	pf, x_mm, y_mm = load_data() # load data to find collection of points
	cf = generate_centroids(pf, x_mm[0], x_mm[1], y_mm[0], y_mm[1])
	k_means(pf, cf) # perform the clustering algorithm
	plot_data(pf, cf)
	exit(0)