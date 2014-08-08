/* Projecto final MPD
 Equipa: 
 Tiago Formiga Nº35416
 Flávio Cadete Nº35383

 */
package pt.isel.deetc.g10.sqlmapper.Binder;

/**
 *
 * @author Tiago
 */

//TODO para melhorar eficiencia podemos guardar os elementos do key num mapa para evitar mais reflexão para o mesmo elemento no futuro
public interface IBinder<T> {
   boolean bind(T target,String key, Object value);
}

//