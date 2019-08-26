import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.appium.java_client.windows.WindowsDriver;

public class BuggyFormTest {

    private static WindowsDriver buggyFormSession = null;
    private Logger logger = Logger.getLogger(BuggyFormTest.class.getName());

    @BeforeClass
    public static void Setup() {
        try {
            DesiredCapabilities appCapabilities = new DesiredCapabilities();
            appCapabilities.setCapability("app", "C:\\dev\\code\\BuggyForm\\BuggyForm\\bin\\Debug\\BuggyForm.exe");
            buggyFormSession = new WindowsDriver(new URL("http://127.0.0.1:4723"), appCapabilities);
            buggyFormSession.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        }catch(Exception e){
            e.printStackTrace();
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
    public void StartAndExitBuggyForm()
    {
        buggyFormSession.findElementByAccessibilityId("buttonClose").click();
    }

    @Test
    public void EditTextBoxes() {
        WebElement editText1 = buggyFormSession.findElementByName("textBox1");
        WebElement editText2 = buggyFormSession.findElementByName("textBox2");
        editText1.sendKeys("Hello");
        editText2.sendKeys("World");
        buggyFormSession.findElementByAccessibilityId("buttonGo").click();
    }

    @Test
    public void SelectSecondItemFromComboBox() {
        WebElement comboBox1 = buggyFormSession.findElementByAccessibilityId("comboBox1");
        WebElement comboBox1OpenButton = comboBox1.findElement(By.name("Open"));
        comboBox1OpenButton.click();
        WebElement comboBox1SecondListItem = comboBox1.findElement(By.name("Second item"));
        comboBox1SecondListItem.click();
        buggyFormSession.findElementByAccessibilityId("buttonGo").click();
    }

    @Test
    public void SwitchToSecondaryTabAndClickCheckBox() {
        WebElement secondaryTab = buggyFormSession.findElementByName("Secondary Tab");
        secondaryTab.click();
        WebElement checkBox = buggyFormSession.findElementByName("checkBox");
        String checkBoxToggleState = checkBox.getAttribute("Toggle.ToggleState");
        if(checkBoxToggleState.equalsIgnoreCase("0")) {
            checkBox.click();
        }
        buggyFormSession.findElementByAccessibilityId("buttonGo").click();
    }

    @Test
    public void SwitchToSecondaryTabAndClickCheckBoxWithDuplicateName() {
        WebElement secondaryTab = buggyFormSession.findElementByName("Secondary Tab");
        secondaryTab.click();
        WebElement checkBox = buggyFormSession.findElementByName("checkBox2");
        String checkBoxToggleState = checkBox.getAttribute("Toggle.ToggleState");
        if(checkBoxToggleState.equalsIgnoreCase("0")) {
            checkBox.click();
        }
    }

    @Test
    public void SwitchToSecondaryTabAndClickCheckBoxWithDuplicateNameXPath() {
        WebElement secondaryTab = buggyFormSession.findElementByName("Secondary Tab");
        secondaryTab.click();
        WebElement checkBox = buggyFormSession.findElementByXPath("//CheckBox[@Name='checkBox2'][2]");
        String checkBoxToggleState = checkBox.getAttribute("Toggle.ToggleState");
        if(checkBoxToggleState.equalsIgnoreCase("0")) {
            checkBox.click();
        }
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

}
