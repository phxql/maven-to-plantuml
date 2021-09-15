# Maven to PlantUML

This small tool is intended to plot a graph to show the dependencies between Maven modules. It ignores external dependencies and shows only inter-module relationships.

## Download

Check the [releases page](https://github.com/phxql/maven-to-plantuml/releases).

## Usage

1. Run `mvn dependency:tree > dep.txt` in your project. This generates the dependency tree which is used in the next step.
2. Run `java -jar maven-to-plantuml.jar --input dep.txt --output dep.puml`. This transforms the dependency tree output to a PlantUML diagram.
3. Run `plantuml dep.puml`. This will render the PlantUML diagram to a PNG file.
4. View the file `dep.png`.

### Advanced

```
usage: maven-to-plantuml
    --console-output   Instead of generating a PlantUML file, print the
                       dependency graph to the console
 -e,--exclude <arg>    Artifact names of modules to exclude. Separated by
                       comma.
    --help             Prints this help
 -i,--input <arg>      Input file
 -o,--output <arg>     Output file
```

## Example

The graph of the [example project](example/), run with `--input dep.txt --output dep.puml --exclude example` produces 
[this PlantUML file](doc/example.puml) and is rendered to this graph:

![](/doc/example.png)

## Building

Run `./mvnw clean package` and check the `target` folder.

## License

Licensed under [GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html).