#!/bin/sh

idImagen=$(docker images | head -2 | tail -1 | awk '{print $3;}')
#Este id es para pruebas, lo mejor seria pasar el id por parametro habiendolo guarado nada mas crearlo

while test $# -gt 0; do
        case "$1" in
                -h|--help)
                        echo 'usage' $0 '[--id idImage]'
                        echo " "
                        echo "options:"
                        echo "-i, --id                  specify an image id if not, it will stop the last one the system created"
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
                        *)
                        echo 'bad usage'
                        echo 'usage' $0 '[--id idImagen]'
                        echo " "
                        echo "options:"
                        echo "-i, --id                  specify an idImagen id if not, it will stop the last one the system created"
                        exit 0
                        ;;

        esac
done

docker run  -i -d $idImagen
