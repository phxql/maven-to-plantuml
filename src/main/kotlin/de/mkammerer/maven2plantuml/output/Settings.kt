package de.mkammerer.maven2plantuml.output

import de.mkammerer.maven2plantuml.model.Module

data class Settings(
        val excludeModules: Set<Module>
)