FROM openjdk:16-jdk as builder
WORKDIR /etc/astro-devlog
COPY . .
USER root
# Create the shadowjar (chmod +x makes the gradlew script executable)
RUN chmod +x ./gradlew
RUN ./gradlew shadowJar

FROM openjdk:16-jdk
WORKDIR /opt/astro-devlog
# Copy the shadowjar in the current workdir
COPY --from=builder ./etc/astro-devlog/build/libs/ .
# Entrypoint is used instead of CMD because the image is not intended to run another executable instead of the jar
ENTRYPOINT java \
    # java -D tag --> set a system property
    -Dkotlin.script.classpath="/opt/astro-devlog/astro-devlog.jar" \
    -jar \
    ./astro-devlog.jar
