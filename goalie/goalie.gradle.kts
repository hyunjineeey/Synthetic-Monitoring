
val spockVersion: String by project
val springBootVersion: String by project

plugins {
	id("io.spring.dependency-management")
	id("org.springframework.boot")
	id("io.freefair.lombok")
}

apply(from = rootProject.file("gradle/convention.gradle.kts"))

dependencies {
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation(project(":goalie-api"))
	implementation("com.google.protobuf:protobuf-java-util:3.5.1")
	implementation("com.google.protobuf:protobuf-java:3.7.1")
	implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.amazonaws:amazon-kinesis-producer:0.13.1")
    implementation("software.amazon.awssdk:kinesis:2.10.47")
    implementation("software.amazon.awssdk:sts:2.10.59")
    implementation("org.springframework:spring-web:5.2.7.RELEASE")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.spockframework:spock-core:$spockVersion")
    testImplementation("org.spockframework:spock-spring:$spockVersion")
    testImplementation("javax.servlet:javax.servlet-api:3.1.0")
    testImplementation("commons-io:commons-io:2.6")
    testImplementation("cglib:cglib-nodep:3.2.8")
    testImplementation("org.objenesis:objenesis:3.0")
}

sourceSets {
	getByName("test").java.srcDirs("src/test/groovy")
	getByName("test").resources.srcDirs("src/test/groovy")
}

tasks {
	bootJar {
		mainClassName = "com.jin.goalie.GoalieApplication"
	}

	bootRun {
		jvmArgs = listOf("-Dspring.config.name=goalie")
	}

	compileJava {
		options.compilerArgs.add("-Xlint:unchecked")
	}
}
