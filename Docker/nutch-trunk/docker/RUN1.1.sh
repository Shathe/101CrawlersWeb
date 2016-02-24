cd crawler/
ls
bin/nutch inject micrawl/crawldb/ urls
 bin/nutch generate micrawl/crawldb/ micrawl/segments
s1=$(ls -d micrawl/segments/2* |tail -1)
bin/nutch fetch $(echo $s1)
bin/nutch parse $(echo $s1)
bin/nutch updatedb micrawl/crawldb/ $(echo $s1)
