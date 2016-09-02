import csv
data = list(csv.reader(open('movie-ratings.csv')))
headers = data[0][1:]
def evalmap(x):
    return list(map(eval,x))
def tupler(x):
    return (x[0],evalmap(x[1:]))
ratings = list(map(tupler,data[1:]))
data = list(csv.reader(open('user_preference.csv')))[0]
upref = (data[0],list(map(eval,data[1:])))
from math import sqrt
def pearson(x):
    relevant = list(filter(lambda t: t[0]!=-1 and t[1]!=-1,list(zip(x[1],upref[1]))))
    n = len(relevant)
    mux = sum(list(map(lambda t: t[0],relevant)))/n
    muy = sum(list(map(lambda t: t[1],relevant)))/n
    muxy = sum(list(map(lambda t: t[0]*t[1],relevant)))/n
    sx = sqrt(sum(list(map(lambda t: (t[0]-mux)**2,relevant)))/n)
    sy = sqrt(sum(list(map(lambda t: (t[1]-mux)**2,relevant)))/n)
    pearsoncoeff = (muxy-mux*muy)/(sx*sy)
    # print(pearsoncoeff)
    return pearsoncoeff
sortedRatings = sorted(ratings, key = pearson, reverse=True)
print(sortedRatings)
# print(headers)
# print(ratings)
# print(upref)
