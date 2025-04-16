# java21-demo-maven
用java21、maven学习的demo

## 运行环境要求
1. java21+
2. maven3+

### spring-boot3-demo注意事项

#### windows执行脚本查看GraalVM根目录下的release获取JAVA_VERSION和GRAALVM_VERSION
```shell
type release | findstr /R "\<JAVA_VERSION\> \<GRAALVM_VERSION\>"
```
1. 项目运行的JDK一定要和打包的JDK版本完全一致，防止打包失败！
2. JAVA_VERSION和GRAALVM_VERSION版本不一定相等！即：JAVA_VERSION = "21.0.6"，GRAALVM_VERSION = "23.1.6"
3. 通过GraalVM安装的根路径下的release可以查看JAVA_VERSION和GRAALVM_VERSION版本号