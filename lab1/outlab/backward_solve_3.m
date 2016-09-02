function y = backward_solve_3(x) 
sx = [x(1,:) x(2,:) x(3,:)]'; 
load Ainvmod2;
sy = mod(Ainvmod2*sx,2);
y = [sy(1:3,1)'; sy(4:6,1)'; sy(7:9,1)'];
endfunction;