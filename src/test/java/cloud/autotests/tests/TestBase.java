package cloud.autotests.tests;

import cloud.autotests.helpers.AllureAttachments;
import cloud.autotests.helpers.BrowserSettings;
import cloud.autotests.helpers.DriverUtils;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.junit5.AllureJunit5;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import static cloud.autotests.helpers.AllureAttachments.*;
import static cloud.autotests.helpers.DriverHelper.isVideoOn;
import static com.codeborne.selenide.WebDriverRunner.closeWebDriver;

@ExtendWith({AllureJunit5.class})
public class TestBase {
    @BeforeAll
    static void setUp() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        BrowserSettings.configureDriver();
    }

    @AfterEach
    public void addAttachments() {
        String sessionId = DriverUtils.getRemoteSessionIdFromSelenoid();

        AllureAttachments.addScreenshotAs("Last screenshot");
        AllureAttachments.addPageSource();
//        AllureAttachments.attachNetwork(); // todo
        AllureAttachments.addBrowserConsoleLogs();

        Selenide.closeWebDriver();

        if (isVideoOn()) {
            AllureAttachments.addVideo(sessionId);
        }
    }
}
