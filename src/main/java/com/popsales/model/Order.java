/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Renato
 */
public class Order {

    private String id;
    private String num_order;
    private ClientInfo clientInfo;
    private Boolean delivery;
    private String forma;
    private Boolean troco;
    private Double trocoPara;
    private Address address;
    private DeliverTime deliverTime;
    private String status;
    private Integer itemNumber;
    private String deliveryDateTime;
    private String deliveryDate;
    private String deliveryTime;
    private Merchant merchant;
    private List<Item> products = null;
    private String coupon;
    private BigDecimal discountValue;
    private BigDecimal deliveryCost;
    private BigDecimal subtotal;
    private BigDecimal total;
    private String updatedAt;
    private String createdAt;
    private String store;
    private String origin;
    private String restaurant;
    private String observations;
    private Integer deliveryLimit;
    private String discount;
    private String dtRegister;
    private Date dtRegistro;
    private String cancelmentReason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public Boolean getDelivery() {
        if (delivery == null) {
            try {
                if (address != null && address.getStreet() != null) {
                    delivery = true;
                } else {
                    delivery = false;
                }
            } catch (Exception e) {
                delivery = false;
            }
        }
        return delivery;
    }

    public void setDelivery(Boolean delivery) {
        this.delivery = delivery;
    }

    public String getForma() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public Boolean getTroco() {
        return troco;
    }

    public void setTroco(Boolean troco) {
        this.troco = troco;
    }

    public Double getTrocoPara() {
        if (trocoPara == null) {
            trocoPara = 0.0;
        }
        return trocoPara;
    }

    public void setTrocoPara(Double trocoPara) {
        this.trocoPara = trocoPara;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public DeliverTime getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(DeliverTime deliverTime) {
        this.deliverTime = deliverTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getDeliveryDateTime() {
        return deliveryDateTime;
    }

    public void setDeliveryDateTime(String deliveryDateTime) {
        this.deliveryDateTime = deliveryDateTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public List<Item> getProducts() {
        return products;
    }

    public void setProducts(List<Item> products) {
        this.products = products;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(BigDecimal deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Integer getDeliveryLimit() {
        return deliveryLimit;
    }

    public void setDeliveryLimit(Integer deliveryLimit) {
        this.deliveryLimit = deliveryLimit;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDtRegister() {
        return dtRegister;
    }

    public void setDtRegister(String dtRegister) {
        this.dtRegister = dtRegister;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public String getNum_order() {
        return num_order;
    }

    public void setNum_order(String num_order) {
        this.num_order = num_order;
    }

    public Date getDtRegistro() {
        return dtRegistro;
    }

    public void setDtRegistro(Date dtRegistro) {
        this.dtRegistro = dtRegistro;
    }

    public String getCancelmentReason() {
        return cancelmentReason;
    }

    public void setCancelmentReason(String cancelmentReason) {
        this.cancelmentReason = cancelmentReason;
    }

}
