#!/bin/sh

idContainer=$(docker ps -a | head -2 | tail -1 | awk '{print $1;}')
#Este id es para pruebas, lo mejor seria pasar el id por parametro habiendolo guarado nada mas crearlo


while test $# -gt 0; do
        case "$1" in
                -h|--help)
                        echo 'usage' $0 '[--id idContainer]'
                        echo " "
                        echo "options:"
                        echo "-i, --id                  specify an container id if not, it will stop the last one the system created"
                        exit 0
                        ;;
                -i|--id)
                        shift
                        if test $# -gt 0; then
                                idContainer=$1
                        else
                                echo "no id specified"
                                exit 1
                        fi
                        shift
                        ;;
              *)
                        echo 'bad usage'
                        echo 'usage' $0 '[--id idContainer]'
                        echo " "
                        echo "options:"
                        echo "-i, --id                  specify an container id if not, it will stop nutch in the last container the system created"
                        exit 0
                        ;;

        esac
done

docker exec  $idContainer kill -9 $(docker exec  $idContainer ps  | grep crawl  | awk '{print $1;}')
docker exec  $idContainer kill -9 $(docker exec  $idContainer ps  | grep java  | awk '{print $1;}')
echo  'Nutch stopped in' $idContainer
