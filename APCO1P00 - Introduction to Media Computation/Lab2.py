#Exercise 1, Program 1
def sum(someNumber):
  total = 0
  for counter in range(1, someNumber + 1):
    total = total + counter
  print total
  
#Exercise 1, Program 1b
def cumulativeSum(someNumber):
  sum = 0
  for num in range(number):
    sum = sum + num
  print sum
  
#Exercise 1, Program 2
def bottles(someNumber):
  for c in range(someNumber, 0, -1):
    print c, " bottles of pop on the wall.",
    print c, " bottles of pop!"
    print "Take one down, pass it around. ",
    print (c-1), " bottles of pop on the wall."
    
#Exercise 2
def pickAndShow():
  myFile = pickAFile()
  picture = makePicture(myFile)
  show(myPict)
def makeSunset(picture):
  for p in getAllPixels(picture):
    value = getBlue(p)
    setBlue(p, value*0.7)
    value = getGreen(p)
    setGreen(p, value*0.7)
def makeSunsetMethods(picture):
  reduceBlue(picture)
  reduceGreen(picture)
def reduceBlue(picture):
  for p in getAllPixels(picture):
    value = getBlue(p)
    setBlue(p, value*0.3)
def reduceGreen(picture):
  for p in getAllPixels(picture):
    value = getGreen(p)
    setGreen(p, value*0.3)