package cloud.autotests.helpers;

import com.codeborne.selenide.*;
import com.codeborne.selenide.impl.StaticConfig;
import com.codeborne.selenide.impl.StaticDriver;
import com.codeborne.selenide.impl.ThreadLocalSelenideDriver;

public class Page {

    // public static SelenideDriver defaultDriver = new ThreadLocalSelenideDriver();
    // OR:
    public static SelenideDriver defaultDriver = new SelenideDriver(new StaticConfig(), new StaticDriver());


    protected SelenideDriver driver;

    public Page() {
        this.driver = defaultDriver;
    }

    public Page open(String relativeOrAbsoluteUrl)
    {
        this.driver.open(relativeOrAbsoluteUrl);
        return this;
    }

    public SelenideElement $(String cssSelector){
        return this.driver.$(cssSelector);
    }

    public ElementsCollection $$(String cssSelector){
        return this.driver.$$(cssSelector);
    }

    public SelenideElement $x(String xpathSelector){
        return this.driver.$x(xpathSelector);
    }

    public ElementsCollection $$x(String xpathSelector){
        return this.driver.$$x(xpathSelector);
    }
}
