package pl.lodz.p.it.ssbd2023.ssbd01.util.email;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import jakarta.inject.Inject;
import java.util.Locale;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.lodz.p.it.ssbd2023.ssbd01.common.i18n;

public class EmailService {

  @Inject
  @ConfigProperty(name = "unconfirmed.account.deletion.timeout.hours")
  private int UNCONFIRMED_ACCOUNT_DELETION_TIMEOUT_HOURS;

  @Inject
  @ConfigProperty(name = "email.address")
  private String EMAIL_ADDRESS;

  @Inject
  @ConfigProperty(name = "server.url")
  private String BASE_URL;

  private ClientOptions options;

  private MailjetClient client;

  public EmailService() {
    options =
        ClientOptions.builder()
            .apiKey(ConfigProvider.getConfig().getValue("mailjet.key", String.class))
            .apiSecretKey(ConfigProvider.getConfig().getValue("mailjet.secret", String.class))
            .build();

    client = new MailjetClient(options);
  }

  public void sendEmailWhenRemovedDueToNotConfirmed(String email, String name, Locale locale) {
    String subject = i18n.getMessage(i18n.MAIL_ACCOUNT_REMOVED_SUBJECT, locale);
    String body = i18n.getMessage(i18n.MAIL_ACCOUNT_REMOVED_BODY, locale, name);

    MailjetRequest request = getMailjetRequest(email, name, subject, body);
    try {
      client.post(request);
    } catch (MailjetException e) {
      e.printStackTrace();
    }
  }

  public void sendEmailAccountBlocked(String email, String name, Locale locale) {
    String subject = i18n.getMessage(i18n.MAIL_ACCOUNT_BLOCKED_SUBJECT, locale);
    String body = i18n.getMessage(i18n.MAIL_ACCOUNT_BLOCKED_BODY, locale, name);

    MailjetRequest request = getMailjetRequest(email, name, subject, body);
    try {
      client.post(request);
    } catch (MailjetException e) {
      e.printStackTrace();
    }
  }

  public void sendEmailAccountUnblocked(String email, String name, Locale locale) {
    String subject = i18n.getMessage(i18n.MAIL_ACCOUNT_UNBLOCKED_SUBJECT, locale);
    String body = i18n.getMessage(i18n.MAIL_ACCOUNT_UNBLOCKED_BODY, locale, name);

    MailjetRequest request = getMailjetRequest(email, name, subject, body);
    try {
      client.post(request);
    } catch (MailjetException e) {
      e.printStackTrace();
    }
  }

  public void sendEmailAccountBlockedTooManyLogins(String email, String name, Locale locale) {
    String subject = i18n.getMessage(i18n.MAIL_ACCOUNT_BLOCKED_SUBJECT, locale);
    String body = i18n.getMessage(i18n.MAIL_ACCOUNT_BLOCKED_TOO_MANY_LOGINS_BODY, locale, name);

    MailjetRequest request = getMailjetRequest(email, name, subject, body);
    try {
      client.post(request);
    } catch (MailjetException e) {
      e.printStackTrace();
    }
  }

  public void sendEmailResetPassword(String email, String name, Locale locale, String token) {
    String subject = i18n.getMessage(i18n.MAIL_PASSWORD_RESET_SUBJECT, locale);
    String link = BASE_URL + "/new-password/" + token;
    String body = i18n.getMessage(i18n.MAIL_PASSWORD_RESET_BODY, locale, name, link);

    MailjetRequest request = getMailjetRequest(email, name, subject, body);
    try {
      client.post(request);
    } catch (MailjetException e) {
      e.printStackTrace();
    }
  }

  public void sendEmailAccountActivated(String email, String name, Locale locale) {
    String subject = i18n.getMessage(i18n.MAIL_ACCOUNT_ACTIVATED_SUBJECT, locale);
    String body = i18n.getMessage(i18n.MAIL_ACCOUNT_ACTIVATED_BODY, locale, name);

    MailjetRequest request = getMailjetRequest(email, name, subject, body);
    try {
      client.post(request);
    } catch (MailjetException e) {
      e.printStackTrace();
    }
  }

  public void sendRegistrationEmail(String email, String name, Locale locale, String token) {
    String subject = i18n.getMessage(i18n.MAIL_ACCOUNT_REGISTER_SUBJECT, locale);
    String link = BASE_URL + "/confirm-account/" + token;
    String body = i18n.getMessage(i18n.MAIL_ACCOUNT_REGISTER_BODY, locale, name, link);

    MailjetRequest request = getMailjetRequest(email, name, subject, body);
    try {
      client.post(request);
    } catch (MailjetException e) {
      e.printStackTrace();
    }
  }

  public void sendEmailChangeEmail(String email, String name, Locale locale, String token) {
    String subject = i18n.getMessage(i18n.MAIL_CHANGE_EMAIL_SUBJECT, locale);
    String link = BASE_URL + "/confirm-account/" + token;
    String body = i18n.getMessage(i18n.MAIL_CHANGE_EMAIL_BODY, locale, name, link);

    MailjetRequest request = getMailjetRequest(email, name, subject, body);
    try {
      client.post(request);
    } catch (MailjetException e) {
      e.printStackTrace();
    }
  }

  private MailjetRequest getMailjetRequest(
      String email, String recipientName, String subject, String body) {
    return new MailjetRequest(Emailv31.resource)
        .property(
            Emailv31.MESSAGES,
            new JSONArray()
                .put(
                    new JSONObject()
                        .put(Emailv31.Message.FROM, new JSONObject().put("Email", EMAIL_ADDRESS))
                        .put(
                            Emailv31.Message.TO,
                            new JSONArray()
                                .put(
                                    new JSONObject()
                                        .put("Email", email)
                                        .put("Name", recipientName)))
                        .put(Emailv31.Message.SUBJECT, subject)
                        .put(Emailv31.Message.TEXTPART, body)));
  }
}
