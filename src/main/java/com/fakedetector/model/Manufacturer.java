package com.fakedetector.model;

public class Manufacturer {
    private int manufacturerId;
    private String companyName;
    private String email;
    private String passwordHash;

    public Manufacturer(String companyName, String email, String passwordHash)
    {
        this.companyName = companyName;
        this.email=email;
        this.passwordHash =  passwordHash;
    }
    public Manufacturer (int manufacturerId,String companyName, String email, String passwordHash)
    {
        this(companyName, email, passwordHash);
        this.manufacturerId= manufacturerId;
    }
    public int getManufacturerId()
    { 
        return manufacturerId;
    }
    public void setManufacturerId(int manufacturerId)
    { 
        this.manufacturerId = manufacturerId;
    }
    public String getCompanyName()
    { 
        return companyName;
    }
    public void setCompanyName(String companyName)
    { 
        this.companyName = companyName;
    }

    public String getEmail()
    {
         return email;
    }
    public void setEmail(String email)
    { 
        this.email = email;
    }

    public String getPasswordHash()
    { 
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash)
    { 
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "Manufacturer{id=" + manufacturerId + ", companyName='" + companyName 
                + "', email='" + email + "'}";
    }
    
}
