/* Projecto final MPD
 Equipa: 
 Tiago Formiga Nº35416
 Flávio Cadete Nº35383

 */
package pt.isel.deetc.g10.sqlmapper.northwind;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;
import pt.isel.deetc.g10.sqlmapper.annotations.DatabaseTable;
import pt.isel.deetc.g10.sqlmapper.annotations.PrimaryKey;

/**
 *
 * @author Tiago
 */
@DatabaseTable(name="Employees")
public class Employee {

    @PrimaryKey()
    public Integer EmployeeID;
    public String LastName;
    public String FirstName;
    public String Title;
    public String TitleOfCourtesy;
    public Timestamp BirthDate;
    public Timestamp HireDate;
    public String Address;
    public String City;
    public String Region;
    public String PostalCode;
    public String Country;
    public String HomePhone;
    public String Extension;
    public byte[] Photo;
    public String Notes;
    /* FK */
    public Employee ReportsTo;
    public String PhotoPath;
    
    public Employee(){
        
    }
    
public Employee(int EmployeeID, String LastName, String FirstName, String Title, String TitleOfCourtesy, Timestamp BirthDate, Timestamp HireDate, String Address, String City, String Region, String PostalCode, String Country, String HomePhone, String Extension, byte[] Photo, String Notes, Employee ReportsTo, String PhotoPath) {
        this.EmployeeID = EmployeeID;
        this.LastName = LastName;
        this.FirstName = FirstName;
        this.Title = Title;
        this.TitleOfCourtesy = TitleOfCourtesy;
        this.BirthDate = BirthDate;
        this.HireDate = HireDate;
        this.Address = Address;
        this.City = City;
        this.Region = Region;
        this.PostalCode = PostalCode;
        this.Country = Country;
        this.HomePhone = HomePhone;
        this.Extension = Extension;
        this.Photo = Photo;
        this.Notes = Notes;
        this.ReportsTo = ReportsTo;
        this.PhotoPath = PhotoPath;
    }

    public Employee(String FirstName,String LastName) {
        this.LastName = LastName;
        this.FirstName = FirstName;
    }
   
    public void setEmployeeID(int EmployeeID) {
        this.EmployeeID = EmployeeID;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setTitleOfCourtesy(String TitleOfCourtesy) {
        this.TitleOfCourtesy = TitleOfCourtesy;
    }

    public void setBirthDate(Timestamp BirthDate) {
        this.BirthDate = BirthDate;
    }

    public void setHireDate(Timestamp HireDate) {
        this.HireDate = HireDate;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public void setRegion(String Region) {
        this.Region = Region;
    }

    public void setPostalCode(String PostalCode) {
        this.PostalCode = PostalCode;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public void setHomePhone(String HomePhone) {
        this.HomePhone = HomePhone;
    }

    public void setExtension(String Extension) {
        this.Extension = Extension;
    }

    public void setPhoto(byte[] Photo) {
        this.Photo = Photo;
    }

    public void setNotes(String Notes) {
        this.Notes = Notes;
    }

    public void setReportsTo(Employee ReportsTo) {
        this.ReportsTo = ReportsTo;
    }

    public void setPhotoPath(String PhotoPath) {
        this.PhotoPath = PhotoPath;
    }

    public int getEmployeeID() {
        return EmployeeID;
    }

    public String getLastName() {
        return LastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getTitle() {
        return Title;
    }

    public String getTitleOfCourtesy() {
        return TitleOfCourtesy;
    }

    public Timestamp getBirthDate() {
        return BirthDate;
    }

    public Timestamp getHireDate() {
        return HireDate;
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

    public String getHomePhone() {
        return HomePhone;
    }

    public String getExtension() {
        return Extension;
    }

    public byte[] getPhoto() {
        return Photo;
    }

    public String getNotes() {
        return Notes;
    }

    public Employee getReportsTo() {
        return ReportsTo;
    }

    public String getPhotoPath() {
        return PhotoPath;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.EmployeeID);
        hash = 23 * hash + Objects.hashCode(this.LastName);
        hash = 23 * hash + Objects.hashCode(this.FirstName);
        hash = 23 * hash + Objects.hashCode(this.Title);
        hash = 23 * hash + Objects.hashCode(this.TitleOfCourtesy);
        hash = 23 * hash + Objects.hashCode(this.BirthDate);
        hash = 23 * hash + Objects.hashCode(this.HireDate);
        hash = 23 * hash + Objects.hashCode(this.Address);
        hash = 23 * hash + Objects.hashCode(this.City);
        hash = 23 * hash + Objects.hashCode(this.Region);
        hash = 23 * hash + Objects.hashCode(this.PostalCode);
        hash = 23 * hash + Objects.hashCode(this.Country);
        hash = 23 * hash + Objects.hashCode(this.HomePhone);
        hash = 23 * hash + Objects.hashCode(this.Extension);
        hash = 23 * hash + Arrays.hashCode(this.Photo);
        hash = 23 * hash + Objects.hashCode(this.Notes);
        hash = 23 * hash + Objects.hashCode(this.ReportsTo);
        hash = 23 * hash + Objects.hashCode(this.PhotoPath);
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
        final Employee other = (Employee) obj;
        if (!Objects.equals(this.EmployeeID, other.EmployeeID)) {
            return false;
        }
        if (!Objects.equals(this.LastName, other.LastName)) {
            return false;
        }
        if (!Objects.equals(this.FirstName, other.FirstName)) {
            return false;
        }
        if (!Objects.equals(this.Title, other.Title)) {
            return false;
        }
        if (!Objects.equals(this.TitleOfCourtesy, other.TitleOfCourtesy)) {
            return false;
        }
        if (!Objects.equals(this.BirthDate, other.BirthDate)) {
            return false;
        }
        if (!Objects.equals(this.HireDate, other.HireDate)) {
            return false;
        }
        if (!Objects.equals(this.Address, other.Address)) {
            return false;
        }
        if (!Objects.equals(this.City, other.City)) {
            return false;
        }
        if (!Objects.equals(this.Region, other.Region)) {
            return false;
        }
        if (!Objects.equals(this.PostalCode, other.PostalCode)) {
            return false;
        }
        if (!Objects.equals(this.Country, other.Country)) {
            return false;
        }
        if (!Objects.equals(this.HomePhone, other.HomePhone)) {
            return false;
        }
        if (!Objects.equals(this.Extension, other.Extension)) {
            return false;
        }
        if (!Arrays.equals(this.Photo, other.Photo)) {
            return false;
        }
        if (!Objects.equals(this.Notes, other.Notes)) {
            return false;
        }
        if (!Objects.equals(this.ReportsTo, other.ReportsTo)) {
            return false;
        }
        if (!Objects.equals(this.PhotoPath, other.PhotoPath)) {
            return false;
        }
        return true;
    }

}
