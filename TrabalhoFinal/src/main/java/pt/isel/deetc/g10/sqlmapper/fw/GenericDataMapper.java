/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pt.isel.deetc.g10.sqlmapper.fw;

import java.security.InvalidParameterException;
import java.util.List;
import pt.isel.deetc.g10.sqlmapper.Binder.SqlConverter;
import pt.isel.deetc.g10.sqlmapper.Binder.SqlSerializer;
import pt.isel.deetc.g10.sqlmapper.sqlExecutor.AbstractSqlExecutor;

/**
 *
 * @author Cadete
 * @param <T>
 */
public class GenericDataMapper<T> extends AbstractDataMapper<T>{

    private String sqlGetAll;
    private String sqlUpdate;
    private String sqlInsert;
    private String sqlDelete;
    private SqlConverter<T> sqlconverter;
    private SqlSerializer<T> insertsqlserializer;
    private SqlSerializer<T> updatesqlserializer;
    private SqlSerializer<T> deletesqlserializer;
    private String table;

    public GenericDataMapper(AbstractSqlExecutor exec, SqlConverter<T> conv, SqlSerializer<T> insertSerial, SqlSerializer<T> updateSerial, SqlSerializer<T> deleteSerial,String table, List<String> primaryKey, List<String> fields) {
        super(exec, primaryKey);
        if(primaryKey==null || exec ==null) throw new InvalidParameterException("You must specify the primary key");
        
        this.table = table;      
        this.sqlconverter=conv;
        this.insertsqlserializer = insertSerial;
        this.updatesqlserializer = updateSerial;
        this.deletesqlserializer = deleteSerial;
        
        
        StringBuilder sqlGetAllBuilder = new StringBuilder("SELECT ");
        StringBuilder sqlUpdateBuilder = new StringBuilder("UPDATE ");
        StringBuilder sqlInsertBuilder = new StringBuilder("INSERT INTO ");
        StringBuilder sqlDeleteBuilder = new StringBuilder("DELETE FROM ");
        
        appendPrimaryKeys(sqlGetAllBuilder,true);
        sqlGetAllBuilder.append(", ");
        sqlUpdateBuilder.append('[').append(table).append("] SET");
        sqlInsertBuilder.append('[').append(table).append("](");
        
        sqlDeleteBuilder.append('[').append(table).append("] WHERE ");
        
        appendPrimaryKeys(sqlDeleteBuilder,false);
        
        String delimiter=" ";
        
        StringBuilder insertHelper = new StringBuilder();
        for (String name : fields) {
            
            sqlGetAllBuilder.append(delimiter).append('[').append(name).append(']');
            sqlUpdateBuilder.append(delimiter).append('[').append(name).append(']').append("=?");
            sqlInsertBuilder.append(delimiter).append('[').append(name).append(']');
            insertHelper.append(delimiter).append("?");
            delimiter=", ";
        }
        
        sqlInsertBuilder.append(") VALUES(").append(insertHelper.toString()).append(")");
        
        sqlGetAllBuilder.append(" FROM ");
        sqlGetAllBuilder.append('[').append(table).append(']');
        
        sqlUpdateBuilder.append(" WHERE ");
        appendPrimaryKeys(sqlUpdateBuilder,false);
        
        this.sqlGetAll = sqlGetAllBuilder.toString();
        this.sqlUpdate = sqlUpdateBuilder.toString();
        this.sqlInsert = sqlInsertBuilder.toString();
        this.sqlDelete = sqlDeleteBuilder.toString();
        
    }

    @Override
    public String sqlGetAll() {
        return sqlGetAll;
        
    }

    @Override
    public String sqlUpdate() {
        return sqlUpdate;
    }

    @Override
    public String sqlInsert() {
        return sqlInsert;
    }

    @Override
    public String sqlDelete() {
    return sqlDelete;
    }

    @Override
    protected SqlConverter<T> conv() {
        return sqlconverter;
    }
    
    @Override
    protected SqlSerializer<T> insertserializer() {
        return insertsqlserializer;
    }

    @Override
    protected SqlSerializer<T> updateserializer() {
        return updatesqlserializer;
    }

    @Override
    protected SqlSerializer<T> deleteserializer() {
        return deletesqlserializer;
    }
    
    

    private void appendPrimaryKeys(StringBuilder builder,boolean onlyKey) {
        String delimiter = " ";
        for (String key : primaryKey) {
            builder.append(delimiter).append(key);
                    if(!onlyKey)builder.append("=?");
            delimiter=", ";
        }
    }

    @Override
    public void close() throws Exception {
        exec.closeConnection();
    }
}
