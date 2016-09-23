brokenReg = function(x,y) {
  index = 0
  b0 = b1 = b2 = 1
  minError = Inf
  n = length(x)
  
  for(i in 1:n) {
    x1 = x;
    x2 = (x >= x[i])*(x-x[i])
    minFit = lm(y ~ x1 + x2)
    error = sum(minFit$residuals^2)
    if(error < minError) {
      minError = error
      index = i
      b0 = minFit$coefficients[1]
      b1 = minFit$coefficients[2]
      b2 = minFit$coefficients[3]
    }
  }
  
  x2 = (x >= x[index])*(x-x[index])
  plot(x,y)
  lines(x,b0 + b1*x + b2*x2, col="blue")
  return(c(index,minError,b0,b1,b2))
}

taskAdata = read.csv('data_bi_lin.csv')
taskAdata = taskAdata[with(taskAdata, order(x)),]
x = taskAdata$x
y = taskAdata$y
samples = length(x)
simple_lm = lm(y ~ x)

regressionResults = brokenReg(x,y)
abline(simple_lm,col="red")
cat('error lm =',sum(simple_lm$residuals^2),"\n")
cat('error broken =',regressionResults[2],"\n")
cat('C =',x[regressionResults[1]],"\n")

