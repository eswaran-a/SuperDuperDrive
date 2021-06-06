package com.superduperdrive.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CredentialsModal {
    @FindBy(css="#credential-url")
    private WebElement credUrlInput;

    @FindBy(css="#credential-username")
    private WebElement credUsernameInput;

    @FindBy(css="#credential-password")
    private WebElement credPasswordInput;

    @FindBy(css="#save-credential")
    private WebElement credSubmitButton;

    private WebDriverWait wait;

    public CredentialsModal(WebDriver driver) {
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 15, 1000);
    }

    public void addCredential(String url, String username, String password) {
        wait.until(ExpectedConditions.elementToBeClickable(credSubmitButton));
        credUrlInput.sendKeys(url);
        credUsernameInput.sendKeys(username);
        credPasswordInput.sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(credSubmitButton)).click();
    }

    public void editCredential(String url, String username, String password) {
        wait.until(ExpectedConditions.elementToBeClickable(credSubmitButton));
        credUrlInput.clear();
        credUrlInput.sendKeys(url);
        credUsernameInput.clear();
        credUsernameInput.sendKeys(username);
        credPasswordInput.clear();
        credPasswordInput.sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(credSubmitButton)).click();
    }

    public String getPasswordContent() {
        wait.until(ExpectedConditions.elementToBeClickable(credPasswordInput));
        return credPasswordInput.getText();
    }
}
