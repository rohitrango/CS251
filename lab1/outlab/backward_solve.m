function res =  backward_solve(x)
	b = x(:);
	m = size(x,1);
	n = size(x,2);
	A = zeros(m*n);

	% generating the matrix A 

	for ai = 1:m*n
		if(ai>1 && mod(ai,m)!=1)
			A(ai,ai-1) = 1;
		endif 

		if(ai<m*n && mod(ai,m)!=0)
			A(ai,ai+1) = 1;
		endif 

		if(ai>m)
			A(ai,ai-m) = 1;
		endif 

		if(ai<=m*n-m)
			A(ai,ai+m) = 1; 
		endif

	endfor

	% A(i,i) = 1 for all i
	A = A + eye(m*n);
	adjA = round(det(A)*inv(A));	% compute adjoint A

	res = mod(adjA*b,2);			% solve the equation 
	res = reshape(res,m,n);			% reshape to appropriate size

endfunction 