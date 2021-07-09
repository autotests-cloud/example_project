package cloud.autotests.helpers;

import cloud.autotests.config.Project;
import com.codeborne.selenide.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;

public class DriverManager {

    public static SelenideConfig defaultConfig = newConfigWithDefaults();

    public static SelenideDriver newDriver() {
        return new SelenideDriver(newConfigWithDefaults());
    }

    public static SelenideConfig newConfigWithDefaults() {
        SelenideConfig instance = new SelenideConfig();
        instance.browser(Project.config.browser())
                .browserVersion(Project.config.browserVersion())
                .browserSize(Project.config.browserSize());

        DesiredCapabilities capabilities = new DesiredCapabilities();

        if (Project.isWebMobile()) { // for chrome only
            ChromeOptions chromeOptions = new ChromeOptions();
            Map<String, Object> mobileDevice = new HashMap<>();
            mobileDevice.put("deviceName", Project.config.browserMobileView());
            chromeOptions.setExperimentalOption("mobileEmulation", mobileDevice);
            capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        }

        if (Project.isRemoteWebDriver()) {
            capabilities.setCapability("enableVNC", true);
            capabilities.setCapability("enableVideo", true);
            instance.remote(Project.config.remoteDriverUrl());
        }

        instance.browserCapabilities(capabilities);

        return instance;
    }

    public static void configureDefaultDriver() {
        // TODO: this code is useless here, we can just move it to TestBase.setup()
        Configuration.browser = defaultConfig.browser();
        Configuration.browserVersion = defaultConfig.browserVersion();
        Configuration.browserSize = defaultConfig.browserSize();
        Configuration.remote = defaultConfig.remote();
        Configuration.browserCapabilities = defaultConfig.browserCapabilities();
    }
}
