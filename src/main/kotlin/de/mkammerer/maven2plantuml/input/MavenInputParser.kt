package de.mkammerer.maven2plantuml.input

import de.mkammerer.maven2plantuml.model.Dependency
import de.mkammerer.maven2plantuml.model.Module
import de.mkammerer.maven2plantuml.model.Project
import org.slf4j.LoggerFactory
import java.io.InputStream

object MavenInputParser : InputParser {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun parse(input: InputStream): Project {
        val modules = mutableSetOf<Module>()

        var currentModule: Module? = null
        var currentModuleDependencies = mutableSetOf<Dependency>()

        input.bufferedReader().useLines { lines ->
            for (line in lines) {
                val module = parseModuleLine(line)
                if (module != null) {
                    currentModule?.let {
                        modules.add(it.copy(dependencies = currentModuleDependencies))
                        currentModuleDependencies = mutableSetOf()
                    }

                    currentModule = module
                } else {
                    val dependency = parseDependencyLine(line)
                    if (dependency != null) {
                        if (currentModule == null) throw IllegalStateException("Dependency without module found")
                        currentModuleDependencies.add(dependency)
                    }
                }
            }
        }

        currentModule?.let {
            modules.add(it.copy(dependencies = currentModuleDependencies))
            currentModuleDependencies = mutableSetOf()
            currentModule = null
        }

        return Project(modules)
    }

    private const val identifier = """[a-zA-Z_0-9.\-]+"""
    // [INFO] group:artifact:type:version
    private val modulePattern = """\[INFO] ($identifier):($identifier):($identifier):($identifier)""".toRegex()
    // [INFO] +- group:artifact:type:version:scope
    private val directDependencyPattern = """\[INFO] \+- ($identifier):($identifier):($identifier):($identifier):($identifier)""".toRegex()
    // [INFO] |  +- group:artifact:type:version:scope
    private val transitiveDependencyPattern = """\[INFO] \|.*?($identifier):($identifier):($identifier):($identifier):($identifier)""".toRegex()

    private fun parseModuleLine(line: String): Module? {
        val result = modulePattern.matchEntire(line) ?: return null
        logger.trace("Found module line '{}'", line)

        val group = result.groupValues[1]
        val artifact = result.groupValues[2]

        return Module(group, artifact).also {
            logger.debug("Found module {}", it)
        }
    }

    private fun parseDependencyLine(line: String): Dependency? {
        val result = directDependencyPattern.matchEntire(line) ?: return parseTransitiveDependencyLine(line)
        logger.trace("Found direct dependency line '{}'", line)

        val group = result.groupValues[1]
        val artifact = result.groupValues[2]
        val scope = result.groupValues[5]

        return Dependency(group, artifact, scope, false).also {
            logger.debug("Found direct dependency {}", it)
        }
    }

    private fun parseTransitiveDependencyLine(line: String): Dependency? {
        val result = transitiveDependencyPattern.matchEntire(line) ?: return null
        logger.trace("Found transitive dependency line '{}'", line)

        val group = result.groupValues[1]
        val artifact = result.groupValues[2]
        val scope = result.groupValues[5]

        return Dependency(group, artifact, scope, true).also {
            logger.debug("Found transitive dependency {}", it)
        }
    }
}