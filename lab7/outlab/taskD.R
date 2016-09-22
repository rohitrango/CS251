rawdata = as.list(read.csv("mutualFundPerformance.csv"))

#loads rawdata into usable format udata{

#converts each cell to list{
lister = function(x){
  if(class(x)=="numeric" || class(x)=="logical") return(x)
  if(x=="") return(NA)
  stripped = strsplit(x,'\"')[[1]]
  charlist = strsplit(stripped[length(stripped)],' ')
  return(lapply(charlist, as.numeric))[[1]]
}
#}
colproc = function(col){
  if(class(col)=="factor"){
    lolol = (lapply(col,function(x) lister(levels(factor(x))))) #listoflistoflist
    return(lapply(lolol,function(x) x[1][[1]]))
  }
  else{
    lolol = (lapply(col,list))
    return(lapply(lolol,function(x) x[1][[1]]))
  }
}
udata = lapply((2:length(rawdata)),function(x) colproc(rawdata[[x]]))

#}