# jdk
FROM openjdk:11.0.13-slim-buster
MAINTAINER mzy
# 拷贝jar包
COPY target/*.jar /app.jar
# 容器启动时执行的命令
ENTRYPOINT ["java","-jar","app.jar"]
