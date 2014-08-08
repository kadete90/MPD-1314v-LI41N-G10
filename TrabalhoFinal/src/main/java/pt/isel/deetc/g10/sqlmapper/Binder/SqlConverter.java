/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pt.isel.deetc.g10.sqlmapper.Binder;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Cadete
 */
@FunctionalInterface
public interface SqlConverter<T>{
    T convert(ResultSet rs) throws SQLException;
    
}
