function y = final_state_3(i,a)
si = [i(1,:) i(2,:) i(3,:)]';
sa = [a(1,:) a(2,:) a(3,:)]';
load A;
sy = mod(si+A*sa,2);
y = [sy(1:3,1)'; sy(4:6,1)'; sy(7:9,1)'];
endfunction;