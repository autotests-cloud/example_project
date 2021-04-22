package cloud.autotests.helpers;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;


public class AttachmentsHelper {
    private static final Logger LOG = LoggerFactory.getLogger(AttachmentsHelper.class);


    @Attachment(value = "{attachName}", type = "text/plain")
    public static String attachAsText(String attachName, String message) {
        return message;
    }

    @Attachment(value = "{attachName}", type = "image/png")
    public static byte[] attachScreenshot(String attachName) {
        return ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Page source", type = "text/html")
    public static byte[] attachPageSource() {
        return getWebDriver().getPageSource().getBytes(StandardCharsets.UTF_8);
    }

    public static void attachVideo(String sessionId) {
        try {
            URL videoUrl = new URL(DriverHelper.getVideoUrl() + sessionId + ".mp4");
            InputStream is = null;
            Thread.sleep(1000);
            for (int i = 0; i < 10; i++) {
                try {
                    is = videoUrl.openStream();
                    i = 10;
                } catch (FileNotFoundException e) {
                    Thread.sleep(1000);
                }
            }
            Allure.addAttachment("Video", "video/mp4", is, "mp4");
        } catch (Exception e) {
            System.out.println("attachAllureVideo");
            e.printStackTrace();
        }
    }
}
