function y = ytheta(x)
	% [x1,y1,d,w,eta] = load('input_outlab_task_A1.txt')
	p = load('input_outlab_task_A1.txt');
	y = p(4)*(1 - (cosd(x)/sqrt(p(5)^2 - (sind(x))^2))) - p(2) + p(1)*cotd(x);
endfunction