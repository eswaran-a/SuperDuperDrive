package com.superduperdrive.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NotesModal {
    @FindBy(css="#note-title")
    private WebElement noteTitleInput;

    @FindBy(css="#note-description")
    private WebElement noteDescriptionInput;

    @FindBy(css="#save-note")
    private WebElement noteSubmitButton;

    private WebDriverWait wait;

    public NotesModal(WebDriver driver) {
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 15, 1000);
    }

    public void addNote(String noteTitle, String noteDescription) {
        wait.until(ExpectedConditions.elementToBeClickable(noteSubmitButton));
        noteTitleInput.sendKeys(noteTitle);
        noteDescriptionInput.sendKeys(noteDescription);
        wait.until(ExpectedConditions.elementToBeClickable(noteSubmitButton)).click();
    }

    public void editNote(String noteTitle, String noteDescription) {
        wait.until(ExpectedConditions.elementToBeClickable(noteSubmitButton));
        noteTitleInput.clear();
        noteTitleInput.sendKeys(noteTitle);
        noteDescriptionInput.clear();
        noteDescriptionInput.sendKeys(noteDescription);
        wait.until(ExpectedConditions.elementToBeClickable(noteSubmitButton)).click();
    }
}
