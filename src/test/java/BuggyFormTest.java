import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;
import java.net.URL;
import io.appium.java_client.windows.WindowsDriver;

public class BuggyFormTest {

    private static WindowsDriver buggyFormSession = null;

    @BeforeClass
    public static void Setup() {
        try {
            DesiredCapabilities appCapabilities = new DesiredCapabilities();
            appCapabilities.setCapability("app", "C:\\dev\\code\\BuggyForm.exe");
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
    public void EditTextBoxesAndClickGo() {
        WebElement editText1 = buggyFormSession.findElementByName("textBox1");
        WebElement editText2 = buggyFormSession.findElementByName("textBox2");

        editText1.sendKeys("Hello");
        editText2.sendKeys("World");

        buggyFormSession.findElementByAccessibilityId("buttonGo").click();
        buggyFormSession.findElementByAccessibilityId("buttonClose").click();
    }
}
