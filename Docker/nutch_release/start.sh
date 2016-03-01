#!/bin/sh

idImagen=$(docker images | head -2 | tail -1 | awk '{print $3;}')
#Este id es para pruebas, lo mejor seria pasar el id por parametro habiendolo guarado nada mas crearlo
if test "$#" -ne 1; then
  docker run  -i -d $idImagen
else
  docker run  -i -d $1
fi
