package de.mkammerer.maven2plantuml.input

import de.mkammerer.maven2plantuml.model.Project
import java.io.InputStream

interface InputParser {
    fun parse(input: InputStream): Project
}