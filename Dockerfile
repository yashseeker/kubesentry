# FROM eclipse-temurin:21-jdk
#
# WORKDIR /app
#
# COPY target/*.jar app.jar
#
# EXPOSE 8080
#
# ENTRYPOINT ["java", "-jar", "app.jar"]

# ---------- Build Stage ----------
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# ---------- Runtime Stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN addgroup --system spring && adduser --system spring --ingroup spring
# system not a human login group, spring group , user -> spring belonging to spring group
USER spring:spring
# user spring , group spring
COPY --from=builder /app/target/*.jar app.jar
# copy from the previous stage
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
