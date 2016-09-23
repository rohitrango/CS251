rawdata = (read.csv("mutualFundPerformance.csv",header=FALSE))
ncol = length(rawdata)
nentries = length(rawdata[[1]])
lmax = 1
#loads rawdata into usable format udata
#converts each cell to list
lister = function(x){
  if(class(x)=="numeric" || class(x)=="logical") return(x)
  if(x=="") return(NA)
  stripped = strsplit(x,'\"')[[1]]
  charlist = strsplit(stripped[length(stripped)],' ')
  retvalue = (lapply(charlist, as.numeric))[[1]]
  lmax = max(lmax,length(retvalue))
  return(retvalue)
}

# return column converted into usable format
colproc = function(col){
  if(class(col)=="factor"){
    lolol = (lapply(col,function(x) lister(levels(factor(x))))) #listoflistoflist
    lmax = max(lmax,length(lolol))
    return(lolol)
  }
  else{
    lolol = (lapply(col,list))
    lmax = max(lmax,length(lolol))
    return(lapply(lolol,function(x) x[1][[1]]))
  }
}

vdata = udata = lapply((1:length(rawdata)),function(x) colproc(rawdata[[x]]))

# Normalization, x can be list, array or anything else
normalize = function(x,mu,stdev){
  if(class(x)=="logical") return(NA)
  if(class(x)=="list") return(lapply(x,function(x) normalize(x,mu,stdev)))
  return ((x-mu)/stdev)
}

# length of x of any list
mylength = function(x){
  if(class(x)=="logical"){return(0)}
  else{return(length(x))}
}

## Normalise columns individually
for(icol in (1:ncol)) {
  # vctr.sum = 0
  # vctr.num = 0
  # vctr.sqsum = 0
  # for(cell in udata[[icol]]) {
  #   if(!is.na(cell[1])){
  #     vctr.num = vctr.num + length(cell)
  #     vctr.sum = vctr.sum + sum(unlist(cell))
  #     vctr.sqsum = vctr.sqsum + sum(unlist(lapply(cell,function(x) x**2)))
  #   }
  # }
  v = unlist(udata[[icol]])
  vctr.mu = mean(v , na.rm = T)
  vctr.stdev = sd(v,na.rm = T)

  udata[[icol]] = normalize(udata[[icol]],vctr.mu,vctr.stdev)
  udata[[icol]] = normalize(udata[[icol]],-6,0.1)
}

### All cool
###################################################################################################
# Ranking 1
# Give the non-NA entries for the list
countnum = function(x){
  return(sum(unlist(lapply(x,is.numeric))))
}

X=15
firstScores = list()

for(i in (1:nentries)){
  firstScores[[i]] = sum(unlist(sapply(udata,function(x) sum(unlist(x[[i]]),na.rm=TRUE))),na.rm=TRUE)
  entry.num = sum(unlist(sapply(udata,function(x) countnum(x[[i]]))),na.rm=TRUE)
  firstScores[[i]] = firstScores[[i]] / entry.num
  if(entry.num == 0)
    firstScores[[i]] = NA
}

firstScores = sort(unlist(firstScores),decreasing =TRUE,index.return=TRUE)
firstScores$ranks = c(1)

#determine ranks
for(i in 2:length(firstScores$x)) {
  if(firstScores$x[i] < firstScores$x[i-1])  {            # be either decreasing or equal
  firstScores$ranks[i] = i
  }  
  else {
    firstScores$ranks[i] = firstScores$ranks[i-1]
  }
}

# print here
for(i in 1:nentries) {
  if(firstScores$x[i] > -Inf) {
      if(firstScores$ranks[i] > X) {
        break
      }
      else{
        cat(firstScores$ix[i],firstScores$x[i],'\n')
      }
  }
  else
    break
}
cat('\n\n')

# Ranking2
# Give best-n normalised scores
X=15
n=10
secondScores = list()           # list to insert -Inf or total score of 1:n
udata = normalize(udata,60,10)  # normalize the scores from standarised data
## find the top X scores
for(i in (1:nentries)){
    rowScore = sort(unlist(sapply(udata,function(x) x[[i]])),decreasing=TRUE)
    if(length(rowScore) < n) {
        secondScores[[i]] = -Inf
    }
    else {
        secondScores[[i]] = sum(rowScore[1:n])
    }
}

secondScores = sort(unlist(secondScores),decreasing =TRUE,index.return=TRUE)
secondScores$ranks = c(1)
#determine ranks
for(i in 2:length(secondScores$x)) {
  if(secondScores$x[i] < secondScores$x[i-1])  {            # be either decreasing or equal
  secondScores$ranks[i] = i
  }  
  else {
    secondScores$ranks[i] = secondScores$ranks[i-1]
  }
}

