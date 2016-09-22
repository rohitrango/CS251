inp = read.csv("data_bi_lin.csv")
plot(y~x,data=inp)
lmout = lm(y~x, data=inp)
abline(lmout, col="red")
errorlm = sum(lmout$residuals^2)
n=nrow(inp)
minerror = Inf
mini = 3
for(i in 1:(n-1)){
  error = 0
  lmout = lm(y~x, data=inp[1:i,])
  error = sum(lmout$residuals^2)
  lmout = lm(y~x, data=inp[(i+1):n,])
  error = error + sum(lmout$residuals^2)
  minerror=min(error,minerror)
  if(minerror==error) mini = i
}
lmout = lm(y~x, data=inp[1:mini,])
abline(lmout, col="green")
lmout = lm(y~x, data=inp[(mini+1):n,])
abline(lmout, col="green")
cat("error lm = ", errorlm)
cat("error broken = ", minerror)
cat("C =",inp[mini,2])
