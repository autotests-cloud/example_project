package cloud.autotests.helpers;

import cloud.autotests.config.Project;
import com.codeborne.selenide.Selenide;

import static org.openqa.selenium.logging.LogType.BROWSER;

public class DriverHelper {

    public static boolean isWebMobile() {
        return !Project.config.browserMobileView().equals("");
    }

    public static String getWebRemoteDriver() {
        // https://%s:%s@selenoid.autotests.cloud/wd/hub/
        return String.format(
                Project.config.remoteDriverUrl(),
                Project.config.remoteDriverUser(),
                Project.config.remoteDriverPassword()
        );
    }

    public static boolean isRemoteWebDriver() {
        return !Project.config.remoteDriverUrl().equals("");
    }

    public static boolean isVideoOn() {
        return !Project.config.videoStorage().equals("");
    }

    public static String getConsoleLogs() { // todo refactorfi
        return String.join("\n", Selenide.getWebDriverLogs(BROWSER));
    }
}
