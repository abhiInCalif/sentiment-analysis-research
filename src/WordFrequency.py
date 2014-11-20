import WordFrequencyModule
import nltk
from nltk.book import *


class WordFrequency(WordFrequencyModule):
    def __init__(self):
        print "Object created"
    
    def calculateWordFrequency(self, text):
        fdist1 = FreqDist(text)
        fdist1.plot(cumulative=True)
