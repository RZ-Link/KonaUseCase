package org.example;

import cn.hutool.core.thread.ThreadUtil;
import com.tencent.kona.crypto.KonaCryptoProvider;
import com.tencent.kona.pkix.KonaPKIXProvider;
import com.tencent.kona.ssl.KonaSSLProvider;

import javax.net.ssl.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.Security;

public class Server {

    public static void main(String[] args) {
        try {
            // 加载国密Provider
            Security.addProvider(new KonaCryptoProvider()); // 实现国密密码学算法SM2，SM3和SM4
            Security.addProvider(new KonaPKIXProvider()); // 实现国密证书的解析与验证，并可加载和创建包含国密证书的密钥库
            Security.addProvider(new KonaSSLProvider()); // 实现中国的传输层密码协议（TLCP）
            // 加载可以信任的客户端证书
            KeyStore trustStore = KeyStore.getInstance("PKCS12", "KonaPKIX");
            trustStore.load(Server.class.getResourceAsStream("/org/example/client.ks"), "testpasswd".toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
            tmf.init(trustStore);
            // 加载服务器证书，客户端通过服务器证书进行验证
            KeyStore keyStore = KeyStore.getInstance("PKCS12", "KonaPKIX");
            keyStore.load(Server.class.getResourceAsStream("/org/example/server.ks"), "testpasswd".toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
            kmf.init(keyStore, "testpasswd".toCharArray());

            // 创建SSLContext
            SSLContext context = SSLContext.getInstance("TLCPv1.1");
            context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

            SSLServerSocketFactory sslServerSocketFactory = context.getServerSocketFactory();
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(9090);
            sslServerSocket.setNeedClientAuth(true); // 服务器需要验证客户端身份

            // 启动服务器，监听端口，实现业务逻辑
            while (true) {
                SSLSocket socket = (SSLSocket) sslServerSocket.accept();
                ThreadUtil.execute(() -> {
                    try {
                        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                        String receive = dataInputStream.readUTF();

                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        dataOutputStream.writeUTF(receive);

                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
