[<img src=https://user-images.githubusercontent.com/6883670/31999264-976dfb86-b98a-11e7-9432-0316345a72ea.png height=75 />](https://reactome.org)

# Reactome Pathways Overview Layout Generator

## What is the Reactome Pathways Overview?

The pathways overview (AKA Fireworks) is meant to be a genome-wide, hierarchical visualisation of [Reactome](https://reactome.org) 
pathways in a space-filling graph. To do so the nodes and their layout are set up in a off-line process.


## Why an offline layout generator?

The pathways overview layout generator needs to access Reactome graph database to retrieve pathways information in order to
create the overview content json files. It assigns a size and a position in the plane for each node in Fireworks (which 
represents a Reactome pathway).

The generated json files remain immutable for a given Reactome data release, what means they can be generated once and
stored for later widget consumption. This strategy ensures the layout generation is not a handicap for the future
visualisation.

## How to use it?

**IMPORTANT**: It is possible that some tweaking is needed to the top level pathways configuration after a certain release. If that is the case in the [Fireworks bursts configuration folder](config) there are different files where the top level pathways initial point in the map are defined. Edit them in order to change the position, start angle or burst direction.

The configuration file for [Homo sapiens](config/Homo_sapiens_bursts.json) contains the data related to the Reactome main species so it is the one with more top level pathways.

To run the layout algorithm, please follow the following steps:

1. First package this project to generate the fireworks.jar file:


```console
$mvn clean package
```


2. Generate the json files for Homo Sapiens and then decide whether some tweaking is needed

* Only Homo Sapiens:


```console
$java -jar fireworks-exec.jar \
      -h bolt_url\
      -u user \
      -p password \
      -s Homo_sapiens \
      -f /path/to/config \
      -o /path/to/output \
      --verbose
```


 * Main species and a given "Other species" (being species_name its name):


```console
$java -jar fireworks-exec.jar \
      -h bolt_url \
      -u user \
      -p password \
      -s Species_name \
      -f /path/to/config \
      -o /path/to/output \
      --verbose
```

4. Generate the json files for all species in Reactome:

```console
$java -jar fireworks-exec.jar \
      -h bolt_url \
      -u user \
      -p password \
      -f /path/to/config \
      -o /path/to/output \
      --verbose
```

### To take into account

The generated pathway overview json files are meant to be consumed by the Widget, so they will need to be available from 
the web clients, so please specify a reachable ```/path/to/output```.

In Reactome, the files for the current release are available under the
[https://reactome.org/download/current/fireworks](https://reactome.org/download/current/fireworks/)
folder. So for example, the file for "Homo Sapiens" is
[Homo_sapiens.json](https://reactome.org/download/current/fireworks/Homo_sapiens.json).

### Miscellaneous

Fireworks version 2 exclusively uses the graph-database to generate the layout. You might remember this project existed
using the analysis intermediate data, but this is not longer the case. That approach has been deprecated and unmaintained.


### Logging

When running in the console, Spring Boot Logging will be included and DEBUG logging will be printed. In order to force the fireworks.jar
to use project's logback.xml add: `-Dlogback.configurationFile=src/main/resources/logback.xml`

```console
java -Dlogback.configurationFile=src/main/resources/logback.xml -jar target/fireworks-exec.jar ....
```