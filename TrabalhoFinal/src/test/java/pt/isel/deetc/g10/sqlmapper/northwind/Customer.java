/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.isel.deetc.g10.sqlmapper.northwind;

import pt.isel.deetc.g10.sqlmapper.annotations.DatabaseTable;
import pt.isel.deetc.g10.sqlmapper.annotations.ForeignKey;
import pt.isel.deetc.g10.sqlmapper.annotations.PrimaryKey;

/**
 *
 * @author Tiago
 */
@DatabaseTable(name = "Customers")

public class Customer {

    @PrimaryKey
    public String CustomerID;
    @ForeignKey(Order.class)
    public Iterable<Order> OrderID;
    
    private  String CompanyName;
    public String ContactName;
    private String ContactTitle;
    public String Address;
    public String City;
    private String Region;

    public String PostalCode;
    public  String Country;

    public String Phone;
    public String Fax;
    

    public Customer(String CustomerID, String CompanyName) {
        this.CustomerID = CustomerID;
        this.CompanyName = CompanyName;
    }

    public Customer() {
    }

    public Customer(String CustomerID, String CompanyName, String ContactName, String ContactTitle, String Address, String City, String Region, String PostalCode, String Country, String Phone, String Fax) {
        this.CustomerID = CustomerID;
        this.CompanyName = CompanyName;
        this.ContactName = ContactName;
        this.ContactTitle = ContactTitle;
        this.Address = Address;
        this.City = City;
        this.Region = Region;
        this.PostalCode = PostalCode;
        this.Country = Country;
        this.Phone = Phone;
        this.Fax = Fax;
    }

    public void setCustomerID(String CustomerID) {
        this.CustomerID = CustomerID;
    }

    public void setCompanyName(String CompanyName) {
        this.CompanyName = CompanyName;
    }

    public void setContactName(String ContactName) {
        this.ContactName = ContactName;
    }

    public void setContactTitle(String ContactTitle) {
        this.ContactTitle = ContactTitle;
    }

    private void setAddress(String Address) {
        this.Address = Address;
    }

    private void setCity(String City) {
        this.City = City;
    }

    public void setRegion(String Region) {
        this.Region = Region;
    }

    public void setPostalCode(String PostalCode) {
        this.PostalCode = PostalCode;
    }

    private void setCountry(String Country) {
        this.Country = Country;
    }

    private void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public void setFax(String Fax) {
        this.Fax = Fax;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public String getContactName() {
        return ContactName;
    }

    public String getContactTitle() {
        return ContactTitle;
    }

    public String getAddress() {
        return Address;
    }

    public String getCity() {
        return City;
    }

    public String getRegion() {
        return Region;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public String getCountry() {
        return Country;
    }

    public String getPhone() {
        return Phone;
    }

    public String getFax() {
        return Fax;
    }

    @Override
    public String toString() {
        return "Customer{" + "CustomerID=" + CustomerID + ", CompanyName=" + CompanyName + ", ContactName=" + ContactName + ", ContactTitle=" + ContactTitle + ", Address=" + Address + ", City=" + City + ", Region=" + Region + ", PostalCode=" + PostalCode + ", Country=" + Country + ", Phone=" + Phone + ", Fax=" + Fax + '}';
    }

}
