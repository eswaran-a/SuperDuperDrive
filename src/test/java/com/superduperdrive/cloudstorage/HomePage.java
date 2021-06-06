package com.superduperdrive.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    @FindBy(css="#logoutButton")
    private WebElement logoutButton;

    @FindBy(css="#nav-notes-tab")
    private WebElement notesTabLink;

    @FindBy(css="#nav-credentials-tab")
    private WebElement credTabLink;

    @FindBy(id = "addNoteButton")
    private WebElement addNoteButton;

    @FindBy(id = "addCredentialButton")
    private WebElement addCredButton;

    private WebDriver driver;
    private WebDriverWait wait;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        wait = new WebDriverWait(driver, 15, 2000);
    }

    public void logout() {
        this.logoutButton.click();
    }

    public void navigateToNotes() {
        this.notesTabLink.click();
    }
    public void navigateToCredential() {
        this.credTabLink.click();
    }
    public void clickAddNote() {
        wait.until(ExpectedConditions.elementToBeClickable(addNoteButton)).click();
    }
    public void clickAddCredential() {
        wait.until(ExpectedConditions.elementToBeClickable(addCredButton)).click();
    }

    public String getRecentlyAddedNote() {
        wait.until(ExpectedConditions.elementToBeClickable(notesTabLink)).click();
        By lastRow = new By.ByXPath("//*[@id=\"notesTable\"]/tbody/tr");
        return this.driver.findElement(lastRow).getText();
    }
    public String getRecentlyAddedCredential() {
        wait.until(ExpectedConditions.elementToBeClickable(credTabLink)).click();
        By lastRow = new By.ByXPath("//*[@id=\"credentialTable\"]/tbody/tr");
        return this.driver.findElement(lastRow).getText();
    }

    public boolean isNotesTableEmpty() {
        wait.until(ExpectedConditions.elementToBeClickable(notesTabLink)).click();
        By lastRow = new By.ByXPath("//*[@id=\"notesTable\"]/tbody/tr");
        return this.driver.findElements(lastRow).isEmpty();
    }
    public boolean isCredentialTableEmpty() {
        wait.until(ExpectedConditions.elementToBeClickable(credTabLink)).click();
        By lastRow = new By.ByXPath("//*[@id=\"credentialTable\"]/tbody/tr");
        return this.driver.findElements(lastRow).isEmpty();
    }

    public void clickEditNote() {
        wait.until(ExpectedConditions.elementToBeClickable(notesTabLink)).click();
        By editButton = new By.ByXPath("//*[@id=\"notesTable\"]/tbody/tr/td[1]/button");
        wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();
    }
    public void clickEditCredential() {
        wait.until(ExpectedConditions.elementToBeClickable(credTabLink)).click();
        By editButton = new By.ByXPath("//*[@id=\"credentialTable\"]/tbody/tr/td[1]/button");
        wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();
    }

    public void clickDeleteNote() {
        wait.until(ExpectedConditions.elementToBeClickable(notesTabLink)).click();
        By deleteButton = new By.ByXPath("//*[@id=\"notesTable\"]/tbody/tr/td[1]/a");
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
    }
    public void clickDeleteCredential() {
        wait.until(ExpectedConditions.elementToBeClickable(credTabLink)).click();
        By deleteButton = new By.ByXPath("//*[@id=\"credentialTable\"]/tbody/tr/td[1]/a");
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
    }
}
