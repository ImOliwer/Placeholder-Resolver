plugins {
    java
}

group = "xyz.oliwer"
version = "0.0.1-B"

repositories {
    mavenCentral()
}

dependencies {
    // shaded

    // compile time
    implementation("com.jsoniter:jsoniter:0.9.23")
    implementation("com.github.ben-manes.caffeine:caffeine:3.0.4")

    // test
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

tasks.test {
    useJUnitPlatform()
}