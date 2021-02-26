import matplotlib.pyplot as plt
import pandas as pd
import network_params as par

def out_console(auto, epoch, mse, trp, tep):
	"""Console Output function.
	Outputs to console performance measures over epochs.

	Parameters:
		auto : flag for automation script.
		epoch : the epoch to output data for.
		mse : mean squared error over epochs.
		trp : training accuracy over epochs.
		tep : testing accuracy over epochs.
	"""
	# if not performed from automation script, output verbose statistics
	if not auto:
		print(f'{epoch}, {mse[-1]:.4f}, {trp[-1]:.2f}, {tep[-1]:.2f}')
	# if from automation script, only need error per epoch
	else:
		print(f'{mse[-1]:.4f}')

def load_data(filename):
	"""Loads CSV for splitting into training and testing data.

	Parameters:
		filename : the filename of the file to load.

	Returns:
		Two lists, each corresponding to training and testing data.
	"""
	# get holdout ratio
	train_percent = par.get_holdout()
	# load into pandas dataframe
	df = pd.read_csv(filename, header=None, dtype=float)
	# divide all large values (>1000) by 100; compensates for outdoor data
	df[df.gt(1000)] = df.div(100)
	# normalize the data
	for features in range(len(df.columns)-1):
		df[features] = (df[features] - df[features].mean())/df[features].std()
	train = df.sample(frac=train_percent).fillna(0.00) # get training portion
	test = df.drop(train.index).fillna(0.00) # remainder testing portion
	return train.values.tolist(), test.values.tolist()

def plot_data(epochs, mse, trp, tep):
	"""Plots data.
	Displays MSE, training accuracy, and testing accuracy over time.

	Parameters:
		epochs : the number of training epochs to plot over.
		mse : the mean squared error over epochs.
		trp : the training accuracy over epochs.
		tep : the testing accuracy over epochs.
	"""
	x = range(0, epochs+1)
	fig, ax2 = plt.subplots()
	ax2.set_xlabel('Epoch')
	ax2.set_ylabel('MSE', color='blue')
	ax2.plot(x, mse, '-', c='blue', lw='1', label='MSE')
	ax1 = ax2.twinx()
	ax1.set_ylabel('Accuracy (%)', color='green')
	ax1.plot(x, trp, '-', c='green', lw='1', label='Training')
	ax1.plot(x, tep, ':', c='green', lw='1', label='Testing')
	fig.legend(loc='center')
	ax1.set_ylim(0, 101)
	plt.show()
	plt.clf()
