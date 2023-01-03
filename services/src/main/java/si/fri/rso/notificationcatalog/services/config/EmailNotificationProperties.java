package si.fri.rso.notificationcatalog.services.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("email-notification")
public class EmailNotificationProperties {

    @ConfigValue(watch = true)
    private String emailNotificationHost;

    @ConfigValue(watch = true)
    private String emailNotificationApiKey;

    public String getEmailNotificationHost() {
        return emailNotificationHost;
    }

    public void setEmailNotificationHost(String emailNotificationHost) {
        this.emailNotificationHost = emailNotificationHost;
    }

    public String getEmailNotificationApiKey() {
        return emailNotificationApiKey;
    }

    public void setEmailNotificationApiKey(String emailNotificationApiKey) {
        this.emailNotificationApiKey = emailNotificationApiKey;
    }
}
