/* Projecto final MPD
 Equipa: 
 Tiago Formiga Nº35416
 Flávio Cadete Nº35383

 */
package pt.isel.deetc.g10.utils;

import java.io.InvalidClassException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import pt.isel.deetc.g10.sqlmapper.annotations.DatabaseField;
import pt.isel.deetc.g10.sqlmapper.annotations.DatabaseTable;
import pt.isel.deetc.g10.sqlmapper.annotations.ForeignKey;
import pt.isel.deetc.g10.sqlmapper.annotations.PrimaryKey;

/**
 *
 * @author Cadete
 */
public class AnnotationMethods {
    
    public static String getFieldName(Field f){
        DatabaseField fieldAnnot = f.getDeclaredAnnotation(DatabaseField.class);
        
        return (fieldAnnot != null) 
                ? fieldAnnot.name()
                : f.getName();
    }
    
    public static List<String> getPrimaryKey(Field f, HashMap<Class, List<String>> classPrimaryKeyName) {
        Class type=getFieldType(f);
        
        return getPrimaryKey(type, classPrimaryKeyName);
    }

    public static List<String> getPrimaryKey(Class type, HashMap<Class, List<String>> classPrimaryKeyName) {
        if(classPrimaryKeyName.containsKey(type)) return classPrimaryKeyName.get(type);
        List<String> pkName = new LinkedList<>();
        for (Field field : type.getDeclaredFields()) {
            PrimaryKey fieldAnnot = field.getDeclaredAnnotation(PrimaryKey.class);
            if (fieldAnnot != null) {
                pkName.add(getFieldName(field));
            }
        }
        if (pkName.isEmpty()){
            Sneak.sneakyThrow(new InvalidClassException(type.getName(),"Your class must a primary key"));
        }
        classPrimaryKeyName.put(type, pkName);
        
        return pkName;
    }

    public static Class getFieldType(Field f) {
        Class type=f.getType();
        if(type.isAssignableFrom(Iterable.class)){
            ForeignKey fk = f.getAnnotation(ForeignKey.class);
            
            
            if(fk==null || fk.value() == Object.class )
                Sneak.sneakyThrow(new InvalidClassException("Your field " + f.getName() + " must have defined the class wich it refers"));
            
            DatabaseTable dt =(DatabaseTable) fk.value().getAnnotation(DatabaseTable.class);
            if(dt==null)
                Sneak.sneakyThrow(new InvalidClassException("The class "+ fk.value() + " of " + f.getName() + " must be annotated with DatabaseTable"));
            type=fk.value();
        }
        return type;
    } 
    
   
}
