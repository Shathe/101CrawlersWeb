#!/bin/sh

idContainer=$(docker ps -a | head -2 | tail -1 | awk '{print $1;}')
#Este id es para pruebas, lo mejor seria pasar el id por parametro habiendolo guarado nada mas crearlo
tiempo=1;

while test $# -gt 0; do
        case "$1" in
                -h|--help)
                        echo 'usage' $0 '[-t seconds --id idContainer]'
                        echo " "
                        echo "options:"
                        echo "-t, --time                sets the time to wait before stopping the crawl"
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

                -t|--time)
                        shift
                        if test $# -gt 0; then
                                tiempo=$1
                        else
                                echo "no time specified"
                                exit 1
                        fi
                        shift
                        ;;
                *)
                        echo 'bad usage'
                        echo 'usage' $0 '[-t seconds --id idContainer]'
                        echo " "
                        echo "options:"
                        echo "-t, --time                sets the time to wait before stopping the crawl"
                        echo "-i, --id                  specify an container id if not, it will stop the last one the system created"
                        exit 0
                        ;;
        esac
done

docker stop -t $tiempo $idContainer
echo $idContainer 'stopped'
