/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.model;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Renato
 */
public class Item {

    private String id;
    private String sku; //codigo do produto
    private String name;
    public BigDecimal price;
    public BigDecimal quantity;
    public BigDecimal totalAds;
    public BigDecimal total;
    public String obs;
    public List<Attribute> attributes;
    public List<AttributeValue> attributesValues;
    public Product product;
    public String printer;
    private List<FlavorPizza> flavors;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public List<AttributeValue> getAttributesValues() {
        return attributesValues;
    }

    public void setAttributesValues(List<AttributeValue> attributesValues) {
        this.attributesValues = attributesValues;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalAds() {
        return totalAds;
    }

    public void setTotalAds(BigDecimal totalAds) {
        this.totalAds = totalAds;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<FlavorPizza> getFlavors() {
        return flavors;
    }

    public void setFlavors(List<FlavorPizza> flavors) {
        this.flavors = flavors;
    }

}
