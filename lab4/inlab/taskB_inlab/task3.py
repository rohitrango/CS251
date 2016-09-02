f = open('matrix.txt','r')
ft = f.read().split("\n")
m,n = list(map(eval,ft[0].split(' ')))
mat = [[None]*n]*m  		# initializes with 'none' elements
f.close()

for i in range(m):
	mat[i] = list(map(eval,ft[i+1].split(" "))) # output of map = iterator to ith row, list returns ith row

transpose = [[None]*m]*n

transpose = list(zip(*mat)) # * unpacks matrix to individual rows, list(zip) forms the tuples,
transpose = list(map(list,transpose)) # map(list) converts each tuple to list iterator

g = open('transpose.txt','w')
g.write('%d %d\n'%(n,m))
stringMat = str(transpose).replace("], ","\n").replace("[","").replace(",","").replace("]","")
g.write(stringMat)
g.close()