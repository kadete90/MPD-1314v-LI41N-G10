/* Projecto final MPD
 Equipa: 
 Tiago Formiga Nº35416
 Flávio Cadete Nº35383

 */
package pt.isel.deetc.g10.sqlmapper.Binder;

import java.lang.reflect.Field;
import java.util.List;
import pt.isel.deetc.g10.utils.Sneak;

/**
 *
 * @author Tiago
 */
public class SqlSerializerImplementation<T> implements SqlSerializer<T> {
    private final List<Field> fields;
    public SqlSerializerImplementation(List<Field> f){
        fields = f;
    }

    @Override
    public Object[] serialize(T src) {
        Object[] res = new Object[fields.size()];
            int idx = 0;
            for (Field f : fields) {
                f.setAccessible(true);
                try {
                    res[idx++] = f.get(src);
                } catch (IllegalAccessException ex) {
                    Sneak.sneakyThrow(ex);
                } finally {
                    f.setAccessible(false);
                }
            }
            return res;
    }
    
    
}
