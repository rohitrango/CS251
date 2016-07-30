function y = lookup_solve_3(x)			% return the matrix which tells what buttons to be pressed	
y = zeros(3);							% initialization
while (!isequal(x,zeros(3)))			% check until all switches are 'off'

	for i=1:3							% to close buttons in the 1st row, we press corresponding buttons in 2nd row.
		if(x(1,i)==1)
			x = pressButton(x,2,i);		% helper function, see its docs
			y(2,i) = 1 - y(2,i);
		endif
	endfor

	for i=1:3							% to close buttons in the 2nd row, we press corresponding buttons in 3rd row.
		if(x(2,i)==1)
			x = pressButton(x,3,i);
			y(3,i) = 1 - y(3,i);
		endif
	endfor

	topRowPress = zeros(1,3);			% to store the result from lookup table
	switch(x(3,:))
		case [0 0 1]
			topRowPress = [0 1 1];
		case [0 1 0]
			topRowPress = [1 1 1];
		case [0 1 1]
			topRowPress = [1 0 0];
		case [1 0 0]
			topRowPress = [1 1 0];
		case [1 0 1]
			topRowPress = [1 0 1];
		case [1 1 0]
			topRowPress = [0 0 1];
		case [1 1 1]
			topRowPress = [0 1 0];
	endswitch
	
	for i=1:3							% press the corresponding buttons in the first row according the result from topRowPress vector
		if(topRowPress(1,i)==1)
			x = pressButton(x,1,i);
			y(1,i) = 1 - y(1,i);
		endif
	endfor

endwhile
endfunction
