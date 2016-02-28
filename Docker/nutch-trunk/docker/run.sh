id=$(sudo docker ps -a | head -2 | tail -1 | awk '{print $1;}')
#Este id es para pruebas, lo mejor seria pasar el id por parametro habiendolo guarado nada mas crearlo

#bin/crawl <seedDir> <crawlDir> <solrURL> <numberOfRounds>
sudo docker exec  $id crawler/bin/nutch inject crawler/micrawl/crawldb/ urls
sudo docker exec $id crawler/bin/nutch generate crawler/micrawl/crawldb/ crawler/micrawl/segments
sudo docker exec  $id s1=$(ls -d crawler/micrawl/segments/2* |tail -1)
sudo docker exec  $id crawler/bin/nutch fetch $(echo $s1)
sudo docker exec  $id crawler/bin/nutch parse $(echo $s1)
sudo docker exec  $id crawler/bin/nutch updatedb crawler/micrawl/crawldb/ $(echo $s1)
