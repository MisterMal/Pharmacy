package pl.lodz.p.it.ssbd2023.ssbd01.config;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import lombok.extern.java.Log;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import pl.lodz.p.it.ssbd2023.ssbd01.common.SignableEntity;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.TrackerInterceptor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Properties;


@Log
public class EntityIdentitySignerVerifier {

  @Inject
  @ConfigProperty(name = "etag.secret")
  private String ETAG_SECRET;

  public void checkEtagIntegrity(SignableEntity entity, String etag) {
    if (!verifyEntityIntegrity(entity, etag)) {
      throw ApplicationException.createEtagNotValidException();
    }
  }

  public String calculateEntitySignature(SignableEntity entity) {
    try {
      JWSSigner signer = new MACSigner(ETAG_SECRET);

      JWSObject jwsObject =
          new JWSObject(
              new JWSHeader(JWSAlgorithm.HS256), new Payload(entity.getSignablePayload()));
      jwsObject.sign(signer);
      return jwsObject.serialize();
    } catch (JOSEException e) {
      throw ApplicationException.createEtagCreationException();
    }
  }

  public boolean validateEntitySignature(String tag) {
    try {
      JWSObject jwsObject = JWSObject.parse(tag);
      JWSVerifier verifier = new MACVerifier(ETAG_SECRET);
      return jwsObject.verify(verifier);
    } catch (ParseException | JOSEException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean verifyEntityIntegrity(SignableEntity entity, String tag) {
    try {
      final String header = JWSObject.parse(tag).getPayload().toString();
      final String signablePayload = entity.getSignablePayload();
      return validateEntitySignature(tag) && signablePayload.equals(header);
    } catch (ParseException e) {
      e.printStackTrace();
      return false;
    }
  }
}
