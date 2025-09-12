FROM eclipse-temurin:21.0.8_9-jre-alpine AS builder
WORKDIR /builder
COPY ./applications/app-service/build/libs/reports.jar .
RUN java -Djarmode=tools -jar reports.jar extract --layers --destination extracted

FROM eclipse-temurin:21.0.8_9-jre-alpine
WORKDIR /app

COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "reports.jar"]
