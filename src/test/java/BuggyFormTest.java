import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.appium.java_client.windows.WindowsDriver;

import static java.lang.Thread.sleep;

public class BuggyFormTest {

    private static WindowsDriver<WebElement> buggyFormSession;
    private static final Logger logger = Logger.getLogger(BuggyFormTest.class.getName());

    @BeforeClass
    public static void Setup() {
        String winAppDriverAddress = "127.0.0.1";
        String winAppDriverPort = "4723";
        try {
            File winAppDriverPath = new File("C:/Program Files (x86)/Windows Application Driver/WinAppDriver.exe");
            if (winAppDriverPath.isFile()) {
                new ProcessBuilder(String.valueOf(winAppDriverPath), winAppDriverAddress, winAppDriverPort).inheritIO().start();
                logger.log(Level.INFO, "Running WinAppDriver from application folder.");
            } else {
                logger.log(Level.SEVERE, "WinAppDriver was not Found, Please verify that is installed");
            }
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Failed to start WinAppDriver: " + e.getMessage());
        }

        try {
            DesiredCapabilities appCapabilities = new DesiredCapabilities();
            appCapabilities.setCapability("app", "C:/dev/code/BuggyForm/BuggyForm/bin/Debug/BuggyForm.exe");
            buggyFormSession = new WindowsDriver<>(new URL("http://" + winAppDriverAddress + ":" + winAppDriverPort), appCapabilities);
            buggyFormSession.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        }catch(Exception e){
            logger.log(Level.INFO, "Failed to connect to WinAppDriver: " + e.getMessage());
        }
    }

    @AfterClass
    public static void TearDown()
    {
        if (buggyFormSession != null) {
            buggyFormSession.quit();
        }
        buggyFormSession = null;
    }

    @Test
    public void EditTextBoxes() {
        switchToMainTab();
        WebElement editText1 = buggyFormSession.findElementByName("textBox1");
        WebElement editText2 = buggyFormSession.findElementByName("textBox2");
        editText1.clear();
        editText1.sendKeys("Hello");
        editText2.clear();
        editText2.sendKeys("World");
        buggyFormSession.findElementByAccessibilityId("buttonGo").click();
    }

    @Test
    public void SelectSecondItemFromComboBox() {
        switchToMainTab();
        WebElement comboBox1 = buggyFormSession.findElementByAccessibilityId("comboBox1");
        WebElement comboBox1OpenButton = comboBox1.findElement(By.name("Open"));
        comboBox1OpenButton.click();
        WebElement comboBox1SecondListItem = comboBox1.findElement(By.name("Second item"));
        comboBox1SecondListItem.click();
        buggyFormSession.findElementByAccessibilityId("buttonGo").click();
    }

    @Test
    public void SwitchToSecondaryTabAndClickCheckBox() {
        switchToSecondaryTab();
        WebElement checkBox = buggyFormSession.findElementByName("checkBox");
        String checkBoxToggleState = checkBox.getAttribute("Toggle.ToggleState");
        if(checkBoxToggleState.equalsIgnoreCase("0")) {
            checkBox.click();
        }
        buggyFormSession.findElementByAccessibilityId("buttonGo").click();
    }

    @Test
    public void SwitchToSecondaryTabAndClickCheckBoxWithDuplicateName() {
        switchToSecondaryTab();
        WebElement checkBox = buggyFormSession.findElementByName("checkBox2");
        String checkBoxToggleState = checkBox.getAttribute("Toggle.ToggleState");
        if(checkBoxToggleState.equalsIgnoreCase("0")) {
            checkBox.click();
        }
    }

    @Test
    public void SwitchToSecondaryTabAndClickCheckBoxWithDuplicateNameXPath() {
        switchToSecondaryTab();
        WebElement checkBox = buggyFormSession.findElementByXPath("//CheckBox[@Name='checkBox2'][2]");
        String checkBoxToggleState = checkBox.getAttribute("Toggle.ToggleState");
        if(checkBoxToggleState.equalsIgnoreCase("0")) {
            checkBox.click();
        }
    }

    @Test
    public void HoverMouseAndSendKeys() {
        switchToSecondaryTab();
        WebElement textBox3 = buggyFormSession.findElementByName("textBoxMouseHover");

        Actions mouseOverTextBox3 = new Actions(buggyFormSession);
        mouseOverTextBox3.moveToElement(textBox3, 50, 50);
        mouseOverTextBox3.perform();

        try {
            sleep(5000);
        } catch(Exception e) {
            logger.log(Level.WARNING, "Error while sleeping: " + e.getMessage());
        }

        Assert.assertEquals("Mouse Hover", textBox3.getText());

        Actions builder = new Actions(buggyFormSession);
        builder.sendKeys(Keys.chord(Keys.ALT, "G"));
        builder.perform();
    }

    @Test
    public void DoAllTheThings() {
        EditTextBoxes();
        scrollEditTextToTheBottom();
        SelectSecondItemFromComboBox();
        scrollEditTextToTheBottom();
        SwitchToSecondaryTabAndClickCheckBox();
        scrollEditTextToTheBottom();
    }

    private void scrollEditTextToTheBottom() {
        WebElement pageDown;

        try {
            pageDown = buggyFormSession.findElementByName("Page down");
        } catch (Exception e) {
            logger.log(Level.WARNING, "Didn't find page down button: " + e.getMessage());
            return;
        }

        while(pageDown.isEnabled() && pageDown.isDisplayed()) {
            pageDown.click();
        }
    }

    private void switchToMainTab() {
        WebElement mainTab = buggyFormSession.findElementByName("Main Tab");
        if(!mainTab.isSelected()) {
            mainTab.click();
        }
    }

    private void switchToSecondaryTab() {
        WebElement secondaryTab = buggyFormSession.findElementByName("Secondary Tab");
        if(!secondaryTab.isSelected()) {
            secondaryTab.click();
        }
    }
}
