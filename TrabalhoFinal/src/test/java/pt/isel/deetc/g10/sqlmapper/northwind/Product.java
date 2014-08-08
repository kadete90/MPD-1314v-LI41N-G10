/*
 * Copyright 2014 Miguel Gamboa at CCISEL.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pt.isel.deetc.g10.sqlmapper.northwind;

import java.math.BigDecimal;
import java.util.Objects;
import pt.isel.deetc.g10.sqlmapper.annotations.DatabaseField;
import pt.isel.deetc.g10.sqlmapper.annotations.DatabaseTable;
import pt.isel.deetc.g10.sqlmapper.annotations.PrimaryKey;


@DatabaseTable(name ="Products")
public class Product {

    @PrimaryKey()
    @DatabaseField(name="ProductID")
    public Integer ID;
    @DatabaseField(name="ProductName")
    public String Name;
    private BigDecimal unitPrice;
    public int unitsInStock;
    public Supplier supplierId;

    public Product(){
        
    }
    public Product(int productID, String productName, BigDecimal unitPrice, int unitsInStock, Supplier supplierId) {
        this.ID = productID;
        this.Name = productName;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
        this.supplierId = supplierId;
    }

    @Override
    public String toString() {
        String sup = (supplierId==null)?"NULL":supplierId.toString();
        return "Product{" + "ID=" + ID + ", Name=" + Name + ", unitPrice=" + unitPrice + ", unitsInStock=" + unitsInStock + "\n\t supplierId=" + sup + "}";
    }
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.ID);
        hash = 11 * hash + Objects.hashCode(this.Name);
        hash = 11 * hash + Objects.hashCode(this.unitPrice);
        hash = 11 * hash + this.unitsInStock;
        hash = 11 * hash + Objects.hashCode(this.supplierId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Product other = (Product) obj;
        if (!Objects.equals(this.ID, other.ID)) {
            return false;
        }
        if (!Objects.equals(this.Name, other.Name)) {
            return false;
        }
        if (!Objects.equals(this.unitPrice, other.unitPrice)) {
            return false;
        }
        if (this.unitsInStock != other.unitsInStock) {
            return false;
        }
        if (!Objects.equals(this.supplierId, other.supplierId)) {
            return false;
        }
        return true;
    }

    
    
}
