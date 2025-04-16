ARG JDK
FROM $JDK

# 镜像作者
MAINTAINER Meta39

ARG JAR_NAME
ADD ./target/$JAR_NAME.jar /app.jar

# JAVA参数，如-Xmx8g
ENV JAVA_OPTS="-Xms256m -Xmx256m"

# OPS参数
ENV OPS_OPTS=""

# Spring Boot 参数，如：--spring.profiles.active=${spring.profiles.active:dev}
ENV SPRING_BOOT_OPTS="--spring.profiles.active=dev"

# 暴露端口
EXPOSE 8080

# 运行jar包
ENTRYPOINT ["/bin/sh","-c","java ${JAVA_OPTS} ${OPS_OPTS} -jar app.jar ${SPRING_BOOT_OPTS}"]