# Pentcho Tchomakov
# 260632861

from ctypes import *

# Import the library made of fast_filter.c + fast_filter.h
fastLib = cdll.LoadLibrary("./libfast_filter.so")
import sys

# Code for profiling taken from example @ https://docs.python.org/2/library/profile.html
import cProfile
import StringIO
import pstats

profile = cProfile.Profile()
profile.enable()
# Create a variable from the stdin that represents the size of the matrix
width = int(sys.argv[3]) 
size = width*width

# Create an array from the stdin that represents the filter matrix
weights = []
for i in range(size):
	weights.append(float(sys.argv[4 + i]))

# Extract the content of the original image we want to apply a filter to
with open(sys.argv[1], "rb") as inputFile:
		inputBMPData = inputFile.read()

# For ctypes, reference @ https://docs.python.org/2/library/ctypes.html
# Redefine the passing variables of the doFiltering method to accept ctype variables
doFiltering = fastLib.doFiltering
doFiltering.argtypes = [POINTER(c_ubyte), POINTER(c_float), c_int, POINTER(c_ubyte)]

# Convert all the python variables into ctype variables
inputBMPData = (c_ubyte * len(inputBMPData)).from_buffer_copy(inputBMPData)
weights = (c_float * len(weights))(*weights)
outputBMPData = (c_ubyte * len(inputBMPData))()

# Execute the filter method in libfast_filter
doFiltering(inputBMPData, weights, c_int(width), outputBMPData)

# Write the binary data returned from the libfast_filter into a new BMP.
out_img = open(sys.argv[2], "wb")
out_img.write(outputBMPData)
out_img.close()

# Code for profiling taken from example @ https://docs.python.org/2/library/profile.html
profile.disable()
s = StringIO.StringIO()
sortby = 'cumulative'
ps = pstats.Stats(profile, stream=s).sort_stats(sortby)
ps.print_stats()
print s.getvalue()