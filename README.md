# Maven to PlantUML

This small tool converts the output from `mvn dependency:tree` into a PlantUML diagram.

## Usage

1. Run `mvn dependency:tree > dep.txt` in your project. This generates the dependency tree which is used in the next step.
2. Run `maven-to-plantuml --input dep.txt --output dep.puml`. This transforms the dependency tree output to a PlantUML diagram.
3. Run `plantuml dep.puml`. This will render the PlantUML diagram to a PNG file.
4. View the file `dep.png`.

```
usage: maven-to-plantuml
 -e,--exclude <arg>   Artifact names of modules to exclude. Separated by
                      comma.
    --help            Prints this help
 -i,--input <arg>     Input file
 -o,--output <arg>    Output file
```

## Building

Run `./mvnw clean package` and check the `target` folder.

## License

Licensed under [GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html).