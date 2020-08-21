import com.google.protobuf.gradle.ProtobufConvention
import com.google.protobuf.gradle.protoc

plugins {
    id("com.google.protobuf") version "0.8.7"
    id("idea")
}

val protobufVersion = "3.6.1"

dependencies {
    compile("com.google.protobuf:protobuf-java:$protobufVersion")
}

protobuf {
    getProtobuf().generatedFilesBaseDir = "$projectDir/src/generated"
    getProtobuf().protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
}

idea {
    module {
        sourceDirs.add(file("${protobuf.getProtobuf().generatedFilesBaseDir}/main/java"))
    }
}

tasks {
    clean {
        project.delete(files(protobuf.getProtobuf().generatedFilesBaseDir))
    }
}
