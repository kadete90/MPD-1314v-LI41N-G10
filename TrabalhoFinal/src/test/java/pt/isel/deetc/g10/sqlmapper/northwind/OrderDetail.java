/* Projecto final MPD
 Equipa: 
 Tiago Formiga Nº35416
 Flávio Cadete Nº35383

 */
package pt.isel.deetc.g10.sqlmapper.northwind;

import java.math.BigDecimal;
import pt.isel.deetc.g10.sqlmapper.annotations.DatabaseTable;
import pt.isel.deetc.g10.sqlmapper.annotations.PrimaryKey;

/**
 *
 * @author Tiago
 */
@DatabaseTable(name = "Order Details")
public class OrderDetail {
    @PrimaryKey
   public Integer OrderID;
    @PrimaryKey
   public Integer ProductID;
   public BigDecimal UnitPrice;
   public Short Quantity;
   public Float Discount;

    public OrderDetail() {
    }

   
    public OrderDetail(Integer OrderID, Integer ProductID, BigDecimal UnitPrice, Short Quantity, Float Discount) {
        this.OrderID = OrderID;
        this.ProductID = ProductID;
        this.UnitPrice = UnitPrice;
        this.Quantity = Quantity;
        this.Discount = Discount;
    }
}
