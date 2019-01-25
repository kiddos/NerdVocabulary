# -*- coding: utf-8 -*-

import requests
import os
import re

from bs4 import BeautifulSoup


URL = 'https://gre.economist.com/gre-advice/gre-vocabulary/which-words-study/most-common-gre-vocabulary-list-organized-difficulty'
r = requests.get(URL)
soup = BeautifulSoup(r.text)


with open('word-set-01.txt', 'w') as f:
  for item in soup.select('.article-body p'):
    text = item.get_text(strip=True)
    word_pattern = re.compile(r'^(\w+): ')
    word = re.findall(r'^(\w+): ', text)
    if len(word) > 0:
      processed = text.replace(u'“', '"')
      word = word[0]
      word_type = re.findall(r': (\w+),', text)[0]
      definition = re.findall(r', (.+?)"', processed)[0]
      processed = text.replace(u'“', '<start-token>').replace(u'”', '<end-token>')
      sentence = re.findall(r'<start-token>(.+?)<end-token>', processed)
      definition = definition.split('Synonyms:')[0]
      print(word)
      print(word_type)
      print(definition)

      f.write(word + '\n')
      f.write(word_type + '\n')
      f.write(definition.encode('utf-8') + '\n')

      #  print(text)
      if len(sentence) > 0:
        index = sentence[0].lower().find(word.lower())
        sen = sentence[0]

        if index >= 0:
          part = sentence[0][index:index+len(word)]
          sen = sentence[0].replace(part, u' ' + part + u' ')
        print(sen)
        f.write(sen.encode('utf-8') + '\n')
      f.write('\n')
