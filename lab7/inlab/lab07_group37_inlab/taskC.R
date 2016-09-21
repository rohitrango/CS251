mydata = read.csv('data_non_lin.csv')
x1 = mydata$x
x2 = x1^2
y = mydata$y
formula = y ~ x1 + x2
lmout = lm(formula=formula)
xrange = seq(-10,10,0.1)
predictor = predict(lmout,list(x1=xrange,x2=xrange^2))
const = lmout$coefficients[1]
coeffx = lmout$coefficients[2]
coeffx2 = lmout$coefficients[3]
cat ('error lm =',sum(lmout$residuals^2),"\n")
cat ('const =',const,"\n")
cat ('coeffx =',coeffx,"\n")
cat ('coeffx2 =',coeffx2,"\n")

png('taskC.png')
plot(x1,y,col="blue",xlab="Values of x",main = "Non-linear fit", ylab = "Values of y")
lines(xrange, predictor, col = "darkgreen", lwd = 3)
