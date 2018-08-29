# problematic-web-app
A web application with various commonly encountered problems that can be used to train your profiling and diagnostic chops

# Building
```bash
mvn package
```
# Running
The build will produce a batch file to launch the application. 

On Windows use:

```bash
target\bin\webapp.bat
```

On Mac OS X use:

```bash
target/bin/webapp
```

Port is by default 8080. Set the environment variable PORT to change.