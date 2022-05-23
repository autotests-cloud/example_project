package cloud.autotests.tests.demowebshop;

import cloud.autotests.config.demowebshop.App;
import cloud.autotests.helpers.AllureRestAssuredFilter;
import cloud.autotests.tests.TestBase;
import com.codeborne.selenide.Configuration;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

@Story("Login tests")
public class LoginTests extends TestBase {

    static String login,
            password;

    @BeforeAll
    static void configureBaseUrl() {
        RestAssured.baseURI = App.config.apiUrl();
        Configuration.baseUrl = App.config.webUrl();

        login = App.config.userLogin();
        password = App.config.userPassword();
    }

    @Test
    @Tag("demowebshop")
    @Disabled("Example test code for further test development")
    @DisplayName("Successful authorization to some demowebshop (UI)")
    void loginTest() {
        step("Open login page", () ->
                open("/login"));

        step("Fill login form", () -> {
            $("#Email").setValue(login);
            $("#Password").setValue(password)
                    .pressEnter();
        });

        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(login)));
    }

    @Test
    @Tag("demowebshop")
    @Disabled("Example test code for further test development")
    @DisplayName("Successful authorization to some demowebshop (API + UI)")
    void loginWithCookieTest() {
        step("Get cookie by api and set it to browser", () -> {
            String authorizationCookie =
                    given()
                            .filter(AllureRestAssuredFilter.withCustomTemplates())
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .formParam("Email", login)
                            .formParam("Password", password)
                            .when()
                            .post("/login")
                            .then()
                            .statusCode(302)
                            .extract()
                            .cookie("NOPCOMMERCE.AUTH");

            step("Open minimal content, because cookie can be set when site is opened", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));

            step("Set cookie to to browser", () ->
                    getWebDriver().manage().addCookie(
                            new Cookie("NOPCOMMERCE.AUTH", authorizationCookie)));
        });

        step("Open main page", () ->
                open(""));

        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(login)));
    }
}
