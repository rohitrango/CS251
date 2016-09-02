def function2(listVariable, n):
	print(list(map(lambda tup: tup[n], listVariable)))	# 'map' maps each element of list to corresponding value of function, returns iterator to output list, 'list' iterates over the iterator
	return(sorted(listVariable,key=lambda tup: tup[n]))
