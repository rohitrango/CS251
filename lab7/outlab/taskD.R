mfpdata = read.csv("mutualFundPerformance.csv")
lister = function(x){
  if(class(x)=="numeric" || class(x)=="logical") return(x)
  sx = strsplit(x,'\"')[[1]]
  newx = sx[length(sx)]
  x.split = strsplit(newx,' ')
  return(lapply(x.split, as.numeric))[[1]]
}
columnstandardize = function(col){
  f = as.list(col)[[1]]
  op  = lapply(levels(f),lister)
}