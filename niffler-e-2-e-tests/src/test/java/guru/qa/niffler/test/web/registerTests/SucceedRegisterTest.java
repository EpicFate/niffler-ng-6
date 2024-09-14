package guru.qa.niffler.test.web.registerTests;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class SucceedRegisterTest {

    @Test
    public void checkSucceedRegister() {
        final String userName = new Faker().name().username();
        final String password = "123";

        Selenide.open(Config.getInstance().frontUrl(), LoginPage.class)
                .clickCreateNewAccount()
                .createNewUser(userName, password)
                .checkParagraphSuccessForm();
    }
}