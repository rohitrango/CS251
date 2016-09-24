rawdata = (read.csv("mutualFundPerformance.csv",header=FALSE))
ncol = length(rawdata)
nentries = length(rawdata[[1]])
lmax = 1
X = 15
options(warn=-1)
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

# Determine ranks 
determineRanks = function(values) {
    values$ranks = c(1)
    for(i in 2:length(values$x)) {
      if(values$x[i] < values$x[i-1])  {            # be either decreasing or equal
        values$ranks[i] = i
      }  
      else {
        values$ranks[i] = values$ranks[i-1]
      }
    }
    values      ## Supposed to return the value
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

printTopRanks = function(values) {
    for(i in 1:nentries) {
      if(values$x[i] > -Inf) {
          if(values$ranks[i] > X) {
            break
          }
          else{
            cat(values$ix[i],values$x[i],'\n')
          }
      }
      else
        break
    }
    cat('\n')
}

# Inversions
inversions = function(left,right) {
  nl = sum(sapply(left$x,function(t) t>-Inf))
  nr = sum(sapply(right$x,function(t) t>-Inf))
  # rank of left$x[[i]]=left$rank[[i]], index of left$x[[i]]=left$ix[[i]]
  if(nl < nr) return(inversions(right,left))
  truerankl = list()
  for(i in 1:nl){
    truerankl[[left$ix[[i]]]] = left$rank[[i]]
  }
  ninv = 0
  for(i in 1:nr){
    for(j in i+1:nl){
      if(j>nl) break
      if((right$rank[[i]]>right$rank[[j]] && truerankl[[right$ix[[i]]]]< truerankl[[right$ix[[j]]]]) || (right$rank[[i]]<right$rank[[j]] && truerankl[[right$ix[[i]]]]> truerankl[[right$ix[[j]]]])) ninv = ninv+1
    }
  }
  return(ninv)
}

vdata = udata = lapply((1:length(rawdata)),function(x) colproc(rawdata[[x]]))
# vdata is the original data
# Normalization, x can be list, array or anything else
normalize = function(x,mu,stdev){
  if(class(x)=="logical") return(NA)
  if(class(x)=="list") return(lapply(x,function(x) normalize(x,mu,stdev)))
  return ((x-mu)/stdev)
}

# length of x of any list
mylength = function(x){
  if(class(x[[1]])=="logical"){return(0)}
  else{return(length(x))}
}

## standarrdize columns individually from given raw data
standardizeColumns = function(mydata) {
  for(icol in (1:ncol)) {
    v = unlist(mydata[[icol]])
    vctr.mu = mean(v , na.rm = T)
    vctr.stdev = sd(v,na.rm = T)

    mydata[[icol]] = normalize(mydata[[icol]],vctr.mu,vctr.stdev)
    mydata[[icol]] = normalize(mydata[[icol]],-6,0.1)
  }
  mydata
}


## standardize bins from original data
standardizeBins = function(mydata) {

    for(icol in (seq(1,ncol,4))) {
      normalisedVector = c(unlist(mydata[[icol]]),unlist(mydata[[icol+1]]),unlist(mydata[[icol+2]]),unlist(mydata[[icol+3]]))
      vctr.mu = mean(normalisedVector,na.rm = TRUE)
      vctr.stdev = sd(normalisedVector,na.rm = TRUE)
      for(j in 0:3) {
          mydata[[icol+j]] = normalize(mydata[[icol+j]],vctr.mu,vctr.stdev)
          mydata[[icol+j]] = normalize(mydata[[icol+j]],-6,0.1)
      }
    }

    mydata
}

## Find average company score from norm/stand mydata
avgCompanyScore = function(values,mydata) { 
    for(i in (1:nentries)){
      values[[i]] = sum(sapply(mydata,function(x) sum(unlist(x[[i]]),na.rm=TRUE)),na.rm=TRUE)   ## extract rows

      entry.num  = sum(unlist(sapply(mydata,function(x) {       ## sum of values 
        if(is.na(x[[i]][1])) {
          0
        }
        else {
          length(x[[i]])
        }
      })),na.rm=TRUE)

      values[[i]] = values[[i]] / entry.num           ## Average
      if(entry.num == 0)
        values[[i]] = NA
    }
    values
} 

#### Top n companies
topNScores = function(values,mydata,Nval) {
    for(i in 1:nentries) {
      rowScore = sort(unlist(sapply(mydata,function(x) x[[i]])),decreasing=TRUE)      # extract rows
      if(length(rowScore) < Nval) {                                                   # if length < 15 or 10, give it sum of -Inf
          values[[i]] = -Inf
      }
      else {
          values[[i]] = sum(rowScore[1:Nval])
      }
    }
    values
}

countnum = function(x){
  return(sum(unlist(lapply(x,is.numeric))))
}

###################################################################################################
# Ranking 1
# Give the non-NA entries for the list
udata = standardizeColumns(udata)

X=15
firstScores = list()
firstScores = avgCompanyScore(firstScores,udata)

firstScores = sort(unlist(firstScores),decreasing =TRUE,index.return=TRUE)
firstScores = determineRanks(firstScores)
cat('Company indices and Scores for Task D4 (Top 15 ranks listed)','\n')
printTopRanks(firstScores)


# Ranking2
# Give best-n normalised scores
udata = normalize(udata,60,10)  # normalize the scores from standarised data
X=15
secondScores = list()           # list to insert -Inf or total score of 1:n
secondScoresX_10 = list()
## find the top X scores
secondScores = topNScores(secondScores,udata,15)
secondScoresX_10 = topNScores(secondScoresX_10,udata,10)

secondScores = sort(unlist(secondScores),decreasing =TRUE,index.return=TRUE)
secondScoresX_10 = sort(unlist(secondScoresX_10),decreasing =TRUE,index.return=TRUE)

secondScores$ranks = c(1)
secondScoresX_10$ranks = c(1)
# determine ranks
# For both X = 15 and X = 10
secondScores = determineRanks(secondScores)
secondScoresX_10 = determineRanks(secondScoresX_10)
## print second Scores
cat('Top 15 Company indices and Scores (TaskD5) (n=15)','\n')
printTopRanks(secondScores)
cat('Top 15 Company indices and Scores (TaskD5) (n=10)','\n')
printTopRanks(secondScoresX_10)

inv12_X15 = inversions(firstScores,secondScores)
inv12_X10 = inversions(firstScores,secondScoresX_10)

cat('No. of inversions of ratings of TaskD4 and TaskD5(n=15):', inv12_X15,'\n')
cat('No. of inversions of ratings of TaskD4 and TaskD5(n=10):', inv12_X10,'\n')

## ndata is used for the 4 bin system
## Third part, put into bins!
## Already normalized data
## thirdScores[[i]][[j]] = ith company, jth bin
ndata = vdata  
ndata = standardizeBins(ndata)
# use original data to standardize
## Normalise columns '4' bins at a time

thirdScores = list()
thirdScoresX_10 = list()

## find the top X scores
thirdScores = topNScores(thirdScores,ndata,15)
thirdScoresX_10 = topNScores(thirdScoresX_10,ndata,10)

#find topRanks after filtering
thirdScores = sort(unlist(thirdScores),decreasing =TRUE,index.return=TRUE)
thirdScoresX_10 = sort(unlist(thirdScoresX_10),decreasing =TRUE,index.return=TRUE)

#determine ranks
thirdScores = determineRanks(thirdScores)
thirdScoresX_10 = determineRanks(thirdScoresX_10)
# print Rank3 
cat('Top 15 Company indices and Scores (TaskD6) (n=15)','\n')
printTopRanks(thirdScores)
cat('Top 15 Company indices and Scores (TaskD6) (n=10)','\n')
printTopRanks(thirdScoresX_10)

inv23_X15 = inversions(secondScores,thirdScores)
inv23_X10 = inversions(secondScoresX_10,thirdScoresX_10)

cat('No. of inversions of ratings of TaskD5 and TaskD6(n=15):', inv23_X15,'\n')
cat('No. of inversions of ratings of TaskD5 and TaskD6(n=10):', inv23_X10,'\n')

#### The Shapiro Wilk test on all columns
lambda1 = rep(NA,ncol)        ## optimised values of lambda1
lambda2 = rep(NA,(ncol/4))    ## optimised values of lambda2
# Store the values which are not Saphiro Test - positive, later replace them by corresponding lambda


countNormalDist = 0
for(i in 1:ncol) {
  pval = shapiro.test(unlist(vdata[[i]]))$p.value
  if(pval > 0.02) {
      countNormalDist = countNormalDist + 1
  }
  else 
    lambda1[i] = pval
}
nGaussian1 = 1 - (countNormalDist/ncol) 
cat(nGaussian1,'fraction is NOT normally distributed.\n')

#### The Shapiro Wilk test for four columns at once
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
  else 
    lambda2[(i+3)/4] = pval
}
nGaussian2 = 1-countNormalDist/(ncol/4)
cat(nGaussian2,'fraction is NOT normally distributed. (in bins of 4)\n')


