package utils;


import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

/* Projecto final MPD
 Equipa: 
 Tiago Formiga Nº35416
 Flávio Cadete Nº35383

 */

/**
 *
 * @author Tiago
 */
public class ConnectionManager {

    private static SQLServerDataSource getFormigaDS(){
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser("MPD");
        ds.setPassword("1234");
        ds.setServerName("localhost");
        ds.setPortNumber(1433);
        ds.setDatabaseName("Northwind");
        
        return ds;
    }
    
    private static SQLServerDataSource getCadeteDS(){
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser("Kadete");
        ds.setPassword("cadete02");
        ds.setServerName("localhost");
        ds.setPortNumber(1433);
        ds.setDatabaseName("Northwind");
        
        return ds;
    }
    
    public static SQLServerDataSource getDS() {
        String computerName = System.getenv("COMPUTERNAME");
        
        if(computerName.equalsIgnoreCase("formiga-pc"))
            return getFormigaDS();
        else 
            return getCadeteDS();
    }
    
    
    
    
    
    
}
