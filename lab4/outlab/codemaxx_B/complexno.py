class ComplexNumber:
	def __init__(self, real=0.0, imag=0.0):
		self.real = real
		self.imag = imag

	def __add__(self, other):
		if type(other) is ComplexNumber:
			sum = ComplexNumber(self.real + other.real, self.imag + other.imag)
			return sum
		elif type(other) is int or type(other) is float:
			sum = ComplexNumber(self.real + other, self.imag)
			return sum

	def __mul__(self, other):
		if type(other) is ComplexNumber:
			prod = ComplexNumber(self.real*other.real - self.imag*other.imag, self.real*other.imag + self.imag*other.real)
			return prod
		elif type(other) is int or type(other) is float:
			prod = ComplexNumber(self.real*other, self.imag*other)
			return prod

	def __truediv__(self,other):
		if type(other) is int or type(other) is float:
			div = ComplexNumber(self.real/other, self.imag/other)
			return div


	def __str__(self):
		return "(%.04f,%.04f)"%(self.real,self.imag)

	def mysquare(self):
		return self*self

	def __repr__(self):
		return 'ComplexNumber(real=%.04f, imag=%.04f)' % (self.real,self.imag)
