package proud.collection.validation;

import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CompromisedPasswordValidator {
    public boolean isPasswordCompromised(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        String sha1 = sb.toString();
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.pwnedpasswords.com/range/" + sha1.substring(0, 5);
        String response = restTemplate.getForObject(url, String.class);
        if (response != null && response.contains(sha1.substring(5).toUpperCase())) {
            return true;
        }
        return false;
    }
}
