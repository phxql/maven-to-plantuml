package de.mkammerer.maven2plantuml.model

data class Module(
        val group: String,
        val artifact: String,
        val dependencies: Set<Dependency> = emptySet()
) {
    fun findModuleDependencies(modules: Set<Module>, onlyDirect: Boolean = false): Set<Dependency> {
        return dependencies.filter { dependency ->
            if (onlyDirect && dependency.transitive) false
            else modules.any { it.group == dependency.group && it.artifact == dependency.artifact }
        }.toSet()
    }

    override fun toString(): String = "$group:$artifact"
}