"""
Robert Scott
4582342
rs09co@brocku.ca
COSC4P76 Assign1
Mar 6 2020
"""

Prior to running the program, preprocess input data such that the CSV files for
both training and testing data have their classes appended to each row. The CSV
provided had the decision in the first column, not the last, so change it to
the final column. For example:

 1, 0, 0, 1, ..., 0, 1, 0
 0, 0, 1, 1, ..., 1, 1, 0
 1, 1, 1, 0, ..., 0, 0, 0
 ... ... ... ... ... ...
 0, 1, 0, 0, ..., 0, 0, 1
 1, 1, 1, 1, ..., 1, 0, 1
 1, 0, 0, 0, ..., 1, 1, 1

I've already done this process, so the included heart-train.csv and 
heart-test.csv and tennis.csv are correct.

Then lastly before execution, ensure you're in a Python3 environment and the
dependencies are met. This is done on the Brock labs but skip this step if
you're executing the program at home:

 $ scl enable rh-python36 bash
 $ python3 -m pip install --user numpy
 $ python3 -m pip install --user pandas

Cale set up a bash shell with Python3.6 functionality which is accessed via the
first line. The second two will install numpy (used for some mathenatucs) and
pandas (used for CSV containerization). No machine learning code is being used
nor do I think these libraries even have that.

Then execute the script as one of the below:

 $ ./id3_tree.py <File> <Holdout> <Print>
 $ ./id3_tree.py <Train> <Test> <Print>

In the former, a single data set is used for both train/test:
 <File> -- the .CSV file of examples
 <Holdout> -- the proportion of training examples (0.00..1.00)
 <Print> -- whether to print tree (1 = T, 0 = F)

For the latter, separate data sets are used instead:
 <Train> -- the .CSV file of training examples
 <Test> -- the .CSV file of testing examples
 <Print> -- whether to print tree (1 = T, 0 = F)

If using 100% of the data (eg. for tennis.csv), the system is deterministic
and creates the optimal tree. For a holdout rate of less than 100%, it is
stochastic. You need separated data for the heart data otherwise you may get
a slice of the training data which contains only one class (therefore it can't
learn to distinguish classes).

I don't advise printing the heart tree because it'll flood the terminal, but
the tennis tree is small enough to show.