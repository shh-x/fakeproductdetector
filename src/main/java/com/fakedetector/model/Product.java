package com.fakedetector.model;

import java.math.BigDecimal;
import java.time.LocalDate;


public class Product {
    private int productId;
    private String name;
    private BigDecimal price;
    private LocalDate manufactureDate;
    private int manufacturerId;

    public Product(String name,BigDecimal price,LocalDate manufactureDate,int manufacturerId)
    {
        //this.productId = productId;
        this.name = name;
        this.price = price;
        this.manufactureDate = manufactureDate;
        this.manufacturerId = manufacturerId;
    }
    public Product(int productId,String name,BigDecimal price,LocalDate manufactureDate ,int manufacturerId)
    {
        this(name,price,manufactureDate,manufacturerId);
        this.productId = productId;
    }
    public int getProductId()
    {
        return productId;
    }
    public void setProductId(int productId)
    {
        this.productId = productId;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public BigDecimal getPrice()
    {
        return price;
    }
    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }
    public LocalDate getManufactureDate()
    {
        return manufactureDate;
    }
    public void setManufactureDate(LocalDate manufactureDate)
    {
        this.manufactureDate = manufactureDate;
    }
    public int getManufacturerId()
    {
        return manufacturerId;
    }
    public void setManufacturerId(int manufacturerId)
    {
        this.manufacturerId = manufacturerId;
    }
    @Override
    public String toString() {
        return "Product{id=" + productId + ", name='" + name + "', price=" + price
                + ", date=" + manufactureDate + ", manufacturerId=" + manufacturerId + "}";
    }
}
