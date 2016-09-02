function res = findFactor(size)
m = size(1);
n = size(2);
ID = eye(m*n);
res = ID;
ID(n:n:m*n,n:n:m*n) = 0;
res(m*n+1 : (m*n)^2) += ID(1 : m*n*(m*n-1));
ID(n:n:m*n,n:n:m*n) = eye(m);
ID(n+1:n:m*n,n+1:n:m*n)=0;
res(1 : m*n*(m*n-1)) += ID(m*n+1 : (m*n)^2);
ID(n+1:n:m*n,n+1:n:m*n)= eye(m-1);
res(m*n^2+1 : (m*n)^2) += ID(1 : m*n^2*(m-1));
res(1 : m*n^2*(m-1)) += ID(m*n^2+1 : (m*n)^2);
endfunction;