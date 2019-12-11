package com.adechinan.javaseleniumjsoup.services;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class SeleniumDemo {
    public static void main(String[] args) {

        ChromeDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        driver.navigate().to("http://www.galabra.co.il");

        String aboutXpathButton = "//*[@id=\"about\"]/div/a";

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(aboutXpathButton)));

        driver.findElement(By.xpath(aboutXpathButton)).click();

        String languagesParagraphXpath = "//*[@id=\"page1\"]/div[2]/div[5]";

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(languagesParagraphXpath)));

        List<WebElement> languageNamesList = driver.findElements(By.xpath("//*[@id=\"page1\"]/div[2]/div[5]/p"));

        languageNamesList.forEach(el -> System.out.println(el.getText()));

        driver.close();


    }
}
