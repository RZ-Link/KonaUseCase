package org.example;

import com.tencent.kona.crypto.KonaCryptoProvider;
import com.tencent.kona.pkix.KonaPKIXProvider;
import com.tencent.kona.ssl.KonaSSLProvider;

import javax.net.ssl.*;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.Security;
import java.time.LocalDateTime;

public class Client {
    public static void main(String[] args) {
        try {
            // 加载国密Provider
            Security.addProvider(new KonaCryptoProvider()); // 实现国密密码学算法SM2，SM3和SM4
            Security.addProvider(new KonaPKIXProvider()); // 实现国密证书的解析与验证，并可加载和创建包含国密证书的密钥库
            Security.addProvider(new KonaSSLProvider()); // 实现中国的传输层密码协议（TLCP）
            // 加载可以信任的服务器证书
            KeyStore trustStore = KeyStore.getInstance("PKCS12", "KonaPKIX");
            trustStore.load(Client.class.getResourceAsStream("/org/example/server.ks"), "testpasswd".toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
            tmf.init(trustStore);
            // 加载客户端证书，服务器通过客户端证书进行验证
            KeyStore keyStore = KeyStore.getInstance("PKCS12", "KonaPKIX");
            keyStore.load(Client.class.getResourceAsStream("/org/example/client.ks"), "testpasswd".toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
            kmf.init(keyStore, "testpasswd".toCharArray());

            // 创建SSLContext
            SSLContext context = SSLContext.getInstance("TLCPv1.1");
            context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

            // 连接服务器
            while (true) {
                SSLSocket socket = (SSLSocket) context.getSocketFactory().createSocket("127.0.0.1", 9090);

                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF("你好" + LocalDateTime.now());
                dataOutputStream.flush();

                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                System.out.println("echo " + dataInputStream.readUTF());

                Thread.sleep(5000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
