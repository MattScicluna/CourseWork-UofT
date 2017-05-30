import urllib.request
from bs4 import BeautifulSoup
import csv
import time
import random
import re

#Initialize text file
text_file1 = open("debates1.txt", "w")
text_file2 = open("debates2.txt", "w")
text_file3 = open("debates3.txt", "w")
text_file4 = open("debates4.txt", "w")
text_file5 = open("debates5.txt", "w")

#Get debate 1 transript
debate1URL = "http://time.com/3988276/republican-debate-primetime-transcript-full-text/"
urllines = urllib.request.urlopen(debate1URL)
pagedat = urllines.read()
urllines.close()
soup = BeautifulSoup(pagedat)
words = soup.body.text
details = soup.find('p', text=re.compile('p')).parent
fulltext = details.get_text()

text_file1.write(fulltext)
text_file1.close()

#get debate 2 transcript
debate2URL ='http://time.com/4037239/second-republican-debate-transcript-cnn/'
urllines = urllib.request.urlopen(debate2URL)
pagedat = urllines.read()
urllines.close()
soup = BeautifulSoup(pagedat)
words = soup.body.text
details = soup.find('p', text=re.compile('p')).parent
fulltext = details.get_text()

text_file2.write(fulltext)
text_file2.close()

#get debate 3 transcript
debate3URL ='http://time.com/4091301/republican-debate-transcript-cnbc-boulder/'
urllines = urllib.request.urlopen(debate3URL)
pagedat = urllines.read()
urllines.close()
soup = BeautifulSoup(pagedat)
words = soup.body.text
details = soup.find('p', text=re.compile('p')).parent
fulltext = details.get_text()

text_file3.write(fulltext)
text_file3.close()

#get debate 4 transcript
debate4URL ='http://time.com/4107636/transcript-read-the-full-text-of-the-fourth-republican-debate-in-milwaukee/'
urllines = urllib.request.urlopen(debate4URL)
pagedat = urllines.read()
urllines.close()
soup = BeautifulSoup(pagedat)
words = soup.body.text
details = soup.find('p', text=re.compile('p')).parent
fulltext = details.get_text()

text_file4.write(fulltext)
text_file4.close()
