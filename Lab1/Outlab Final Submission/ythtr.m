function res = ythtr(thtr)
[x0 y0 xw yw v2 vl d w n t1] = num2cell(load('input_outlab_task_A2.txt')){:};
x = xw-x0;
y = yw-y0;
th = thtr;
r = asind(sind(th)/n);
%t = (n*w/(vl*(cosd(r)))+(y+v2*t1-w)/(vl*cosd(th)))/(1-v2/(vl*(cosd(th))));
t = (w*tand(r)*(n*n-1)+x)/(vl*sind(th));
%res = tand(th)-(x)/(y+v2*(2*t1+t)-w*(1-tand(r)/tand(th)));
res = tand(th)-(x+w*(tand(th)-tand(r)))/(y+v2*(t1+t));
endfunction;