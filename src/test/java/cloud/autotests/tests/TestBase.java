package cloud.autotests.tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import static com.codeborne.selenide.WebDriverRunner.closeWebDriver;
import static cloud.autotests.helpers.AttachmentsHelper.*;
import static cloud.autotests.helpers.AttachmentsHelper.attachVideo;
import static cloud.autotests.helpers.DriverHelper.*;


public class TestBase {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = TestData.getApiUrl();
        configureDriver();
    }

    @AfterEach
    public void addAttachments(){
        String sessionId = getSessionId();

        attachScreenshot("Last screenshot");
        attachPageSource();
//        attachNetwork(); // todo
        attachAsText("Browser console logs", getConsoleLogs());
        if (isVideoOn()) attachVideo(sessionId);

        closeWebDriver();
    }
}
