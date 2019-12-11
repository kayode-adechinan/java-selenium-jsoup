package com.adechinan.javaseleniumjsoup.services;

import com.adechinan.javaseleniumjsoup.models.Product;
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

public class Auchan {

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


                    String url = "https://www.auchan.fr/femme/vetements/t-shirt-top-femme/c-201603011748?sort=position-asc&engine=fh&show=FORTY_EIGHT&page="+i;

                    driver.navigate().to(url);

                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("product-list--container")));



                    List<WebElement> links = driver.findElements(By.className("product-item--container"));
                    links.forEach(el -> {
                        productLinks.add(el.findElement(By.tagName("a")).getAttribute("href"));
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

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("product-detail--title")));


                WebElement titleElement = driver.findElement(By.className("product-detail--title"));
                if (titleElement != null && !titleElement.getText().isEmpty()) {
                    name = titleElement.getText().trim();
                }


                WebElement priceElement = driver.findElement(By.className("product-price--formattedValue"));
                if (priceElement != null && !priceElement.getText().isEmpty()) {
                    price = (int) (Float.parseFloat(priceElement.getText()
                            .replace(",", ".")
                            .replace("€", "")
                            .trim()) * 656);
                }


                WebElement imageElement = driver.findElement(By.className("x-scroller"));
                if (imageElement != null && !imageElement.findElement(By.tagName("meta")).getAttribute("content").isEmpty()) {

                    image = imageElement.findElement(By.tagName("meta")).getAttribute("content").trim();
                }


                if (name != null && price != 0 && image != null) {

                    Product product = new Product();
                    product.setName(name);
                    product.setPrice(price);
                    product.setImage(image);
                    product.setProvider("auchan");
                    product.setLink(l);
                    product.setLogo("https://upload.wikimedia.org/wikipedia/commons/1/1e/Auchan_logo.svg");

                    System.out.println(product);

                   /* HttpResponse<Product> productHttpResponse = Unirest.post("http://localhost:8080/api/v1/products")
                            .header("Content-Type", "application/json")
                            .body(product)
                            .asObject(Product.class);

                    System.out.println(productHttpResponse.getStatus());
                    System.out.println(productHttpResponse.getBody());*/

                }


            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
            }






        });




        driver.close();


    }
}
