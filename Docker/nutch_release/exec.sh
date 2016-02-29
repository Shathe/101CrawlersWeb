idContainer=$(sudo docker ps -a | head -2 | tail -1 | awk '{print $1;}')
#Este id es para pruebas, lo mejor seria pasar el id por parametro habiendolo guarado nada mas crearlo

#bin/crawl <seedDir> <crawlDir> <solrURL> <numberOfRounds>

#el crawl llevarlo a >>devnull y luego hacer un cat de dump aqui fuer para tenerlo en tu ordena

if test "$#" -ne 1; then
  sudo docker exec  $idContainer  sh crawler/run.sh
else
  sudo docker exec  $1  sh crawler/run.sh
fi
