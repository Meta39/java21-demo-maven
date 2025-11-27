@echo off
@REM 下面这行是为了解决中文乱码
chcp 65001 > nul
set PROFILES=dev
set THREAD=1C

@echo 如果JAVA_HOME和mvn不一致，则手动修改jdk版本。并且下面这个版本号要和pom.xml 里 spotify 设置的 JDK 一致
set JAVA_HOME=C:\Users\x\.jdks\graalvm-jdk-21.0.7

set COMMAND=mvn clean package -DskipTests=true -T%THREAD% -f pom.xml -P%PROFILES%
echo %COMMAND%
%COMMAND% & mvn dockerfile:build

@REM mvn dockerfile:build dockerfile:push

echo 运行：docker run -d -p 8080:8080 --name spring-boot3-demo --restart=always localhost:5000/spring-boot3-demo:1
echo 验证是否启动成功。浏览器访问：localhost:8080/hello

exit
