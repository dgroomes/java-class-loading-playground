plugins {
    java
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "dgroomes.Runner"
        }
    }
}
