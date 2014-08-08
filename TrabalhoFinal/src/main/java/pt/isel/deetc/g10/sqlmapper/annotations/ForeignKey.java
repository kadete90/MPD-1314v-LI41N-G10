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
@Target(ElementType.FIELD)
public @interface ForeignKey {
    public Class value() default Object.class; //This field is only used when the relation is 1 to many otherwise its used the class of the field itself
}
