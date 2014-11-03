# This file scrapes the rotten tomatoes review api
# eventually this will scape the rotten tomatoes review api
# in a truly randomized and gaussian way.
import requests
import json
import re

from bs4 import BeautifulSoup

def scrape():
    # uses the api to scrape some reviews
    apikey ='qpbuunhuhmmp9xsapp3gjcp2'
    reviews_array = []
    review_ext_links = [] # external websites that actually have the reviews
    query_terms = ['action', 'comedy', 'family', 'animation', 'foreign', 'classics', 'documentary', 'drama', 'horror', 'mystery', 'romance', 'fantasy']
    accepted_publications = ['Washington Post', 'Seattle Times', 'San Francisco Chronicle', 'Boston Globe']
    forgoed_count = 0
    empty_count = 0

    print 'Grabbing all hte movies'
    for query_term in query_terms:
        for i in range(1,20):
            # get the first 15 pages of rotten tomatoes data set
            url = 'http://api.rottentomatoes.com/api/public/v1.0/movies.json'
            url = url + '?apikey=qpbuunhuhmmp9xsapp3gjcp2&q=' + str(query_term) + '&page=' + str(i)
            response = requests.get(url)
            json_rep = json.loads(response.text)
            movies = json_rep.get('movies')
            if movies is None:
                continue

            for movie in movies:
                # second option, lets do it all streamlined
                link = movie['links']['reviews']
                if link is None:
                    continue

                response = requests.get(str(link) + '?apikey=' + apikey)
                json_response = json.loads(response.text)

                reviews = json_response.get('reviews')
                if reviews is None:
                    # this is way to high, needs to be looked into and fixed
                    import pdb; pdb.set_trace()
                    print json_response
                    empty_count = empty_count + 1
                    continue
                
                for review in reviews:
                    review_publication = review['publication']

                    if review_publication not in accepted_publications:
                        forgoed_count = forgoed_count + 1
                        continue

                    ext_link = review['links'].get('review')
                    if ext_link is None:
                        continue

                    response = requests.get(ext_link)
                    soup = BeautifulSoup(response.text)
                    textList = soup.find_all('p') # find all the paragraphs

                    # write out each review out to a file in a directory
                    # this will help us build the proper corpus of rotten tomatoes
                    # documents
                    filename = soup.title.text.replace(' ', '_')
                    filename = re.sub('[^A-Za-z0-9]+', '_', filename)
                    f = open('/Users/abhinavkhanna/Documents/Princeton/Independent Work/rotten_tomato/rotten_tomato_crawler/' + filename + '.xml', 'w')
                    xmlOpen ='<?xml version="1.0" encoding="utf-8"?><?xml-model  href="../schema/digcor.rng" type="application/xml" schematypens="http://relaxng.org/ns/structure/1.0"?>'
                    f.write(xmlOpen)
                    f.write('<items>')
                    f.write('<item>')
                    f.write('<date></date><year></year><day></day>')
                    f.write('<solr-datetime></solr-datetime>')
                    f.write('<headline>' + soup.title.text.encode('utf-8') + '</headline>')
                    f.write('<source>Rotten Tomatoes</source>')
                    f.write('<body>')
                    for text in textList:
                        t = re.sub('[^A-Za-z0-9\s]+', '', text.text)
                        f.write(t.encode('utf-8'))
                    f.write('</body>')
                    f.write('</item>')
                    f.write('</items>')
                    f.close()


scrape()
