"""
Robert Scott
4582342
rs09co@brocku.ca
COSC4P76 Assign1
Mar 6 2020
"""

Prior to running the program, preprocess input data such that the CSV files for
both training and testing data have their classes appended to each row. Then
concatenate each CSV file. For example, your CSV should look like the below:

 0.53, 0.01, 0.42, 0.68, 0.21, ..., 0.89, 0.92, 0
 0.63, 0.03, 0.23, 0.65, 0.34, ..., 0.63, 0.32, 0
 0.75, 0.55, 0.44, 0.23, 0.12, ..., 0.39, 0.02, 0
 ... , ... , ... , ... , ... , ..., ... , ... , ...
 0.63, 0.75, 0.64, 0.69, 0.31, ..., 0.11, 0.34, 1
 0.99, 0.51, 0.12, 0.63, 0.83, ..., 0.52, 0.84, 1
 0.58, 0.81, 0.11, 0.52, 0.63, ..., 0.73, 0.11, 1
 ... , ... , ... , ... , ... , ..., ... , ... , ...
 ... , ... , ... , ... , ... , ..., ... , ... , ...
 ... , ... , ... , ... , ... , ..., ... , ... , ...
 0.73, 0.62, 0.77, 0.83, 0.24, ..., 0.93, 0.91, 8
 0.71, 0.07, 0.46, 0.38, 0.64, ..., 0.34, 0.32, 8
 0.82, 0.77, 0.22, 0.93, 0.33, ..., 0.75, 0.53, 8
 ... , ... , ... , ... , ... , ..., ... , ... , ...
 0.37, 0.74, 0.82, 0.33, 0.41, ..., 0.52, 0.82, 9
 0.51, 0.71, 0.82, 0.93, 0.50, ..., 0.23, 0.23, 9
 0.83, 0.22, 0.11, 0.17, 0.34, ..., 0.92, 0.10, 9

I've already done this process, so the included train.csv and test.csv are what
you need to run the program. The full data is 7000 examples train, 4000 test.

Then lastly before execution, ensure you're in a Python3 environment and the
dependencies are met. This is done on the Brock labs but skip this step if
you're executing the program at home:

 $ scl enable rh-python36 bash
 $ python3 -m pip install --user numpy
 $ python3 -m pip install --user pandas

Cale set up a bash shell with Python3.6 functionality which is accessed via the
first line. The second two will install numpy (used for random and unique) and
pandas (used for CSV containerization). No machine learning code is being used
nor do I think these libraries even have that.

Then execute the script as the below:

 $ ./backprop_nn.py <Type> <H> <LR> <MR> <E> <S>

Where the arguments are as below:
 <Type> -- type of activation function:
        -- LOG - logistic sigmoid
        -- TANH - hyperbolic tanget
 <H> -- hidden layer structure
     -- eg. 15-10 means two layers of 15 and 10 neurons each
	 -- eg. 15 means one layer of 15 neurons
 <LR> -- learning rate [0.00..10.00]
 <MR> -- momentum rate [0.00..10.00]
 <E> -- the number of epochs
 <S> -- proportion of examples to use [0.00..1.00]

Once the program begins, it will outputs the parameters used as well as the
epoch, MSE, accuracy vs training and accuracy vs testing. 

I will warn that performance can be slow depending on the proportion of examples
to use as well as the number of epochs. At home, using the full data set and 15
hidden neurons, it takes about 3.5 seconds per epoch. The bottlenecks I've found
were math.exp takes a while to compute (but numpy.exp took 5x longer!) and
storing temporal data took some time. Thankfully I could make bash scripts to
run multiple runs concurrently in the background and not have my CPU catch fire.

Now the elephant in the room: I believe I coded the network such that it is
innately meant for online learning. What I did was the 3P71 assignment first
and then tweaked it to fit my needs. Unfortunately trying to implement many of
the alternative algorithms (rProp, etc) meant batch learning needed to be
implemented. I just couldn't implement that with the way I did my original
algorithm and time didn't permit, even with extension, to redo the whole thing.

I did include something you can loosely call rProp which uses online learning.
However, it does not perform well and is only a slightly modification of my
existing code. rProp is meant to be batch learning so it's not the best.

Quickprop did seem promising but in implementation, as it can be done online, I
kept encountering either denominators of zero or NaN errors, and when I could
fix those issues, it wouldn't learn. It would flip flop between 10-15% at which
point it may as well be guessing at random. I regret not being able to implement
a second network algorithm to compare.

Something you may notice is I'm using a list/list/dict structure rather than
matrices. It seems the most intuitive to me but a lot of literature relied on
matrices. I had to look at other literature which didn't use the same formulas
or mechanics as the ones mentioned in lecture/tutorial. So while the system
works (and works well!), the algorithms used may not be 100% what you see in
lecture or some textbooks. I kept finding divisive algorithm pseudocodes online
and just chose one which best fit my structure.

Lastly, as for cross-validation and holdout, I decided to do this through bash
manually. So far it's an arduous task so I'm not doing it for every parameter.
However, you can read more when my report is ready. It was just confusing to
implement it in Python without extra use of external libraries.