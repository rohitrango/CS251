for i = [1:11:90]
[shootangle,residue,info] = fsolve('ytheta',40);			%solves for shootangle with different initial guesses
if(info==1)												%implies correct solution found
break;
endif;
endfor;

[xtie ytie xwing ywing v2 vlaser d w n t1] = num2cell(load('input_outlab_task_A2.txt')){:};		%reads from file and assigns the variables respectively

x = xwing-xtie;
y = ywing-ytie;											%relative position required
r = asind(sind(shootangle)/n); 							%snells law
t = (w*tand(r)*(n*n-1)+x)/(vlaser*sind(shootangle));	%time between shoot and hit = sum of (delta x)/(x comp of velocity)

final_xpos = xwing;										%final position of xwing
final_ypos = ywing+v2*(t1+t);

fid=fopen('output_outlab_task_A2.txt','w+');
fprintf(fid,"%.2f %f %f",shootangle,xwing,ywing+v2*(t1+t));
fclose(fid);