data <- read.csv('data.csv')
y = data$y
x = data$x
meany = mean(y)
mediany = median(y)
sdy = sd(y)
png('plotA.png')
plot(x,y)
abline(h=(meany+sdy), lty = 2, col='purple')
abline(h=(meany-sdy), lty = 2, col='purple')
abline(h=meany, lty = 2, col='green')
abline(h=mediany, lty = 2, col='blue')
abline(lm(y ~ x),col='red')
legend(2,295,legend=c('fit','mean','median','sigma_bar'),col=c('red','green','blue','purple'),lty=c(1,2,2,2),cex=0.8)
error <- sum(lm(y~x)$residuals^2)
intercept <- lm(y~x)$coefficients[[1]]
slope <- lm(y~x)$coefficients[[2]]
cat("mean =",meany)
cat("\nmedian =",mediany)
cat("\nstandard deviation =",sdy)
cat("\nslope =",slope)
cat("\ny intercept =",intercept)
cat("\nerror =",error,"\n")