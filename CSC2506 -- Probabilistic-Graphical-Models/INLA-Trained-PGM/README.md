# INLA-Trained-PGM
Final project for CSC2506

This was the final project for CSC2506. I explored using Integrated Nested Laplacian Approximation (INLA) to model
handwritten digits. We trained Restricted Boltzmann Machines (RBMs) to clas-
sify these digits and compared the results of the classifier trained on the original
data and on the data modified using reconstructions from INLA. We then trained a
classifier on both the aforementioned training sets taken together as a larger train-
ing set. We finally explored using the reconstruction as a starting point for training
the weights of the RBM. We found that we could improve model performance by
training the model on the larger training set or by using the reconstruction as a
starting point- but not both.

## Skills Learned
I learned the following things:
* How to write a paper in [NIPS](https://nips.cc/) format.
* How to do perform research in Machine Learning.

## Software Used
* The report was written in RMarkdown and all computations were done in R.
* We used the following R packages:
- [INLA](http://www.r-inla.org/) to use INLA.
- [RandomFields](https://cran.r-project.org/web/packages/RandomFields/RandomFields.pdf) to train Gaussian MRFs.

## How to Use
* Run ProjBackup.Rmd to do the computations and generate the figures.
* Run FinalPaper.tex to generate report.


