/* Projecto final MPD
 Equipa: 
 Tiago Formiga Nº35416
 Flávio Cadete Nº35383

 */
package pt.isel.deetc.g10.sqlmapper.fw;

import java.security.InvalidParameterException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import pt.isel.deetc.g10.sqlmapper.Binder.SqlConverter;
import pt.isel.deetc.g10.sqlmapper.sqlExecutor.AbstractSqlExecutor;
import pt.isel.deetc.g10.utils.Sneak;

/**
 *
 * @author Tiago
 * @param <T>
 */
public class SqlIterable<T> implements ISqlIterable<T> {

    private final AbstractSqlExecutor exec;
    private final SqlConverter<T> converter;
    private final String query;
    private final Object[] args;
    private final List<String> whereClauses;
    private final List<IteratorImpl> iterators = new LinkedList<>();

    public SqlIterable(AbstractSqlExecutor executor, SqlConverter<T> converter, 
           String query, Object... args) {
        this.query = query;
        this.args = args;
        this.exec = executor;
        this.converter = converter;
        this.whereClauses = new LinkedList<>();
    }

    private SqlIterable(AbstractSqlExecutor executor, SqlConverter<T> converter, 
           String query, Object[] args, List<String> whereClause) {
        this.query = query;
        this.args = args;
        this.exec = executor;
        this.converter = converter;
        this.whereClauses = whereClause;
    }

    @Override
    public SqlIterable<T> where(String clause) {
        List<String> listWhere = new LinkedList<>(whereClauses);
        listWhere.add(clause);
        return new SqlIterable<>(exec, converter, query, args, listWhere);
    }

    @Override
    public SqlIterable<T> bind(Object... bindArgs) {
        List<String> where = new LinkedList<>();
        SqlIterable<T> iterable = this;
        if (!whereClauses.isEmpty()) {
            int bindIdx = 0;
            for (String clause : whereClauses) {
                if (clause.contains("?")) {
                    where.add(clause.replace("?", getBindArg(bindArgs, bindIdx++)));
                }
            }
            if (bindIdx != bindArgs.length) {
                Sneak.sneakyThrow(new InvalidParameterException("You must bind as many object as your query has parameters"));
            }

            iterable = new SqlIterable(exec, converter, query, args, where);
        }
        return iterable;
    }

    @Override
    public int count() {
        int count = 0;
        for (T t : this) {
            ++count;
        }
        return count;
    }

    @Override
    public Iterator<T> iterator() {
        StringBuilder sqlQueryBuilder = new StringBuilder(query);
        if (!whereClauses.isEmpty()) {
            String delimiter = " ";
            sqlQueryBuilder.append(delimiter).append("WHERE");
            for (String clause : whereClauses) {
                sqlQueryBuilder.append(delimiter).append(clause);
                delimiter = " AND ";
            }
        }
        String sqlQuery = sqlQueryBuilder.toString();
        IteratorImpl iter = new IteratorImpl(exec, sqlQuery);
        iterators.add(iter);
        return iter;
    }

    @Override
    public void close() {
        iterators.forEach(iter -> {
            iter.close();
            iterators.remove(iter);
        });
    }

    private CharSequence getBindArg(Object[] bindArgs, int bindIdx) {
        if (bindIdx > bindArgs.length) {
            Sneak.sneakyThrow(new InvalidParameterException("Can't bind that many arguments !"));
        }
        Object arg = bindArgs[bindIdx];
        if (arg == null) {
            Sneak.sneakyThrow(new InvalidParameterException("Your bind can't have null elements !"));
        }
        boolean isString = arg instanceof String;
        StringBuilder sb = new StringBuilder();
        if (isString) {
            sb.append("'");
        }
        sb.append(arg.toString());
        if (isString) {
            sb.append("'");
        }
        return sb.toString();
    }

    private final class IteratorImpl implements Iterator<T>, AutoCloseable {

        private ResultSet rs;
        boolean calledHasNext = false;
        boolean hasNext = false;
        private final AbstractSqlExecutor exec;
        private PreparedStatement cmd;

        IteratorImpl(AbstractSqlExecutor exec, String query) {
            this.exec = exec;
            try {
                cmd = exec.beginConnection().prepareStatement(query);
                cmd.setFetchSize(1);
                int idx = 1;
                for (Object arg : args) {
                    cmd.setObject(idx, arg);
                    idx++;
                }
                rs = cmd.executeQuery();

            } catch (SQLException ex) {
                close();
                Sneak.sneakyThrow(ex);
            }
        }

        @Override
        public boolean hasNext() {
            calledHasNext = true;
            try {
                hasNext = rs.next();
            } catch (SQLException ex) {
                close();
                Sneak.sneakyThrow(ex);
            }
            if (!hasNext) {
                close();
            }
            return hasNext;
        }

        @Override
        public T next() {
            if (calledHasNext && hasNext || !calledHasNext && hasNext()) {
                calledHasNext = false;
                try {
                    return converter.convert(rs);
                } catch (SQLException ex) {
                    exec.closeAfterCommand();
                    Sneak.sneakyThrow(ex);
                }
            }
            Sneak.sneakyThrow(new NoSuchElementException());
            return null;

        }

        @Override
        public void close() {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (cmd != null) {
                    cmd.close();
                }
                if (exec != null) {
                    exec.closeAfterCommand();
                }
                iterators.remove(this);
            } catch (SQLException ex) {
                Sneak.sneakyThrow(ex);
            }
        }
    }

}
