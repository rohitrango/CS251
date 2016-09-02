% function res =  backward_solve(x)
% 	b = x(:);
% 	m = size(x,1);
% 	n = size(x,2);
% 	A = zeros(m*n);

% 	% generating the matrix A, when boxes are numbered vertically

% 	%%% NOTE: 	we have a faster version to calculate A, using no for loop. Written explanation is difficult. 
% 	%%% 		But we can explain verbally. It is attached as a comment below. 

% 	for ai = 1:m*n
% 		if(ai>1 && mod(ai,m)!=1)		%checks if it is not boundary cases
% 			A(ai,ai-1) = 1;				%i toggles i-1
% 		endif

% 		if(ai<m*n && mod(ai,m)!=0)		%checks if it is not boundary cases
% 			A(ai,ai+1) = 1;				%i toggles i+1
% 		endif

% 		if(ai>m)						%checks if it is not boundary cases
% 			A(ai,ai-m) = 1;				%i toggles i-m
% 		endif

% 		if(ai<=m*n-m)					%checks if it is not boundary cases
% 			A(ai,ai+m) = 1;				%i toggles i+m
% 		endif

% 	endfor

% 	A = A + eye(m*n);					%i toggles i
% 	adjA = round(det(A)*inv(A));	% compute adjoint A

% 	res = mod(adjA*b,2);			% solve the equation
% 	res = reshape(res,m,n);			% reshape to appropriate size

% endfunction

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%% FASTER VERSION:-

function res =  backward_solve(x)
	b = x(:);
	m = size(x,1);
	n = size(x,2);
	A = zeros(m*n);

	% generating the matrix A

	A = findFactor(size(x'));

	% A(i,i) = 1 for all i
	adjA = round(det(A)*inv(A));	% compute adjoint A

	res = mod(adjA*b,2);			% solve the equation
	res = reshape(res,m,n);			% reshape to appropriate size

endfunction

%% file findFactor.m :-

% function res = findFactor(size)
% m = size(1);
% n = size(2);
% ID = eye(m*n);
% res = ID;
% ID(n:n:m*n,n:n:m*n) = 0;
% res(m*n+1 : (m*n)^2) += ID(1 : m*n*(m*n-1));
% ID(n:n:m*n,n:n:m*n) = eye(m);
% ID(n+1:n:m*n,n+1:n:m*n)=0;
% res(1 : m*n*(m*n-1)) += ID(m*n+1 : (m*n)^2);
% ID(n+1:n:m*n,n+1:n:m*n)= eye(m-1);
% res(m*n^2+1 : (m*n)^2) += ID(1 : m*n^2*(m-1));
% res(1 : m*n^2*(m-1)) += ID(m*n^2+1 : (m*n)^2);
% endfunction;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

