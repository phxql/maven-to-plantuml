package de.mkammerer.maven2plantuml.output

import de.mkammerer.maven2plantuml.model.Project
import java.io.OutputStream

object PlantUmlOutputWriter : OutputWriter {
    override fun write(project: Project, outputStream: OutputStream) {
        outputStream.bufferedWriter().use {
            readHeader().forEach { line ->
                it.write(line)
                it.newLine()
            }


            for (module in project.modules) {
                it.write("class \"${module.artifact}\"")
                it.newLine()
            }

            it.newLine()

            for (module in project.modules) {

                val dependencies = module.findModuleDependencies(project.modules, true)
                for (dependency in dependencies) {
                    it.write("\"${module.artifact}\" --> \"${dependency.artifact}\"")
                    it.newLine()
                }
            }

            readFooter().forEach { line ->
                it.write(line)
                it.newLine()
            }
        }
    }

    private fun readHeader(): List<String> {
        return javaClass.getResourceAsStream("/plantuml/header.puml").reader().use {
            it.readLines()
        }
    }

    private fun readFooter(): List<String> {
        return javaClass.getResourceAsStream("/plantuml/footer.puml").reader().use {
            it.readLines()
        }
    }
}