####################################################################
## Extra Credit
####################################################################
pvalues1 = c()   # store max p-values 
pvalues2 = c()   # store max p-values
## Returns the p-value for given lambda transformation and index of the column
## One column to be evaluated
transformFn1 = function(lambda,index) {
  x = unlist(vdata[[index]])        # take the index of the non-Gaussian column, extract column, return the Shapiro value
  if(lambda == 0) {
    x = log(x)
  }
  else {
    x = ((x^lambda) - 1)/lambda
  }
  shapiro.test(x)$p.value
}

## Returns the p-value for given lambda transformation and index of the column
## Four columns to be evaluated
transformFn2 = function(lambda,index) {
  # take the index, and combine the 4 columns
  index = 4*(index-1) + 1
  x = c(unlist(vdata[[index]]),unlist(vdata[[index+1]]),unlist(vdata[[index+2]]),unlist(vdata[[index+3]]))
  if(lambda==0)
    x = log(x)
  else
    x = ((x^lambda) - 1)/lambda 

  shapiro.test(x)$p.value
}

ndata = vdata                 ## take second copy (single col)
udata = vdata                 ## take a copy of the data again (single bin)
# optimise the values of lambda for each column first 
# use ndata for this one
# non-NA values are not fit, try to optimize them, and if they dont, leave them as NA
for(i in 1:ncol) {
  if(!is.na(lambda1[i])) {    # this column has p < 0.02, optimise it
      optimiser = optimise(f = transformFn1, interval = c(-10,10), lower=-10, upper=10,index = i, maximum = TRUE)
      pvalues1[i] = optimiser$objective     # store the best p-value

      if(optimiser$objective > 0.02) {      # optimised, store the lambda value
          lambda1[i] = optimiser$maximum    # store in lambda, we are going to change this column
      }  
      else {
        lambda1[i] = NA                     # no optimizations, leave it as it is
      }
  }
  else 
    pvalues1[i] = NA           # if the value is already Gaussian, no p-value
}

