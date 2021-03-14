package com.example.hcspaymentapp;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDatabase {
    private Connection connect = null;

    static final String DRIVER = "org.postgresql.Driver";
    static final String URL = "jdbc:postgresql://localhost:5432/housingsector";
    static final String USER = "postgres";
    static final String PASS = "IVTalexsim";

    public boolean connectPostgreSQL()
    {
        try
        {
            Class.forName(DRIVER);
            connect = DriverManager.getConnection(URL, USER, PASS);
        } catch(Exception error){
            System.err.println(error.getMessage());
            return false;
        }
        return true;
    }

    public Connection getDBConnection(){ return connect; }

    public void destroy() throws Exception{
        connect.close();
    }
}
