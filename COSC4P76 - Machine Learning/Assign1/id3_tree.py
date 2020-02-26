#!/usr/bin/env python3

from sys import exit, argv

try: # attempt to import dependencies
	import pandas as pd
	import numpy as np
except ImportError: # if not found
	print("\nModules could not be loaded.")
	print("Ensure both `pandas` and `numpy` are installed before execution.\n")
	exit(1)
	
def id3(df, t, f):
	"""
	ID3 Decision Tree generator.
	
	Parameter:
	df -- the dataframe of training data
	t -- target attribute
	f -- list of attributes
	
	Return:
	root -- the fully formed decision tree
	"""
	root, ig = {}, {} # root note, dict of IG
	for a in f: # for each attr
		ig[a] = find_information_gain(df, t, a) # find IG for each attr split
	highest_ig = f[np.argmax(ig)] # get best attr to split on
	root = {highest_ig:{}} # make root node
	split = make_split(df, highest_ig) # split root based on outcomes
	for v in np.unique(df[highest_ig]):
		df_branch = df.where(df[highest_ig] == v).dropna()
		if len(df_branch) == 0:
			return 
		#branch = id3(df_branch, t, [i for i in f if i != highest_ig])
	
def find_entropy(t):
	"""
	Finds entropy of target attribute in training set.
	H(S) = \sum_{x\inX}{ -p(x)*log_2{p(x)} }
	
	Parameter:
	t -- target attribute
	
	Return:
	h -- entropy of target attribute
	"""
	h = 0
	v, n = np.unique(t, return_counts = True) # get values and distinct v
	for x in range(len(v)):
		px = n[x]/np.sum(n)
		h += -px * np.log2(px)
	return h
	
def find_information_gain(df, t, s):
	"""
	Finds information gain of target attribute in training set.
	IG(S,A) = H(S) - \sum_{t\inT}{ p(t) * H(t)} = H(S) - H(S|A)
	
	Parameter:
	df -- the dataframe of training data
	t -- target attribute
	s -- splitting attribute
	"""
	total_h = find_entropy(df[t]) # find entropy of entire system
	split_h = 0 # entropy after potential split
	v, n = np.unique(df[s], return_counts = True) # get values and distinct v
	for x in range(len(v)):
		pt = n[x]/np.sum(n)
		split = df.where(df[s] == v[x]).dropna()[t] # remove missing attrs
		split_h += pt * find_entropy(split)
	return total_h - split_h

def make_split(df, t):
	new_df = {}
	for df_key in df.groupby(t).groups.keys():
		new_df[df_key] = df.groupby(t).get_group(df_key)
	return new_df
	
def load_csv(f):
	"""
	Loads CSV file into pandas dataframe.
	.CSV file is organized such that decision is the last column and features
	are other columns. The first row is the name of decision and features.
	
	Parameter:
	f -- the filename for the .CSV file
	
	Return:
	df -- a dataframe of examples
	"""
	try:
		df = pd.read_csv(f) # open file as parse CSV into dataframe
	except:
		print("\nData could not be loaded, ensure the argument is correct.\n")
		exit(1)
	return df

def holdout(df, p):
	"""
	Splits a dataframe of examples into training and testing data.
	
	Parameter:
	df -- a dataframe of examples
	p -- proportion of training vs testing (0.00..1.00]
	
	Return:
	train -- training examples
	test -- testing examples
	"""
	if 0.00 < p <= 1.00:
		d = df.copy()
		train = d.sample(frac=p) # split, and randomize
		test = d.drop(train.index) # remove train data from df
		return train, test
	else:
		print("\nThe proportion of training examples must be (0.00..1.00].\n")
		exit(1)

if __name__ == '__main__':
	df = load_csv("./data/tennis.csv")
	train, test = holdout(df, 1)
	decision_name = train.columns[len(train.columns)-1]
	id3(train, decision_name, train.columns[:-1])
	exit(0)