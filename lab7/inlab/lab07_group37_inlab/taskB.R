data <- read.csv('data_lin.csv')
x = data$x
y = data$y
X = matrix(x,nrow=30,ncol=2)
X[,1] = 1
Y <- matrix(y, nrow=30, ncol=1)
ans = solve(t(X)%*%X)%*%t(X)%*%Y
cat("slope =",ans[2])
cat("\ny intercept =",ans[1])
png('taskB.png')
plot(y~x)
abline(a=ans[1],b=ans[2],col='red')