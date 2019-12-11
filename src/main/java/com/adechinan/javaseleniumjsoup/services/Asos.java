package com.adechinan.javaseleniumjsoup;

import com.adechinan.javaseleniumjsoup.models.Product;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class Asos {

    public static void main(String[] args) {

        fashion();

    }



    public static void fashion() {

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(true);
        //ChromeDriver driver = new ChromeDriver(chromeOptions);
        ChromeDriver driver = new ChromeDriver();


       /* FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setHeadless(true);
        //ChromeDriver driver = new ChromeDriver(chromeOptions);
        FirefoxDriver driver = new FirefoxDriver();*/

        WebDriverWait wait = new WebDriverWait(driver, 30);


        Set<String> productLinks = new HashSet<>();

        IntStream.range(1, 2)
                .forEach(i -> {

                    try{

                        String url = "https://www.asos.fr/femme/vetements-de-sport/shorts/cat/?cid=27164&nlid=ww|v%C3%AAtements+de+sport|voir+par+produit";

                        driver.navigate().to(url);

                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-auto-id='productList']")));

                        List<WebElement> links = driver.findElements(By.cssSelector("article[data-auto-id='productTile']"));
                        links.forEach(el -> {
                            //productLinks.add(el.findElement(By.tagName("a")).getAttribute("href"));


                            String image = null;

                            String link = el.findElement(By.tagName("a")).getAttribute("href");
                            String name = el.findElement(By.cssSelector("div[data-auto-id='productTileDescription']"))
                                    .findElement(By.tagName("p")).getText().trim();

                            if(el.findElement(By.cssSelector("img[data-auto-id='productTileImage']")) != null){
                                image = el.findElement(By.cssSelector("img[data-auto-id='productTileImage']"))
                                        .getAttribute("src").trim();
                            }



                            int price = 0;

                            if(!el.findElement(By.cssSelector("span[data-auto-id='productTilePrice']")).getText().contains("À partir de")){

                                price = (int)Float.parseFloat(el.findElement(By.cssSelector("span[data-auto-id='productTilePrice']"))
                                        .getText()
                                        .replace(",", "")
                                        .replace("€", "")
                                        .trim());
                            }

                            if ( price != 0 && image !=null) {

                                Product product = new Product();
                                product.setName(name);
                                product.setPrice(price);
                                product.setImage(image);
                                product.setProvider("asos");
                                product.setLink(link);
                                product.setLogo("https://upload.wikimedia.org/wikipedia/commons/a/a8/Asos.svg");

                                System.out.println(product);

                            }



                        });

                    }
                    catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }




                });


        //productLinks.forEach(System.out::println);
        //System.out.println(productLinks.size());



/*

        productLinks.forEach(l -> {

            try {

                String name = null;
                int price = 0;
                String image = null;


                System.out.println(l);
                driver.navigate().to(l);

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("product-hero")));

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("current-price")));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("gallery-image")));


                WebElement titleElement = driver.findElement(By.className("product-hero"));

                //By.cssSelector("*[attributeName='value']")
                //By.cssSelector("meta[property='og:title']"
                //System.out.println(driver.getPageSource());
                if (titleElement != null && !titleElement.findElement(By.tagName("h1")).getText().isEmpty()) {
                    name = titleElement.findElement(By.tagName("h1")).getText().trim();

                }



                WebElement priceElement = driver.findElement(By.className("current-price"));
                if (priceElement != null && !priceElement.getText().isEmpty()) {
                    price = (int) (Float.parseFloat(priceElement.getText()
                            .replace(",", ".")
                            .replace("€", "")
                            .trim()) * 656);
                }


                WebElement imageElement = driver.findElement(By.className("gallery-image"));
                if (imageElement != null && !imageElement.getAttribute("src").isEmpty()) {

                    image = imageElement.getAttribute("src").trim();
                }


                if (name != null && price != 0 && image != null) {

                    Product product = new Product();
                    product.setName(name);
                    product.setPrice(price);
                    product.setImage(image);
                    product.setProvider("cdiscount");
                    product.setLink(l);
                    product.setLogo("https://cdn.worldvectorlogo.com/logos/cdiscount.svg");

                    System.out.println(product);

                    */
/*HttpResponse<Product> productHttpResponse = Unirest.post("http://localhost:8080/api/v1/products")
                            .header("Content-Type", "application/json")
                            .body(product)
                            .asObject(Product.class);

                    System.out.println(productHttpResponse.getStatus());
                    System.out.println(productHttpResponse.getBody());*//*

                }


            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
            }

        });

*/




        driver.close();


    }
}
