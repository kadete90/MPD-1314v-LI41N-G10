/* Projecto final MPD
 Equipa: 
 Tiago Formiga Nº35416
 Flávio Cadete Nº35383

 */
package pt.isel.deetc.g10.sqlmapper.sqlExecutor;

/**
 *
 * @author Cadete
 */
public interface ISqlExecutor{
    void rollback();
    void commit();
    void closeConnection();
}
