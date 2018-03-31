package de.mkammerer.maven2plantuml.output

import de.mkammerer.maven2plantuml.model.Project
import java.io.OutputStream

interface OutputWriter {
    fun write(project: Project, settings: Settings, outputStream: OutputStream)
}