function y = lookup_solve_3(x) 
A = load('Amatrix.txt'); 
y = -inv(A)*x;
endfunction;