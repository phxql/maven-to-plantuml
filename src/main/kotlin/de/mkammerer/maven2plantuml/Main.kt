package de.mkammerer.maven2plantuml

import de.mkammerer.maven2plantuml.input.InputParser
import de.mkammerer.maven2plantuml.input.MavenInputParser
import de.mkammerer.maven2plantuml.output.OutputWriter
import de.mkammerer.maven2plantuml.output.PlantUmlOutputWriter
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
    options.addOption("i", "input", true, "Input file")
    options.addOption("o", "output", true, "Output file")

    val parser = DefaultParser()
    val cli = parser.parse(options, args)

    if (!cli.hasOption("input") || !cli.hasOption("output")) {
        printHelp(options)
        return
    }

    val inputParser: InputParser = MavenInputParser
    val outputWriter: OutputWriter = PlantUmlOutputWriter

    val inputFile = Paths.get(cli.getOptionValue("input")).toAbsolutePath()
    logger.info("Using input file {}", inputFile)
    val outputFile = Paths.get(cli.getOptionValue("output")).toAbsolutePath()
    logger.info("Using output file {}", inputFile)

    val project = Files.newInputStream(inputFile).use {
        inputParser.parse(it)
    }

    Files.newOutputStream(outputFile).use {
        outputWriter.write(project, it)
    }
}

private fun printHelp(options: Options) {
    val formatter = HelpFormatter()
    formatter.printHelp("maven-to-plantuml", options)
}