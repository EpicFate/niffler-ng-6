package guru.qa.niffler.test.web.registerTests;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class UnsuccessfulRegisterTests {

    @Test
    public void wrongUsername() {
        final String expectedMassagePatten = "Username `%s` already exists";
        final String username = "epic";
        final String password = "123";

        Selenide.open(Config.getInstance().frontUrl(), LoginPage.class)
                .clickCreateNewAccount()
                .createNewUser(username, password)
                .checkErrorMassage(expectedMassagePatten.formatted(username));
    }

    @Test
    public void wrongPassword() {
        final String expectedMassage = "Passwords should be equal";
        final String username = "epic";
        final String password = "123";
        final String passwordSubmit = "321";

        Selenide.open(Config.getInstance().frontUrl(), LoginPage.class)
                .clickCreateNewAccount()
                .setUsernameInput(username)
                .setPasswordInput(password)
                .setPasswordSubmit(passwordSubmit)
                .clickSubmitButton()
                .checkErrorMassage(expectedMassage);
    }
}