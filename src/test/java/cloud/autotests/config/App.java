package cloud.autotests.config;

import org.aeonbits.owner.ConfigFactory;

public class App {
    public static AppConfig config = ConfigFactory.create(AppConfig.class, System.getProperties());
}
