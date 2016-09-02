import csv

movie_data = list(csv.reader(open('movie-ratings.csv')))
user_data = list(csv.reader(open('user_preference.csv')))
