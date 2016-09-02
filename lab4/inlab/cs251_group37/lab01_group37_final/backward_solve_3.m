%Function for backward solving 3*3 matrix

function y = backward_solve_3(x)
b = x'(:);    % Convert input matrix to a vector
load Apush;     % Load the matrix which shows the lights toggled on pressing a specific button.
AdjA = round(det(A)*inv(A));    % Calculating the adjoint of the above matrix
z = mod(-AdjA*b,2);      % Solving the equation (b + Ax) % 2 = 0 for x using modular arithmetics
                         % x = (-AdjA*b) % 2
y = reshape(z,3,3)';     % Coverting vector to a 3*3 matrix showing the buttons to be pressed for lights out
endfunction
