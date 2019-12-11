package com.adechinan.javaseleniumjsoup.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class Product {
    private String id;
    private String name;
    private String image;
    private String link;
    private String provider;
    private String logo;
    private int price;
    private Long randomId;
    private Map<Object, Object> attributes;
}
