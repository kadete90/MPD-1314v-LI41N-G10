/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.isel.deetc.g10.sqlmapper.fw;

import java.sql.SQLException;
import java.util.List;
import pt.isel.deetc.g10.sqlmapper.Binder.SqlConverter;
import pt.isel.deetc.g10.sqlmapper.Binder.SqlSerializer;
import pt.isel.deetc.g10.sqlmapper.sqlExecutor.AbstractSqlExecutor;
import pt.isel.deetc.g10.sqlmapper.sqlExecutor.ISqlExecutor;
import pt.isel.deetc.g10.utils.Sneak;

/**
 *
 * @author Cadete
 * @param <T>
 */
public abstract class AbstractDataMapper<T> implements DataMapper<T>, ISqlExecutor,AutoCloseable{

    protected final List<String> primaryKey;

    protected final AbstractSqlExecutor exec;

    protected abstract String sqlGetAll();

    protected abstract String sqlUpdate();

    protected abstract String sqlInsert();

    protected abstract String sqlDelete();

    protected abstract SqlConverter<T> conv();

    protected abstract SqlSerializer<T> insertserializer();

    protected abstract SqlSerializer<T> updateserializer();

    protected abstract SqlSerializer<T> deleteserializer();

    protected AbstractDataMapper(AbstractSqlExecutor exec, List<String> primaryKey) {
        this.exec = exec;
        this.primaryKey = primaryKey;
    }

    @Override
    public final SqlIterable<T> getAll(){
        try {
            return exec.executeQuery(
                    sqlGetAll(),
                    conv());
        } catch (SQLException ex) {
            Sneak.sneakyThrow(ex);
        }
        return null;
    }

    @Override
    public final void insert(T val){
        try {
            exec.beginConnection();
            exec.executeInsert(
                    sqlInsert(),
                    val,
                    primaryKey,
                    insertserializer().serialize(val));
            exec.closeAfterCommand();
        } catch (SQLException ex) {
            Sneak.sneakyThrow(ex);
        }
    }

    @Override
    public final void update(T val){
        try {
            exec.beginConnection();
            exec.executeUpdate(
                    sqlUpdate(),
                    updateserializer().serialize(val));
            exec.closeAfterCommand();
        } catch (SQLException ex) {
            Sneak.sneakyThrow(ex);
        }
    }

    @Override
    public final void delete(T val) {
        try {
            exec.beginConnection();
            exec.executeUpdate(
                    sqlDelete(),
                    deleteserializer().serialize(val));
            exec.closeAfterCommand();
        } catch (SQLException ex) {
            Sneak.sneakyThrow(ex);
        }
        
    }

    @Override
    public void rollback(){
        exec.rollback();
    }

    @Override
    public void commit() {
        exec.commit();
    }

    @Override
    public void closeConnection() {
        exec.closeConnection();
    }  
}
