@echo off
@REM 下面这行是为了解决中文乱码
chcp 65001 > nul
set PROFILES=dev
set THREAD=1C

@echo 如果JAVA_HOME和mvn不一致，则手动修改jdk版本。并且下面这个版本号要和pom.xml 里 spotify 设置的 JDK 一致
set JAVA_HOME=C:/Users/X/.jdks/graalvm-jdk-21.0.4

@echo 用于启动运行 Maven 的 JVM 的参数
set MAVEN_OPTS=-Xms2048m -Xmx2048m -XX:MaxMetaspaceSize=512m -XX:ReservedCodeCacheSize=512m

@echo 从 Maven 3.9.0 开始，此变量包含在 CLI 参数之前传递给 Maven 的参数。“^”符号表示命令将在下一行继续
set MAVEN_ARGS=-Dmaven.test.skip=true ^
-Dmaven.compile.fork=true ^
-T %THREAD% ^
-Dmaven.artifact.threads=%NUMBER_OF_PROCESSORS% ^
-Dmaven.site.skip=true ^
-Dmaven.javadoc.skip=true ^
-q ^
-o

echo mvn clean package ......
start /B /WAIT cmd /c "mvn clean package -f pom.xml -P %PROFILES%"
@echo 如果cmd执行失败，则后续的命令不会执行。
if %ERRORLEVEL% neq 0 (
    echo Error: mvn clean package failed.
    exit /b %ERRORLEVEL%
)

echo mvn dockerfile:build ......
echo 如果报这个错就要开代理，因为docker镜像源在国内用不了："[ERROR] Get "https://registry-1.docker.io/v2/"
start /B /WAIT cmd /c "mvn dockerfile:build dockerfile:push -f pom.xml"
if %ERRORLEVEL% neq 0 (
    echo Error: mvn dockerfile:build failed.
    exit /b %ERRORLEVEL%
)

echo mvn dockerfile:build ......
echo 因为没有docker仓库，所以下面这行不执行，如果有仓库，下面这行 echo 的命令就是推送到docker仓库。
echo start /B /WAIT cmd /c "mvn dockerfile:build dockerfile:push -f pom.xml"
if %ERRORLEVEL% neq 0 (
    echo Error: mvn dockerfile:build failed.
    exit /b %ERRORLEVEL%
)

echo 运行：docker run -d -p 8080:8080 --name spring-boot3-demo --restart=always localhost:5000/spring-boot3-demo:1
echo 验证是否启动成功。浏览器访问：localhost:8080/hello

exit /b 0
