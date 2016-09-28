from matplotlib import pyplot as plt
activeMines = []
inactiveMines = []

ground = open('Mines','r')
for mine in ground:
	mine = map(int,mine.split(' '))
	if (mine[0]+mine[1])%2 == 1 :			# odd, active
		activeMines.append(mine)
	else:
		inactiveMines.append(mine)
ground.close()

plt.figure("Mine plot!")
plt.scatter(map(lambda x: x[0],activeMines),map(lambda x: x[1],activeMines),c='lightblue',marker='o')
plt.scatter(map(lambda x: x[0],inactiveMines),map(lambda x: x[1],inactiveMines),c='red',marker='s')
plt.show()
