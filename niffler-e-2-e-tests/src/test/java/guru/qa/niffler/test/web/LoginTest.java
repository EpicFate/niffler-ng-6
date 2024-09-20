package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class LoginTest {

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        Selenide.open(Config.getInstance().frontUrl(), LoginPage.class)
                .login("epic", "123")
                .checkSpendingTitleIsVisible();
    }

//    @Test
//    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
//
//    }
}
