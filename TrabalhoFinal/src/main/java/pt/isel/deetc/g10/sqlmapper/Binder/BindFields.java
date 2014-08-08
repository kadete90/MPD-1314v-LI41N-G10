/* Projecto final MPD
 Equipa: 
 Tiago Formiga Nº35416
 Flávio Cadete Nº35383

 */
package pt.isel.deetc.g10.sqlmapper.Binder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import pt.isel.deetc.g10.utils.Sneak;

/**
 *
 * @author Tiago
 */
public class BindFields<T> implements IBinder<T>{
    private final Map<String,Field> fields = new HashMap<>();
    public BindFields(Class<T> klass){
        Field[]f = klass.getDeclaredFields();
        for (Field field : f) {
            fields.put(field.getName(), field);
        }
    }
    
    @Override
    public boolean bind(T target,String key, Object value) {
        boolean containsKey = fields.containsKey(key);
        if(containsKey){
            Field field = fields.get(key);
                field.setAccessible(true);
                try {
                    field.set(target, value);
                } catch (        IllegalArgumentException | IllegalAccessException ex) {
                    Sneak.sneakyThrow(ex);
                }finally{
                    field.setAccessible(false);
                }
            }
         return containsKey;
    }    
}
