package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.common.CategoryButtons;
import guru.qa.niffler.common.ModalButtons;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static guru.qa.niffler.common.CategoryButtons.ARCHIVE_CATEGORY;
import static guru.qa.niffler.common.CategoryButtons.UNARCHIVE_CATEGORY;
import static guru.qa.niffler.common.ModalButtons.ARCHIVE;
import static guru.qa.niffler.common.ModalButtons.UNARCHIVE;

public class ProfilePage {

    private final SelenideElement newSpendingButton = $("a[href='/spending']");
    private final SelenideElement uploadImageInput = $("input#image__input");
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement saveChangesButton = $("button[type='submit']");
    private final SelenideElement archiveCheckbox = $x(
            "//*[text()='Show archived']//preceding::input[@type='checkbox']");
    private final SelenideElement addNewCategoryInput = $("#category");
    private final ElementsCollection categories = $$x(
            "//*[@aria-label]//ancestor::div[contains(@class, 'MuiGrid-root MuiGrid-item')]");
    private final SelenideElement dialogModal = $("[role=dialog]");
    private final SelenideElement alertModal = $("[role='alert']");
    private final ElementsCollection bubblesArchived = $$(".MuiChip-filled.MuiChip-colorDefault");
    private final ElementsCollection bubbles = $$(".MuiChip-filled.MuiChip-colorPrimary");

    private By categoryButton(CategoryButtons button) {
        return By.cssSelector("button[aria-label='%s']".formatted(button.getAriaLabel()));
    }

    private By modalButton(ModalButtons button) {
        return By.xpath(".//button[text()='%s']".formatted(button.getButtonName()));
    }

//    private SelenideElement categoryByName(String categoryName) {
//        return $x("//*[text()='%s']//ancestor::div[contains(@class, 'MuiGrid-root MuiGrid-item')]"
//                .formatted(categoryName));
//    }

    public ProfilePage activateShowArchivedCheckbox() {
        if(!archiveCheckbox.isSelected())
            archiveCheckbox.click();
        return new ProfilePage();
    }

    public ProfilePage deactivateShowArchivedCheckbox() {
        if(archiveCheckbox.isSelected())
            archiveCheckbox.click();
        return new ProfilePage();
    }

    public ProfilePage archiveCategoryAndAcceptModal(String categoryName) {
        clickCategoryButtonAndAcceptModal(categoryName, ARCHIVE_CATEGORY, ARCHIVE);
        return new ProfilePage();
    }

    public ProfilePage unarchivedCategoryAndAcceptModal(String categoryName) {
        clickCategoryButtonAndAcceptModal(categoryName, UNARCHIVE_CATEGORY, UNARCHIVE);
        return new ProfilePage();
    }

    public ProfilePage clickCategoryButtonAndAcceptModal(String categoryName, CategoryButtons categoryButton, ModalButtons modalButton) {
        SelenideElement category = categories.find(exactText(categoryName));
        category.$(categoryButton(categoryButton))
                .click();
        dialogModal.shouldBe(visible)
                .$(modalButton(modalButton))
                .click();
        dialogModal.shouldNotBe(visible);
        return new ProfilePage();
    }

    public ProfilePage checkAlertModal(String expectedMassage) {
        alertModal
                .shouldBe(visible)
                .shouldHave(exactText(expectedMassage));
        return new ProfilePage();
    }

    public ProfilePage checkArchivedCategoryExists(String category) {
        activateShowArchivedCheckbox();
        bubblesArchived.find(text(category)).shouldBe(visible);
        return this;
    }

    public ProfilePage checkCategoryExists(String category) {
        bubbles.find(text(category)).shouldBe(visible);
        return this;
    }


}
