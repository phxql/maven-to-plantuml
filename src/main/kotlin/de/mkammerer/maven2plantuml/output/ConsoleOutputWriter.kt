package de.mkammerer.maven2plantuml.output

import de.mkammerer.maven2plantuml.model.Project
import java.io.OutputStream

object ConsoleOutputWriter : OutputWriter {
    override fun write(project: Project, outputStream: OutputStream) {
        for (module in project.modules) {
            println("Module $module")
            val directModuleDependencies = module.findModuleDependencies(project.modules, true)
            for (dependency in directModuleDependencies) {
                println("+- $dependency")
            }
        }
    }
}