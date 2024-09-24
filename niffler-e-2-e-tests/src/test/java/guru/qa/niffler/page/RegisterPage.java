package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement paragraphSuccessForm = $(".form__paragraph_success");
    private final SelenideElement proceedLoginButton = $(".form_sign-in");
    private final SelenideElement errorMassage = $("span[class='form__error']");

    public RegisterPage setPasswordInput(String password) {
        passwordInput.setValue(password);
        return new RegisterPage();
    }

    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.setValue(password);
        return new RegisterPage();
    }

    public RegisterPage setUsernameInput(String username) {
        usernameInput.setValue(username);
        return new RegisterPage();
    }

    public RegisterPage clickSubmitButton() {
        submitButton.click();
        return new RegisterPage();
    }

    public RegisterPage createNewUser(String username, String password) {
        setUsernameInput(username);
        setPasswordInput(password);
        setPasswordSubmit(password);
        clickSubmitButton();
        return new RegisterPage();
    }

    public RegisterPage checkParagraphSuccessForm() {
        paragraphSuccessForm.shouldHave(exactText("Congratulations! You've registered!"));
        return new RegisterPage();
    }

    public RegisterPage checkErrorMassage(String expectedErrorMassage) {
        errorMassage.shouldHave(exactText(expectedErrorMassage));
        return new RegisterPage();
    }



    public RegisterPage fillRegisterPage(String login, String password, String passwordSubmit) {
        usernameInput.setValue(login);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(passwordSubmit);
        return this;
    }

    public LoginPage successSubmit() {
        submit();
        proceedLoginButton.click();
        return new LoginPage();
    }

    public void submit() {
        submitButton.click();
    }

    public RegisterPage checkAlertMessage(String errorMessage) {
        errorMassage.shouldHave(text(errorMessage));
        return this;
    }
}
