FROM node:18-alpine as web-builder

WORKDIR /tmp/app

COPY ui/package.json .
RUN npm i --legacy-peer-deps

COPY ui/tsconfig.json .
COPY ui/preact.config.js .
COPY ui/replace-react.js .
COPY ui/rollup.config.js .
COPY ui/rollup.config-process-ts.js .
COPY ui/generateInfo.sh .
COPY ui/src src

RUN npm run dist


FROM bellsoft/liberica-openjdk-alpine:11  as builder

RUN apk add --no-cache bash

WORKDIR /tmp/app
COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
RUN ./gradlew --no-daemon

COPY build.gradle .
RUN ./gradlew --no-daemon -Dorg.gradle.jvmargs="-Xmx2g -Xms2g" resolveDependencies

COPY src src
COPY --from=web-builder /tmp/app/build src/main/resources/public

RUN ./gradlew --no-daemon -Dorg.gradle.jvmargs="-Xmx2g -Xms2g" fatJar

FROM bellsoft/liberica-openjdk-alpine:17

WORKDIR /app

COPY --from=builder /tmp/app/build/libs/wallpaper-unnotcher-all-1.0-SNAPSHOT.jar app.jar

ENV JAVA_OPTS "-Xmx256m \
 -Xss256k \
 -XX:+UseShenandoahGC \
 -XX:+UnlockExperimentalVMOptions \
 -XX:ShenandoahUncommitDelay=10000 \
 -XX:ShenandoahGuaranteedGCInterval=30000 \
 --add-opens java.base/java.lang=ALL-UNNAMED \
 --add-opens java.base/java.nio=ALL-UNNAMED \
 "

EXPOSE 8080/tcp

CMD ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]