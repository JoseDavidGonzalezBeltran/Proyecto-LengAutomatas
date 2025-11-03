# Proyecto-LengAutomatas

This project has been prepared to build with Java 21 (LTS).

Quick steps to build and run locally:

1. Install Java 21 (JDK 21) and Maven.
   - On Debian/Ubuntu you can use a distro package or install an upstream JDK (Adoptium/Eclipse Temurin). Example with apt when available:
     - sudo apt update && sudo apt install -y openjdk-21-jdk maven
   - Alternatively, use an SDK manager (sdkman) or manually install JDK 21 and point JAVA_HOME to it.

2. Verify Java 21 is active:

   java -version

   The output should mention version "21".

3. Build with Maven:

   mvn -DskipTests package

4. If you prefer to compile without Maven (javac), set JAVA_HOME to a JDK 21 installation and run a javac compile over the source files in the project root.

Notes:
- A minimal `pom.xml` has been added to the project and sets the compiler to Java 21.
- If you want, I can add a Maven wrapper, CI pipeline, or help install JDK 21 in this development container.
# Proyecto-LengAutomatas