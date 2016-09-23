suppressMessages(library('Rlof'))
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
  lines(x,b0 + b1*x + b2*x2, col="darkred")
  return(c(index,minError,b0,b1,b2))
}


taskBdata = read.csv('data_non_lin.csv')
taskBdata = taskBdata[with(taskBdata, order(x)),]
x = taskBdata$x
y = taskBdata$y
outlierX = c() ; outlierY = c();
fitX = c(); fitY = c();
density = lof(y,5);
threshold = 5                   # looks like it can handle good outliers
for(i in 1:length(x)) {
  if(density[i] <= threshold) {
    fitX[length(fitX)+1] = x[i];
    fitY[length(fitY)+1] = y[i];
  }
  else {
    outlierX[length(outlierX)+1] = x[i];
    outlierY[length(outlierY)+1] = y[i];
  }
}

plot(fitX,fitY,xlim = c(min(x),max(x)), ylim = c(min(y),max(y)) )
points(outlierX,outlierY,col="red")
# fitX, fitY, fitX2 have the values of non-outliers
# outlierX and outlierY have values of outliers
#############################################################
# Fit the graph (polynomial regression)
#############################################################
fitX2 = fitX^2
lmpoly = lm(fitY ~ fitX + fitX2)
errpoly = sum(lmpoly$residuals^2)
lines(fitX, lmpoly$coefficients[1] + lmpoly$coefficients[2]*fitX + lmpoly$coefficients[3]*fitX2, col="blue")
#############################################################
# Fit the graph (broken regression)
#############################################################
linData = brokenReg(fitX,fitY)
errorBrokenLine = linData[2]
cat('error brokenline =',errorBrokenLine,'\n')
cat('error polynomial =',errpoly,'\n')
cat('outliers','\n')
for(i in 1:length(outlierX)) {
  cat(outlierX[i],outlierY[i],'\n')
}
