A(x) = (x/sigma**2)*exp(-(x**2)/2/sigma**2)
N(x) = 1/sqrt(2*pi)/s*exp(-x**2/(2*s**2))
D(x) = A(x) + 0.05*N(x)
s=10;sigma=10;

fit D(x) 'data.dat' using 1:2 via s,sigma
set xlabel "Values of x"
set ylabel "Data points"
set title "Extracting distribution and error from data"
plot 'data.dat' w lines lt rgb "red" t "Given data" , D(x) w points lt rgb "blue" t "Fitting data (D(x))", A(x) w lines lt rgb "green" t "Original distribution (A(x))"
pause -1