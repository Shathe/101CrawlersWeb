Esta carpeta contiene el dockerfile y los scripts implementados para manejar de forma sencilla docker.

Guía de uso:

1- Tener instalado docker en el sistema (y en funcionamiento). Para más información https://docs.docker.com/linux/ https://docs.docker.com/mac/ https://docs.docker.com/windows/ según el sistema operativo que se tenga.

2- Ejecutar el script build.sh para crear la imagen de docker. Uso: sh build.sh nombre seed 
Donde el nombre es el nombre de la imagen que se le quiera dar y seed es la semilla para nutch. (En la siguiente versión se podrán introducir varias semillas y más configuraciones)

3- Ejecutar start.sh para arrancar la imagen en un contenedor. Su uso es sh start.sh (idImagen)
Es decir, se le puede pasar el ID de la imagen docker la cual se quiere arrancar, o si no se le pasa, se ejecturá con la última imagen creada en el sistema.

4- Ejecutar exec.sh para arrancar nutch. Su uso es sh exec.sh (idContenedor)
Es decir, se le puede pasar el ID del contenedor docker en el cual se quiere arrancar nuch, o si no se le pasa, se ejecturá en el último contenedor creado en el sistema.
