allprojects {
    group = "com.amazonaws"

    repositories {
        mavenCentral()
        mavenLocal()
    }

    plugins.withId("java-library") {
        configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8

            withJavadocJar()
            withSourcesJar()
        }

        dependencies {
            configurations.configureEach {
                if (isCanBeResolved && !isCanBeConsumed) {
                    add(name, platform(project(":dependencyManagement")))
                }
            }
        }

        tasks {
            named<Javadoc>("javadoc") {
                val options = options as StandardJavadocDocletOptions

                options.quiet()
                options.addBooleanOption("-allow-script-in-comments", true)
                options.footer = "<script src=\"/SdkStatic/sdk-priv.js\" async=\"true\"></script>"
                options.bottom = "Copyright &#169; 2018 Amazon Web Services, Inc. All Rights Reserved."

                // TODO(anuraaga): Enable doclint except for required @param/@returns
                options.addBooleanOption("Xdoclint:none", true)
                // options.addBooleanOption("Xdoclint:accessibility", true)
                // options.addBooleanOption("Xdoclint:html", true)
                // options.addBooleanOption("Xdoclint:reference", true)
                // options.addBooleanOption("Xdoclint:syntax", true)
            }
        }
    }

    plugins.withId("maven-publish") {
        configure<PublishingExtension> {
            publications {
                register<MavenPublication>("maven") {
                    plugins.withId("java-platform") {
                        from(components["javaPlatform"])
                    }
                    plugins.withId("java-library") {
                        from(components["java"])
                    }
                    
                    versionMapping {
                        allVariants {
                            fromResolutionResult()
                        }
                    }
                    pom {
                        afterEvaluate {
                            pom.name.set(project.description)
                        }
                        description.set("The Amazon Web Services X-Ray Recorder SDK for Java provides Java APIs for " +
                                "emitting tracing data to AWS X-Ray. AWS X-Ray helps developers analyze and debug " +
                                "distributed applications. With X-Ray, you can understand how your application and " +
                                "its underlying services are performing to identify and troubleshoot the root cause of " +
                                "performance issues and errors.")
                        url.set("https://aws.amazon.com/documentation/xray/")


                        licenses {
                            license {
                                name.set("Apache License, Version 2.0")
                                url.set("https://aws.amazon.com/apache2.0")
                                distribution.set("repo")
                            }
                        }

                        developers {
                            developer {
                                id.set("amazonwebservices")
                                organization.set("Amazon Web Services")
                                organizationUrl.set("https://aws.amazon.com")
                                roles.add("developer")
                            }
                        }

                        scm {
                            url.set("https://github.com/aws/aws-xray-sdk-java.git")
                        }

                        properties.put("awsxrayrecordersdk.version", project.version.toString())
                    }
                }
            }
        }
    }
}
