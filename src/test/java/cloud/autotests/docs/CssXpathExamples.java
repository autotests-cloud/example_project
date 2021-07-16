package cloud.autotests.docs;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

/*
    Locators examples
    Author: https://github.com/svasenkov
 */

public class CssXpathExamples {

    void cssXpathExamples() {

        // <input type="email" class="inputtext login_form_input_box" name="email" id="email" data-testid="royal_email">

        $("[data-testid=royal_email]");
        $(by("data-testid", "royal_email"));


        // <input type="email" class="inputtext login_form_input_box" name="email" id="email">

        $("#email");
        $(byId("email"));
        $(By.id("email"));
        $("[id=email]");
        $("[id='email']");
        $("[id=\"email\"]");
        $("input[id=email]");
        $("input#email");
        $(by("id", "email"));
        $x("//*[@id='email']");
        $x("//input[@id='email']");
        $(byXpath("//input[@id='email']"));


        // <input type="email" class="inputtext login_form_input_box" name="email">

        $("[name='email']");
        $("input[name='email']");
        $(by("name", "email"));
        $(byName("email"));


        // <input type="email" class="inputtext login_form_input_box">

        $(byClassName("login_form_input_box"));
        $(".login_form_input_box");
        $(".inputtext.login_form_input_box");
        $("input.inputtext.login_form_input_box");
        $x("//*[@class='login_form_input_box']");

        // <div type="email" class="inputtext">
        //      <input class="login_form_input_box">
        // </div>
        $("input.inputtext .login_form_input_box");

    }
}