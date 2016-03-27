# Guía de uso

## Requisitos
Configuración.yml que siga esta [especificación] (https://github.com/Shathe/101CrawlersWeb/wiki/Especificaci%C3%B3n-del-fichero-de-configuraci%C3%B3n-yaml)

##Ejecución desde jar/aplicación

### Comandos

Las imagenes y los contenedores que se crean, tendrán el nombre formado por:
idUsuario_idCrawler
Así serán únicos y fácilmente identificables.

CONFIG:
- El comando config, crea todos los ficheros the configuración necesarios si el fichero está correctamente formado
- Si el fichero está mal formado se informará que tipo de error y qué valor es el erróneo
- Config command, the one which crates every configuration file as long as the configuration file is well formed
- If there's any error, the system will infom about it, both the error name and the value
```sh
$> config --file conf.yml --idUser 1 --idCrawl 1
$> config --help
```

CONFIG:
- El comando config, crea todos los ficheros the configuración necesarios si el fichero está correctamente formado
- Config command, the one which crates every configuration file as long as the configuration file is well formed
```sh
$> config --file conf.yml --idUser 1 --idCrawl 1
$> config --help
```

BUILD:
- El comando build, crea la imagen de docker siempre que existan los ficheros necesarios
- Build command, the one which creates the docker image as long as the files exist
```sh
$> build --idUser 1 --idCrawl 1
$> build --help
```

START:
- El comando start, crea el contenedor docker siempre que la imagen docker exista
- Start command, the one which create and start the docker container as long as the docker image exists
```sh
$> start --idUser 1 --idCrawl 1
$> start --help
```

RUN:
- El comando run, arranca el sistema de crawling siempre que el contendor exista
- Run command, the one which starts the crawling as long as the container exists
```sh
$> run --idUser 1 --idCrawl 1
$> run --help
```

EXTRACT:
- El comando extract, extrae la información recopilada siempre que el contendor exista
- Extract command, the one which extract the information crawled as long as the container exists
```sh
$> extract --idUser 1 --idCrawl 1
$> extract --help
```

STOPCRAWL:
- El comando stopCrawl, para el crawler siempre que el contenedor exista
- stopCrawl command, the one which stops the crawler as long as the container exists
```sh
$> stopCrawl --idUser 1 --idCrawl 1
$> stopCrawl --help
```


STOPCONTAINER:
- El comando stopContainer, para el contenedor siempre que este exista
- StopContainer command, the one which stops the container as long as the container exists
```sh
$> stopContainer --idUser 1 --idCrawl 1
$> stopContainer --help
```



##Ejecución desde código
Crear un objeto Adaptador, indicándoles el nombre del crawler (normalmente será idUsuario+idCrawler), y el Path donde está el fichero yml de configuración.

```java
AdaptadorBuilder builder = new AdaptadorBuilder(id, ruta);
builder.crearFicherosConfiguracion();		
```

Se generarán los ficheros necesarios para configurar en una carpeta cuyo nombre es el nombre pasado al adaptador.
