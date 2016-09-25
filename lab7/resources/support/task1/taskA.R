data = read.csv("data_bi_lin.csv")
x = data$x
y = data$y
plot(x,y)
abline(lm(y ~ x),col='red')
error <- sum(lm(y~x)$residuals^2)

errors = list()
for(i in x){
  index = match(i,x)
  xc = sapply(x, function(a){ return(max(a-i, 0))})
  broken = lm(y ~ x + xc)
  error[[index]] <- sum(lm(y ~ x + xc)$residuals^2)
}
error

C.index = which.min(error)
C.value = x[[C.index]]

# first.x = x[1:C.index]
# first.y = y[1:C.index]
xc = sapply(x, function(a){ return(max(a-C.value, 0))})
# second.x = (x+xc)[(C.index+1):length(x)]
# second.y = y[(C.index+1):length(x)]
# 
# first = lm(first.y ~ first.x)
# second = lm(second.y ~ second.x)
# 
# abline(first, col = 'blue')
# abline(second, col = 'blue')
broken = lm(y~x+xc)
b0 = broken$coefficients[[1]]
b1 = broken$coefficients[[2]]
b2 = broken$coefficients[[3]]
plot(x,y)
lines(x, b0 + b1*x + b2*xc, col='blue')
