broken.regression = function(x,y){
  error = list()
  for(i in x){
    index = match(i,x)
    xc = sapply(x, function(a){ return(max(a-i, 0))})
    broken = lm(y ~ x + xc)
    error[[index]] <- sum(lm(y ~ x + xc)$residuals^2)
  }
  C.index = which.min(error)
  C.value = x[[C.index]]
  xc = sapply(x, function(a){ return(max(a-C.value, 0))})
  return(list(lm(y~x+xc),xc))
}

data = read.csv("data_non_lin.csv")
x = data$x
x2 = x^2
y = data$y
data.lof = lof(data, 5)
thres = 5
newdata.good = lapply(seq_along(data.lof), function(a) {
  if(data.lof[[a]] < thres) return(y[[a]]) else return(NA)
  })

newdata.bad = lapply(seq_along(data.lof), function(a) {
  if(data.lof[[a]] >= thres) return(y[[a]]) else return(NA)
  })

plot(x,newdata.good, ylim=c(-150, 250))
points(x,newdata.bad, ylim=c(-150, 250),col='red')
# lines(x,predict(lm(y ~ x + x2), list(x,x2)), col = 'blue', lwd=3)
lines(x,predict(lm(unlist(newdata.good) ~ x + x2), list(x,x2)), col = 'blue', lwd=3)

broken = broken.regression(x,unlist(newdata.good))
lines(x,predict(broken[[1]], list(x,broken[[2]])), col = 'red', lwd=2)
