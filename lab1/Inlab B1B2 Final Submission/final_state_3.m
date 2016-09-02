function y = final_state_3(initial, action)			%performs action on initial matrix and returns the answer
A = [1 1 0 1 0 0 0 0 0; 1 1 1 0 1 0 0 0 0; 0 1 1 0 0 1 0 0 0; 1 0 0 1 1 0 1 0 0; 0 1 0 1 1 1 0 1 0; 0 0 1 0 1 1 0 0 1; 0 0 0 1 0 0 1 1 0; 0 0 0 0 1 0 1 1 1; 0 0 0 0 0 1 0 1 1 ];
% A is the 9*9 matrix where A(i,j) = 1 <==> button i is toggled on pressing button j
b = initial'(:);		
x = action'(:);
% b and x are the column vectors of initial and action, for easier calculation
z = mod((b + A*x),2);		% algorithm gives the required solution
y = reshape(z,3,3)';		% reshape to 3*3 matrix
endfunction
