#################################################################
#####helper function that computes responsibilities in E step####
#################################################################

compute_responsibility <- function(data.c, beta.c, theta.c)
{
  r = matrix(0,nrow=nrow(data.c),ncol=length(theta.c)) #initialize the responsibility matrix
  
  for (i in 1:5){
    rmat=data.c==i
    bet=log(beta.c[i,,])
    r<- r + rmat%*%bet
  }
  
  r <- r + matrix(rep(log(theta.c),nrow(data.c)), nrow=nrow(data.c)) 
  
  r <-  log( exp(r)+1e-32 )
  r <- exp(r - log(rowSums(exp(r))))

}

#######################################################################
#####helper function that computes Expected Complete Log Likelihood####
#######################################################################

compute_ll <- function(data.c, beta.c, theta.c, resp.c)
{
  llmat <- matrix(0, nrow=nrow(data.c),ncol=length(theta.c))
  
  for (i in 1:5){
    rmat=data.c==i
    bet=log(beta.c[i,,])
    llmat<- llmat + rmat%*%bet
  }  
  
  llmat = llmat + matrix(rep(log(theta.c),nrow(data.c)), nrow=nrow(data.c))    
  
  sum(rowSums(llmat*resp.c))
}

  
  
  
  
  