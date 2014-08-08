/* Projecto final MPD
 Equipa: 
 Tiago Formiga Nº35416
 Flávio Cadete Nº35383

 */
package pt.isel.deetc.g10.sqlmapper.tests;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.Iterator;
import org.junit.Assert;
import org.junit.Test;
import pt.isel.deetc.g10.sqlmapper.Binder.BindFields;
import pt.isel.deetc.g10.sqlmapper.Binder.BindProperties;
import pt.isel.deetc.g10.sqlmapper.Binder.IBinder;
import pt.isel.deetc.g10.sqlmapper.fw.Builder;
import pt.isel.deetc.g10.sqlmapper.fw.DataMapper;
import pt.isel.deetc.g10.sqlmapper.fw.SqlIterable;
import pt.isel.deetc.g10.sqlmapper.northwind.Customer;
import pt.isel.deetc.g10.sqlmapper.northwind.Employee;
import pt.isel.deetc.g10.sqlmapper.northwind.OrderDetail;
import pt.isel.deetc.g10.sqlmapper.northwind.Product;
import pt.isel.deetc.g10.utils.Sneak;
import utils.ConnectionManager;

/**
 *
 * @author Cadete
 */
public abstract class ConnectionTests {
    
    // = new Builder(SqlSingleExecutor.class, ds,BindFields.class);
    protected final Builder builder;
    protected final boolean toRollback;
    protected final boolean print = false;
    
    public ConnectionTests(Builder builder, boolean toRollback) {
        this.builder= builder;
        this.toRollback = toRollback;
    }
    
    public void Rollback(DataMapper<?> dataMapper) { 
        if(toRollback){
            dataMapper.rollback();
        }
    }

    @Test
    public void TestProductMapper() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException
    {
        DataMapper<Product> prodMapper = null;
        try {
            
            prodMapper = builder.build(Product.class);
            SqlIterable<Product> prods = prodMapper.getAll();
                if(print)prods.forEach(System.out::println);
            Assert.assertEquals(77, prods.count());
        } finally{
            if(prodMapper!= null)
                Rollback(prodMapper);
        }
        Assert.assertTrue( true );
    }
    
    @Test
    public void TestEmployeesWith1_1Relation() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException{
        DataMapper<Employee> empMapper = null;
        try {
            empMapper = builder.build(Employee.class);
            SqlIterable<Employee> emps = empMapper.getAll();
            int countWithReportTo=0;
            for (Employee emp : emps) {
                if(emp.ReportsTo!=null)countWithReportTo++;
                if(print)System.out.println(emp);
            }
            Assert.assertEquals(8,countWithReportTo);
        }finally{
            if(empMapper!= null)
                Rollback(empMapper);
        }
        Assert.assertTrue( true );    
    }
    
    @Test
    public void TestCustomerMapper() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException
    {
        DataMapper<Customer> customerMapper = null;
        try {
            customerMapper = builder.build(Customer.class);
            SqlIterable<Customer> customers = customerMapper.getAll();
            if(print)customers.forEach(System.out::println);
            
            customers.close();
        } finally{
            if(customerMapper!= null)
                Rollback(customerMapper);
        }
        
        Assert.assertTrue( true );
    }
    
    
    @Test
    public void TestCustomerMapperWithWhere() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException{
        DataMapper<Customer> customerMapper = null;
        try {
            customerMapper = builder.build(Customer.class);
            SqlIterable<Customer> customers = customerMapper.getAll().where("ContactTitle='Owner'");
            if(print)customers.forEach(System.out::println);
            Assert.assertEquals(17, customers.count());
            Assert.assertEquals(4, customers.where("Region IS NOT NULL").count());
            Assert.assertEquals(13, customers.where("Region IS NULL").count());
            
        }
        finally{
            if(customerMapper!= null)
                Rollback(customerMapper);
        }
    }
    
    
    
