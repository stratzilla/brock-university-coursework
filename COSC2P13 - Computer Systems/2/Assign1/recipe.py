#!/usr/bin/env python3

import sys
import threading

# recipe resources
FLOUR = threading.Lock()
TOMATO_HAMPER = threading.Lock()
EGGS = threading.Lock()
SALT_MALLET = threading.Lock()
PREP_COUNTER = threading.Lock()
STOVE = threading.Lock()
OVEN = threading.Lock()
SINK = threading.Lock()
OUTPUT = threading.Lock()

def pasta_recipe(max_count, reporting_count):
	"""Pasta recipe threaded function.

	Performs the necessary steps to create order(s) of pasta.

	Parameters:
		max_count : how many in total to make.
		reporting_count : at what threshold to report orders are done.
	"""
	count = 0
	while count < max_count:
		PREP_COUNTER.acquire() # Step 1
		FLOUR.acquire()
		FLOUR.release()
		EGGS.acquire() # Step 2
		EGGS.release()
		PREP_COUNTER.release() # Step 4
		SINK.acquire() # Step 6
		SINK.release()
		SALT_MALLET.acquire() # Step 7
		SALT_MALLET.release()
		STOVE.acquire() # Step 8
		STOVE.release() # Step 10
		TOMATO_HAMPER.acquire() # Step 11
		TOMATO_HAMPER.release()
		count += 1 # one order of pasta is finished, add to count
		if count % reporting_count == 0 and count < max_count:
			OUTPUT.acquire()
			print(f"{count} orders of pasta are done.")
			OUTPUT.release()
	OUTPUT.acquire()
	print(f"All {max_count} orders of pasta are done.")
	OUTPUT.release()

def pizza_recipe(max_count, reporting_count):
	"""Pizza recipe threaded function.

	Performs the necessary steps to create order(s) of pizza.

	Parameters:
		max_count : how many in total to make.
		reporting_count : at what threshold to report orders are done.
	"""
	count = 0
	while count < max_count:
		FLOUR.acquire() # Step 1
		FLOUR.release()
		PREP_COUNTER.acquire() # Step 2
		TOMATO_HAMPER.acquire()
		TOMATO_HAMPER.release()
		PREP_COUNTER.release() # Step 4
		OVEN.acquire() # Step 5
		OVEN.release()
		STOVE.acquire() # Step 6
		STOVE.release()
		count += 1 # one order of pizza is finished, add to count
		if count % reporting_count == 0 and count < max_count:
			OUTPUT.acquire()
			print(f"{count} orders of pizza are done.")
			OUTPUT.release()
	OUTPUT.acquire()
	print(f"All {max_count} orders of pizza are done.")
	OUTPUT.release()

def frozen_recipe(max_count, reporting_count):
	"""Frozen pizza recipe threaded function.

	Performs the necessary steps to create order(s) of frozen pizza.

	Parameters:
		max_count : how many in total to make.
		reporting_count : at what threshold to report orders are done.
	"""
	count = 0
	while count < max_count:
		OVEN.acquire() # Step 1
		OVEN.release()
		count += 1 # one order of frozen pizza is finished, add to count
		if count % reporting_count == 0 and count < max_count:
			OUTPUT.acquire()
			print(f"{count} orders of frozen pizza are done.")
			OUTPUT.release()
	OUTPUT.acquire()
	print(f"All {max_count} orders of frozen pizza are done.")
	OUTPUT.release()

def chicken_recipe(max_count, reporting_count):
	"""Chicken recipe threaded function.

	Performs the necessary steps to create order(s) of chicken.

	Parameters:
		max_count : how many in total to make.
		reporting_count : at what threshold to report orders are done.
	"""
	count = 0
	while count < max_count:
		SALT_MALLET.acquire() # Step 1
		SALT_MALLET.release()
		for _ in range(2): # a couple of passes
			EGGS.acquire() # Step 2
			EGGS.release()
			FLOUR.acquire()
			FLOUR.release()
		SINK.acquire() # Step 3
		SINK.release()
		STOVE.acquire() # Step 4
		STOVE.release()
		count += 1 # one order of chicken is finished, add to count
		if count % reporting_count == 0 and count < max_count:
			OUTPUT.acquire()
			print(f"{count} orders of chicken are done.")
			OUTPUT.release()
	OUTPUT.acquire()
	print(f"All {max_count} orders of chicken are done.")
	OUTPUT.release()

def prompt_user():
	"""Prompts the user for their choices on how many dishes to make.

	Returns:
		pasta_num : how many pasta dishes to make.
		pizza_num : how many pizza dishes to make.
		frozen_num : how many frozen pizza dishes to make.
		chicken_num : how many chicken dishes to make.
		report_num : how frequently progress is reported.
	"""
	pasta_choice = input("\nMake pasta? (y/n): ")
	pasta_num = int(input("How many? ")) if pasta_choice == 'y' else 0
	pizza_choice = input("Make pizza? (y/n): ")
	pizza_num = int(input("How many? ")) if pizza_choice == 'y' else 0
	frozen_choice = input("Make frozen pizza? (y/n): ")
	frozen_num = int(input("How many? ")) if frozen_choice == 'y' else 0
	chicken_choice = input("Make chicken? (y/n): ")
	chicken_num = int(input("How many? ")) if chicken_choice == 'y' else 0
	total_dishes = pasta_num + pizza_num + frozen_num + chicken_num
	report_num = int(input("Report frequency? ")) if total_dishes > 0 else 0
	print(f"\n{pasta_num} order(s) of pasta will be made.")
	print(f"{pizza_num} order(s) of pizza will be made.")
	print(f"{frozen_num} order(s) of frozen pizza will be made.")
	print(f"{chicken_num} order(s) of chicken will be made.")
	print(f"Progress will be reported every {report_num} dish(es).\n")
	return pasta_num, pizza_num, frozen_num, chicken_num, report_num

if __name__ == '__main__':
	COUNTS = prompt_user() # get user options for execution
	THREADS = [] # start a list of threads to execute shortly
	if COUNTS[0] > 0: # if pasta is being made
		THREADS.append(threading.Thread(target=pasta_recipe, \
			args=(COUNTS[0], COUNTS[4],)))
	if COUNTS[1] > 0: # if pizza is being made
		THREADS.append(threading.Thread(target=pizza_recipe, \
			args=(COUNTS[1], COUNTS[4],)))
	if COUNTS[2] > 0: # if frozen pizza is being made
		THREADS.append(threading.Thread(target=frozen_recipe, \
			args=(COUNTS[2], COUNTS[4],)))
	if COUNTS[3] > 0: # if chicken is being made
		THREADS.append(threading.Thread(target=chicken_recipe, \
			args=(COUNTS[3], COUNTS[4],)))
	for t in THREADS: # begin each thread
		t.start()
	for t in THREADS: # have each thread rejoin main
		t.join()
	print("\nAll dishes have been successfully made.\n")
	sys.exit()
