pckgs = c("MASS")

func <- function(x){
  if(!is.element(x, rownames(installed.packages())))
  {install.packages(x, quiet=TRUE)}
}

lapply(pckgs, func)
lapply(pckgs, library, character.only=TRUE)



rm(list=ls())
#options(digits=5)


##############################
####Read the Training Data####
##############################


URL <- url("http://www.cs.toronto.edu/~rsalakhu/STA414_2015/train1x")
raw <- matrix(scan(URL),,8,byrow=TRUE)
TrainX <- data.frame(House = raw[,1],
                     Asian = raw[,2],
                     Age = raw[,3],
                     Persons = raw[,4],
                     Minority = raw[,5],
                     Sale = raw[,6],
                     Rooms = raw[,7],
                     Vacant6Mo = raw[,8])

URL <- url("http://www.cs.toronto.edu/~rsalakhu/STA414_2015/train1y")
raw <- matrix(scan(URL),,1,byrow=TRUE)
TrainY <- data.frame(Price = raw[,1])


##########################
####Read the Test Data####
##########################


URL <- url("http://www.cs.toronto.edu/~rsalakhu/STA414_2015/testx")
raw <- matrix(scan(URL),,8,byrow=TRUE)
TestX <- data.frame(House = raw[,1],
                     Asian = raw[,2],
                     Age = raw[,3],
                     Persons = raw[,4],
                     Minority = raw[,5],
                     Sale = raw[,6],
                     Rooms = raw[,7],
                     Vacant6Mo = raw[,8])

URL <- url("http://www.cs.toronto.edu/~rsalakhu/STA414_2015/testy")
raw <- matrix(scan(URL),,1,byrow=TRUE)
TestY <- data.frame(Price = raw[,1])


##############################
####Fitting a Linear Model####
##############################

ptm <- proc.time()
RegModel <- function(XR, YR) #Function computes Regression model and finds error for 2 dataframes
{
X <- as.matrix(XR)
Intercept <- numeric(nrow(XR))+1
X <- cbind(Intercept, X)
Y <- as.matrix(YR)
LSWeights <- solve(t(X)%*%X)%*%t(X)%*%Y #Computing OLS
Predictions <- X%*%LSWeights
SError <- round(mean((Predictions-Y)^2),4) #Computing MSE
print(paste("The Mean squared error of the model is", SError))
return(list(MSE=SError, Weights=LSWeights)) #Output is a list
}

LinModel <- RegModel(TrainX,TrainY) #make the linear model

#Check MSE on test cases
X <- as.matrix(TestX)
Intercept <- numeric(nrow(X))+1
X <- cbind(Intercept, X)
Predictions <- X%*%LinModel$Weights
Y <- as.matrix(TestY)
SError <- round(mean((Predictions-Y)^2),4) #Computing MSE
print(paste("The Mean squared error of the model on the testing data is", SError))

proc.time() - ptm #Ellapsed time for Regression

#So MSE of training data is 0.2802 and MSE of test data is 0.2876.


###############################################################
####Fitting a Gaussian Process Model With Linear Covariance####
###############################################################

ptm <- proc.time()

#Set Seed
set.seed(2015)

#Initialize Data
X <- as.matrix(TrainX)
Y <- as.matrix(TrainY)
XT <- as.matrix(TestX)
YT <- as.matrix(TestY)

#Initialize Hyperparameters
Alpha = 10000
Beta = 1

#Compute Kernel
Kernel<-function(x1, x2, Alpha)
{Alpha*t(x1)%*%x2}

#Compute Gram Matrix column
GramColumn <- function(j)
{apply(X,1,Kernel,X[j,],10000)}

#Compute C inverse
C <- sapply(as.matrix(seq(1,250)),GramColumn)
C <- C + Beta*diag(nrow(X))
CInv <- solve(C)

#Predict from XN
GPPred <- function(XN)
{K <- apply(X,1,Kernel,XN,10000)
c <- Kernel(XN,XN,10000) + Beta
Mu <- t(K)%*%CInv%*%Y
St.dev <- c - t(K)%*%CInv%*%K
#Can sample from normal using
#rnorm(1,Mu,St.dev)
return(Mu)}

Predtt <- apply(as.matrix(XT), 1, GPPred)

SError <- round(mean((Predtt-YT)^2),4) #Computing MSE
print(paste("The Mean squared error of the linear GP model on the testing data is", SError))
#Test Error is 0.4435

proc.time() - ptm #time elapsed

#Visualize the GP
PriorSample<-mvrnorm(3, rep(0,250), CInv)

plot(PriorSample[1,],type="l",col=2, xlab="Xi", ylab="F(Xi)", main="Sample from Prior of Gaussian Process")
par(new=T)
plot(PriorSample[2,],type="l",axes=F,col=3, xlab="", ylab="")
par(new=T)
plot(PriorSample[3,],type="l",axes=F,col=4, xlab="", ylab="")
par(new=F)


