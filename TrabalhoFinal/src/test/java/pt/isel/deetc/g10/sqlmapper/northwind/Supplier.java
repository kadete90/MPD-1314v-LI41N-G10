/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pt.isel.deetc.g10.sqlmapper.northwind;

import pt.isel.deetc.g10.sqlmapper.annotations.DatabaseTable;
import pt.isel.deetc.g10.sqlmapper.annotations.PrimaryKey;

@DatabaseTable(name = "Suppliers")
public class Supplier {
    @PrimaryKey
    public int SupplierID;
    private String CompanyName;
    public String ContactName;
    private String City;
   
    
    public Supplier(){}
    public Supplier(int supplierID, String companyName, String contactName, String City) {
        this.SupplierID = supplierID;
        this.CompanyName = companyName;
        this.ContactName = contactName;
        this.City = City;
//        this.products = products;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    private void setCompanyName(String companyName) {
        this.CompanyName = companyName;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        this.ContactName = contactName;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        this.City = city;
    }

//    public Iterable<Product> getProducts() {
//        return products;
//    }
//
//    public void setProducts(Iterable<Product> products) {
//        this.products = products;
//    }

    @Override
    public String toString() {
        return "Supplier{" + "supplierID=" + SupplierID + ", companyName=" + CompanyName + ", contactName=" + ContactName + ", City=" + City + '}';
    }
    
}
