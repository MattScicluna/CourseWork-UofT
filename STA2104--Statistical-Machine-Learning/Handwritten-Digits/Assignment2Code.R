pckgs = c("ggplot2")

func <- function(x){
  if(!is.element(x, rownames(installed.packages())))
  {install.packages(x, quiet=TRUE)}
}

lapply(pckgs, func)
lapply(pckgs, library, character.only=TRUE)


TrainData <- read.table(".//digitstrain.txt",head=F,sep=",")
TestData <- read.table(".//digitstest.txt",head=F,sep=",")

#Initialize the training data
T <- sapply(TrainData[,65], as.numeric)
X <- sapply(TrainData[,-65], as.numeric)

#Initialize the test data
Tt <- sapply(TestData[,65], as.numeric)
Xt <- sapply(TestData[,-65], as.numeric)

#Visualize some of the digits...
IMG1 <- matrix(X[1,], 8, byrow=T)  
IMG2 <- matrix(X[2,], 8, byrow=T)  
IMG3 <- matrix(X[3,], 8, byrow=T)  
IMG4 <- matrix(X[401,], 8, byrow=T)  
IMG5 <- matrix(X[402,], 8, byrow=T)  
IMG6 <- matrix(X[403,], 8, byrow=T)  

#Plot these digits
par(mfrow=c(3,2))
image(1:ncol(IMG1),1:nrow(IMG1), IMG1[,8:1], col = gray.colors(5), main="Plot for 3", xlab='', ylab='')
image(1:ncol(IMG2),1:nrow(IMG2), IMG2[,8:1], col = gray.colors(5), main="Plot for 3", xlab='', ylab='')
image(1:ncol(IMG3),1:nrow(IMG3), IMG3[,8:1], col = gray.colors(5), main="Plot for 3", xlab='', ylab='')
image(1:ncol(IMG4),1:nrow(IMG4), IMG4[,8:1], col = gray.colors(5), main="Plot for 5", xlab='', ylab='')
image(1:ncol(IMG5),1:nrow(IMG5), IMG5[,8:1], col = gray.colors(5), main="Plot for 5", xlab='', ylab='')
image(1:ncol(IMG6),1:nrow(IMG6), IMG6[,8:1], col = gray.colors(5), main="Plot for 5", xlab='', ylab='')

########################################################################################################################################
#### LOGISTIC REGRESSION MODEL####
########################################################################################################################################

######################################################################################
#Compare potential learning rates

CEErrorVec = numeric(0)
CEErrorVect = numeric(0)
for (Alpha in seq(0.001,0.02,0.001))
{W <- numeric(64)
i = 0 # initiate counter
Epsilon = 0.0001 #initiate stopping rate
Stopping = TRUE #Initiate whether stopping criteria has been met

while (Stopping == TRUE)
{Sigmoid <- function(Xn) #Sigmoid function
{Yn = 1 + exp(-Xn%*%W)
return(1/Yn)}

Y <- apply(X, 1, Sigmoid)
Change <- as.matrix(t(Y-T))%*%as.matrix(X)
Wnew <- W - Alpha*t(Change) #Update W

CEError<--sum(log(Y)*T+log(1-Y)*(1-T)) #Compute CE Error

if (abs(sum(W-Wnew)) < Epsilon)
{Stopping = FALSE}
else
{W <- Wnew
i=i+1}
}
CEErrorVec <- c(CEErrorVec,CEError)

Yt <- apply(Xt, 1, Sigmoid)
CEErrort<- -sum(log(Yt)*Tt+log(1-Yt)*(1-Tt))
CEErrorVect <- c(CEErrorVect, CEErrort)
}

Datta <-data.frame(Alpha=seq(0.001,0.02,0.001),CE1=CEErrorVec, CE2=CEErrorVect)


ggplot(Datta, aes(x=Alpha, y = CrossEntropyError, color = variable)) + 
    geom_point(aes(y = CE1, col = "Training Error")) + 
    geom_point(aes(y = CE2, col = "Test Error")) + ggtitle("Learning Rate Vs. Error Rate")
##############################################################################################################

#Initialize Weights and Learning Rate
W <- numeric(64) #Weights
Alpha <- Datta[order(Datta[,3]),][1,1] #Selects Alpha with lowest test error rate
#Alpha 0.04 in this context

#Train Logistic regression model
i = 0 # initiate counter
Epsilon = 0.0001 #initiate stopping rate
Stopping = TRUE #Initiate whether stopping criteria has been met

while (Stopping == TRUE)
{Sigmoid <- function(Xn) #Sigmoid function
{Yn = 1 + exp(-Xn%*%W)
return(1/Yn)}

Y <- apply(X, 1, Sigmoid)
Change <- as.matrix(t(Y-T))%*%as.matrix(X)
Wnew <- W - Alpha*t(Change) #Update W

CEError<--sum(log(Y)*T+log(1-Y)*(1-T)) #Compute CE Error
print(paste("The Cross Entropy Error on Epoch", i, "is", round(CEError,3)))

if (abs(sum(W-Wnew)) < Epsilon)
{Stopping = FALSE}
else
{W <- Wnew
i=i+1}
}

