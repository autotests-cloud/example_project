package cloud.autotests.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/app.properties"
})
public interface AppConfig extends Config {

    @Key("web.url")
    String webUrl();

    @Key("api.url")
    String apiUrl();

    @Key("user.login")
    String userLogin();

    @Key("user.password")
    String userPassword();

}
