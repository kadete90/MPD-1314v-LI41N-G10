/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pt.isel.deetc.g10.sqlmapper.sqlExecutor;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.sql.SQLException;
import pt.isel.deetc.g10.sqlmapper.Binder.Binder;
import pt.isel.deetc.g10.utils.Sneak;

/**
 *
 * @author Cadete
 */
public class SqlSingleExecutor extends AbstractSqlExecutor {
    public SqlSingleExecutor(SQLServerDataSource  ds, Binder BinderED) throws SQLException {
        super(ds,BinderED,false);
    }        

    @Override
    public void rollback() {
        
        try {
            if(c==null|| c.isClosed()) return;
            c.rollback();
        } catch (SQLException ex) {
            Sneak.sneakyThrow(ex);
        }
    }

    @Override
    public void commit() {
        try {
            if(c==null|| c.isClosed()) return;
            c.commit();
        } catch (SQLException ex) {
            Sneak.sneakyThrow(ex);
        }
    }

}
