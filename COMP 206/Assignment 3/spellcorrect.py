# Import all indeed libraries
import difflib
import sys
import operator
import pickle

tries = {}

start = True

# Open the required file with a file
with open(sys.argv[1], 'rb') as file:
    words = pickle.load(file)

compare = {}

# Remove all punctuation from the file. Only the one specified
for item in words:
    itemN = item.replace("[", "").replace("]", "").replace("'", "").replace(",", "")
    itemN = itemN.split()
    key = (itemN[0],itemN[1])    compare[key] = words[item]

# Put the input into a list
while start:
    wording = raw_input()
    wording = wording.split()
    wordingInProgess = wording[0]
    finalWord = wording[1]
    
# Initlialize Dictonary
statement = {}
    
    for key in compare:
        if wordingInProgess == key[0]:
            print "The word is corrected"
            break

# Check if word is totally wrong
if len(statement) == 0:
    print "ERROR: The word is incorred"
    for key in compare:
        if key[1] == finalWord:
            statement[key] = compare[key]

# Create list
listOf = ''
    for j in statement:
        listOf = listOf + " " + j[0]
listOf = listOf.split()
    
    
    # Compare to library
    big10 = difflib.get_close_matches(wordingInProgess, listOf, n=10, cutoff=0)
    
    for k in range(0, 10, 1):
        for key in statement:
            if key[0] == big10[k]:
                tries[key] = statement[key]

# Give a solution to the wrong
suggestion = max(tries.iteritems(), key=operator.itemgetter(1))[0]
    suggestion = " ".join(suggestion)
    print "Fix: ", suggestion
