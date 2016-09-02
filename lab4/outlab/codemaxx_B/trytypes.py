from complexno_codemaxx import *


def myexponent(x):
	fact = 1.0
	ex = 1.0
	y = x
	for i in range(1,5):
		fact = fact*i
		ex = y/fact + ex
		y = y*x
	print(ex)

myexponent(1)
b = ComplexNumber(1,1)
myexponent(b)