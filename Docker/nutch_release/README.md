Esta carpeta contiene el dockerfile y los scripts implementados para manejar de forma sencilla docker.

## Guía de uso

1. Tener instalado docker en el sistema (y en funcionamiento). Para más información [instalación completa y configurando] (https://docs.docker.com/engine/installation/) o instalación rápida en [linux] (https://docs.docker.com/linux/) [Mac] (https://docs.docker.com/mac/) [Windows] (https://docs.docker.com/windows/).  Se recomienda la instalación completa, puesto que indica como configurar, por ejemplo en linux docker para no tener que dar permisos de root (Si no, estos scripts, seguramente no funcionarán).
   Puede que aparezca este mensaje:
   ```
   WARNING: Error loading config file:/home/user/.docker/config.json - stat /home/user/.docker/config.json: permission denied
   ```

   Pero no da problemas pues no es necesario (se puede crear un fchero con un Json vacío {}  para que no e aparezca).
   El mensaje que no te tiene que aparecer es `'Cannot connect to the Docker daemon. Is 'docker daemon' running on this host?'` lo cual significará que se ha configurado mal docker.


1. Ejecutar el script build.sh para crear la imagen de docker (En la siguiente versión se podrán introducir más configuraciones).

   Ayuda:

   usage build.sh [ -n imageName -s numberOfSeeds (seed)* ]

    options:

    -n, --name                  sets the name of the image, if there's no name, it will be set as a random name

    -s, --seed                  first specify the number of seed(s), followed by the seed(s) for the crawl

   Ejemplo ejecucion:
   ```
   $> sh build.sh -n contendor1 -s 2 http://www.google.es http://www.yahoo.es
   ```
   Salida deseada:
   ```
   Sending build context to Docker daemon 9.216 kB
   Step 1 : FROM ubuntu:14.04
  ---> 14b59d36bae0
   Step 2 : MAINTAINER inigo alonso <inigol22zgz@gmail.com>
   ..
   ..
   ..
   Step 17 : ENV JAVA_HOME /usr/lib/jvm/java-7-oracle
   ---> Using cache
   ---> 24cb54109da2
   Successfully built 24cb54109da2
   ````

1. Ejecutar start.sh para arrancar la imagen en un contenedor.

   Ayuda:

    usage start.sh [--id idImage]

    options:

    -i, --id                  specify an image id if not, it will stop the last one the system created

   Es decir, se le puede pasar el ID de la imagen docker la cual se quiere arrancar, o si no se le pasa, se ejecturá con la última imagen creada en el sistema.
   Ejemplo:
   ```
   $> sh start.sh
   ```
   Salida:
   ````
   5d0c8c3765be5d5710ac4d2b91283b146e52770f8baf68722bd344e4c8aa9a0a
   ````
   Que es una clave generada

1. Ejecutar exec.sh para arrancar nutch.
   Ayuda:

    usage exec.sh [-id idContainer -r numberOfRounds]

    options:

    -i, --id                  specify an container id if not, it will stop the last one the system created

    -r, --rounds               specify the number of rounds to execute (2 by defect)

   Es decir, se le puede pasar el ID del contenedor docker en el cual se quiere arrancar nuch, o si no se le pasa, se ejecturá en el último contenedor creado en el sistema.
   Ejemplo:
   ```
   sh exec.sh -r 2
   ```
   Salida deseada:
   ```
   Injector: starting at 2016-03-01 13:13:48
   Injector: crawlDb: micrawl/crawldb
   ..
   ..
   ..
   ..
   LinkDb: merging with existing linkdb: micrawl/linkdb
   LinkDb: finished at 2016-03-05 14:29:29, elapsed: 00:00:02
   SegmentReader: dump segment: micrawl/segments/20160305142333
   SegmentReader: done
   ```

1. Para extraer la información recogida por el crawler, se debe ejecutar el script extraer.sh.
    Ayuda:

    usage extract.sh [--id idImage -p pathToCopy]

    options:

    -i, --id                  specify an image id if not, it will stop the last one the system created

    -p, --path                specify the path where Nutch's output will be copy

    Es decir, se le puede pasar el ID del contenedor docker en el cual se quiere arrancar nuch, o si no se le pasa, se ejecturá en el último contenedor creado en el sistema.
    Ejemplo:
    ```
    sh extract.sh -p salida.txt
    ```
    Salida deseada:
    ```
    output copied to salida.txt
    ```

  Y en el fichero salida.txt se habrá volacado todo el contenido.


1. Si en algún momento se quiere parar nutch dentro de la imagen, se debe ejecutar stopNutch.sh. Ayuda:

    usage pararNutch.sh [--id idContainer]

    options:

    -i, --id                  specify an container id if not, it will stop nutch in the last container the system created

    Es decir, se le puede pasar el ID del contenedor docker en el cual se quiere parar nuch, o si no se le pasa, se parará en el último contenedor creado en el sistema.
    Ejemplo:
    ```
    sh stopNutch.sh
    ```
    Salida deseada:
    ```
    Nutch stopped in (idContainer)
  ```


1. Si en algún momento se quiere parar el contenedor docker, se debe ejecutar stopContainer.sh
     Ayuda:

     usage stopContainer.sh [--id idContainer]

     options:

     -i, --id                  specify an container id if not, it will stop the last one the system created

     Es decir, se le puede pasar el ID del contenedor docker el cual se quiere parar, o si no se le pasa, se parará el último contenedor creado en el sistema.
     Ejemplo:
     ```
     sh stopContainer.sh
     ```
     Salida deseada:
     ```
     (idContainer) stopped
     ```



Si alguna vez sale el error:
      ```
     docker: An error occurred trying to connect: Post http://%2Fvar%2Frun%2Fdocker.sock/v1.22/containers/create: read unix       @->/var/run/docker.sock: read: connection reset by peer.
     ```
Tan solo hay que ejecutar esto:
      ```
     sudo service docker stop
     sudo rm /var/lib/docker/network/files/local-kv.db
     sudo service docker start
     ```