# optimise the values of lambda for each bin 
# use udata for this one
# non-NA values are not fit, try to optimize them, and if they dont, leave them as NA
for(i in 1:(ncol/4)) {
    if(!is.na(lambda2[i])) {
        optimiser = optimise(f = transformFn2, interval = c(-10,10),lower=-10,upper=10, index = i, maximum = TRUE)
        pvalues2[i] = optimiser$objective

        if(optimiser$objective > 0.02) {      # optimised, store the lambda value
          lambda2[i] = optimiser$maximum
        }  
        else {
         lambda2[i] = NA 
        }
    }
    else
      pvalues2[i] = NA
}

transformFnX = function(x,lambda) {
  if(lambda == 0)
    log(x)
  else
    ((x^lambda)-1)/lambda
}
## at this point we have the lambda arrays to transform the array
# ndata -> single col
for(i in 1:ncol) {
  if(!is.na(lambda1[i])) {
      ndata[[i]] = lapply(ndata[[i]],function(x) {
        transformFnX(x,lambda1[i])
      })
  }
}

## udata -> single bin
for(i in 1:(ncol/4)) {
  if(!is.na(lambda2[i])) {
      tmpIndex = 4*(i-1) + 1
      for(j in 0:3) {
        udata[[tmpIndex+j]] = lapply(udata[[tmpIndex+j]],function(x) {
          transformFnX(x,lambda2[i])
        })
      }
  }
}

