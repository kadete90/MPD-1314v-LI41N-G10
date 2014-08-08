/* Projecto final MPD
 Equipa: 
 Tiago Formiga Nº35416
 Flávio Cadete Nº35383

 */
package pt.isel.deetc.g10.sqlmapper.tests;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import org.junit.AfterClass;
import pt.isel.deetc.g10.sqlmapper.Binder.BindFields;
import pt.isel.deetc.g10.sqlmapper.fw.Builder;
import pt.isel.deetc.g10.sqlmapper.sqlExecutor.SqlSingleExecutor;
import utils.ConnectionManager;

/**
 *
 * @author Cadete
 */
public class SingleConnection extends ConnectionTests{
    
    private final static SQLServerDataSource ds = ConnectionManager.getDS();
    private final static boolean ToRollback = true;
    private final static Builder newBuilder = new Builder(SqlSingleExecutor.class, ds,BindFields.class);
    
    
    public SingleConnection() {
        super(newBuilder, ToRollback);
    }
    
    @AfterClass
    public static void tearDownClass() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SQLException {
        newBuilder.closeAllConnections();
    }
    
}
