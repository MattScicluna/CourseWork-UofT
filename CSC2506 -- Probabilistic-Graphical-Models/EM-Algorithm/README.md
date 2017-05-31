# EM-Algorithm
This was the third assignment I did in tin CSC2506 Class. We train a Multinomial mixture model on the [MovieLens-100k dataset](http://grouplens.org/datasets/movielens/). Then we derive and implement the EM algorithm to train the Multinomial Mixture model with a Dirichlet Prior.

## Skills Learned
I learned the following things:
* How to derive the equations for the E and M step of the EM Algorithm for a Multinomial mixture model
* How to derive a Dirichlet Prior for the model. 
* How to handle missing data. The EM algorithm included indicators to handle the missing ratings for each user (since users did not watch every movie).

## Software Used
This project was done in R. I used the following packages:
* [R.matlab](https://cran.r-project.org/web/packages/R.matlab/R.matlab.pdf) to read matlab files into R

## How to Use
* knit ```CSC2506_Assignment_3.Rmd``` in RStudio to use the assignment.
