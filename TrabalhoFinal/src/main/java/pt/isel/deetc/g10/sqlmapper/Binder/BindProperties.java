/* Projecto final MPD
 Equipa: 
 Tiago Formiga Nº35416
 Flávio Cadete Nº35383

 */
package pt.isel.deetc.g10.sqlmapper.Binder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import pt.isel.deetc.g10.utils.Sneak;

/**
 *
 * @author Tiago
 */

public class BindProperties<T> implements IBinder<T>{
    private final Map<String,Method> properties = new HashMap<>();
    public BindProperties(Class<T> klass){
        Method[] methods = klass.getDeclaredMethods();
        for (Method method : methods) {
            if(method.getName().toLowerCase().startsWith("set")){
                properties.put(method.getName().substring(3).toLowerCase(),method);
            }
        }
    }

    @Override
    public boolean bind(T target, String key, Object value) {

        key=key.toLowerCase();
        boolean containsKey = properties.containsKey(key);
        if(containsKey){
            Method m = properties.get(key);
            m.setAccessible(true);
            try {
                m.invoke(target, value);
            } catch (    IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Sneak.sneakyThrow(ex);
            }finally{
            m.setAccessible(false);}
        }
        
        
        return containsKey;
    }
   
    
}
