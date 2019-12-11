package com.adechinan.javaseleniumjsoup.services;

import com.adechinan.javaseleniumjsoup.models.Product;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class Amazon {

    public static void main(String[] args) {

       fashion();


    }


    public static void fashion() {

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        //ChromeDriver driver = new ChromeDriver(chromeOptions);
        ChromeDriver driver = new ChromeDriver();

        WebDriverWait wait = new WebDriverWait(driver, 30);


        Set<String> productLinks = new HashSet<>();

        IntStream.rangeClosed(1, 20)
                .forEach(i -> {


                    String url = "https://www.amazon.fr/s?rh=n%3A340855031%2Cn%3A%21340856031%2Cn%3A436559031%2Cn%3A464641031%2Cn%3A2308721031&page=" + i + "&qid=1575966060&ref=lp_2308721031_pg_2";

                    driver.navigate().to(url);

                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("s-result-list")));


                    // System.out.println(driver.getPageSource());


                    List<WebElement> mainResults = driver.findElements(By.className("a-size-mini"));
                    mainResults.forEach(el -> {
                        productLinks.add(el.findElement(By.tagName("a")).getAttribute("href"));
                    });


                });


        productLinks.forEach(System.out::println);
        System.out.println(productLinks.size());


        productLinks.forEach(l -> {

            String name = null;
            int price = 0;
            String image = null;


            System.out.println(l);
            driver.navigate().to(l);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("productTitle")));


            WebElement titleElement = driver.findElement(By.id("productTitle"));
            if (titleElement != null && !titleElement.getText().isEmpty()) {
                name = titleElement.getText().trim();
            }


            WebElement priceElement = driver.findElement(By.id("cerberus-data-metrics"));
            if (priceElement != null && !priceElement.getAttribute("data-asin-price").isEmpty()) {
                price = (int) (Float.parseFloat(priceElement.getAttribute("data-asin-price").trim()) * 656);
            }


            WebElement imageElement = driver.findElement(By.id("landingImage"));
            if (imageElement != null && !imageElement.getAttribute("data-old-hires").isEmpty()) {
                image = imageElement.getAttribute("data-old-hires").trim();
            }


            if (name != null && price != 0 && image != null) {

                Product product = new Product();
                product.setName(name);
                product.setPrice(price);
                product.setImage(image);
                product.setProvider("amazon");
                product.setLink(l);
                product.setLogo("https://upload.wikimedia.org/wikipedia/commons/6/62/Amazon.com-Logo.svg");

                System.out.println(product);

                HttpResponse<Product> productHttpResponse = Unirest.post("http://localhost:8080/api/v1/products")
                        .header("Content-Type", "application/json")
                        .body(product)
                        .asObject(Product.class);

                System.out.println(productHttpResponse.getStatus());
                System.out.println(productHttpResponse.getBody());

            }

                /*WebElement sizeContainer = driver.findElement(By.id("native_dropdown_selected_size_name"));

                sizeContainer.findElements(By.tagName("option"))
                        .stream()
                        .skip(1)
                        .forEach(e-> System.out.println(e.getText().trim()));*/


        });


        driver.close();


    }
}
