package fr.sos.projetmines.commonutils.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public abstract class DatabaseConnection {

    /**
     * Logger for the database connection
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnection.class);

    /**
     * Connection to the database, instantiated when the {@link #connect()} method is called and succeed
     */
    protected Connection connection;

    /**
     * Database address
     */
    private final String address;

    /**
     * Database username and password
     */
    private final String username, password;

    /**
     * @param address database address
     * @param username database username
     * @param password database password
     */
    public DatabaseConnection(String address, String username, String password) {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.error("H2 driver not found!");
        }
        this.address = address;
        this.username = username;
        this.password = password;
    }

    /**
     * Tries to connect to the Database
     * @return whether the connection is successful
     */
    public boolean connect() {
        assert address != null && username != null && password != null;
        try {
            connection = DriverManager.getConnection(address, username, password);
            LOGGER.info("Connection to the database established!");
            return isConnected();
        } catch (SQLException e) {
            LOGGER.error("Connection to the database cannot be established! ({})", e.getMessage());
            return false;
        }
    }

    /**
     * Closes the connection to the database
     */
    public void closeConnection() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @return whether the connection is alive
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(0);
        } catch (SQLException e) {
            return false;
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
