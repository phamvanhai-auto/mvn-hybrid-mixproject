package com.bankguru.payment;

import java.util.concurrent.TimeUnit;

import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import commons.BaseTest;
import environmentConfig.Environment;

public class Level_21_Multi_Enviroment extends BaseTest {
	WebDriver driver;
	Environment environment;

	@BeforeClass
	@Parameters({ "browser" })
	public void initBrowser(String browserName) {

		// get envir value from Maven cmd
		String prefixEnvirName = System.getProperty("envMaven");
		ConfigFactory.setProperty("envOwner", prefixEnvirName);
		System.out.println("Envir value from Mvn cmd:" + " " + prefixEnvirName);

		environment = ConfigFactory.create(Environment.class);
		driver = openMultipleBrowsers(browserName, environment.appURL());
		System.out.println(environment.appURL());
		System.out.println(environment.appUsername());
		System.out.println(environment.appPassword());
		System.out.println(environment.osName());

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();

	}

	@Test
	public void Employee_01_Add_New_Employee() {

	}

	@AfterClass
	public void quiteBrowser() {
		driver.quit();
	}

}
