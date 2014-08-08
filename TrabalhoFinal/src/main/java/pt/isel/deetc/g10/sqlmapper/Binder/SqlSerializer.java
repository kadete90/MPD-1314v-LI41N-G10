/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pt.isel.deetc.g10.sqlmapper.Binder;

/**
 *
 * @author Cadete
 */
@FunctionalInterface
public interface SqlSerializer<T>{
    Object[] serialize(T src);
}
