# Collaborative-Filtering
This was the first Assignment I did from CSC2506. It reinforced concepts from Bayesian Statistics; specifically about implementing models
with prior and posterior distributions and learning hyperparameters.

## Skills Learned
I learned the following things:
* How to derive the distribution and mean and variance equations for Bayesian Regression. T
* How to derive the conjugate prior for a univariate Gaussian. 
* How to clean data and implement a Logistic Regression. 
* How to implement a Collaborative Filtering model. We trained the model on the famous [MovieLens-100k dataset](http://grouplens.org/datasets/movielens/).

## Software Used
This project was done in R. I used the following packages:
* [glmnet](https://cran.r-project.org/web/packages/glmnet/glmnet.pdf) package to get a l2 regularized logistic regression function.
* [Caret](https://cran.r-project.org/web/packages/caret/caret.pdf) package contained the _train_ function which we used to train the regularization hyperparameter (lambda) using 5 fold cross validation.

## How to Use
* knit hw1-998367342.Rmd in RStudio to use the assignment.
* The program will return a pdf and cache the computations into a seperate R file.
