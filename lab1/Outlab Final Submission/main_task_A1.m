file = fopen('output_outlab_task_A1.txt', 'w');

[x y d w n] = num2cell(load('input_outlab_task_A1.txt')){:}; %Load input into variables

fprintf(file, '%f\n', fsolve(@(theta) tand(theta)-(x + w*sind(theta)*(1 - cosd(theta)/sqrt(n^2 - sind(theta)^2))*secd(theta))/y, 45));

% lateral_displacement = w*sind(theta)*(1 - cosd(theta)/sqrt(n^2 - sind(theta)^2))  % Caused due to smoke screen
% x_shoot = x + lateral_displacement*secd(theta)   %  x-coordinate of the point to shoot at.
% y_shoot = y_shoot  %   y-coordinate of the point to shoot at.
% Equation used -> tand(theta) = x_shoot/y_shoot

fclose(file);
