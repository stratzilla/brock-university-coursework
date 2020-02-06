#!/usr/bin/env python3

import sys
from urllib.request import urlretrieve
from urllib import error

# remind user if invalid arguments
if len(sys.argv) != 2:
	print("\nExecute the script as below:")
	print(" $ ./barcode_scraper.py <ID>")
	print("Where <ID> is the student number of the barcode.\n")
	sys.exit(1)

def get_barcode(arg):
	"""
	Connects and grabs barcode image of supplied argument.

	Parameter:
	arg -- the student number to find barcode for
	"""
	print(f"Attempting to grab barcode with id {arg}...", end=' ')
	url = f"https://www.cosc.brocku.ca/barcode/{arg}.png"
	try: # attempt to find image
		urlretrieve(url, "barcode.png")
	except error.HTTPError: # 404, image doesn't exist
		print("failed!")
		print("Barcode doesn't exist for this number.\n")
		sys.exit(1)
	print("grabbed!")
	print("Barcode saved as 'barcode.png'.\n")

if __name__ == "__main__":
	print()
	get_barcode(sys.argv[1])
	sys.exit(0)
