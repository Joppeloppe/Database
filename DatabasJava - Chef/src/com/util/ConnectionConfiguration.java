package com.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Joppeloppe on 2015-05-13.
 */
public class ConnectionConfiguration {

        public static Connection getConnection() {
            Connection connection = null;

            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://195.178.235.60/ad3897", "ad3897", "databas");
            }   catch (Exception e) {
                System.err.print("Oh no!\n\n");
                e.printStackTrace();
                System.err.println("Connection failed!");
            }

            return connection;
        }
    }

