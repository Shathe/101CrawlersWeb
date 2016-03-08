## Ejecución

Para compilar, colocarse dentro del directorio Crawler (Si no, cambiar las rutas si hiciera falta de compilación, por ejemplo, si estuiveras en la carpeta padre a esta, ya no sería serc/*/*.java sino Crawler/src/*/*.java

   ```
javac -cp "./bin/:./snakeyaml-1.9.jar" src/*/*.java 
   ```

Para ejecutar


   ```
java  -cp "./bin/:./snakeyaml-1.9.jar" Builders.Coordinator conf.yml 
   ```
