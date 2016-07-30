warning('off');
i = 10;
info = -1;
while(info!=1)
[x2,residue,info] = fsolve(@ythtr,40)
i += 10;
endwhile;