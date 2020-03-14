#!/usr/bin/env python3

import sys
from math import sqrt, inf
import os.path

try:
	import pandas as pd # some containerization for input data
	import numpy as np # random
	if len(sys.argv) == 6:
		import matplotlib.pyplot as plt # plotting
		import matplotlib.patches as mpat # for matplot legends
except ImportError:
	print("\nModules could not be loaded.")
	print("Ensure module dependencies are installed before execution.\n")
	sys.exit(1)

def execution_instructions():
	"""Reminds user how to execute on error."""
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
	print(" <Save> -- (optional) path to save plot to\n")
	print("If no argument is provided for <Save>, no plots will be made.")
	print("Ensure <File> and <Save> point to valid files and directories.\n")
	sys.exit(1)

try:
	if not 6 >= len(sys.argv) >= 5:
		raise ValueError
	FILENAME = sys.argv[1]
	if not os.path.isfile(FILENAME):
		raise FileNotFoundError
	TYPE = int(sys.argv[2])
	K_PARAM = int(sys.argv[3])
	MAX_EPOCH = int(sys.argv[4])
	OUTPUT = None
	if not 3 >= TYPE >= 1 or K_PARAM < 1 or MAX_EPOCH < 1:
		raise ValueError
	if len(sys.argv) == 6:
		OUTPUT = sys.argv[5]
		if not os.path.isdir('/'.join(OUTPUT.split('/')[:-1])):
			raise NotADirectoryError
except (ValueError, FileNotFoundError, NotADirectoryError):
	execution_instructions()

class Point:
	"""Point class.

	Containerizes an x,y-coordinate along with cluster data.

	Attributes:
		x_c : the x-coordinate of the Point.
		y_c : the y-coordinate of the Point.
		cluster : which cluster the Point belongs to.
	"""

	def __init__(self, x, y):
		"""Constructs Point based on x,y-coordinate."""
		self.x_c, self.y_c, self.cluster = x, y, None

	def find_cluster(self, clusters):
		"""Method to determine which cluster the point belongs to.

		Parameters:
			clusters : a list of cluster centroids.
		"""
		best_distance, best_cluster = inf, None
		for c in clusters:
			d = distance(self, c)
			if d < best_distance:
				best_distance, best_cluster = d, c
		self.set_cluster(best_cluster)

	def set_coord(self, x, y):
		"""x,y-coordinate mutator method."""
		self.x_c, self.y_c = x, y

	def get_coord(self):
		"""x,y-coordinate accessor method."""
		return self.x_c, self.y_c

	def set_cluster(self, c):
		"""cluster mutator method."""
		self.cluster = c

	def get_cluster(self):
		"""cluster accessor method."""
		return self.cluster

	def __str__(self):
		"""Print overload method."""
		return str(int(self.x_c)) + ", " + str(int(self.y_c))

	def __eq__(self, other):
		"""Equality overload method."""
		return self.x_c == other.x_c and self.y_c == other.y_c

	def __neq__(self, other):
		"""Inequality overload method."""
		return not self.__eq__(other)

def k_means(points, clusters, epochs=MAX_EPOCH):
	"""Performs the k-means clustering on the data.

	Before the algorith is run, the data points would have been loaded and k
	random centroids were placed within the coordinate system. Then this
	algorithm will find all cluster neighbors for every point, find the mean
	position of points within a cluster, and move the centroids to that
	position. This process repeats for a number of iterations (epochs).

	Parameters:
		points : a list of Points.
		clusters : a list of cluster centroids.
		epochs : how many iterations the algorithm should perform.
	"""
	for _ in range(1, epochs+1):
		find_clusters(points, clusters)
		move_centroids(points, clusters)
	print(f"{dunn_index(points, clusters)}")

def dunn_index(points, clusters):
	"""Calculates the Dunn Index of the clustering.

	The Dunn Index is a function of the spread of clusters and the spread of
	points within a cluster. DI = min cluster distance / max cluster diameter.
	A smaller Dunn Index indicates better clustering.

	Parameters:
		points : a list of Points.
		clusters : a list of cluster centroids.

	Returns:
		The Dunn Index of the clustering.
	"""
	min_cluster_dist = inf
	# compare distances between cluster centroids, choose smallest
	for c_i in clusters:
		for c_j in clusters:
			if c_i is c_j:
				continue # distance between cluster and itself is 0
			min_cluster_dist = min(min_cluster_dist, distance(c_i, c_j))
	max_cluster_diam = 0
	# compare diameters of each cluster, choose largest
	for c in clusters:
		for p_i in points:
			if p_i.get_cluster() is not c:
				continue # if not in cluster
			for p_j in points:
				if p_j.get_cluster() is not c:
					continue # if not in cluster
				if p_i is p_j:
					continue # distance between point and itself is 0
				max_cluster_diam = max(max_cluster_diam, distance(p_i, p_j))
	return round(min_cluster_dist/max_cluster_diam, 4)

