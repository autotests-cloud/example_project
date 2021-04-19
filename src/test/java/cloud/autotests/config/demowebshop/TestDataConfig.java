package cloud.autotests.config.demowebshop;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/demowebshop/testdata.properties"
})
public interface TestDataConfig extends Config {
    @Key("web.url")
    String webUrl();

    @Key("api.url")
    String apiUrl();

    @Key("user.login")
    String userLogin();

    @Key("user.password")
    String userPassword();

}