    @Test
    public void TestCustomerMapperWithBindedWhere() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SQLException{
        DataMapper<Customer> customerMapper = null;
        try {
            customerMapper = builder.build(Customer.class);
            SqlIterable<Customer> customers = customerMapper.getAll().where("ContactTitle=?").bind("owner");
            if(print)customers.forEach(System.out::println);
            Assert.assertEquals(17, customers.count());
            Assert.assertEquals(4, customers.where("Region IS NOT NULL").count());
            Assert.assertEquals(13, customers.where("Region IS NULL").count());
        }
        finally{
            if(customerMapper!= null)
                Rollback(customerMapper);
        }
    }
    
    @Test(expected = InvalidParameterException.class)
    public void TestCustomerMapperWithBadBindedWhere() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SQLException{
        DataMapper<Customer> customerMapper = null;
        try {
            customerMapper = builder.build(Customer.class);
            SqlIterable<Customer> customers = customerMapper.getAll().where("ContactTitle=?");
            customers.bind("owner",1,2).count();
        }
        finally{
            if(customerMapper!= null)
                Rollback(customerMapper);
        }
    }
    
    @Test
    public void TestMultiIterableBind()throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException{
        DataMapper<Product> prodMapper = null;
        try{
            prodMapper = builder.build(Product.class);
            try(SqlIterable<Product> res = prodMapper.getAll().where("UnitPrice > ?").where("UnitsInStock > ?")){
            int count = res.bind(20, 10).count();
            Assert.assertEquals(30, count);
            count = res.bind(30.8, 5).count();
            Assert.assertEquals(21, count);
            }
        }finally{
            if (prodMapper!=null)
                Rollback(prodMapper);
        }
    }
    
    @Test(expected = InvalidParameterException.class)
    public void TestMultiIterableBindWithoutReduction()throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException{
        DataMapper<Product> prodMapper = null;
        try{
            prodMapper = builder.build(Product.class);
            SqlIterable<Product> res = prodMapper.getAll().where("UnitPrice > ?").where("UnitsInStock > ?");
            SqlIterable<Product> bind1 = res.bind(20, 10);
            SqlIterable<Product> bind2 = res.bind(30.8, 5);
            SqlIterable<Product> bind3 = bind1.bind(30.8, 5);
            Assert.assertEquals(30, bind1.count());
            Assert.assertEquals(21, bind2.count());
            Assert.assertEquals(30, bind3.count());
        }finally{
            if (prodMapper!=null)
                Rollback(prodMapper);
        }
    }
    
    @Test
    public void TestInsertEmployeeWithBindProperties() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException{
        TestInsertEmployeeWithBinders(BindProperties.class);
    }
    @Test
    public void TestInsertEmployeeWithBindPropertiesAndBindFields() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException{
        TestInsertEmployeeWithBinders(BindProperties.class,BindFields.class);
    }
    @Test
     public void TestInsertEmployeeWithBindFieldsAndBindProperties() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException{
        TestInsertEmployeeWithBinders(BindFields.class, BindProperties.class);
    }
    @Test
    public void TestInsertEmployeeWithBindFields() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException{
        TestInsertEmployeeWithBinders(BindFields.class);
    }
    
    @Test
    public void TestInsertUpdateAndDeleteEmployeeWithBindProperties() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException{
        TestInsertUpdateAndDeleteEmployeeWithBinders(BindProperties.class);
    }
    @Test
    public void TestInsertUpdateAndDeleteEmployeeWithBindPropertiesAndBindFields() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException{
        TestInsertUpdateAndDeleteEmployeeWithBinders(BindProperties.class,BindFields.class);
    }
    @Test
     public void TestInsertUpdateAndDeleteEmployeeWithBindFieldsAndBindProperties() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException{
        TestInsertUpdateAndDeleteEmployeeWithBinders(BindFields.class, BindProperties.class);
    }
    @Test
    public void TestInsertUpdateAndDeleteEmployeeWithBindFields() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException{
        TestInsertUpdateAndDeleteEmployeeWithBinders(BindFields.class);
    }
    