def distance(p, q, t=TYPE):
	"""Distance calculator.

	Determine the distance between points based on CLI argument. Included metric
	spaces are Euclidean, Manhattan (Rectilinear), and Chebyshev.

	Parameters:
		p : Point p.
		q : Point q.
		t : the type of metric to use.

	Returns:
		The distance between Points p and q.
	"""
	(p_x, p_y), (q_x, q_y) = p.get_coord(), q.get_coord()
	# Euclidean Distance measure, d(p, q) = sqrt((q_1 - p_2)^2 + (q_2 - p_2)^2)
	if t == 1:
		return sqrt((q_x - p_x)**2 + (q_y - p_y)**2)
	# Manhattan Distance measure, d(p, q) = |q_1 - p_1| + |q_2 - p_2|
	if t == 2:
		return abs(q_x - p_x) + abs(q_y - p_y)
	# Chebyshev Distance measure, d(p, q) = max(|q_1 - p_1|, |q_2 - p_2|)
	return max(abs(q_x - p_x), abs(q_y - p_y))

def find_clusters(points, clusters):
	"""Pairs each Point with its closest neighbor cluster.

	Finds cluster based on distance between Point and cluster centroid.

	Parameters:
		points : a list of Points.
		clusters : a list of cluster centroids.
	"""
	for p in points:
		p.find_cluster(clusters)

def generate_centroids(points, x, y, k=K_PARAM):
	"""Generates centroids within the dataset at random.

	Initially, a population of k random centroids are placed within the data to
	eventually find clusters with.

	Parameters:
		points : a list of Points.
		x : the lower/upper bound for x-coordinate for centroid.
		y : the lower/upper bound for y-coordinate for centroid.
		k : how many centroids to generate.

	Returns:
		A list of k centroids.
	"""
	def rand(bounds):
		return np.random.uniform(bounds[0], bounds[1])
	clusters = []
	# initially centroids are randomly distributed
	for _ in range(k):
		clusters.append(Point(rand(x), rand(y)))
	while check_div_zero(points, clusters):
		# If a centroid were to have no nearest points, it will result in
		# division-by-zero errors later. This is random and depends on centroid
		# initial position. If a div-by-zero error were to occur, catch it here
		# and reinitialize the centroids at new random positions. Repeat until
		# no possibility of div-by-zero errors.
		clusters = []
		for _ in range(k):
			clusters.append(Point(rand(x), rand(y)))
	return clusters

def check_div_zero(points, clusters):
	"""Pre-emptively catch a division-by-zero error later.

	Determines if a centroid has no nearest points by finding clusters for each
	Point before the k-means algorithm.

	Parameters:
		points : a list of Points.
		clusters : a list of cluster centroids.

	Returns:
		Whether there exists a centroid without any member points.
	"""
	find_clusters(points, clusters)
	for c in clusters:
		count = 0
		for p in points:
			if p.get_cluster() is c: # determine if centroid has points
				count += 1
		if count == 0: # if cluster has points, this won't be zero
			return True # but if cluster doesn't, need to reinit as random
		count = 0
	return False

def move_centroids(points, clusters):
	"""Repositions cluster centroids to the mean Point position.

	Parameters:
		points : a list of Points.
		clusters : a list of cluster centroids.
	"""
	for c in clusters:
		mean_x, mean_y, count = 0, 0, 0
		for p in points:
			if p.get_cluster() is c: # if Point belongs to cluster
				# aggregate the mean position of cluster Points
				mean_x += p.get_coord()[0]
				mean_y += p.get_coord()[1]
				count += 1
		# move centroid to mean position of Points in cluster
		c.set_coord(mean_x/count, mean_y/count)

def load_data(file=FILENAME):
	"""Loads data from file and inserts into list of Points.

	Parameters:
		file : filename of local file to parse Points from.

	Returns:
		A list of Points along with the upper/lower bounds for x,y-coordinates.
	"""
	def min_max(t): # allows grabbing min/max values per column
		return pd.Series(index=['min', 'max'], data=[t.min(), t.max()])
	df = pd.read_csv(file, names=['x', 'y'], delimiter=r'\s+')
	df_minmax = df.apply(min_max) # find minimum and maximum of each column
	arr_points = []
	# convert from pd df to Points, append to list
	for _, d in df.iterrows():
		arr_points.append(Point(d['x'], d['y']))
	return (arr_points, df_minmax['x'], df_minmax['y'])

def setup_plot(file=FILENAME):
	"""Sets up the plot with some attributes.

	Parameters:
		file : loaded data filename to put in plot title.
	"""
	plt.figure(f'K-Means Clustering ({file})')
	plt.xticks([])
	plt.yticks([])
	plt.margins(0.05, 0.05)
	legend_data = mpat.Patch(color='black', label='data')
	legend_cent = mpat.Patch(color='red', label='centroid')
	plt.legend(handles=[legend_data, legend_cent], loc=2, prop={'size': 8})

def plot_data(points, clusters, file=OUTPUT):
	"""Plots data and cluster centroids.

	Parameters:
		points : a list of Points.
		clusters : a list of cluster centroids.
		out : filename to save plot as.
	"""
	setup_plot()
	for p in points:
		x, y = p.get_coord()
		plt.scatter(x, y, marker='o', color='k', s=2, linewidths=1)
	for c in clusters:
		x, y = c.get_coord()
		plt.scatter(x, y, marker='x', color='r', s=25, linewidths=1)
	plt.savefig(file, bbox_inches='tight', pad_inches=0.1) # save file
	plt.show()
	plt.clf()

def main():
	"""k-means algorithm main driver."""
	points, x_bounds, y_bounds = load_data()
	clusters = generate_centroids(points, x_bounds, y_bounds)
	k_means(points, clusters)
	if len(sys.argv) == 6:
		plot_data(points, clusters)

if __name__ == '__main__':
	main()
	sys.exit(0)
