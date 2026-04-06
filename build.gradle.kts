plugins {
    id("net.fabricmc.fabric-loom-remap") version "1.15-SNAPSHOT"
    id("ploceus") version "1.15-SNAPSHOT"
}

group = "dev.lunasa"
version = "1.0.1"

val shadow by configurations.creating { isCanBeResolved = false }

repositories {
    maven {
        name = "legacy-fabric"
        url = uri("https://maven.legacyfabric.net/")
    }
}

dependencies {
    "minecraft"("com.mojang:minecraft:1.8.9")
    "mappings"("net.legacyfabric:legacy-yarn:1.8.9+build.4:v2")
    "modImplementation"("net.fabricmc:fabric-loader:0.18.4")

    shadow(api("io.netty:netty-all:4.2.12.Final")!!)
}

ploceus {
    setIntermediaryGeneration(2)
}