package cloud.autotests.tests.demomultibrowser;

import cloud.autotests.helpers.DriverManager;
import cloud.autotests.helpers.Page;
import cloud.autotests.tests.TestBase;
import com.codeborne.selenide.SelenideDriver;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;

public class TodoMvcTest extends TestBase {
    // TODO: the code below should be yet verified to work correctly in parallel mode

    @Nested
    class StraightForwardStyle {

        @Test
        void storageIsResetForNewUserInNewBrowser(){
            open("https://todomvc.com/examples/emberjs/");
            $("#new-todo").setValue("a").pressEnter();
            $("#new-todo").setValue("b").pressEnter();
            $("#new-todo").setValue("c").pressEnter();
            $$("#todo-list>li").shouldHave(texts("a", "b", "c"));

            // SelenideDriver otherUser = new SelenideDriver(DriverManager.newConfigWithDefaults());
            // OR:
            SelenideDriver otherUser = DriverManager.newDriver();
            otherUser.open("https://todomvc.com/examples/emberjs/");

            otherUser.$$("#todo-list>li").shouldHave(size(0));
        }
    }

    @Nested
    class PageObjectsStyle {

        // the `extends Page` part is not mandatory
        // for the page that is not planned for multi-browser support
        // and added here "just in case"
        class TodoMvcPage extends Page {

            // if page is not planned for multi-browser usage,
            // no need to define constructor for explicit driver

            public void visit() {
                open("https://todomvc.com/");
            }

            // some other methods...
        }

        class TodoMvcEmberJsPage extends Page {

            public TodoMvcEmberJsPage() {
                super();
            }

            public TodoMvcEmberJsPage(SelenideDriver driver) {
                this.driver = driver;
            }

            public void visit() {
                open("https://todomvc.com/examples/emberjs/");
            }

            public void add(String... todos) {
                for(String todo: todos) {
                    $("#new-todo").setValue(todo).pressEnter();
                }
            }

            public void shouldHave(String... todos) {
                $$("#todo-list>li").shouldHave(texts(todos));
            }

            public void shouldHaveAmount(int todos) {
                $$("#todo-list>li").shouldHave(size(todos));
            }
        }

        @Test
        void storageIsResetForNewUserInNewBrowser(){
            TodoMvcEmberJsPage todos = new TodoMvcEmberJsPage();
            todos.visit();
            todos.add("a", "b", "c");
            todos.shouldHave("a", "b", "c");

            TodoMvcEmberJsPage otherUser = new TodoMvcEmberJsPage(DriverManager.newDriver());
            otherUser.visit();

            otherUser.shouldHaveAmount(0);
        }
    }
}
