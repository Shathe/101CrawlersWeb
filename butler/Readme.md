# Guía de uso

## Requisitos
Configuración.yml que siga esta [especificación] (https://github.com/Shathe/101CrawlersWeb/wiki/Especificaci%C3%B3n-del-fichero-de-configuraci%C3%B3n-yaml)

##Ejecución desde jar/aplicación

### Comandos
```sh
$> config --file config.yml --idUser 1 --idCrawl 1
$> config --help
```



##Ejecución desde código
Crear un objeto Adaptador, indicándoles el nombre del crawler (normalmente será idUsuario+idCrawler), y el Path donde está el fichero yml de configuración.

```java
AdaptadorBuilder builder = new AdaptadorBuilder(id, ruta);
builder.crearFicherosConfiguracion();		
```

Se generarán los ficheros necesarios para configurar en una carpeta cuyo nombre es el nombre pasado al adaptador.
