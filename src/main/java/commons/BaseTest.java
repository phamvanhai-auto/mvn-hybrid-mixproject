package commons;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.opera.OperaDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
	WebDriver driver;
	protected final Log log;
	
	protected BaseTest() {
		 log = LogFactory.getLog(getClass());
	}
	
	String projectPath = System.getProperty("user.dir");
	String osName = System.getProperty("os.name");

	public enum BROWSER {
		CHROME, FIREFOX, IE, EDGE, SAFARI, CHEADLESS, FHEADLESS, COCCOC, OPERA
	}

	public WebDriver openMultipleBrowsers(String browserName) {
		BROWSER browser = BROWSER.valueOf(browserName.toUpperCase());

		if (browser == BROWSER.FIREFOX) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		} else if (browser == BROWSER.CHROME) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		} else if (browser == BROWSER.EDGE) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		} else {
			throw new RuntimeException("Please check browser again!");
		}

		return driver;
	}

	public WebDriver openMultipleBrowsers(String browserName, String urlPage) {

		BROWSER browser = BROWSER.valueOf(browserName.toUpperCase());

		if (browser == BROWSER.FIREFOX) {
			WebDriverManager.firefoxdriver().setup();
			// System.setProperty("webdriver.gecko.driver", projectPath + ".\\browserDrivers\\geckodriver.exe");
			//setBrowserDriverProperty();
			
			//add extension
			FirefoxProfile profile = new FirefoxProfile();
			File file = new File(GlobalConstants.PROJECT_PATH + File.separator + "browserExtension" + File.separator + "selectorshub-3.3.0-fx.xpi");
			profile.addExtension(file);
			FirefoxOptions options = new FirefoxOptions();
			options.setProfile(profile);
			
			//disable console log
			System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, GlobalConstants.PROJECT_PATH + File.separator + "browserLogs" + File.separator + "Firefox.log");
			
			driver = new FirefoxDriver(options);
			
		} else if (browser == BROWSER.CHROME) {
			WebDriverManager.chromedriver().setup();
			// System.setProperty("webdriver.chrome.driver", projectPath + ".\\browserDrivers\\chromedriver.exe");
			//setBrowserDriverProperty();
			//add extention
			File file = new File(GlobalConstants.PROJECT_PATH + File.separator + "browserExtension" + File.separator + "chrome_4_0_5_0.crx");
			ChromeOptions options = new ChromeOptions();
			options.addExtensions(file);	
			
			options.addArguments("--disable-geolocation");
			options.addArguments("--disable-notification");
			options.addArguments("--disable-infobars");
			driver = new ChromeDriver(options);
			
		} else if (browser == BROWSER.EDGE) {
			//WebDriverManager.edgedriver().setup();
			// System.setProperty("webdriver.edge.driver", projectPath + ".\\browserDrivers\\msedgedriver.exe");
			setBrowserDriverProperty();
			driver = new EdgeDriver(); 
		} else if (browser == BROWSER.COCCOC) {
			WebDriverManager.edgedriver().setup();
			// System.setProperty("webdriver.edge.driver", projectPath + ".\\browserDrivers\\msedgedriver.exe");
			//setBrowserDriverProperty();
			ChromeOptions options = new ChromeOptions();
			options.setBinary("C:\\Program Files (x86)\\CocCoc\\Browser\\Application\\browser.exe");
			driver = new ChromeDriver(options); 
		} else if (browser == BROWSER.OPERA) {
			WebDriverManager.edgedriver().setup();
			// System.setProperty("webdriver.edge.driver", projectPath + ".\\browserDrivers\\msedgedriver.exe");
			//setBrowserDriverProperty();
			
			driver = new OperaDriver();
		} 
		else {
			throw new RuntimeException("Please check browser again!");
		}

		driver.get(urlPage);
		return driver;
	}

	public WebDriver getDriver() {
		return this.driver;
	}
	
	//C??ch 1: t??? vi???t h??m
	private String getDirectorySlash(String folderName) {
		if (isMac() || isUnix() || isSolaris()) {
			folderName = "/" + folderName + "/";
		} else if (isWindows()) {
			folderName = "\\" + folderName + "\\";
		} else {
			folderName = null;
		}
		return folderName;
	}
	
	//x??? l?? ph???n ??u??i c???a driver (.exe or not)
	protected void setBrowserDriverProperty() {
		String browserFolderPath = projectPath + getDirectorySlash("browserDrivers");
		//String browserFolderPath = projectPath + getDirectorySlashByFileSeparator("browserDrivers");
		
		if (isWindows()) {
			System.setProperty("webdriver.gecko.driver", browserFolderPath + "geckodriver.exe");
			System.setProperty("webdriver.chrome.driver", browserFolderPath + "chromedriver.exe");
			System.setProperty("webdriver.edge.driver", browserFolderPath + "msedgedriver.exe");
		} else if (isMac()) {
			System.setProperty("webdriver.gecko.driver", browserFolderPath + "geckodriver_mac");
			System.setProperty("webdriver.chrome.driver", browserFolderPath + "chromedriver_mac");
			System.setProperty("webdriver.edge.driver", browserFolderPath + "msedgedriver_mac");
		} else {
			System.setProperty("webdriver.gecko.driver", browserFolderPath + "geckodriver_linux");
			System.setProperty("webdriver.chrome.driver", browserFolderPath + "chromedriver_linux");
		}
	}
	
	//C??ch 2: S??? d???ng th?? vi???n Java (file.separator)
	private String getDirectorySlashByFileSeparator(String folderName) {
		String separator = File.separator;
		return separator + folderName + separator;

	}
	
	//C??ch 3: s??? d???ng th?? vi???n ngo??i WebDriver Manager (open source) ????? t??? check & download driver/browser version v??? & run
	
	private boolean isWindows() {
		return (osName.toLowerCase().indexOf("win") >= 0);
	}

	private boolean isMac() {
		return (osName.toLowerCase().indexOf("mac") >= 0);
	}

	private boolean isUnix() {
		return (osName.toLowerCase().indexOf("nix") >= 0 || osName.toLowerCase().indexOf("nux") >= 0);
	}

	private boolean isSolaris() {
		return (osName.toLowerCase().indexOf("sunos") >= 0);
	}

	public String genEmail() {
		Random rand = new Random();
		return "autofc" + rand.nextInt(9999) + "@mail.com";
		
	}

	private boolean checkTrue(boolean condition) {
		boolean pass = true;
		try {
			if (condition == true) {
				log.info(" -------------------------- PASSED -------------------------- ");
			} else {
				log.info(" -------------------------- FAILED -------------------------- ");
			}
			Assert.assertTrue(condition);
		} catch (Throwable e) {
			pass = false;

			// Add l???i v??o ReportNG
			VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
			Reporter.getCurrentTestResult().setThrowable(e);
		}
		return pass;
	}

	protected boolean verifyTrue(boolean condition) {
		return checkTrue(condition);
	}

	private boolean checkFailed(boolean condition) {
		boolean pass = true;
		try {
			if (condition == false) {
				log.info(" -------------------------- PASSED -------------------------- ");
			} else {
				log.info(" -------------------------- FAILED -------------------------- ");
			}
			Assert.assertFalse(condition);
		} catch (Throwable e) {
			pass = false;
			VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
			Reporter.getCurrentTestResult().setThrowable(e);
		}
		return pass;
	}

	protected boolean verifyFalse(boolean condition) {
		return checkFailed(condition);
	}

	private boolean checkEquals(Object actual, Object expected) {
		boolean pass = true;
		try {
			Assert.assertEquals(actual, expected);
			log.info(" -------------------------- PASSED -------------------------- ");
		} catch (Throwable e) {
			pass = false;
			log.info(" -------------------------- FAILED -------------------------- ");
			VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
			Reporter.getCurrentTestResult().setThrowable(e);
		}
		return pass;
	}

	protected boolean verifyEquals(Object actual, Object expected) {
		return checkEquals(actual, expected);
	}
	
	@BeforeTest
	public void deleteAllFilesInReportNGScreenShot() {
		log.info("---------- START delete file in folder ----------");
		try {
			String workingDir = System.getProperty("user.dir");
			String pathFolderDownload = workingDir + "\\allure-json";
			File file = new File(pathFolderDownload);
			File[] listOfFiles = file.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					//System.out.println(listOfFiles[i].getName());
					new File(listOfFiles[i].toString()).delete();
				}
			}
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		log.info("---------- END delete file in folder ----------");
	}

	protected void closeBrowserAndDriver() {
		String cmd = "";
		
		try {
			String osName = System.getProperty("os.name").toLowerCase();
			log.info("OS name = " + osName);
			
			// Quit driver executable file in Task Manager
			if (driver.toString().contains("chrome")) {
				if (osName.contains("windows")) {
					cmd = "taskkill /F /FI \"IMAGENAME eq chromedriver*\"";
					
				} else {
					cmd = "pkill chromedriver";
				}
			} else if (driver.toString().contains("internetexplorer")) {
				if (osName.contains("window")) {
					cmd = "taskkill /F /FI \"IMAGENAME eq IEDriverServer*\"";
				}
			} else if (driver.toString().contains("firefox")) {
				if (osName.contains("windows")) {
					cmd = "taskkill /F /FI \"IMAGENAME eq geckodriver*\"";
				} else {
					cmd = "pkill geckodriver";
					
				}
			} else if (driver.toString().contains("edge")) {
				if (osName.contains("windows")) {
					cmd = "taskkill /F /FI \"IMAGENAME eq msedgedriver*\"";
				} else {
					cmd = "pkill msedgedriver";
				}
			}
			
			if (driver != null) {
				//IE browser
				driver.manage().deleteAllCookies();
				driver.quit();
			}

		} catch (Exception e) {
			log.info(e.getMessage());
			
		} finally {
			try {
				Process process = Runtime.getRuntime().exec(cmd);
				process.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
				
			}

			log.info("---------- QUIT BROWSER SUCCESS ----------");
		}
	}
}
