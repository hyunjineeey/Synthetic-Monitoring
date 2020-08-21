//import com.github.spotbugs.SpotBugsExtension

//apply(plugin = "com.github.spotbugs")
apply(plugin = "pmd")
apply(plugin = "checkstyle")
apply(plugin = "jacoco")

configure<CheckstyleExtension> {
	configFile = file("${rootProject.projectDir}/gradle/checkstyle/checkstyle.xml")
	isIgnoreFailures = true
}

configure<PmdExtension> {
	rulePriority = 4
	isIgnoreFailures = false
	reportsDir = file("${project.buildDir}/coverage/findbugs")
	ruleSetFiles = files("${rootProject.projectDir}/gradle/pmd/rules.xml")
	ruleSets = listOf()
}

//configure<SpotBugsExtension> {
//    excludeFilter = file("${rootProject.projectDir}/gradle/findbugs/excludeFilter.xml")
//    reportLevel = "low"
//    effort = "max"
//    isIgnoreFailures = false
//    reportsDir = file("${project.buildDir}/coverage/spotbugs")
//}

//tasks.withType(com.github.spotbugs.SpotBugsTask::class.java) {
//    reports.xml.isEnabled = false
//    reports.html.isEnabled = true
//}

tasks.withType(JacocoCoverageVerification::class.java) {
	violationRules {
		rule {
			limit {
				minimum = "0.59".toBigDecimal()
			}
		}
	}
}

tasks.withType(JacocoReport::class.java) {
	reports {
		xml.isEnabled = true
	}
}