    public void TestInsertEmployeeWithBinders(Class<? extends IBinder>...binders) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException{
        DataMapper<Employee> employees = null;
        try {
            employees = builder.build(Employee.class);
            Employee oldEmployee = new Employee("Tiago2","Formiga");
            employees.insert(oldEmployee);

            String id = "EmployeeID="+oldEmployee.EmployeeID;
            Employee employee = employees.getAll().where(id).iterator().next();
            
            Assert.assertEquals("Tiago2", employee.FirstName);
            Assert.assertEquals("Formiga", employee.LastName);
            if(print)employees.getAll().forEach(System.out::println);

        }
        finally{
            if(employees!= null)
                Rollback(employees);
        }
    }
    
    
//    @Test
//    public void TestInsertMultipleKeyObject() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException{
//        DataMapper<OrderDetail> orderDetails = null;
//        
//        try{
//            orderDetails = builder.build(OrderDetail.class);
//            SqlIterable orders = orderDetails.getAll().where("OrderId=?");
//            orders = orders.bind(10248);
//            
//            Assert.assertEquals(3, orders.count());
//            
//            OrderDetail od1 = new OrderDetail(10248, 5, BigDecimal.valueOf(15),Short.valueOf("2"),Float.valueOf(4));
//            OrderDetail od2 = new OrderDetail(10248, 25, BigDecimal.valueOf(15),Short.valueOf("2"),Float.valueOf(4));
//            OrderDetail od3 = new OrderDetail(10248, 55, BigDecimal.valueOf(15),Short.valueOf("2"),Float.valueOf(4));
//            orderDetails.insert(od1);orderDetails.insert(od2);orderDetails.insert(od3);
//            orders = orderDetails.getAll().where("OrderId=?");
//            orders = orders.bind(10248);
//            
//            Assert.assertEquals(6, orders.count());
//            
//        }finally{
//            if(orderDetails != null)
//                Rollback(orderDetails);
//        }
//    }
    
    
    public void TestInsertUpdateAndDeleteEmployeeWithBinders(Class<? extends IBinder> ... binders)throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException{
        DataMapper<Employee> employees = null;
        try {
            employees = builder.build(Employee.class);

            Employee oldEmployee = new Employee("Tiago2","Formiga");
            employees.insert(oldEmployee);
            String id = "EmployeeID="+ oldEmployee.EmployeeID;
            
            Employee employee = employees.getAll().where(id).iterator().next();
            Assert.assertEquals("Tiago2", employee.FirstName);
            Assert.assertEquals("Formiga", employee.LastName);
            
            oldEmployee.City = "Mafra";
            oldEmployee.Notes="100";
            
            employees.update(oldEmployee);
            
            Employee updatedEmployee = employees.getAll().where(id).iterator().next();
            
            Assert.assertEquals(oldEmployee.FirstName, updatedEmployee.FirstName);
            Assert.assertEquals(oldEmployee.LastName, updatedEmployee.LastName);
            Assert.assertEquals(oldEmployee.City, updatedEmployee.City);
            Assert.assertEquals(oldEmployee.Notes, updatedEmployee.Notes);
            Assert.assertEquals(oldEmployee, updatedEmployee);
            
            employees.delete(updatedEmployee);
            
            Iterator<Employee> it = employees.getAll().where(id).iterator();
            Assert.assertFalse(it.hasNext());
        }
        finally{
            if(employees!= null)
                Rollback(employees);
        }
    }
    
    @Test
    public void TestGetSameMapper() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException{
        DataMapper<Employee> mapper2 = builder.build(Employee.class);
        DataMapper<Employee> mapper3 = builder.build(Employee.class);
        DataMapper<Employee> employees = builder.build(Employee.class);
        DataMapper<Product> othermapper = builder.build(Product.class);
        Assert.assertSame(employees, mapper2);
        Assert.assertSame(employees, mapper3);
        Assert.assertNotSame(employees, othermapper);
    }
    
    @Test
    public void TestGetAllOrderDetails()throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException{
        DataMapper<OrderDetail> orderDetails = null;
        try{
            orderDetails=builder.build(OrderDetail.class);
            SqlIterable<OrderDetail> all = orderDetails.getAll();
            if(print)all.iterator().forEachRemaining(e->System.out.println());
            all.close();
            Assert.assertNotNull(all);
        }finally{
            if (orderDetails!=null)
                Rollback(orderDetails);
        }
    }
}
