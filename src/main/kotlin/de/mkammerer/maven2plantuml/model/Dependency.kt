package de.mkammerer.maven2plantuml.model

data class Dependency(
        val group: String,
        val artifact: String,
        val scope: String,
        val transitive: Boolean
) {
    override fun toString(): String = "$group.$artifact"
}