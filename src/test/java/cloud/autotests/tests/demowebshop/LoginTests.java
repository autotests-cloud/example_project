package cloud.autotests.tests.demowebshop;

import cloud.autotests.config.App;
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

    @BeforeAll
    static void configureBaseUrl() {
        RestAssured.baseURI = App.config.apiUrl();
        Configuration.baseUrl = App.config.webUrl();
    }

    @Test
    @Tag("regress")
    @Disabled("Example test code for further test development")
    @DisplayName("Successful authorization to some demowebshop")
    void loginTest() {
        step("Open login page", () -> {
            open("/login");
            $(".page-title").shouldHave(text("Welcome, Please Sign In!"));
        });

        step("Fill login form", () -> {
            $("#Email").setValue(App.config.userLogin());
            $("#Password").setValue(App.config.userPassword()).pressEnter();
        });

        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(App.config.userLogin())));
    }

    @Test
    @Tag("api")
    @Disabled("Example test code for further test development")
    @DisplayName("Successful authorization with set cookie, received by API")
    void loginWithCookieTest() {
        step("Get cookie by api and set it to browser", () -> {
            String authorizationCookie =
                    given()
                            .filter(AllureRestAssuredFilter.withCustomTemplates())
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .formParam("Email", App.config.userLogin())
                            .formParam("Password", App.config.userPassword())
                            .when()
                            .post("/login")
                            .then()
                            .statusCode(302)
                            .extract()
                            .cookie("NOPCOMMERCE.AUTH");

            step("Open minimal content, because cookie can be set with site opened", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));

            getWebDriver().manage().addCookie(new Cookie("NOPCOMMERCE.AUTH", authorizationCookie));
        });

        step("Open main page", () -> {
            open("");
            $(".topic-html-content-header").shouldHave(text("Welcome to our store"));
        });

        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(App.config.userLogin())));
    }
}
