from matplotlib import pyplot as plt
## Take in the file first
## assumed that file name is midsem.tsv
titles = []
iofile = open('midsem.tsv')
feedback = []
for line in iofile:
	line = line.split('\t')
	# give the titles the values of the heading
	# push the values into the lines if the overall course feedback is "Very Good"
	if(titles == []):
		titles = line[9:15]
	if(line[1].lower() == 'very good'):
		feedback.append(map(int,line[9:15]))

iofile.close()

labs = []
for i in range(6):
	labs.append(map(lambda x: x[i], feedback))		
	# Take the ith lab from all feedback

# Plot each of the histograms! 
for i in range(6):
	plt.figure(titles[i])
	plt.hist(labs[i], bins=[0.5,1.5,2.5,3.5,4.5,5.5], rwidth = 0.75)
	plt.xlim(0,6)
	plt.suptitle(titles[i])
	plt.xlabel('Rating')
	plt.ylabel('Number of Responses!')
plt.show()
