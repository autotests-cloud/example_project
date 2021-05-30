package cloud.autotests.config;

import org.aeonbits.owner.ConfigFactory;

public class Project {
    public static ProjectConfig config = ConfigFactory.create(ProjectConfig.class, System.getProperties());
}
