package cloud.autotests.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/local_driver.properties",
        "classpath:config/remote_driver.properties"
})
public interface DriverConfig extends Config {

    @Key("web.browser")
    String webBrowser();

    @Key("web.browser.version")
    String webBrowserVersion();

    @Key("web.browser.size")
    String webBrowserSize();

    @Key("web.browser.mobile.view")
    String webBrowserMobileView();

    @Key("web.remote.driver.url")
    String webRemoteDriverUrl();

    @Key("web.remote.driver.user")
    String webRemoteDriverUser();

    @Key("web.remote.driver.password")
    String webRemoteDriverPassword();

    @Key("video.storage")
    String videoStorage();
}
