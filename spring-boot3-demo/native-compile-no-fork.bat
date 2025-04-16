@echo off
@REM 下面这行是为了解决中文乱码
chcp 65001 > nul
@echo 启动exe的时候要追加 -Djavax.xml.accessExternalDTD=all 参数使用 mybatis 的 xml 文件
@echo 确保 Windows 安装了 Visual Studio Community
set PROFILES=test
set THREAD=1C

@echo 如果JAVA_HOME和mvn不一致，则手动修改jdk版本。并且下面这个版本号要和pom.xml 里 spotify 设置的 JDK 一致
set JAVA_HOME=C:/Users/X/.jdks/graalvm-jdk-21.0.4

@echo 用于启动运行 Maven 的 JVM 的参数
set MAVEN_OPTS= -Xms2048m -Xmx2048m -XX:MaxMetaspaceSize=512m -XX:ReservedCodeCacheSize=512m

@echo 从 Maven 3.9.0 开始，此变量包含在 CLI 参数之前传递给 Maven 的参数。“^”符号表示命令将在下一行继续
set MAVEN_ARGS=-Dmaven.test.skip=true ^
-Dmaven.compile.fork=true ^
-T %THREAD% ^
-Dmaven.artifact.threads=%NUMBER_OF_PROCESSORS% ^
-Dmaven.site.skip=true ^
-Dmaven.javadoc.skip=true ^
-q ^
-o

echo mvn clean compile ......
@echo 打包 native 必须使用 -P native否则打完包，启动后输出控制台会报错。
start /B /WAIT cmd /c "mvn clean compile -f pom.xml -Pnative,%PROFILES%"
@echo 如果上面这条 cmd 命令执行失败，则后续命令不会执行
if %ERRORLEVEL% neq 0 (
    echo Error: mvn clean compile failed.
    exit /b %ERRORLEVEL%
)

echo mvn spring-boot:process-aot ......
start /B /WAIT cmd /c "mvn spring-boot:process-aot -f pom.xml"
if %ERRORLEVEL% neq 0 (
    echo Error: mvn spring-boot:process-aot failed.
    exit /b %ERRORLEVEL%
)

echo mvn native:compile-no-fork ......
start /B /WAIT cmd /c "mvn native:compile-no-fork -f pom.xml -Pnative"
if %ERRORLEVEL% neq 0 (
    echo Error: mvn native:compile-no-fork failed.
    exit /b %ERRORLEVEL%
)

exit /b 0
