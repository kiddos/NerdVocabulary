# -*- coding: utf-8 -*-

import requests
import os
import re

from bs4 import BeautifulSoup


OUTPUT = 'gre01.txt'

URL = 'https://www.prepscholar.com/toefl/blog/toefl-vocabulary-list/'
r = requests.get(URL)
soup = BeautifulSoup(r.text, 'html')


with open('toefl01.txt', 'w') as f:
  for i, item in enumerate(soup.select('table tr')):
    if i == 0:
      continue
    entry = item.get_text().split('\n')
    word = entry[1]
    definition = entry[2]
    sentence = entry[3].replace(u'\xe2\x80\x99', '\'')
    f.write(u'%s\n' % (word))
    f.write(u'unknown\n')
    f.write(definition.encode('utf8'))
    f.write('\n')
    f.write(sentence.encode('utf8'))
    f.write('\n\n')
