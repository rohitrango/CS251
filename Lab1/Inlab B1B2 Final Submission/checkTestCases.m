% how we checked our program correctness

for i=0:511
	m = zeros(3,3);
	% generate the matrix
	for j = 0:8
		m(j+1) = mod(floor(i/(2^j)),2);
	endfor

	% find solution and check whether the solution is correct 

	solution = lookup_solve_3(m);
	result = final_state_3(m,solution);
	if(!isequal(result,zeros(3)))
		disp(m);
	endif
	
endfor