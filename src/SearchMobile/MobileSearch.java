package SearchMobile;


import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
//import org.testng.annotations.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;



public class MobileSearch {
    static WebDriver driver;
    String browser;
    String searchText;       // Text to be searched "mobile smartphones under 30000"
    String AmazonWebsiteURL; // This fetches the URL to be searched.
    String driverName;
    String driverPath;


    // getData fetches the input from Excel File
    public   void getData() throws Throwable {
        File f=new File("D:\\Online Mobile Search\\Test Input Data\\TestInput.xls");
        Workbook wb=Workbook.getWorkbook(f);
        Sheet ws= wb.getSheet("Data");

        int row=ws.getRows();

        AmazonWebsiteURL = ws.getCell(0,1 ).getContents();
        searchText = ws.getCell(1, 1).getContents();

        for(int i=1;i<ws.getRows();i++) {

            browser=ws.getCell(2,i).getContents();
            driverPath=ws.getCell(2,i).getContents();

            driverName=ws.getCell(2,i).getContents();


        }

    }

    //Testing on Multiple browsers which uses data fetched from getData function
    public void crossBrowser() throws Exception{

        // Testing on firefox browser
        if(browser.equalsIgnoreCase("firefox")){
            System.setProperty(driverName,driverPath);

            driver=new FirefoxDriver();
            System.out.println("FireFox browser has been opened");
            driver.get(AmazonWebsiteURL);
            screenshot("Home");

        }
        // Testing on chrome browser
        else if(browser.equalsIgnoreCase("chrome")){
            System.setProperty(driverName,driverPath);

            driver=new ChromeDriver();
            System.out.println("Chrome browser has been opened");
            driver.get(AmazonWebsiteURL);
            screenshot("Home");


        }else{
            throw new  Exception("Browser is not correct");
        }
        driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);

    }

    // This function gives input in the search textbox
    public void searchMobile() throws IOException {

        // Here the key is passed with text to be searched "mobile smartphones under 30000" and data is extracted from Excel.
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys(searchText);
        screenshot("SearchText");

        driver.findElement(By.id("nav-search-submit-button")).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    }

    // This function validates the excecution of search string by comparing the text of the elements.
    public void validateSearchString() throws IOException, InterruptedException {
        //It stores the text of the element before selecting the option from drop-down
        String beforeSorting=driver.findElement(By.xpath("//*[@id=\"search\"]/span/div/span/h1/div/div[1]/div/div/span[1]")).getText();

        driver.findElement(By.xpath("//*[@id=\"a-autoid-0-announce\"]/span[1]")).click();

        // Select the "Newest Arrival" option from the drop-down list
        driver.findElement(By.id("s-result-sort-select_4")).click();
        Thread.sleep(2000);
        screenshot("SelectOption");



        //It stores the text of the element after selecting the option from drop-down
        String afterSorting=driver.findElement(By.xpath("//*[@id=\"search\"]/span/div/span/h1/div/div[1]/div/div/span[1]")).getText();

        if(beforeSorting==afterSorting){
            System.out.println("Test Case Failed : Newly arrived mobile phones are not sorted.");
        }else{
            System.out.println("Test Case Passed : Newly arrived mobile phones are displayed.");
        }
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);


    }

    // Checkpoint If newly arrived option got selected or not.
    public void sortedListValidation() throws InterruptedException {

        Select optionsAvailable=new Select( driver.findElement(By.id("s-result-sort-select"))) ;


        WebElement currOption= optionsAvailable.getFirstSelectedOption();

        String toCheck=currOption.getText();

        //Compare the if the first selected option from the list is equlas to the text "Newest Arrival" or not.
        if(toCheck.equals("Newest Arrivals")){
            System.out.println("Newest Arrivals option got selected successfully.");
        }else{
            System.out.println("Test Case Failed.");
        }



    }

    //Closing the browser window
    public void closeBrowser(){

        driver.close();
    }

    // Takes Screenshots at different steps of execution
    public static void screenshot(String ssName) throws IOException {
        File F= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

        FileUtils.copyFile(F, new File("D:\\Online Mobile Search\\Screenshots\\"+ssName+".jpg"));

    }

    // Calls all the functions inside the class
    public static void main(String[] args) throws Throwable {
        MobileSearch MobileObj=new MobileSearch();
        MobileObj.getData();
        MobileObj.crossBrowser();
        MobileObj.searchMobile();
        MobileObj.validateSearchString();
        MobileObj.sortedListValidation();
        MobileObj.closeBrowser();


    }



}
