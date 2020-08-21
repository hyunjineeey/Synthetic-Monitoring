rootProject.name = "woof-root"

include("woof", "goalie", "goalie-api")

rootProject.children.forEach { project ->
	project.buildFileName = "${project.name.toLowerCase()}.gradle.kts"
	assert(project.buildFile.isFile)
}
