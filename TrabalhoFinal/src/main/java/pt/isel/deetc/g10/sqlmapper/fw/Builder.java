/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.isel.deetc.g10.sqlmapper.fw;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.io.InvalidClassException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import pt.isel.deetc.g10.sqlmapper.Binder.Binder;
import pt.isel.deetc.g10.sqlmapper.Binder.IBinder;
import pt.isel.deetc.g10.sqlmapper.Binder.SqlConverter;
import pt.isel.deetc.g10.sqlmapper.Binder.SqlSerializer;
import pt.isel.deetc.g10.sqlmapper.Binder.SqlSerializerImplementation;
import pt.isel.deetc.g10.sqlmapper.annotations.DatabaseTable;
import pt.isel.deetc.g10.sqlmapper.annotations.ForeignKey;
import pt.isel.deetc.g10.sqlmapper.annotations.PrimaryKey;
import pt.isel.deetc.g10.sqlmapper.sqlExecutor.AbstractSqlExecutor;
import pt.isel.deetc.g10.utils.AnnotationMethods;
import static pt.isel.deetc.g10.utils.AnnotationMethods.getFieldName;
import static pt.isel.deetc.g10.utils.AnnotationMethods.getFieldType;
import static pt.isel.deetc.g10.utils.AnnotationMethods.getPrimaryKey;
import pt.isel.deetc.g10.utils.Sneak;

/**
 *
 * @author Cadete
 */
public class Builder{

    SQLServerDataSource DataSource;
    Constructor SqlExecutorConstructor;

    private final HashMap<Class, AbstractDataMapper> mappers;
    private final HashMap<Class, List<String>> classPrimaryKeyName;
    
    private final Class<? extends IBinder>[] bindersClass;

    public Builder(Class<? extends AbstractSqlExecutor> executor, SQLServerDataSource ds, Class<? extends IBinder>... bnds) {

        if (bnds.length == 0) {
            throw new InvalidParameterException("You need to pass at least one IBinder to Builder ctor");
        }
        try {
            SqlExecutorConstructor = executor.getConstructor(SQLServerDataSource.class, Binder.class);
        } catch (NoSuchMethodException | SecurityException ex) {
            Sneak.sneakyThrow(ex);
        }
        DataSource = ds;
        bindersClass = bnds;
        mappers = new HashMap<>();
        classPrimaryKeyName = new HashMap<>();
    }

    public <T> DataMapper build(final Class<T> klass) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

        if (mappers.containsKey(klass)) {
            return mappers.get(klass);
        }

        List<IBinder<T>> thisBinders = new LinkedList<>();
        for (Class<? extends IBinder> binder : bindersClass) {
            IBinder b = instantializeBinder(binder, klass);
            thisBinders.add(b);
        }

        Constructor<Binder> ctorbind = Binder.class.getConstructor(IBinder[].class);
        Binder<T> BinderED = ctorbind.newInstance((Object) thisBinders.toArray(new IBinder<?>[thisBinders.size()]));
        AbstractSqlExecutor newExecutor = (AbstractSqlExecutor) SqlExecutorConstructor.newInstance(DataSource, BinderED);

        final List<Field> primaryKeys = new LinkedList<>();
        final List<Field> elems = new LinkedList<>();
        DatabaseTable DatabaseTableAnnotation = klass.getAnnotation(DatabaseTable.class);
        if (DatabaseTableAnnotation == null) {
            throw new InvalidParameterException("The class that you want to build must have the DatabaseTable anotation");
        }
        String tableName = DatabaseTableAnnotation.name();

        final Field[] fields = klass.getDeclaredFields();

        for (Field f : fields) {

            PrimaryKey fieldAnnot = f.getDeclaredAnnotation(PrimaryKey.class);
            ForeignKey foreignAnnot = f.getDeclaredAnnotation(ForeignKey.class);

            if (fieldAnnot != null) {
                primaryKeys.add(f);
            } else if (foreignAnnot == null) {
                elems.add(f);
            } else {
            }

        }

        final List<String> primaryKeyName = primaryKeys.stream().map(f -> getFieldName(f)).collect(Collectors.toList());
        final List<String> otherFieldsName = elems.stream().map(f -> getFieldName(f)).collect(Collectors.toList());
        classPrimaryKeyName.put(klass, primaryKeyName);