##################################################################
####Fitting a Gaussian Process Model Without Linear Covariance####
##################################################################

#Add hyperparameters
Theta <- 10000

GPPredictor<- function(Gamma,Rho,tX, tY, tXT, tYT)
{#Change Kernel
Kernel2<-function(x1, x2, Gamma, Rho)
{Theta + (Gamma^2)*exp(-(Rho^2)*sum((x1-x2)^2))}

#Compute Gram Matrix column
GramColumn2 <- function(j)
{apply(tX,1,Kernel2,tX[j,], Gamma, Rho)}

#Compute C inverse
C <- sapply(as.matrix(seq(1,nrow(tX))),GramColumn2)
C <- C + Beta*diag(nrow(tX))
CInv <- solve(C)

#Predict from XN
GPPred2 <- function(tXN)
{K <- apply( tX, 1, Kernel2, tXN, Gamma, Rho)
c <- Kernel2(tXN,tXN, Gamma, Rho) + Beta
Mu <- t(K)%*%CInv%*%tY
St.dev <- c - t(K)%*%CInv%*%K
#Can sample from normal using
#rnorm(1,Mu,St.dev)
return(Mu)}

Predss <- apply(as.matrix(tXT), 1, GPPred2)
return(Predss)}

GPFit2 <- function(Gamma,Rho,tX, tY, tXT, tYT)
{Predictions2 <- GPPredictor(Gamma,Rho,tX, tY, tXT, tYT)
SError <- round(mean((Predictions2-tYT)^2),4) #Computing MSE
#print(paste("The Mean squared error of the GP model on the testing data is", SError))
return(list(Gamma=Gamma,Rho=Rho,Error=SError))
}

#Minimize parameters using grid search.

GridSearch <- function( ttX, ttY, ttXT, ttYT)
{
Errors <- matrix(,,3)
colnames(Errors)<-c("Gamma","Rho","Error")
Errors <- Errors[-1,]

for (Gamma in seq(0.1,10,0.5))
{for (Rho in seq(0.01,1,0.05))
{Fit <- GPFit2(Gamma,Rho, ttX, ttY, ttXT, ttYT)
Errors <- rbind(c(Fit$Gamma,Fit$Rho,Fit$Error),Errors)
}}
#Get Optimal Error
MinParams <- Errors[Errors[,3]==min(Errors[,3]),]
return(list(Gamma=MinParams[1],Rho=MinParams[2], Table=Errors))}

#Split Data
T1<- 1:50; V1<-setdiff(1:250,T1)
T2<- 51:100; V2<-setdiff(1:250,T2)
T3<- 101:150; V3<-setdiff(1:250,T3)
T4<- 151:200; V4<-setdiff(1:250,T4)
T5<- 201:250; V5<-setdiff(1:250,T5)
TrainingSets <- list(X[T1,],X[T2,],X[T3,],X[T4,],X[T5,])
TrainingTargets <- list(Y[T1,],Y[T2,],Y[T3,],Y[T4,],Y[T5,])
ValidSets <- list(X[V1,],X[V2,],X[V3,],X[V4,],X[V5,])
ValidTargets <- list(Y[V1,],Y[V2,],Y[V3,],Y[V4,],Y[V5,])

#Compute Optimal params for each training set
ParamList<-list()
for (k in 1:5)
{ParamList[[k]]<-GridSearch(TrainingSets[[k]],TrainingTargets[[k]],ValidSets[[k]],ValidTargets[[k]])}

#Train Model using average of 5 predictors on Test data
ptm <- proc.time()


Predictions3<-matrix(,2500,5)
for (k in 1:5)
{
Gamma <- ParamList[[k]]$Gamma
Rho <- ParamList[[k]]$Rho
Predictions3[,k] <- GPPredictor(Gamma, Rho, X, Y, XT, YT)
}

ModelPredictions <- rowMeans(Predictions3)
proc.time() - ptm #time elapsed

Kernel2<-function(x1, x2, Gamma, Rho)
{Theta + (Gamma^2)*exp(-(Rho^2)*sum((x1-x2)^2))}

#Compute Gram Matrix column
GramColumn2 <- function(j)
{apply(tX,1,Kernel2,tX[j,], Gamma, Rho)}

ParamTable<-matrix(,5,2)
for (k in 1:5)
{ParamTable[k,] <- cbind(ParamList[[k]]$Gamma,ParamList[[k]]$Rho)
Gamma<-ParamList[[k]]$Gamma}

