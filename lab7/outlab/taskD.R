rawdata = as.list(read.csv("mutualFundPerformance.csv",header=FALSE))

lmax = 1 #max no. of values in cell

#loads rawdata into usable format udata{

#converts each cell to list{
lister = function(x){
  if(class(x)=="numeric" || class(x)=="logical") return(x)
  if(x=="") return(NA)
  stripped = strsplit(x,'\"')[[1]]
  charlist = strsplit(stripped[length(stripped)],' ')
  retvalue = (lapply(charlist, as.numeric))[[1]]
  lmax = max(lmax,length(retvalue))
  return(retvalue)
}
#}
colproc = function(col){
  if(class(col)=="factor"){
    lolol = (lapply(col,function(x) lister(levels(factor(x))))) #listoflistoflist
    return(lolol)
    #return(lapply(lolol,function(x) x[1][[1]]))
  }
  else{
    lolol = (lapply(col,list))
    return(lolol)
    #return(lapply(lolol,function(x) x[1][[1]]))
  }
}
udata = lapply((1:length(rawdata)),function(x) colproc(rawdata[[x]]))

#}