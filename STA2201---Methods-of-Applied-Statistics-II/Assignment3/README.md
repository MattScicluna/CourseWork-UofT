# STA2201 Third Assignment

STA2201 Coursework. In this assignment I did the following:
- I used a Generalized Additive Model from the Gamma family to estimate Carbon Dioxide concentrations. I found that, by 2020, the Carbon levels will exceed 400 parts per gallon with 95% confidence.
- I used Integrated Nested Laplacian Approximations (INLA) to Fit a beta distribution to a dataset of Math Scores. I found that differences in test scores where mostly a result of differences between students and not differences between schools.
- I used a Generalized Geostatistical model to estimate the effect of humans and the environment on the lead content of Moss in Galacia. I found that humans have a significant impact on Galacian lead concentration, while the enviornmental variables did not.
- I used INLA to analyse data from the American National Youth Tobacco Survey. Specifically, I modelled the time it took for children to start smoking using a Weibull distribution. I found that, relative to urban dwellers, children who lived in rural areas were about 35% more likely to begin smoking. I also found that there was more variability in the smoking rates between states then between schools.


## Skills Learned
I learned the following things:
* How to set up models with hyperparameters based on domain expertise.
* How do inference using INLA.
* How to interpret the fitted posterior distribution and model parameters.


## Software Used
* This project was done in R, the report was rendered into LaTeX using RMarkdown.
* I used the following R packages:
	- [INLA](https://www.math.ntnu.no/inla/r-inla.org/doc/inla-manual/inla-manual.pdf) 
	- [mgcv](https://cran.r-project.org/web/packages/mgcv/mgcv.pdf) for GAM models.
	- Reshape to melt data as needed.
	- [geostatsp](https://cran.r-project.org/web/packages/geostatsp/geostatsp.pdf)

## How to Use
* Knit ```STA2201_A3.Rmd``` to generate the report.
