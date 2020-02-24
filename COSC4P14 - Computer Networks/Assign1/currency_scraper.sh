#!/bin/sh

echo "Grabbing data...\n"

curl https://www.kitco.com/charts/livegold.html > gold.html
curl https://www.bitcoinprice.com/eth/ > btc.html

# the BTC price page was not as clean to scrape, so I scraped the ETH page
# where it also states BTC price/change

GP=$(cat gold.html | grep "id=\"sp-bid\"" | cut -d'>' -f3 | cut -d'<' -f1)
GC=$(cat gold.html | grep "id=\"sp-chg-value\"" | cut -d'>' -f2 | cut -d'<' -f1)
BP=$(cat btc.html | grep "id=\"snp-price\"" | cut -d'>' -f3 | cut -d'<' -f1)
BC=$(cat btc.html | grep "id=\"snp-change\"" | cut -d'>' -f1 | cut -d'<' -f1)

rm gold.html btc.html

echo "\nGold Price: $GP (change = $GC)"
echo "BTC Price: $BP (change = $BC)\n"