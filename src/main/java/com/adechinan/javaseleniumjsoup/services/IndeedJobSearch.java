package com.adechinan.javaseleniumjsoup.services;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class IndeedJobSearch {

    public static void main(String[] args) throws InterruptedException {



        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 30);

        //
        // Open the Indeed.com homepage
        //
        driver.get("https://www.indeed.co.in");
        //
        // Enter the keyword "Java" in the field, What
        //
        driver.findElement(By.id("what")).clear();
        Thread.sleep(2000);
        driver.findElement(By.id("what")).sendKeys("Java");
        //
        // Enter the keyword, "Hyderabad" in the field, Where
        //
        driver.findElement(By.id("where")).clear();
        Thread.sleep(2000);
        driver.findElement(By.id("where")).sendKeys("Hyderabad");
        //
        // Click the FindJobs button for searching
        //
        driver.findElement(By.id("fj")).click();
        //
        // Print the information from the new page
        //
        System.out.println("Page Title:" + driver.getTitle());
        System.out.println("Jobs Count: " + driver.findElement(By.id("searchCount")).getText());
        Thread.sleep(2000);
        //
        // Close the browser
        //
        driver.close();

    }

}
