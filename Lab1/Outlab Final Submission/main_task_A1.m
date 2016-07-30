function y = lookup_solve_3(x)
inp = x'(:);
A = [1 1 0 1 0 0 0 0 0; 1 1 1 0 1 0 0 0 0; 0 1 1 0 0 1 0 0 0; 1 0 0 1 1 0 1 0 0; 0 1 0 1 1 1 0 1 0; 0 0 1 0 1 1 0 0 1; 0 0 0 1 0 0 1 1 0; 0 0 0 0 1 0 1 1 1; 0 0 0 0 0 1 0 1 1 ];
AdjA = round(det(A)*inv(A));
z = mod(-AdjA*inp,2);
y = reshape(z,3,3)';
endfunction
