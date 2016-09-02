def function1(string1,string2,listVariable):
	zipped = zip(string1,string2) 				#forms elementwise tuples
	listVariable = listVariable + list(zipped)
	return listVariable
s1 = "Homebrew"
s2 = "Group37"
mylist = [1,2,3,4,5]
mynewlist = function1(s1,s2,mylist)
print(mylist)
print(mynewlist)