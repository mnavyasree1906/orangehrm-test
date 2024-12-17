package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pages.LoginPage;
import pages.UserManagementPage;


import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class UserManagementTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private UserManagementPage userManagementPage;
    private Properties properties;
    private WebDriverWait wait;

    @BeforeClass
    public void setup() throws IOException {
        // Load config.properties
        properties = new Properties();
        FileInputStream configStream = new FileInputStream("src\\test\\java\\tests\\config.properties");
        properties.load(configStream);

        // Initialize WebDriver
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Implicit Wait
        int implicitWait = Integer.parseInt(properties.getProperty("implicit.wait.duration"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        // Initialize Page Objects
        loginPage = new LoginPage(driver);
        userManagementPage = new UserManagementPage(driver);

        // WebDriverWait
        int assertionWait = Integer.parseInt(properties.getProperty("assertion.wait.duration"));
        wait = new WebDriverWait(driver, Duration.ofSeconds(assertionWait));
    }

    @Test
    public void testAddNewUser() {
        // Navigate to login page
        driver.get(properties.getProperty("app.url"));

        // Login actions using the new login method
        loginPage.login(properties.getProperty("login.username"), properties.getProperty("login.password"));

        // Navigate to User Management page
        userManagementPage.navigateToUsersPage();

        // Add a new user
        userManagementPage.clickAddButton();
        userManagementPage.enterEmployeeName("John Doe"); // Select an existing employee
        userManagementPage.enterUsername("johndoe");
        userManagementPage.selectStatus("Enabled");
        userManagementPage.enterPassword("password123");
        userManagementPage.confirmPassword("password123");
        userManagementPage.clickSaveButton();

        // Assertion: Check for the success message
        Assert.assertTrue(userManagementPage.isSuccessMessageDisplayed(), "User was not added successfully.");

        // Assertion: Verify that the new user appears in the list
        Assert.assertTrue(userManagementPage.isUserInUserList("johndoe"), "User does not appear in the user list.");
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
