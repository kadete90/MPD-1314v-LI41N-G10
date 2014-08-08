/* Projecto final MPD
 Equipa: 
 Tiago Formiga Nº35416
 Flávio Cadete Nº35383

 */
package pt.isel.deetc.g10.sqlmapper.Binder;

import java.security.InvalidParameterException;
import java.util.Map;
import pt.isel.deetc.g10.utils.Sneak;

/**
 *
 * @author Tiago
 * @param <T>
 */
public class Binder<T> {

    private final IBinder[] binders;

    public Binder(IBinder... binders) {
        this.binders = binders;
    }

    public void bindTo(T target, Map<String, Object> values) {

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            boolean wasBinded = true;
            for (IBinder binder : binders) {
                if (binder.bind(target, entry.getKey(), entry.getValue())) {
                    wasBinded = true;
                    break;
                }
            }
            if (!wasBinded) {
                Sneak.sneakyThrow(new InvalidParameterException("You need a binder for field " + entry.getKey()));
            }
        }
    }
}
