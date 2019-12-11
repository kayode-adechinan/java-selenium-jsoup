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

public class Cdiscount {

    public static void main(String[] args) {

        fashion();

    }



    public static void fashion() {

        ChromeOptions chromeOptions = new ChromeOptions();
        //chromeOptions.addArguments("--headless");
        chromeOptions.setHeadless(true);
        //ChromeDriver driver = new ChromeDriver(chromeOptions);
        ChromeDriver driver = new ChromeDriver();

        WebDriverWait wait = new WebDriverWait(driver, 30);


        Set<String> productLinks = new HashSet<>();

        IntStream.rangeClosed(1, 3)
                .forEach(i -> {


                    String url = "https://www.cdiscount.com/search/10/t+shirt.html?TechnicalForm.SiteMapNodeId=0&TechnicalForm.DepartmentId=10&TechnicalForm.ProductId=&hdnPageType=Search&TechnicalForm.ContentTypeId=16&TechnicalForm.SellerId=&TechnicalForm.PageType=SEARCH_AJAX&TechnicalForm.LazyLoading.ProductSheets=False&TechnicalForm.BrandLicenseId=0&NavigationForm.CurrentSelectedNavigationPath=0&NavigationForm.FirstNavigationLinkCount=1&FacetForm.SelectedFacets.Index=0&FacetForm.SelectedFacets.Index=1&FacetForm.SelectedFacets.Index=2&FacetForm.SelectedFacets.Index=3&FacetForm.SelectedFacets.Index=4&FacetForm.SelectedFacets.Index=5&FacetForm.SelectedFacets.Index=6&FacetForm.SelectedFacets.Index=7&FacetForm.SelectedFacets.Index=8&FacetForm.SelectedFacets.Index=9&FacetForm.SelectedFacets.Index=10&FacetForm.SelectedFacets.Index=11&FacetForm.SelectedFacets.Index=12&SortForm.BrandLicenseSelectedCategoryPath=&SortForm.SelectedNavigationPath=&ProductListTechnicalForm.Keyword=t%2Bshirt&ProductListTechnicalForm.TemplateName=InLine&page="+i+"&_his_#_his_";

                    driver.navigate().to(url);

                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lpBloc")));



                    List<WebElement> links = driver.findElements(By.className("prdtBILDetails"));
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

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("fpDesCol")));
                //wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("meta")));


                WebElement titleElement = driver.findElement(By.className("fpDesCol"));
                //By.cssSelector("*[attributeName='value']")
                //By.cssSelector("meta[property='og:title']"
                //System.out.println(driver.getPageSource());
                if (titleElement != null && !titleElement.findElement(By.cssSelector("h1[itemprop='name']")).getText().isEmpty()) {
                    name = titleElement.findElement(By.cssSelector("h1[itemprop='name']")).getText().trim();

                }



                WebElement priceElement = driver.findElement(By.className("fpDesCol"));
                if (priceElement != null && !priceElement.findElement(By.className("red")).getText().isEmpty()) {
                    price = (int) (Float.parseFloat(priceElement.findElement(By.className("red")).getText()
                            .replace(",", ".")
                            .replace("€", "")
                            .trim()) * 656);
                }


                WebElement imageElement = driver.findElement(By.id("picture0"));
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
