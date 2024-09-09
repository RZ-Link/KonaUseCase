# 基于SM2证书（国密）（腾讯Kona国密套件）的SSL/TLS安全通信协议

## 生成server证书

```shell
D:\JDK\OpenJDK8U-jdk_x64_windows_hotspot_8u422b05\jdk8u422-b05\bin\java.exe -cp "D:\IdeaProjects\KonaUseCase\lib\kona-crypto-1.0.9.jar;D:\IdeaProjects\KonaUseCase\lib\kona-pkix-1.0.9.jar;" com.tencent.kona.pkix.tool.KeyTool -genkeypair -validity 20000 -keystore server.ks -storetype PKCS12 -storepass testpasswd -keyalg EC -groupname curveSM2 -sigalg SM3withSM2 -dname CN=server -alias server
```

## 生成client证书

```shell
D:\JDK\OpenJDK8U-jdk_x64_windows_hotspot_8u422b05\jdk8u422-b05\bin\java.exe -cp "D:\IdeaProjects\KonaUseCase\lib\kona-crypto-1.0.9.jar;D:\IdeaProjects\KonaUseCase\lib\kona-pkix-1.0.9.jar;" com.tencent.kona.pkix.tool.KeyTool -genkeypair -validity 20000 -keystore client.ks -storetype PKCS12 -storepass testpasswd -keyalg EC -groupname curveSM2 -sigalg SM3withSM2 -dname CN=client -alias client
```

## 查看证书

```shell
D:\JDK\OpenJDK8U-jdk_x64_windows_hotspot_8u422b05\jdk8u422-b05\bin\java.exe -cp "D:\IdeaProjects\KonaUseCase\lib\kona-crypto-1.0.9.jar;D:\IdeaProjects\KonaUseCase\lib\kona-pkix-1.0.9.jar;" com.tencent.kona.pkix.tool.KeyTool -list -v -keystore server.ks -storetype PKCS12 -storepass testpasswd
```

## 参考

腾讯Kona国密套件
https://github.com/Tencent/TencentKonaSMSuite

Java Secure Socket Extension (JSSE) Reference Guide
https://docs.oracle.com/en/java/javase/11/security/java-secure-socket-extension-jsse-reference-guide.html

D:\JDK\OpenJDK8U-jdk_x64_windows_hotspot_8u422b05\jdk8u422-b05\bin\java.exe -cp "D:
\IdeaProjects\KonaUseCase\lib\kona-crypto-1.0.9.jar;D:\IdeaProjects\KonaUseCase\lib\kona-pkix-1.0.9.jar;"
com.tencent.kona.pkix.tool.KeyTool -list -v -keystore server.ks -storetype PKCS12 -keyalg EC -groupname curveSM2 -sigalg
SM3withSM2