/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.model;

/**
 *
 * @author Renato
 */
public class Company {

    private String id;
    private String socialReason;
    private String name;
    private String fone;
    private String color;
    private String logo;
    private String cgccpf;

    private String companyName;
    private String phone;
    private String owner;
    private Address address;

    private String aproxTime;
    private Boolean trial;
    private String trialDate;
    private Boolean turnos;
    private Boolean freeVersion;
    private String nameUrl;
    
    private Boolean onlyMenu;

    private Boolean deliveryOnly, withdrawalOnly, decimalQuantity;

    public Company() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSocialReason() {
        return socialReason;
    }

    public void setSocialReason(String socialReason) {
        this.socialReason = socialReason;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getAproxTime() {
        return aproxTime;
    }

    public void setAproxTime(String aproxTime) {
        this.aproxTime = aproxTime;
    }

    public Boolean getTrial() {
        if (trial == null) {
            return Boolean.FALSE;
        } else {
            return trial;
        }
    }

    public void setTrial(Boolean trial) {
        this.trial = trial;
    }

    public String getTrialDate() {
        return trialDate;
    }

    public void setTrialDate(String trialDate) {
        this.trialDate = trialDate;
    }

    public Boolean getTurnos() {
        if (turnos == null) {
            return Boolean.FALSE;
        } else {
            return turnos;
        }
    }

    public void setTurnos(Boolean turnos) {
        this.turnos = turnos;
    }

    public String getNameUrl() {
        return nameUrl;
    }

    public void setNameUrl(String nameUrl) {
        this.nameUrl = nameUrl;
    }

    public String getCgccpf() {
        return cgccpf;
    }

    public void setCgccpf(String cgccpf) {
        this.cgccpf = cgccpf;
    }

    public Boolean getFreeVersion() {
        if (freeVersion == null) {
            freeVersion = false;
        }
        return freeVersion;
    }

    public void setFreeVersion(Boolean freeVersion) {
        this.freeVersion = freeVersion;
    }

    public Boolean getDeliveryOnly() {
        if (deliveryOnly == null) {
            deliveryOnly = true;
        }
        return deliveryOnly;
    }

    public void setDeliveryOnly(Boolean deliveryOnly) {
        this.deliveryOnly = deliveryOnly;
    }

    public Boolean getWithdrawalOnly() {
        if (withdrawalOnly == null) {
            withdrawalOnly = true;
        }
        return withdrawalOnly;
    }

    public void setWithdrawalOnly(Boolean withdrawalOnly) {
        this.withdrawalOnly = withdrawalOnly;
    }

    public Boolean getDecimalQuantity() {
        if (decimalQuantity == null) {
            decimalQuantity = false;
        }
        return decimalQuantity;
    }

    public void setDecimalQuantity(Boolean decimalQuantity) {
        this.decimalQuantity = decimalQuantity;
    }

    public Boolean getOnlyMenu() {
        return onlyMenu;
    }

    public void setOnlyMenu(Boolean onlyMenu) {
        this.onlyMenu = onlyMenu;
    }
    
    

}
