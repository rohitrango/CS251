taskAdata = read.csv('data_bi_lin.csv')
taskAdata = taskAdata[with(taskAdata, order(x),),]
x = taskAdata$x
y = taskAdata$y
samples = length(x)
simple_lm = lm(y ~ x)
plot(x,y)
abline(simple_lm , col= "red")

index = 0
minError = Inf
for(i in 1:samples-1) {
  e1 = sum(lm(y[1:i] ~ x[1:i])$residuals^2)
  e2 = sum(lm(y[i+1:samples] ~ x[i+1:samples])$residuals^2)
  if(e1 + e2 < minError) {
    minError = e1+e2
    index = i
  }
}
fit1 = lm(y[1:index] ~ x[1:index])
fit2 = lm(y[index+1:samples] ~ x[index+1:samples])
m1 = fit1$coefficients[2] ; c1 = fit1$coefficients[1];
m2 = fit2$coefficients[2] ; c2 = fit2$coefficients[1];
x_0 = (c1 - c2)/(m2-m1)

t = 1
while(x[t+1] < x_0) {
  segments(x[t],m1*x[t]+c1,x1 = x[t+1], m1*x[t+1] + c1, col="darkblue")
  t = t+1
}
segments(x[t],m1*x[t]+c1,x1 = x_0, m1*x_0 + c1, col="blue")
segments(x_0,m2*x_0+c2,x1 = x[t+1], m2*x[t+1] + c2, col="blue")
t = t+1
while(t < samples-1) {
  segments(x[t],m2*x[t]+c2,x1 = x[t+1], y1 = m2*x[t+1] + c2, col="darkblue")
  t = t+1
}

cat('error lm =',sum(simple_lm$residuals^2),"\n")
cat('error broken =',minError,"\n")
cat('C =',x[index],"\n")
  
