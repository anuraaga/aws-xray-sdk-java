plugins {
    `java-library`
    `maven-publish`
}

dependencies {
    api(project(":aws-xray-recorder-sdk-core"))

    implementation(project(":aws-xray-recorder-sdk-aws-sdk-core"))

    testImplementation("com.github.stefanbirkner:system-rules:1.16.0")
    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:2.23.4")
    testImplementation("org.powermock:powermock-module-junit4:2.0.2")
    testImplementation("org.powermock:powermock-api-mockito2:2.0.2")
}

description = "AWS X-Ray Recorder SDK for Java - Segment Metrics"
