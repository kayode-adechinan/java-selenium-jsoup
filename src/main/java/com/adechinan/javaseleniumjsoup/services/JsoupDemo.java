package com.adechinan.javaseleniumjsoup.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class JsoupDemo {

    public static void main(String[] args) throws IOException {


        Set<String> productLinks = new HashSet<>();

        Document doc = Jsoup.
                connect("https://bj.coinafrique.com/categorie/telephones-et-tablettes/52")
                .get();

        System.out.println(doc.title());

        Elements links = doc.select("a[href]");

        links.forEach(link -> {
            if(link.attr("href").startsWith("/annonce") &&

                    link.attr("href")
                            .replaceFirst("/", "")
                            .split("/").length == 3)

            {
                //System.out.println("link : " + link.attr("href"));
                //System.out.println("text : " + link.text());

                productLinks.add("https://bj.coinafrique.com"+link.attr("href"));
            }

        });

        //productLinks.forEach(System.out::println);

        productLinks.forEach(el -> {
            System.out.println(el);
            try {
                Document p = Jsoup.
                        connect(el)
                        .get();


                Element title = p.select("p.title").first();
                Element price = p.select("p.price").first();
                Element image = p.select("ul.slides > li")
                        .first()
                        .select("img")
                        .first();


                String cleanTitle = title.text();
                Long cleanPrice = null;
                String cleanImg = image.attr("src");



                System.out.println(cleanTitle);
                if(!price.text().contains("Prix")){

                    cleanPrice = Long.valueOf(price.text()
                            .replace("CFA", "")
                            .replace(" ", ""));

                    System.out.println(cleanPrice);


                }

                System.out.println(cleanImg);


            } catch (IOException e) {
                e.printStackTrace();
            }


        });



    }
}
