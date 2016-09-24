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
  lm(y ~ x + xc)
  error[[index]] <- sum(lm(y ~ x + xc)$residuals^2)
}
error

C.index = which.min(error)
C.value = x[[C.index]]

first.x = x[1:C.index]
first.y = y[1:C.index]
second.x = x[(C.index+1):length(x)]
second.y = y[(C.index+1):length(x)]

first = lm(first.y ~ first.x)
second = lm(second.y ~ second.x)

abline(first, col = 'blue')
abline(second, col = 'blue')
