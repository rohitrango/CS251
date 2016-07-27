function y = lookup_solve_3(initial, action)
A = [1 1 0 1 0 0 0 0 0; 1 1 1 0 1 0 0 0 0; 0 1 1 0 0 1 0 0 0; 1 0 0 1 1 0 1 0 0; 0 1 0 1 1 1 0 1 0; 0 0 1 0 1 1 0 0 1; 0 0 0 1 0 0 1 1 0; 0 0 0 0 1 0 1 1 1; 0 0 0 0 0 1 0 1 1 ];
b = initial'(:);
x = action'(:);
z = mod((b + A*x),2);
y = reshape(z,3,3)';
endfunction
