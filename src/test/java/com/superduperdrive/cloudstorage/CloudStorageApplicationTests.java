package com.superduperdrive.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private static WebDriver driver;

	public String baseURL;
	public String username;
	public String password;
	public String firstname;
	public String lastname;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		driver = new ChromeDriver();
		baseURL = baseURL = "http://localhost:" + port;

		Random rand = new Random();
		username = "username" + rand.nextInt();
		password = "password" + rand.nextInt();
		firstname = "firstname" + rand.nextInt();
		lastname = "lastname" + rand.nextInt();
	}

	@AfterEach
	public void afterAll() {
		driver.quit();
		driver = null;
	}

	private void assertLoginPage(WebDriver driver) {
		assertEquals(baseURL + "/login", driver.getCurrentUrl());
		assertEquals("Login", driver.getTitle());
	}

	private void signUp() {
		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup(firstname, lastname, username, password);
	}

	private void login() {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);
	}

	private void addNote(String noteTitle, String noteDesc) {

		HomePage homePage = new HomePage(driver);
		homePage.navigateToNotes();
		homePage = new HomePage(driver);
		homePage.clickAddNote();

		NotesModal notesModal = new NotesModal(driver);
		notesModal.addNote(noteTitle, noteDesc);
	}

	private void addCredential(String url, String username, String password) {

		HomePage homePage = new HomePage(driver);
		homePage.navigateToCredential();
		homePage = new HomePage(driver);
		homePage.clickAddCredential();

		CredentialsModal credentialsModal = new CredentialsModal(driver);
		credentialsModal.addCredential(url, username, password);
	}
	@Test
	public void testUnAuthorizedUserAccess() {
		//test that verifies that an unauthorized user can only access the login and signup pages.
		driver.get(baseURL + "/home");
		assertLoginPage(driver);

		driver.get(baseURL + "/notes");
		assertLoginPage(driver);

		driver.get(baseURL + "/files");
		assertLoginPage(driver);

		driver.get(baseURL + "/credentials");
		assertLoginPage(driver);

		driver.get(baseURL + "/login");
		assertLoginPage(driver);

		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		assertEquals(baseURL + "/signup", driver.getCurrentUrl());
		assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void testSignupLoginHomePageLogoutAndAccess() {
		// Write a test that signs up a new user, logs in, verifies that the home page is accessible,
		// logs out, and verifies that the home page is no longer accessible.
		signUp();

		assertEquals(driver.findElement(By.id("signup-success-msg")).getText(),
				"You successfully signed up! Please log in to continue.");
		login();

		assertEquals(baseURL + "/home", driver.getCurrentUrl());
		assertEquals("Home", driver.getTitle());

		HomePage homePage = new HomePage(driver);
		homePage.logout();

		assertEquals(baseURL + "/login?logout", driver.getCurrentUrl());
		assertEquals("Login", driver.getTitle());
		assertEquals(driver.findElement(By.id("logout-msg")).getText(),"You have been logged out");

		driver.get(baseURL + "/home");
		assertLoginPage(driver);
	}

	@Test
	public void testCreatingANoteAndDisplayingIt() {
		// test that creates a note, and verifies it is displayed.
		String noteTitle = "note 1";
		String noteDesc = "note 1 description";

		signUp();
		login();
		addNote(noteTitle, noteDesc);

		HomePage homePage = new HomePage(driver);
		String rowContent = homePage.getRecentlyAddedNote();
		assertTrue(rowContent.contains(noteTitle+" "+noteDesc));
	}

	@Test
	public void testEditingExistingNote() {
		// test that edits an existing note and verifies that the changes are displayed.
		// test that creates a note, and verifies it is displayed.
		String noteTitle = "note 1";
		String noteDesc = "note 1 description";

		signUp();
		login();
		addNote(noteTitle, noteDesc);

		noteTitle = "note 1 updated";
		noteDesc = "note 1 description updated";

		HomePage homePage = new HomePage(driver);
		homePage.clickEditNote();

		NotesModal notesModal = new NotesModal(driver);
		notesModal.editNote(noteTitle, noteDesc);

		homePage = new HomePage(driver);
		String rowContent = homePage.getRecentlyAddedNote();
		assertTrue(rowContent.contains(noteTitle+" "+noteDesc));
	}

	@Test
	public void testDeletingExistingNote() {
		// test that edits an existing note and verifies that the changes are displayed.
		// test that creates a note, and verifies it is displayed.
		String noteTitle = "note 1";
		String noteDesc = "note 1 description";

		signUp();
		login();
		addNote(noteTitle, noteDesc);

		HomePage homePage = new HomePage(driver);
		homePage.clickDeleteNote();

		homePage = new HomePage(driver);
		assertTrue(homePage.isNotesTableEmpty());
	}

	@Test
	public void testCreatingACredentialAndDisplayingIt() {
		// test that creates a set of credentials, verifies that they are displayed, and verifies that the displayed password is encrypted.
		String url = "url 1";
		String username = "username 1";
		String password = "password 1";

		signUp();
		login();
		addCredential(url, username, password);

		HomePage homePage = new HomePage(driver);
		String rowContent = homePage.getRecentlyAddedCredential();
		assertTrue(rowContent.contains(url+" "+username));
		assertFalse(rowContent.contains(password));
	}

	@Test
	public void testEditingExistingCredential() {
		// test that views an existing set of credentials, verifies that the viewable password is unencrypted,
		// edits the credentials, and verifies that the changes are displayed.
		String url = "url 1";
		String username = "username 1";
		String password = "password 1";

		signUp();
		login();
		addCredential(url, username, password);

		HomePage homePage = new HomePage(driver);
		homePage.clickEditCredential();

		CredentialsModal credentialsModal = new CredentialsModal(driver);
		//assertEquals(password, credentialsModal.getPasswordContent());

		url += " updated";
		username += " updated";
		password += " updated";

		credentialsModal.editCredential(url, username, password);

		homePage = new HomePage(driver);
		String rowContent = homePage.getRecentlyAddedCredential();
		assertTrue(rowContent.contains(url+" "+username));
		assertFalse(rowContent.contains(password));
	}

	@Test
	public void testDeletingExistingCredential() {
		// test that deletes an existing set of credentials and verifies that the credentials are no longer displayed.
		String url = "url 1";
		String username = "username 1";
		String password = "password 1";

		signUp();
		login();
		addCredential(url, username, password);

		HomePage homePage = new HomePage(driver);
		homePage.clickDeleteCredential();

		homePage = new HomePage(driver);
		assertTrue(homePage.isCredentialTableEmpty());
	}
}
