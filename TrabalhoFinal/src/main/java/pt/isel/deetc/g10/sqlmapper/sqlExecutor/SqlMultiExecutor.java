/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pt.isel.deetc.g10.sqlmapper.sqlExecutor;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import pt.isel.deetc.g10.sqlmapper.Binder.Binder;

/**
 *
 * @author Cadete
 */
public class SqlMultiExecutor extends AbstractSqlExecutor{
    
    public SqlMultiExecutor(SQLServerDataSource ds, Binder binder) throws SQLException {
        super(ds, binder, true);
    }

    @Override
    public void closeAfterCommand(){
        super.closeConnection();
    }

    @Override
    public Connection beginConnection() throws SQLException {
        c=ds.getConnection();
        return c;
    }
    

    @Override
    public void rollback() {
        throw new UnsupportedOperationException("Rollback cannot be called on a multi executor"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void commit() {
        throw new UnsupportedOperationException("Commit cannot be called on a multi executor"); //To change body of generated methods, choose Tools | Templates.
    }
}
