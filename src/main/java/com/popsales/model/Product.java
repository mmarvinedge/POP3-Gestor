package com.popsales.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale.Category;

//@CompoundIndex(unique = true, def = "{'sku': 1, 'companyId': 1}")
public class Product {

    private String id;
//    @Indexed(unique = true)
    private String sku;
    private String companyId;
    private String name;
    private Integer order;
    private Boolean enabled;
    private String description;
    private String imageType;
    private String imageBase64;
    private BigDecimal price;
    private Category categoryMain;
    public List<Attribute> attributes;
    private String printer;

    private String sizePizza;
    private Integer maxPizza;
    private List<FlavorPizza> flavorsPizza;
    private String rulePricePizza;

    public Product() {
    }

    public Boolean getEnabled() {
        if (enabled == null) {
            enabled = true;
        }
        return enabled;
    }

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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategoryMain() {
        return categoryMain;
    }

    public void setCategoryMain(Category categoryMain) {
        this.categoryMain = categoryMain;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

    public String getSizePizza() {
        return sizePizza;
    }

    public void setSizePizza(String sizePizza) {
        this.sizePizza = sizePizza;
    }

    public Integer getMaxPizza() {
        return maxPizza;
    }

    public void setMaxPizza(Integer maxPizza) {
        this.maxPizza = maxPizza;
    }

    public List<FlavorPizza> getFlavorsPizza() {
        return flavorsPizza;
    }

    public void setFlavorsPizza(List<FlavorPizza> flavorsPizza) {
        this.flavorsPizza = flavorsPizza;
    }

    public String getRulePricePizza() {
        return rulePricePizza;
    }

    public void setRulePricePizza(String rulePricePizza) {
        this.rulePricePizza = rulePricePizza;
    }

}
