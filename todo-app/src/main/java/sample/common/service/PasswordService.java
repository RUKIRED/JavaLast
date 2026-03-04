package sample.common.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
  private final String hashAlgorithm;

  public PasswordService(@Value("${password.hash.code:SHA-256}") String hashAlgorithm) {
    this.hashAlgorithm = hashAlgorithm;
  }

  public String hash(String raw) {
    try {
      MessageDigest digest = MessageDigest.getInstance(hashAlgorithm);
      byte[] encoded = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
      StringBuilder sb = new StringBuilder();
      for (byte b : encoded) {
        sb.append(String.format("%02x", b));
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("Unsupported hash algorithm: " + hashAlgorithm, e);
    }
  }
}
