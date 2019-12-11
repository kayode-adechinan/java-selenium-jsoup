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

public class Ebay {

    public static void main(String[] args) {

       // Document doc = Jsoup.parse(driver.getPageSource());


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


                    String url = "https://www.ebay.fr/b/Vetements-et-accessoires/11450/bn_16576896?_from=R40&_pgn="+i;

                    driver.navigate().to(url);

                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("b-list__items_nofooter")));



                    List<WebElement> links = driver.findElements(By.className("s-item__link"));
                    links.forEach(el -> {
                        productLinks.add(el.getAttribute("href"));
                    });


                });


        productLinks.forEach(System.out::println);
        System.out.println(productLinks.size());


        productLinks.forEach(l -> {

            try {

                String name = null;
                int price = 0;
                String image = null;


                System.out.println(l);
                driver.navigate().to(l);

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("itemTitle")));


                WebElement titleElement = driver.findElement(By.id("itemTitle"));
                if (titleElement != null && !titleElement.getText().isEmpty()) {
                    name = titleElement.getText().trim();
                }


                WebElement priceElement = driver.findElement(By.id("prcIsum"));
                if (priceElement != null && !priceElement.getText().isEmpty()) {
                    price = (int) (Float.parseFloat(priceElement.getText().trim()
                            .replace(",", ".")
                            .replace("EUR", "")) * 656);
                }


                WebElement imageElement = driver.findElement(By.id("icThrImg"));
                if (imageElement != null && !imageElement.getAttribute("src").isEmpty()) {
                    image = imageElement.getAttribute("src").trim();
                }


                if (name != null && price != 0 && image != null) {

                    Product product = new Product();
                    product.setName(name);
                    product.setPrice(price);
                    product.setImage(image);
                    product.setProvider("ebay");
                    product.setLink(l);
                    product.setLogo("https://upload.wikimedia.org/wikipedia/commons/1/1b/EBay_logo.svg");

                    System.out.println(product);

                HttpResponse<Product> productHttpResponse = Unirest.post("http://localhost:8080/api/v1/products")
                        .header("Content-Type", "application/json")
                        .body(product)
                        .asObject(Product.class);

                System.out.println(productHttpResponse.getStatus());
                System.out.println(productHttpResponse.getBody());

                }


            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
            }






        });


        driver.close();


    }

}