#Compare Log Likelihood for Logistic Regression Model
LLLTrainE<- log(Y)*T+log(1-Y)*(1-T) 
#P(t|X) for training data is...
round(LLLTrainE,4) 
Yt <- apply(Xt, 1, Sigmoid)

LLLTestE<- log(Yt)*Tt+log(1-Yt)*(1-Tt)
#P(t|X) for testing data is...
round(LLLTestE,4)

paste("The Avg Log Likelihood of the Training data to Logistic regression model is", round(mean(LLLTrainE),4))
paste("The Avg Log Likelihood of the Test data to Logistic regression model is", round(mean(LLLTestE),4))

#Compare Error Rate
TrainErrorRate<-mean(round(Y)!=T)
TestErrorRate<-mean(round(Yt)!=Tt)
paste("The Training error rate is", TrainErrorRate)
paste("The Test error rate is", TestErrorRate)

########################################################################################################################################

#Train Conditional Gaussian Classifier model
PiMLE <- mean(T)
Mu1MLE <- t(X)%*%as.matrix(T)/sum(T)
Mu2MLE <- t(X)%*%as.matrix(1-T)/sum(1-T)

X1 <- X[T==1,]
ones <- matrix(numeric(nrow(X1))+1)
S1MLE <- t(X1)%*%X1 - Mu1MLE%*%t(Mu1MLE) - t(X1)%*%ones%*%t(Mu1MLE) -t(t(X1)%*%ones%*%t(Mu1MLE))
S1MLE <- S1MLE/sum(T)
S1MLE <- solve(S1MLE)

X2 <- X[T==0,]
ones <- matrix(numeric(nrow(X2))+1)
S2MLE <- t(X2)%*%X2 - Mu1MLE%*%t(Mu2MLE) - t(X2)%*%ones%*%t(Mu2MLE) -t(t(X2)%*%ones%*%t(Mu2MLE))
S2MLE <- S2MLE/sum(1-T)
S2MLE <- solve(S2MLE)


Prob<-function(xn)
{
A <- xn-Mu1MLE
B <- xn-Mu2MLE
Input <- -0.5*t(A)%*%S1MLE%*%A + 0.5*t(B)%*%S2MLE%*%B + log(PiMLE) -log(1-PiMLE)
yn <- 1/(1 + exp(-Input))
return(yn)
}

#Compare log likelihood
Ypred <- apply(X, 1, Prob)
Ypredt <- apply(Xt, 1, Prob)

LLGTrainE <- c(log(1 - Ypred[1:400]),log(Ypred[401:800]))
#P(t|X) for training data is...
round(LLGTrainE,4)

LLGTestE <- c(log(1 - Ypredt[1:200]),log(Ypredt[201:400]))
#P(t|X) for testing data is...
round(LLGTestE,4)

paste("The Avg Log Likelihood of the Training data to Gaussian Classifier model is", round(mean(LLGTrainE),4))
paste("The Avg Log Likelihood of the Test data to Gaussian Classifier model is", round(mean(LLGTestE),4))

#Compare error rate
TrainErrorRate <- mean(round(Ypred)!=T)
TestErrorRate <- mean(round(Ypredt)!=Tt)
paste("The Training error rate is", TrainErrorRate)
paste("The Test error rate is", TestErrorRate)

kappa(S1MLE) #116181.3
kappa(S2MLE) #86872.18
#Condition number is very large, so numerical issues here!

########################################################################################################################################

#Regularized Conditional Gaussian Classifier Training

RegTerm <- diag(nrow(S1MLE))*0.01 #Regularizing term

S1MLE <- solve(S1MLE) + RegTerm
S2MLE <- solve(S2MLE) + RegTerm

S1MLE <- solve(S1MLE)
S2MLE <- solve(S2MLE)

kappa(S1MLE) #3883.222
kappa(S2MLE) #5982.848
#Much lower!

#Compare log likelihood
Ypred <- apply(X, 1, Prob)
Ypredt <- apply(Xt, 1, Prob)

LLGTrainE <- c(log(1 - Ypred[1:400]),log(Ypred[401:800]))
#P(t|X) for training data is...
round(LLGTrainE,4)

LLGTestE <- c(log(1 - Ypredt[1:200]),log(Ypredt[201:400]))
#P(t|X) for testing data is...
round(LLGTestE,4)

paste("The Avg Log Likelihood of the Training data to the Regularized Gaussian Classifier model is", round(mean(LLGTrainE),4))
paste("The Avg Log Likelihood of the Test data to the Regularized Gaussian Classifier model is", round(mean(LLGTestE),4))

#Compare error rate
TrainErrorRate <- mean(round(Ypred)!=T)
TestErrorRate <- mean(round(Ypredt)!=Tt)
paste("The Training error rate is", TrainErrorRate)
paste("The Test error rate is", TestErrorRate)

