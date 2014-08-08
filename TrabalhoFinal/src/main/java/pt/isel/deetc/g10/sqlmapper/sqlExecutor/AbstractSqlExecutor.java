/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pt.isel.deetc.g10.sqlmapper.sqlExecutor;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pt.isel.deetc.g10.sqlmapper.Binder.Binder;
import pt.isel.deetc.g10.sqlmapper.Binder.SqlConverter;
import pt.isel.deetc.g10.sqlmapper.fw.SqlIterable;
import pt.isel.deetc.g10.utils.Sneak;

/**
 *
 * @author Cadete
 */
public abstract class AbstractSqlExecutor implements ISqlExecutor{
    
    protected final SQLServerDataSource  ds;
    protected Connection c;
    protected boolean autocommit;
    protected Binder BinderED;
   

    public AbstractSqlExecutor(SQLServerDataSource  ds, Binder BinderED, boolean autocommit) throws SQLException {
        this.ds = ds;
        this.autocommit = autocommit;
        this.BinderED = BinderED;
        this.c = null;
    }
    
    
    public void closeAfterCommand() {
    }
    
    public Connection beginConnection() throws SQLException {
        if (c == null || c.isClosed()) {
            c = ds.getConnection();
            c.setAutoCommit(autocommit);
            
        }
        
        return c;
    }
    
    @Override
    public void closeConnection(){
        if(c!=null){
            try {
                if(!c.isClosed())
                    c.close();
                c=null;
            } catch (SQLException ex) {
                Sneak.sneakyThrow(ex);
            }
        }
    }

    public <T> SqlIterable<T> executeQuery(
            String sqlStmt,
            SqlConverter<T> conv,
            Object... args) throws SQLException {
        
        return new SqlIterable<>(this, conv,sqlStmt,args);
        
    }

    public int executeUpdate(String sqlStmt, Object... args) throws SQLException {
        assert c != null; // activar com a opção -ea
        try (PreparedStatement cmd = c.prepareStatement(sqlStmt)) {
            /*
             * Binder dos parametros
             */
            int idx = 1;
            for (Object arg : args) {
                cmd.setObject(idx, arg);
                idx++;
            }
            /*
             * Execução do comando
             */
            return cmd.executeUpdate();
        }
    }
     
    public <T> int executeInsert(String sqlStmt, T val, List<String> primaryKeys, Object... args) throws SQLException {
        assert c != null; // activar com a opção -ea
        try (PreparedStatement cmd = c.prepareStatement(sqlStmt, Statement.RETURN_GENERATED_KEYS)) {
            /*
             * Binder dos parametros
             */
            int idx = 1;
            for (Object arg : args) {
                cmd.setObject(idx, arg);
                idx++;
            }
            /*
             * Execução do comando
             */
            int nrrowsaffected = cmd.executeUpdate();
            ResultSet rs = cmd.getGeneratedKeys();
            rs.next();
            
            Map<String, Object> map = new HashMap<>();
            
             //TODO: alterar para quando tiver chave composta
            map.put(primaryKeys.get(0),rs.getInt(1));
           
            BinderED.bindTo(val,map);
            return nrrowsaffected;
            
        }
    }

}