### Print here
for(i in 1:nentries) {
  if(secondScores$x[i] > -Inf) {
      if(secondScores$ranks[i] > X) {
        break
      }
      else{
        cat(secondScores$ix[i],secondScores$x[i],'\n')
      }
  }
  else
    break
}
cat('\n\n')


## ndata is used for the 4 bin system
## Third part, put into bins!
## Already normalized data
## thirdScores[[i]][[j]] = ith company, jth bin
X = 15
n = 10
ndata = vdata       # use original data to standardize

## Normalise columns '4' bins at a time
for(icol in (seq(1,ncol,4))) {
  # vctr.sum = 0
  # vctr.num = 0
  # vctr.sqsum = 0
  # for(j in 0:3) {
  #   for(cell in ndata[[icol + j]]) {
  #     if(!is.na(cell[1])){
  #       vctr.num = vctr.num + length(cell)
  #       vctr.sum = vctr.sum + sum(unlist(cell))
  #       vctr.sqsum = vctr.sqsum + sum(unlist(lapply(cell,function(x) x**2)))
  #     }
  #   }
  # }
  # vctr.mu = vctr.sum/vctr.num
  # vctr.stdev = sqrt((vctr.sqsum/vctr.num - vctr.mu**2))
  normalisedVector = c(unlist(ndata[[i]]),unlist(ndata[[i+1]]),unlist(ndata[[i+2]]),unlist(ndata[[i+3]]))
  vctr.mu = sum(normalisedVector,na.rm = TRUE)
  vctr.stdev = sd(normalisedVector,na.rm = TRUE)
  for(j in 0:3) {
      ndata[[icol+j]] = normalize(ndata[[icol+j]],vctr.mu,vctr.stdev)
      ndata[[icol+j]] = normalize(ndata[[icol+j]],-6,0.1)
  }
}

thirdScores = list()
# find the max 15 scores here
## find the top X scores
for(i in (1:nentries)){
    rowScore = sort(unlist(sapply(ndata,function(x) x[[i]])),decreasing=TRUE)
    if(length(rowScore) < n) {
        thirdScores[[i]] = -Inf
    }
    else {
        thirdScores[[i]] = sum(rowScore[1:n])
    }
}


#find topRanks after filtering
thirdScores = sort(unlist(thirdScores),decreasing =TRUE,index.return=TRUE)
thirdScores$ranks = c(1)
#determine ranks
for(i in 2:length(thirdScores$x)) {
  if(thirdScores$x[i] < thirdScores$x[i-1])  {            # be either decreasing or equal
  thirdScores$ranks[i] = i
  }  
  else {
    thirdScores$ranks[i] = thirdScores$ranks[i-1]
  }
}

### Print here
for(i in 1:nentries) {
  if(thirdScores$x[i] > -Inf) {
      if(thirdScores$ranks[i] > X) {
        break
      }
      else{
        cat(thirdScores$ix[i],thirdScores$x[i],'\n')
      }
  }
  else
    break
}
cat('\n\n')

#### The Shapiro Wilk test on all columns
countNormalDist = 0
for(i in 1:ncol) {
  pval = shapiro.test(unlist(vdata[[i]]))$p.value
  if(pval > 0.02) {
      countNormalDist = countNormalDist + 1
  }
}

cat(1-countNormalDist/ncol,'fraction is NOT normally distributed.\n')

countNormalDist = 0
for(i in seq(1,ncol,4)) {
  v = unlist(vdata[[i]])
  for(j in 1:3) {
    v = c(v , unlist(vdata[[i+j]]) )
  }

  pval = shapiro.test(v)$p.value
  if(pval > 0.02) {
      countNormalDist = countNormalDist + 1
  }
}

cat(1-countNormalDist/(ncol/4),'fraction is NOT normally distributed. (in bins of 4)\n')


### we have the binned scores in thirdScores , thirdScores[[i]][[j]] = ith company, jth bin.

# inversion = function(score1,score2,rankList1,rankList2) {
#   rankInverse1 = rep(-Inf,length(score1))
#   rankInverse2 = rankInverse1
#   lastnewrank = 1
#   ninv = 0
#   for(i in 1:length(rankList1)) {
#       rankInverse1[rankList1[i]] = i;
#       if(score1[rankList1[i]]==score1[rankList1[lastnewrank]]) rankInverse1[rankList1] = lastnewrank;
#       else{
#         lastnewrank = i;
#         rankInverse1[rankList1[i]] = i;
#       }
#   }
#   for(i in 1:length(rankList2)){
#     for(j in 1:length(rankList1)){
      
#     }
#   }
# }

