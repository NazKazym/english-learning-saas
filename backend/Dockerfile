FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

# Copy gradle build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Ensure gradlew is executable
RUN chmod +x ./gradlew

# Download dependencies
RUN ./gradlew dependencies

# Copy source code
COPY src src

# Build the application
RUN ./gradlew bootJar -x test

# Extract layered application
RUN mkdir -p build/extracted && \
    java -Djarmode=layertools -jar build/libs/*.jar extract --destination build/extracted

# Create runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create a non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copy layers from build stage
COPY --from=build /workspace/app/build/extracted/dependencies/ ./
COPY --from=build /workspace/app/build/extracted/spring-boot-loader/ ./
COPY --from=build /workspace/app/build/extracted/snapshot-dependencies/ ./
COPY --from=build /workspace/app/build/extracted/application/ ./

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# Set the entrypoint
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]