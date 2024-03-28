package pl.lodz.p.it.ssbd2023.ssbd01.security;

import jakarta.security.enterprise.identitystore.PasswordHash;
import org.apache.commons.codec.digest.DigestUtils;

public class HashAlgorithmImpl implements PasswordHash {

  public static String generate(String password) {
    return DigestUtils.sha256Hex(password);
  }

  public String generate(char[] password) {
    return generate(new String(password));
  }

  public static boolean check(String password, String hashedPassword) {
    String toVerify = generate(password);
    return hashedPassword.equals(toVerify);
  }

  public boolean verify(char[] password, String hashedPassword) {
    return check(new String(password), hashedPassword);
  }
}
