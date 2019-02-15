#!/usr/bin/env sh

python fetch_gre_01.py
python fetch_toefl_01.py

mv gre01.txt app/src/main/assets
mv toefl01.txt app/src/main/assets
