sudo docker run -t -i $1 /bin/bash
cd crawler/
bin/crawl <seedDir> <crawlDir> <solrURL> <numberOfRounds>
