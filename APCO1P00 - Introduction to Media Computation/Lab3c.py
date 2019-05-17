def SelectFile():
  myFile = pickAFile()
  myPict = makePicture(myFile)
  for x in range(0, getWidth(myPict)):
    for y in range(0, getHeight(myPict)):
      pxy = getPixel(myPict, x, y)
      colour = getColor(pxy)
      lighterColour = makeLighter(colour)
      setColor(pxy, lighterColour)
  show(myPict)
    