require("lattice")
Z1 <- matrix(ParamList[[1]]$Table[,3],20,20)
colnames(Z1)<-seq(0.01,1,0.05)
rownames(Z1)<-seq(0.1,10,0.5)
Z2 <- matrix(ParamList[[2]]$Table[,3],20,20)
colnames(Z2)<-seq(0.01,1,0.05)
rownames(Z2)<-seq(0.1,10,0.5)
Z3 <- matrix(ParamList[[3]]$Table[,3],20,20)
colnames(Z3)<-seq(0.01,1,0.05)
rownames(Z3)<-seq(0.1,10,0.5)
Z4 <- matrix(ParamList[[4]]$Table[,3],20,20)
colnames(Z4)<-seq(0.01,1,0.05)
rownames(Z4)<-seq(0.1,10,0.5)
levelplot(Z1, xlab="Gamma", ylab="Rho", main="Hyperparameter Error Values", col.regions = rainbow(100, start=0.5, end=0.66))
levelplot(Z2, xlab="Gamma", ylab="Rho", main="Hyperparameter Error Values", col.regions = rainbow(100, start=0.5, end=0.66))
levelplot(Z3, xlab="Gamma", ylab="Rho", main="Hyperparameter Error Values", col.regions = rainbow(100, start=0.5, end=0.66))
levelplot(Z4, xlab="Gamma", ylab="Rho", main="Hyperparameter Error Values", col.regions = rainbow(100, start=0.5, end=0.66))

#Compute MSE
SError <- round(mean((ModelPredictions-YT)^2),4) #Computing MSE
print(paste("The Mean squared error of the GP model on the testing data is", SError))

#############################################################
####Rescaling the covariates and repeating the experiment####
#############################################################

X[,1] <- X[,1]/10 ; X[,7] <- X[,7]/10
XT[,1] <- XT[,1]/10 ; XT[,7] <- XT[,7]/10

#Compute Optimal params for each training set
TrainingSets <- list(X[T1,],X[T2,],X[T3,],X[T4,],X[T5,])
TrainingTargets <- list(Y[T1,],Y[T2,],Y[T3,],Y[T4,],Y[T5,])
ValidSets <- list(X[V1,],X[V2,],X[V3,],X[V4,],X[V5,])
ValidTargets <- list(Y[V1,],Y[V2,],Y[V3,],Y[V4,],Y[V5,])

#Compute Optimal params for each training set
ParamList<-list()
for (k in 1:5)
{ParamList[[k]]<-GridSearch(TrainingSets[[k]],TrainingTargets[[k]],ValidSets[[k]],ValidTargets[[k]])}

#Train Model using average of 5 predictors on Test data

ptm <- proc.time()
Predictions3<-matrix(,2500,5)
for (k in 1:5)
{
Gamma <- ParamList[[k]]$Gamma
Rho <- ParamList[[k]]$Rho
Predictions3[,k] <- GPPredictor(Gamma, Rho, X, Y, XT, YT)
}

ModelPredictions <- rowMeans(Predictions3)
proc.time() - ptm #time elapsed

ParamTable2<-matrix(,5,2)
for (k in 1:5)
{ParamTable2[k,] <- cbind(ParamList[[k]]$Gamma,ParamList[[k]]$Rho)}

#Visualize the GP
VisFun<-function(Gamma,Rho)
{Kernel2<-function(x1, x2, Gamma, Rho)
{Theta + (Gamma^2)*exp(-(Rho^2)*sum((x1-x2)^2))}

#Compute Gram Matrix column
GramColumn2 <- function(j)
{apply(X,1,Kernel2,X[j,], Gamma, Rho)}

#Compute C inverse
C <- sapply(as.matrix(seq(1,nrow(X))),GramColumn2)
C <- C + Beta*diag(nrow(X))
CInv <- solve(C)

PriorSample<-mvrnorm(3, rep(0,250), C)
plot(PriorSample[1,],type="l",col=2, xlab="Xi", ylab="F(Xi)", main="Sample from Prior of GP")
par(new=T)
plot(PriorSample[2,],type="l",axes=F,col=3, xlab="", ylab="")
par(new=T)
plot(PriorSample[3,],type="l",axes=F,col=4, xlab="", ylab="")
par(new=F)}

par(mfrow=c(2,2))
VisFun(ParamTable[1,1],ParamTable[1,2])
par(new=F)
VisFun(ParamTable[2,1],ParamTable[2,2])
par(new=F)
VisFun(ParamTable[3,1],ParamTable[3,2])
par(new=F)
VisFun(ParamTable[4,1],ParamTable[4,2])
par(new=FALSE)

par(mfrow=c(2,2))
VisFun(ParamTable2[1,1],ParamTable2[1,2])
par(new=F)
VisFun(ParamTable2[2,1],ParamTable2[2,2])
par(new=F)
VisFun(ParamTable2[3,1],ParamTable2[3,2])
par(new=F)
VisFun(ParamTable2[4,1],ParamTable2[4,2])
par(new=FALSE)

par(mfrow=c(2,1))
VisFun(ParamTable[1,1],ParamTable[1,2])
par(new=F)
VisFun(ParamTable2[4,1],ParamTable2[4,2])
par(new=F)

#Compute MSE
SError <- round(mean((ModelPredictions-YT)^2),4) #Computing MSE
print(paste("The Mean squared error of the GP model on the testing data is", SError))