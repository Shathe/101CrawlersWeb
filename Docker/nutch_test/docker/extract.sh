#!/bin/sh

idContainer=$(docker ps -a | head -2 | tail -1 | awk '{print $1;}')
#Este id es para pruebas, lo mejor seria pasar el id por parametro habiendolo guarado nada mas crearlo
path="nutchOutput.txt"
while test $# -gt 0; do
        case "$1" in
                -h|--help)
                        echo 'usage' $0 '[--id idContainer -p pathToCopy]'
                        echo " "
                        echo "options:"
                        echo "-i, --id                  specify an container id if not, it will stop the last one the system created"
                        echo "-p, --path                specify the path where Nutch's output will be copy"
                        exit 0
                        ;;
                -i|--id)
                        shift
                        if test $# -gt 0; then
                                idImagen=$1
                        else
                                echo "no id specified"
                                exit 1
                        fi
                        shift
                        ;;
                -p|path)
                        shift
                        if test $# -gt 0; then
                                path=$1
                        else
                                echo "no path specified"
                                exit 1
                        fi
                        shift
                        ;;
              *)
                        echo 'bad usage'
                        echo 'usage' $0 '[--id idContainer]'
                        echo " "
                        echo "options:"
                        echo "-i, --id                  specify an container id if not, it will stop the last one the system created"
                        echo "-p, --path                specify the path where Nutch's output will be copy"
                        exit 0
                        ;;

        esac
done

docker exec  $idContainer sh crawler/juntarSalidas.sh
docker cp $idContainer:root/crawler/salida/salida  $path
echo output copied to $path