        SqlConverter<T> conv = (ResultSet rs) -> {
            try {
                Map<String, Object> vals = new HashMap<>();

                for (Field f : fields) {
                    String fieldName = f.getName();

                    String fieldNameInDB = getFieldName(f);
                    Object value;
                    if (primaryKeyName.contains(fieldNameInDB) || otherFieldsName.contains(fieldNameInDB)) {
                        value = getFieldValue(f, rs.getObject(fieldNameInDB));
                    } else {
                        List<String> pkValues = AnnotationMethods.getPrimaryKey(f,classPrimaryKeyName);
                        value = getFieldValue(f, pkValues.toArray(new Object[pkValues.size()]));
                    }

                    vals.put(fieldName, value);
                }

                T converted = klass.newInstance();
                BinderED.bindTo(converted, vals);

                return converted;
            } catch (InstantiationException e2) {
                Sneak.sneakyThrow(new InvalidClassException(klass.getName(), "Must have a ctor without parameters"));
            } catch (SQLException | IllegalArgumentException | IllegalAccessException ex) {
                Sneak.sneakyThrow(ex);
            }
            return null;
        };

        SqlSerializer<T> serialInsert = new SqlSerializerImplementation<>(elems);

        SqlSerializer<T> serialDelete = new SqlSerializerImplementation<>(primaryKeys);

        List<Field> updateOrderedElems = new LinkedList<>(elems);
        updateOrderedElems.addAll(primaryKeys);
        SqlSerializer<T> serialUpdate = new SqlSerializerImplementation<>(updateOrderedElems);

        if (primaryKeys.isEmpty()) {
            throw new InvalidParameterException("Domain Entity must have at least one primary key");
        }
        Constructor<GenericDataMapper<?>> ctor = (Constructor<GenericDataMapper<?>>) GenericDataMapper.class.getConstructors()[0];

        GenericDataMapper<?> datamapper = ctor.newInstance(newExecutor, conv, serialInsert, serialUpdate, serialDelete, tableName, primaryKeyName, otherFieldsName); //newExecutor
        mappers.put(klass, datamapper);
        return datamapper;

    }

    private <T> IBinder<T> instantializeBinder(Class<? extends IBinder> binderKlass, Class<T> klass) {
        Constructor<?> defaultCtor = null;
        Constructor<?>[] ctrs = binderKlass.getConstructors();
        for (Constructor<?> constructor : ctrs) {
            if (constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0].equals(Class.class)) {
                try {
                    IBinder<T> ib = (IBinder<T>) constructor.newInstance(klass);
                    return ib;
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Sneak.sneakyThrow(ex);
                }

            } else if (constructor.getParameterCount() == 0) {
                defaultCtor = constructor;
            }
        }

        if (defaultCtor == null) {
            throw new InvalidParameterException("Your binder must have a constructor without parameters or a constructor with a Class<T> parameter");
        }
        IBinder<T> res = null;
        try {
            res = (IBinder<T>) defaultCtor.newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Sneak.sneakyThrow(ex);
        }
        return res;
    }

    private Object getFieldValue(Field f, Object... currValues) {
        Class type = f.getType();
        boolean isIterable=false;
        if(type.equals(Iterable.class)){
                isIterable=true;
                type = getFieldType(f);
            }
        
        Annotation dbtable = type.getAnnotation(DatabaseTable.class);
        if (dbtable != null) {
            DataMapper<?> mapper;

            try {
                if (mappers.containsKey(type)) {
                    mapper = mappers.get(type);
                } else {
                    mapper = build(type);
                }

                List<String> primaryKeyName = getPrimaryKey(type, classPrimaryKeyName);
                assert primaryKeyName.size() == currValues.length;
                StringBuilder stringWhereClause = new StringBuilder();
                String delimiter = " ";
                for (int i = 0; i < currValues.length; i++) {
                    String pk = primaryKeyName.get(i);
                    stringWhereClause.append(delimiter).append(pk).append("=").append(currValues[i]);
                    delimiter = " AND ";
                }

                Iterable<?> iterable = mapper.getAll().where(stringWhereClause.toString());
                if(isIterable) return iterable;
                else{
                    Iterator<?> iterator = iterable.iterator();
                    return iterator.hasNext() ? iterator.next() : null;
                }
                

            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalArgumentException | IllegalAccessException ex) {
                Sneak.sneakyThrow(ex);
            }
        }
        return currValues[0];
    }

    public void closeAllConnections() {
        mappers.entrySet().stream().forEach((entry) -> {
            entry.getValue().exec.closeConnection();
        });
    }
    
}
