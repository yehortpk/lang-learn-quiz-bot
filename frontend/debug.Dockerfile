FROM gradle:8.3.0-jdk17 as frontend
WORKDIR /app

# Copy the Gradle build files (e.g., build.gradle and settings.gradle)
COPY build.gradle settings.gradle /app/
COPY src /app/src

ENV SPRING_PROFILES_ACTIVE=debug

EXPOSE 8081

# # Build and run the application using Gradle
CMD ["gradle", "run"]