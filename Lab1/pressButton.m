function res = pressButton(x,i,j)		% modify the matrix by simulating button press at(i,j) position
res = x;

%changes the current button state
res(i,j) = 1 - x(i,j);

% the vertical adjacent buttons are toggled according to the following if-else statements
									
if(i==1)								
	res(2,j) = 1-x(2,j);
elseif (i==2)
	res(1,j) = 1-x(1,j);
	res(3,j) = 1-x(3,j);
elseif (i==3)
	res(2,j) = 1-x(2,j);
endif

% the horizontal adjacent buttons are toggled according to the following if-else statements

if(j==1)
	res(i,2) = 1-x(i,2);
elseif (j==2)
	res(i,1) = 1-x(i,1);
	res(i,3) = 1-x(i,3);
elseif (j==3)
	res(i,2) = 1-x(i,2);
endif

endfunction
