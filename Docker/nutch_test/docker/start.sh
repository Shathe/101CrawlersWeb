#Este id es para pruebas, lo mejor seria pasar el id por parametro habiendolo guarado nada mas crearlo

idImagen=$(sudo docker images | head -2 | tail -1 | awk '{print $3;}')
if test "$#" -ne 1; then
  sudo docker run  -i $idImagen
else
  sudo docker run  -i $1
fi
