# Pentcho Tchomakov
# 260632861

import string
import re
import sys

# Open file, read it, put data in variable, set all strings to lowercases, remove all punctuation
with open (sys.argv[1], 'r+') as myfile:
    data = myfile.read().lower()

text = data.translate(string.maketrans("",""), string.punctuation)

# Put words into a list
words = text.split()
list = []

# Calculate word count, print word + wordcount only if it's not already printed
for i in words:
    wordfreq = words.count(i)
    if not i in list:
        list.append(i)
        print i + " " + repr(wordfreq)