package fr.azures.bawab.services;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.azures.bawab.Constants;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

public class Yggdrasil {

    public static PublicKey MINECRAFT_PUBLIC_KEY;
    public static HashMap<String, String> serverIds = new HashMap<String, String>();
    private static final Gson gson = new Gson();

    public Yggdrasil() throws Exception {
        MINECRAFT_PUBLIC_KEY = Yggdrasil.getMinecraftPublicKey();
    }

    public static SecretKey generateSharedSecret() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[16];
        secureRandom.nextBytes(randomBytes);

        return new SecretKeySpec(randomBytes, "AES");
    }

    public static String generateServerId(String baseServerId, PublicKey publicKey, SecretKey secretKey) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        messageDigest.update(baseServerId.getBytes("ISO_8859_1"));
        messageDigest.update(secretKey.getEncoded());
        messageDigest.update(publicKey.getEncoded());
        byte[] digestData = messageDigest.digest();
        return new BigInteger(digestData).toString(16);
    }

    public static PublicKey getMinecraftPublicKey() throws Exception {
        URL url = new URL(Constants.MINECRAFT_SERVICES + "/publickeys");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setRequestMethod("GET");
        request.connect();

        if (request.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed to fetch public keys: " + request.getResponseCode());
        }

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(new InputStreamReader(request.getInputStream()));
        JsonObject object = element.getAsJsonObject();

        String publicKeyBase64 = object
                .getAsJsonArray("authenticationKeys")
                .get(0).getAsJsonObject()
                .get("publicKey").getAsString();

        byte[] decoded = Base64.getDecoder().decode(publicKeyBase64);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public static boolean joinServer(String accessToken, UUID selectedProfile, String serverId) throws Exception {
        URL url = new URL(Constants.SESSION_SERVER + "/session/minecraft/join");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setDoOutput(true);
        request.setRequestMethod("POST");
        request.setRequestProperty("Content-Type", "application/json; utf-8");
        request.setRequestProperty("Accept", "application/json");

        JsonObject joinPayload = new JsonObject();
        joinPayload.addProperty("accessToken", accessToken);
        joinPayload.addProperty("selectedProfile", selectedProfile.toString().replace("-", ""));
        joinPayload.addProperty("serverId", serverId);

        String jsonInput = gson.toJson(joinPayload);

        try (OutputStream os = request.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        if (request.getResponseCode() != 204) {
            throw new RuntimeException("Failed to join server: " + request.getResponseCode());
        }
        return true;
    }

    public static boolean hasJoined(String username, String serverId) throws Exception {
        URL url = new URL(Constants.SESSION_SERVER + "/session/minecraft/hasJoined?username=" + username + "&serverId=" + serverId);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setRequestMethod("GET");
        request.connect();

        if (request.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed to hasJoined server: " + request.getResponseCode());
        }
        System.out.println(request.getResponseCode());
        if (request.getResponseCode() == 200) {
            return true;
        } else {
            return false;
        }
    }

}
