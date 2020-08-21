if (!JavaVersion.current().isJava11Compatible) {
	throw IllegalStateException("Must be built with Java 11")
}

buildscript {
	val springBootVersion: String by project

	repositories {
		mavenLocal()
		mavenCentral()
		jcenter()
		maven { url = uri("https://plugins.gradle.org/m2/") }
	}
	dependencies {
		classpath("com.github.jengelman.gradle.plugins:shadow:5.2.0")
		classpath("org.jfrog.buildinfo:build-info-extractor-gradle:4.7.1")
		classpath("com.github.ben-manes:gradle-versions-plugin:0.17.0")
		classpath("gradle.plugin.com.github.spotbugs.snom:spotbugs-gradle-plugin:4.3.0")
		classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
		classpath("io.freefair.gradle:lombok-plugin:4.1.3")
	}
}

apply(plugin = "idea")

allprojects {
	apply(plugin = "java")
	apply(plugin = "com.github.ben-manes.versions")
}

subprojects {
	apply(plugin = "groovy")

	group = "jin"
	repositories {
		mavenLocal()
		mavenCentral()
		jcenter()
		maven { url = uri("http://dl.bintray.com/kotlin/kotlinx") }
		maven { url = uri("https://plugins.gradle.org/m2/") }
	}
}

configure<JavaPluginExtension> {
	sourceCompatibility = JavaVersion.VERSION_11
	targetCompatibility = JavaVersion.VERSION_11
}
