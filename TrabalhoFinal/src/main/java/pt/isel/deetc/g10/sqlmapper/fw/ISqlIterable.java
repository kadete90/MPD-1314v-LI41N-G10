/* Projecto final MPD
 Equipa: 
        Tiago Formiga Nº35416
        Flávio Cadete Nº35383

*/
package pt.isel.deetc.g10.sqlmapper.fw;

/**
 * @author Tiago
 * @param <T>
 */
public interface ISqlIterable<T> extends Iterable<T>,AutoCloseable{
    ISqlIterable<T> where(String clause);
    int count();
    SqlIterable<T> bind(Object ... bindArgs);
}
