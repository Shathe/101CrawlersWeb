#sudo docker run  $1 /bin/bash
sudo docker run -i $1
sudo docker exec ...

id=$(sudo docker ps -a | head -2 | tail -1 | awk '{print $1;}')
sudo  docker start $id
sudo docker exec -d $id cd crawler/
sudo docker exec -d $id bin/nutch inject micrawl/crawldb/ urls

  sudo docker exec -d $id bin/nutch generate micrawl/crawldb/ micrawl/segments
  sudo docker exec -d $id s1=$(ls -d micrawl/segments/2* |tail -1)
  sudo docker exec -d $id bin/nutch fetch $(echo $s1)
  sudo docker exec -d $id bin/nutch parse $(echo $s1)
  sudo docker exec -d $id bin/nutch updatedb micrawl/crawldb/ $(echo $s1)
