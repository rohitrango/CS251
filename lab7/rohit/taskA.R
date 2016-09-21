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
abline(lm(y[1:index] ~ x[1:index]),col="blue")
abline(lm(y[index+1:samples] ~ x[index+1:samples]),col="green")
cat('error lm =',sum(simple_lm$residuals^2),"\n")
cat('error broken =',minError,"\n")
cat('C =',x[index],"\n")