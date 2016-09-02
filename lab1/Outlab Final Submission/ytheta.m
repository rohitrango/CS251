function res = ytheta(theta)
[x0 y0 xw yw v2 vl d w n t1] = num2cell(load('input_outlab_task_A2.txt')){:}; 	%loads file into all variables, assigning
																				%individual variables to vector elements
x = xw-x0;																			%relative position
y = yw-y0;																			%theta = incidence angle
r = asind(sind(theta)/n);															%snells law
t = (w*tand(r)*(n*n-1)+x)/(vl*sind(theta));											%time between shoot and hit = sum of (delta x)/(x comp of velocity)
res = tand(theta)-(x+w*(tand(theta)-tand(r)))/(y+v2*(t1+t));						%res = 0 to be solved by fsolve
endfunction;