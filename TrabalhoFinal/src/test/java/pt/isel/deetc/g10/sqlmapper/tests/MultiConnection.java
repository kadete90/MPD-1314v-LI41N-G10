/* Projecto final MPD
 Equipa: 
 Tiago Formiga Nº35416
 Flávio Cadete Nº35383

 */
package pt.isel.deetc.g10.sqlmapper.tests;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import pt.isel.deetc.g10.sqlmapper.Binder.BindFields;
import pt.isel.deetc.g10.sqlmapper.fw.Builder;
import pt.isel.deetc.g10.sqlmapper.sqlExecutor.SqlMultiExecutor;
import utils.ConnectionManager;

/**
 *
 * @author Cadete
 */
public class MultiConnection extends ConnectionTests{
    
    private final static SQLServerDataSource ds = ConnectionManager.getDS();
    private final static boolean ToRollback = false;
    private final static Builder CleanBuilder = new Builder(SqlMultiExecutor.class,ds,BindFields.class);
    
    //limpar base de dados para o default, dado que o setAutocommit = true neste tipo de conexão
    @BeforeClass
    public static void setUpClass() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SQLServerException, SQLException { 
        Map<String, String> map = new HashMap<>(); //tableName, condição
        map.put("Products", "ProductID > 77");
        map.put("Suppliers","SupplierID > 29");
        map.put("Orders","OrderID > 11077 AND OrderID < 10248");
        map.put("Employees","EmployeeID > 9");
        map.put("Customers","CustomerID = 'XPTO'"); // /!\ alterar para o id que for criado

        Connection c = ds.getConnection();
        c.setAutoCommit(true);

        for(Entry<String, String> entry : map.entrySet()) {
            String sqlStmt = "DELETE FROM " + entry.getKey() + " WHERE "+ entry.getValue();
            PreparedStatement cmd = c.prepareStatement(sqlStmt);
            cmd.executeUpdate();
        }
    }
    
    //antes e depois para não deixar 'lixo'
    @AfterClass
    public static void tearDownClass() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SQLException {
        setUpClass();
        CleanBuilder.closeAllConnections();
    }
    public MultiConnection() {
        super(CleanBuilder, ToRollback);
    }
    
}
