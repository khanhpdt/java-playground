package cryptography;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HMac {

    private static final String HMAC_SHA_256 = "HmacSHA256";

    private static class Server {

        static final Path HMAC_KEY_FILE = Paths.get("hmac.key");

        void generateKey() {
            SecretKey key;
            try {
                KeyGenerator keyGen = KeyGenerator.getInstance(HMAC_SHA_256);
                key = keyGen.generateKey();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new IllegalStateException("No algorithm", e);
            }

            try {
                Files.write(HMAC_KEY_FILE, key.getEncoded(),
                        StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            } catch (IOException e) {
                throw new IllegalStateException("Could not save key");
            }
        }

        void receiveMessage(Message message) {
            String expectedMac = HmacAlgorithms.hmacSha256(message.content, getHmacKey());
            if (message.mac.equals(expectedMac)) {
                System.out.println("Received OK. Message: " + message.content);
            } else {
                System.out.println("Received NOK: Invalid message");
            }
        }

        static SecretKey getHmacKey() {
            try {
                byte[] keyFileContent = Files.readAllBytes(Paths.get("hmac.key"));
                return new SecretKeySpec(keyFileContent, HMAC_SHA_256);
            } catch (IOException e) {
                throw new IllegalStateException("Key file error");
            }
        }

        void deleteHmacKeyFile() {
            try {
                Files.delete(Paths.get("hmac.key"));
            } catch (IOException e) {
                // ignore
            }
        }
    }

    private static class Client {
        Message createMessage() {
            Message message = new Message();
            message.content = "test-message";
            message.mac = HmacAlgorithms.hmacSha256(message.content, Server.getHmacKey());
            return message;
        }
    }

    private static class Message {
        String content;
        String mac;
    }

    private static class HmacAlgorithms {

        private static String hmacSha256(String str, SecretKey key) {
            try {
                Mac macAlgorithm = Mac.getInstance(HMAC_SHA_256);
                macAlgorithm.init(key);

                byte[] macCode = macAlgorithm.doFinal(str.getBytes());

                return Base64.getEncoder().encodeToString(macCode);
            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                e.printStackTrace();
                throw new IllegalStateException("Error");
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.generateKey();

        Client client = new Client();
        Message message = client.createMessage();

        server.receiveMessage(message);

        server.deleteHmacKeyFile();
    }

}
