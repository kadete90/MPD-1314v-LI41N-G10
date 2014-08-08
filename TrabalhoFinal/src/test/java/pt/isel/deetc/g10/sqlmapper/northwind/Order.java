/* Projecto final MPD
 Equipa: 
 Tiago Formiga Nº35416
 Flávio Cadete Nº35383

 */
package pt.isel.deetc.g10.sqlmapper.northwind;

import java.math.BigDecimal;
import java.sql.Timestamp;
import pt.isel.deetc.g10.sqlmapper.annotations.DatabaseTable;
import pt.isel.deetc.g10.sqlmapper.annotations.PrimaryKey;

/**
 *
 * @author Tiago
 */
@DatabaseTable(name = "orders")
public class Order {
    @PrimaryKey
    public int OrderID;

    public Customer CustomerID;
    /*	FK	*/
    public Employee EmployeeID;
    public Timestamp OrderDate;
    public Timestamp RequiredDate;
    public Timestamp ShippedDate;
    /*	FK	*/
    public int ShipVia;
    public BigDecimal Freight;
    public String ShipName;
    public String ShipAddress;
    public String ShipCity;
    public String ShipRegion;
    public String ShipPostalCode;
    public String ShipCountry;
    public Order(){}
    public Order(int OrderID, Customer CustomerID, Employee EmployeeID, Timestamp OrderDate, Timestamp RequiredDate, Timestamp ShippedDate, int ShipVia, BigDecimal Freight, String ShipName, String ShipAddress, String ShipCity, String ShipRegion, String ShipPostalCode, String ShipCountry) {
        this.OrderID = OrderID;
        this.CustomerID = CustomerID;
        this.EmployeeID = EmployeeID;
        this.OrderDate = OrderDate;
        this.RequiredDate = RequiredDate;
        this.ShippedDate = ShippedDate;
        this.ShipVia = ShipVia;
        this.Freight = Freight;
        this.ShipName = ShipName;
        this.ShipAddress = ShipAddress;
        this.ShipCity = ShipCity;
        this.ShipRegion = ShipRegion;
        this.ShipPostalCode = ShipPostalCode;
        this.ShipCountry = ShipCountry;
    }

    /*	FK	*/
    @Override
    public String toString() {
        return "Order{" + "OrderID=" + OrderID + ", CustomerID=" + CustomerID + ", OrderDate=" + OrderDate + ", RequiredDate=" + RequiredDate + ", ShippedDate=" + ShippedDate + ", ShipVia=" + ShipVia + ", Freight=" + Freight + ", ShipName=" + ShipName + ", ShipAddress=" + ShipAddress + ", ShipCity=" + ShipCity + ", ShipRegion=" + ShipRegion + ", ShipPostalCode=" + ShipPostalCode + ", ShipCountry=" + ShipCountry +",\n\t EmployeeID=" + EmployeeID + '}';
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int OrderID) {
        this.OrderID = OrderID;
    }

    public Customer getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(Customer CustomerID) {
        this.CustomerID = CustomerID;
    }

    public Employee getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(Employee EmployeeID) {
        this.EmployeeID = EmployeeID;
    }

    public Timestamp getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(Timestamp OrderDate) {
        this.OrderDate = OrderDate;
    }

    public Timestamp getRequiredDate() {
        return RequiredDate;
    }

    public void setRequiredDate(Timestamp RequiredDate) {
        this.RequiredDate = RequiredDate;
    }

    public Timestamp getShippedDate() {
        return ShippedDate;
    }

    public void setShippedDate(Timestamp ShippedDate) {
        this.ShippedDate = ShippedDate;
    }

    public int getShipVia() {
        return ShipVia;
    }

    public void setShipVia(int ShipVia) {
        this.ShipVia = ShipVia;
    }

    public BigDecimal getFreight() {
        return Freight;
    }

    public void setFreight(BigDecimal Freight) {
        this.Freight = Freight;
    }

    public String getShipName() {
        return ShipName;
    }

    public void setShipName(String ShipName) {
        this.ShipName = ShipName;
    }

    public String getShipAddress() {
        return ShipAddress;
    }

    public void setShipAddress(String ShipAddress) {
        this.ShipAddress = ShipAddress;
    }

    public String getShipCity() {
        return ShipCity;
    }

    public void setShipCity(String ShipCity) {
        this.ShipCity = ShipCity;
    }

    public String getShipRegion() {
        return ShipRegion;
    }

    public void setShipRegion(String ShipRegion) {
        this.ShipRegion = ShipRegion;
    }

    public String getShipPostalCode() {
        return ShipPostalCode;
    }

    public void setShipPostalCode(String ShipPostalCode) {
        this.ShipPostalCode = ShipPostalCode;
    }

    public String getShipCountry() {
        return ShipCountry;
    }

    public void setShipCountry(String ShipCountry) {
        this.ShipCountry = ShipCountry;
    }

}
