package cloud.autotests.helpers;

import cloud.autotests.config.Project;
import com.codeborne.selenide.Selenide;

import static org.openqa.selenium.logging.LogType.BROWSER;

public class DriverHelper {

    public static boolean isWebMobile() {
        return !Project.config.webBrowserMobileView().equals("");
    }

    public static String getWebRemoteDriver() {
        // https://%s:%s@selenoid.autotests.cloud/wd/hub/
        return String.format(
                Project.config.webRemoteDriverUrl(),
                Project.config.webRemoteDriverUser(),
                Project.config.webRemoteDriverPassword()
        );
    }

    public static boolean isRemoteWebDriver() {
        return !Project.config.webRemoteDriverUrl().equals("");
    }

    public static boolean isVideoOn() {
        return !Project.config.videoStorage().equals("");
    }

    public static String getConsoleLogs() { // todo refactor
        return String.join("\n", Selenide.getWebDriverLogs(BROWSER));
    }
}
