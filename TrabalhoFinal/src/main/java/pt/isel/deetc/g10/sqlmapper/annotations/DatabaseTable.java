/* Projecto final MPD
 Equipa: 
        Tiago Formiga Nº35416
        Flávio Cadete Nº35383

*/
package pt.isel.deetc.g10.sqlmapper.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Tiago
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DatabaseTable {
    public String name();
}
