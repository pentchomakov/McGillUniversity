# Pentcho Tchomakov
# 260632861

import re
import sys
import string
import pickle

# Extract data from file lowercase every word and remove punctuation
with open (sys.argv[1], 'r+') as myfile:
    data = myfile.read().lower()
text = data.translate(string.maketrans("",""), string.punctuation)

# Create a list by spliting at every space
listWords = text.split()


# Create a tuple list
for item in range(len(listWords) -1):
    listWords[item] = listWords[item] + "," + " " + listWords[item + 1]

# Initialize our dictionary
words = {}

# Create our dictionary with frequencies
for key in listWords:
    if key in words:
        words[key] += 1
    else:
        words[key] = 1

# Save our dictionary in a pickle format - Serialize the data into a pickle format. Works like JSON objects but for Python
pickle.dump(words, open("save.p", "wb"))

# Print our dictionary
for i in words:
    print i, words[i]