ndata = standardizeColumns(ndata)
udata = standardizeBins(udata)
## use ndata for column values
## We have the new normalized values, find out the findings
newFirstScores = list()
newFirstScores = avgCompanyScore(newFirstScores,ndata)

newFirstScores = sort(unlist(newFirstScores),decreasing =TRUE,index.return=TRUE)
newFirstScores = determineRanks(newFirstScores)
cat('Top 15 Company indices and Scores (TaskD4) (after Shapiro test)','\n')
printTopRanks(newFirstScores)

## Do the same thing for 2nd task -> taking n = 10, 15
newSecondScores = list()           # list to insert -Inf or total score of 1:n
newSecondScoresX_10 = list()
## find the top X scores
newSecondScores = topNScores(newSecondScores,ndata,15)
newSecondScoresX_10 = topNScores(newSecondScoresX_10,ndata,10)

newSecondScores = sort(unlist(newSecondScores),decreasing =TRUE,index.return=TRUE)
newSecondScoresX_10 = sort(unlist(newSecondScoresX_10),decreasing =TRUE,index.return=TRUE)

newSecondScores$ranks = c(1)
newSecondScoresX_10$ranks = c(1)
# determine ranks
# For both X = 15 and X = 10
newSecondScores = determineRanks(newSecondScores)
newSecondScoresX_10 = determineRanks(newSecondScoresX_10)
## print second Scores
cat('Top 15 Company indices and Scores (TaskD5)(n=15) (after Shapiro test)','\n')
printTopRanks(newSecondScores)
cat('Top 15 Company indices and Scores (TaskD5)(n=10) (after Shapiro test)','\n')
printTopRanks(newSecondScoresX_10)


##### Inversion values
newinv12_X15 = inversions(newFirstScores,newSecondScores)
newinv12_X10 = inversions(newFirstScores,newSecondScoresX_10)
cat('No. of inversions of ratings of TaskD4 and TaskD5(n=15) (after Shapiro test):',newinv12_X15,'\n')
cat('No. of inversions of ratings of TaskD4 and TaskD5(n=10) (after Shapiro test):',newinv12_X10,'\n')

newthirdScores = list()
newthirdScoresX_10 = list()

## find the top X scores
newthirdScores = topNScores(newthirdScores,udata,15)
newthirdScoresX_10 = topNScores(newthirdScoresX_10,udata,10)

#find topRanks after filtering
newthirdScores = sort(unlist(newthirdScores),decreasing =TRUE,index.return=TRUE)
newthirdScoresX_10 = sort(unlist(newthirdScoresX_10),decreasing =TRUE,index.return=TRUE)

#determine ranks
newthirdScores = determineRanks(newthirdScores)
newthirdScoresX_10 = determineRanks(newthirdScoresX_10)
# print Rank3 
cat('Top 15 Company indices and Scores (TaskD6)(n=15) (after Shapiro test)','\n')
printTopRanks(newthirdScores)
cat('Top 15 Company indices and Scores (TaskD6)(n=10) (after Shapiro test)','\n')
printTopRanks(newthirdScoresX_10)

newinv23_X15 = inversions(newSecondScores,newthirdScores)
newinv23_X10 = inversions(newSecondScoresX_10,newthirdScoresX_10)

cat('No. of inversions of ratings of TaskD5 and TaskD6(n=15) (after Shapiro test):',newinv23_X15,'\n')
cat('No. of inversions of ratings of TaskD5 and TaskD6(n=15) (after Shapiro test):',newinv23_X10,'\n')
options(warn=0)

mat = c(firstScores$ix[1:15],secondScores$ix[1:15],secondScoresX_10$ix[1:15],thirdScores$ix[1:15],thirdScoresX_10$ix[1:15])
mat = matrix(mat,ncol=5)
write(t(mat),file = 'rankings.txt')
closeAllConnections()