package de.mkammerer.maven2plantuml

import de.mkammerer.maven2plantuml.input.InputParser
import de.mkammerer.maven2plantuml.input.MavenInputParser
import de.mkammerer.maven2plantuml.model.Project
import de.mkammerer.maven2plantuml.output.ConsoleOutputWriter
import de.mkammerer.maven2plantuml.output.OutputWriter
import de.mkammerer.maven2plantuml.output.PlantUmlOutputWriter
import de.mkammerer.maven2plantuml.output.Settings
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Paths

private val logger = LoggerFactory.getLogger("de.mkammerer.maven2plantuml.Main")

fun main(args: Array<String>) {
    logger.info("Started")
    try {
        doRun(args)
    } finally {
        logger.info("Stopped")
    }
}

private fun doRun(args: Array<String>) {
    val options = Options()
    options.addOption(null, "help", false, "Prints this help")
    options.addOption("i", "input", true, "Input file")
    options.addOption("o", "output", true, "Output file")
    options.addOption("e", "exclude", true, "Artifact names of modules to exclude. Separated by comma.")
    options.addOption(null, "console-output", false, "Instead of generating a PlantUML file, print the dependency graph to the console")

    val parser = DefaultParser()
    val cli = parser.parse(options, args)

    if (cli.hasOption("help") || !cli.hasOption("input") || !cli.hasOption("output")) {
        printHelp(options)
        return
    }

    val excludedModules = (cli.getOptionValue("exclude") ?: "").split(',').map { it.trim() }.toSet()

    val inputParser: InputParser = MavenInputParser
    val outputWriter: OutputWriter = if (cli.hasOption("console-output")) ConsoleOutputWriter else PlantUmlOutputWriter

    val inputFile = Paths.get(cli.getOptionValue("input")).toAbsolutePath()
    logger.info("Using input file {}", inputFile)
    val outputFile = Paths.get(cli.getOptionValue("output")).toAbsolutePath()
    logger.info("Using output file {}", outputFile)

    logger.debug("Parsing input file")
    val project = Files.newInputStream(inputFile).use {
        inputParser.parse(it)
    }

    logger.debug("Compiling set of excluded modules")
    val settings = buildSettings(project, excludedModules)
    logger.info("Excluded modules: {}", settings.excludeModules)

    logger.debug("Writing output file")
    Files.newOutputStream(outputFile).use {
        outputWriter.write(project, settings, it)
    }

    logger.info("Success, check {}", outputFile)
}

fun buildSettings(project: Project, excludedModules: Set<String>): Settings {
    return Settings(
            excludeModules = project.modules.filter { excludedModules.contains(it.artifact) }.toSet()
    )
}

private fun printHelp(options: Options) {
    val formatter = HelpFormatter()
    formatter.printHelp("maven-to-plantuml", options)
}