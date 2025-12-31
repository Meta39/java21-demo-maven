@echo off
@REM 下面这行是为了解决中文乱码
chcp 65001 > nul
@echo 启动exe的时候要追加 -Djavax.xml.accessExternalDTD=all 参数使用 mybatis 的 xml 文件
@echo 进入到exe目录。运行cmd，再执行chcp 65001 》 nul。防止乱码。然后再执行：XXX.exe -Djavax.xml.accessExternalDTD=all
@echo 确保 Windows 安装了 Visual Studio Community

@echo 如果JAVA_HOME和mvn不一致，则手动修改jdk版本。如果打包失败，说明这里set JAVA_HOME好像没什么用，还是得设置环境变量为graalvm的地址。
set JAVA_HOME=C:/Users/X/.jdks/graalvm-jdk-21.0.7

set PACKAGE_CMD=mvn clean compile spring-boot:process-aot native:compile -DskipTests -f pom.xml
echo %PACKAGE_CMD% ......
%PACKAGE_CMD%
@REM 不能用start /B /WAIT cmd /c "%PACKAGE_CMD%"，因为新开窗口会丢失 set 的参数
@REM start /B /WAIT cmd /c "%PACKAGE_CMD%"

exit
