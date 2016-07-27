function y = f(x) % Function whose root has to be found
n = load('input_inlab_task_A1.txt'); % loading values of nx and ny
y = sind(x) - (n(1)*cosd(x) + n(2)*sind(x))*sind(x/3);
endfunction
