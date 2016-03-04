#!/bin/sh
#nombre aleatorio por defecto
nombre=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1)
seed=""
while test $# -gt 0; do
        case "$1" in
                -h|--help)
                        echo 'usage' $0 '[-n imageName -s numberOfSeeds (seed)*]'
                        echo " "
                        echo "options:"
                        echo "-n, --name                  sets the name of the image, if there's no name, it will be set as a random name"
                        echo "-s, --seed                  first specify the number of seed(s), followed by the seed(s) for the crawl"
                        exit 0
                        ;;
                -n|--name)
                        shift
                        if test $# -gt 0; then
                                name=$1
                        else
                                echo "no name specified"
                                exit 1
                        fi
                        shift
                        ;;

                -s|--seed)
                        shift
                        if test $# -gt 0; then
                                number=$1
                                i=0;
                                echo entra
                                shift
                                while test $number -gt $i ; do
                                  seed=$1'\n'$seed
                                  i=$(($i+1))
                                  echo $1
                                  shift
                                done

                        else
                                echo "no seeds specified"
                                exit 1
                        fi
                        ;;
                *)
                echo 'usage' $0 '[-n imageName -s numberOfSeeds (seed)*]'
                echo " "
                echo "options:"
                echo "-n, --name                  sets the name of the image, if there's no name, it will be set as a random name"
                echo "-s, --seed                  first specify the number of seed(s), followed by the seed(s) for the crawl"

                exit 0
                ;;
        esac
done
docker build -t $name  --build-arg $seed  .
