# Guía de uso

![alt tag]
(https://raw.githubusercontent.com/Shathe/101CrawlersWeb/master/docs/site/home.png)


## Crear un proyecto.


Al darle al botón de 'new project', se mostrará una nueva pantalla para rellenar los datos necesarios para la creación del proyecto.

### Información mínima necesaria
* Nombre del proyecto
* fichero de configuración Yaml que siga [esta especificación](https://github.com/Shathe/101CrawlersWeb/wiki/Especificaci%C3%B3n-del-fichero-de-configuraci%C3%B3n-yaml)


![alt tag]
(https://raw.githubusercontent.com/Shathe/101CrawlersWeb/master/docs/site/editProject.png)

Tras introducir el nombre y seleccionar el fichero correspondiente al DSL, el fichero se tiene que subir al servidor, así que para poder subir el fichero de configuración, se ha de pulsar a 'upload DSL file'

Una vez subido el fichero, se puede dar a guardar el proyecto. Cuando se le dé, se validará el fichero y si es correcto, el proyecto se creará, sino es así, se notificará del error y no se creará.

### Información opcional 
* Plugins

Los plugins se deben de adjuntar con todos sus ficheros de golpe y ya compilados, es decir, el fichero plugin.xml y los jars. Para poder añadir un plugin, tan solo hay que darle un nombre (el nombre del plugin que viene en el plugin.xml) y adjutnar los ficheros. Al darle a Upload plugin, se subirá el plugin al servidor.
Se pueden añadir un número ilimitado de plugins, pero sus ficheros como máximo pueden ocupar 10 megas.

Si en cualquier momento se quiere resetear las configuraciones/plugins subidos, tan solo se debe clicar el boton 'reset upload'


Una vez creado el proyecto, se puede editar el proyecto, borrar el proyecto, o entrar dentro para crear imágenes y contenedores.

## Crear una imagen.

Lo primero de todo, una imagen, es una forma de organización del proyecto. Dada la última configuración del proyecto, se toma una imagen de esta configuración, para poder crear instancias (contendores) más adelante, y así poder tener en un mismo proyecto, diferentes configuraciones, guardadas en diferentes imágenes.

![alt tag]
(https://raw.githubusercontent.com/Shathe/101CrawlersWeb/master/docs/site/images.png)

Al crear una imagen tan solo se le tiene que especificar el nombre. El proceso de creación de esta imagen puede tardar desde un segundo hasta varios minutos. Esto es debido a que crear una imagen es equivalente a Descargar el sistema operativo, configurarlo, descargar todo el software necesario para el sistema de crawling.

Puede llegar a tardar muy poco debido a la caché de Docker. Todo lo que se ha creado alguna vez para alguna imagen, se reautilizará para las demás, es decir, si yo creo una imagen con la misma configuración que otra que ya ha sido creada, tardará tan uno segundos en tener montado todo el sistema de la imagen.

También se pueden borrar o editar.

## Crear un contenedor.

Los contenedores son instancias de imágenes, teniendo un contenedor, es como tener una máquina virtual, un ordenador funcionando teniendo el sistema de crawling dentro.

Para crearlo, previamente se ha debido de introducirse en una imagen. así tan solo hay que especificar el nombre tras darle a crear nuevo proyecto.

También se pueden borrar o editar.

Una vez lo creas, y clicas en el para introducirte dentro, tardará en cargar un segundo o dos el estado actual del contenedor y mostrará esta pantalla con multiples funcionalidades

![alt tag]
(https://raw.githubusercontent.com/Shathe/101CrawlersWeb/master/docs/site/container.png)

![alt tag]
(https://raw.githubusercontent.com/Shathe/101CrawlersWeb/master/docs/site/container2.png)

### Funcionalidades

* Index: Indexa todo lo que se ha ido recogiendo de información, esta opción está introducida debido a que en el DSL se especifica cuadno se quiere indexar, pero se da la opción de realizarlo manualmente por si el usuario quiere asegurarse o ha cambiado de opinión respecto a cuando realizarla.
* Search: Puedes buscar cualquier contenido en la información que el sistema ha recogido tan solo introduciendo la búsqueda y clicando a este botón. Tan solo muestra los 20 primeros resultados
* Pause/Stop: Pausa o para el sistema para no consumir recursos. Se recomienda tan solo pausarlo.
* Restart: Solo visible si se ha pausado o parado previamente, vuelve a iniciar en el estado anterior el sistema
* Download all results: Descarga en un fichero con TODOS los resultados de la query
* Download all: Descarga TODA la información recogida por el crawler


Se muestra en esta pantalla el estado del contendor y del crawler en todo momento.

La diferencia entre ellos es que el contendor, es como el ordenador, donde se está ejecutando el crawler, en cambio, el crawler es el programa que recoge toda la información.
