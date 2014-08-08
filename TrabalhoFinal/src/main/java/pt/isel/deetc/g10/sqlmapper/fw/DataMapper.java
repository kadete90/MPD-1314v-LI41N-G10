/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pt.isel.deetc.g10.sqlmapper.fw;

import pt.isel.deetc.g10.sqlmapper.sqlExecutor.ISqlExecutor;

/**
 *
 * @author MPD_sv13_14_G10
 * @param <T>
 */
public interface DataMapper<T> extends ISqlExecutor{
    SqlIterable<T> getAll();
    void update(T val);
    void delete(T val);
    void insert(T val);
    
}