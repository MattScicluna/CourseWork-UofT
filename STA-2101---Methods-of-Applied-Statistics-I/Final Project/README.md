# STA2101 Final Project
The final project for STA2101 - an applied statistics course.

##Abstract
We collected transcripts from each of the four televised Republican debates and converted
the word frequencies of each of the candidates’ debate statements into a low dimensional
representation using LSA. We then built a classifier which attempted to distinguish
statements made by each of the leading presidential candidates to Jeb Bush. We compared
the accuracy of each the classifiers to see which candidate is easiest to distinguish from
Bush. We then build a single classifier using the previous classifiers which can determine,
given a statement, which candidate most likely said it. We illustrate this technique by
providing as a demonstration a function which allows user inputted documents to be
visualized in two dimensional latent space along with each of the candidates statements.

##Instructions
* Run ```pip install -r requirements.txt``` in terminal
* Run FinalProjectScript.py to scrape the first 5 Republican primary debate transcripts from the Time Magazine website.
* Knit FinalProjectMD.Rmd to get a PDF of the final project.
