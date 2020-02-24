#!/usr/bin/env python3

import sys
from urllib.request import urlopen, urlretrieve
from json import loads
from urllib import error

def invalid_arguments():
	"""
	Reminder to user on execution.
	"""
	print("\nExecute the script as below:")
	print(" $ ./landsat.py <Date>")
	print("Where <Date> is a hyphen-delimited string MM-DD.")
	print("For example: `./landsat.py 07-26`.\n")
	sys.exit(1)

def verify_date_range(s):
	"""
	Verifies whether or not a date is valid.
	
	Parameter:
	s -- date as str array
	
	Returns:
	Whether date is valid or not
	"""
	s = [int(i) for i in s] # convert str array to integer array
	if 1 <= s[0] <= 12: # if month in range
		# find range end depending on days in month
		day_end = 30 if (s[0] % 2 == 0) else 31
		day_end = 28 if (s[0] == 2) else day_end # February
		if 1 <= s[1] <= day_end: # if day in rage
			return True # date valid
		return False # invalid day
	else:
		return False # invalid month

if len(sys.argv) != 2: # bad args
	invalid_arguments()
elif len(str(sys.argv[1]).split('-')) != 2: # invalid args
	invalid_arguments()
elif not verify_date_range(str(sys.argv[1]).split('-')): # invalid date
	invalid_arguments()

# Coordinates for Brock University
LON = "-79.24"
LAT = "43.11"
COORD = f"lon={LON}&lat={LAT}"

# Date for Image Capture
MON = str(sys.argv[1]).split('-')[0]
DAY = str(sys.argv[1]).split('-')[1]
DATE = f"date=2016-{MON}-{DAY}"

API_KEY = "" # LANDSAT8 API key
ARGS = f"?{COORD}&{DATE}&cloud_score=True&api_key={API_KEY}" # args for URL
URL = f"https://api.nasa.gov/planetary/earth/imagery/{ARGS}" # constructed URL

def get_json():
	"""
	Retriever for LANDSAT8 JSON data.
	
	Returns:
	LANDSAT8 JSON data for date argument
	"""
	print("\nAttempting to grab JSON data...", end=' ')
	try:
		response = urlopen(URL) # open URL
		data = response.read().decode("utf-8") # decode to readable
		response.close()
		json_output = loads(data) # grab JSON data
	except error.HTTPError: # 404, etc
		print("failed!")
		print("A 404 response was received.\n")
		sys.exit(1)
	print("success!\n")
	return json_output

def print_results(j):
	"""
	Printer for JSON data.
	
	Parameter:
	j -- the JSON data previously retrieved
	"""
	print(f"Details from ({LON}, {LAT}) on 2016/{MON}/{DAY}:")
	print(f"Cloud Score: {j['cloud_score']}")
	print(f"Datetime: {j['date']}")
	print(f"ID: {j['id']}")
	print(f"Service Version: {j['service_version']}\n")

def save_image(j):
	"""
	Saver for LANDSAT8 image grabbed from JSON.
	
	Parameter:
	j -- the JSON data previously retrieved
	"""
	print("Attempting to save image...", end=' ')
	try:
		urlretrieve(j['url'], "image.png") # open and save image
	except error.HTTPError: # 404, etc
		print("failed!")
		print("A 404 response was received.\n")
		sys.exit(1)
	print("success!\n")
	print("LANDSAT8 photograph saved as \"image.png\".\n")

if __name__ == '__main__':
	json_result = get_json()
	print_results(json_result)
	save_image(json_result)
	sys.exit(0)