rawdata = as.list(read.csv("mutualFundPerformance.csv",header=FALSE))
ncol = length(rawdata)
nentries = length(rawdata[[1]])
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
    lmax = max(lmax,length(lolol))
    return(lolol)
    #return(lapply(lolol,function(x) x[1][[1]]))
  }
  else{
    lolol = (lapply(col,list))
    lmax = max(lmax,length(lolol))
    #return(lolol)
    return(lapply(lolol,function(x) x[1][[1]]))
  }
}
udata = lapply((1:length(rawdata)),function(x) colproc(rawdata[[x]]))

#}

#Normalization{
normalize = function(x,mu,stdev){
  if(class(x)=="logical") return(NA)
  if(class(x)=="list") return(lapply(x,function(x) normalize(x,mu,stdev)))
  return ((x-mu)/stdev)
}
mylength = function(x){
  if(class(x)=="logical"){return(0)}
  else{return(length(x))}
}

for(icol in (1:ncol)){
  vctr.sum = 0
  vctr.num = 0
  vctr.sqsum = 0
  for(cell in udata[[icol]]){
    if(!is.na(cell[1])){
      vctr.num = vctr.num + length(cell)
      vctr.sum = vctr.sum + sum(unlist(cell))
      vctr.sqsum = vctr.sqsum + sum(unlist(lapply(cell,function(x) x**2)))
    }
  }
  vctr.mu = vctr.sum/vctr.num
  vctr.stdev = sqrt((vctr.sqsum/vctr.num - vctr.mu**2))
  udata[icol] = normalize(udata[icol],vctr.mu,vctr.stdev)
}
#}

