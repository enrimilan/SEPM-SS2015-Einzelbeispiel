package sepm.ss15.e0929003.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A singleton class handling the connection to the database.
 */
public class JDBCSingletonConnection {

    private static Connection con = null;
    private static final Logger logger = LogManager.getLogger(JDBCSingletonConnection.class);

    private JDBCSingletonConnection() throws DAOException {
        try {
            String script = JDBCSingletonConnection.class.getClassLoader().getResource("res/create.sql").getPath().substring(1);
            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/mydb;INIT=RUNSCRIPT FROM '"+script+"'\\;", "sa", "");
            con.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            logger.debug(e.getMessage());
            throw new DAOException(e.getMessage());
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException("Couldn't connect to database. Please try again.");
        }
    }

    /**
     * @return the only possible connection to the database in this application.
     * @throws DAOException if could not establish this connection.
     */
    public static Connection getConnection() throws DAOException {
        if(con==null){
            new JDBCSingletonConnection();
            logger.info("Connection to the database established.");
        }
        return con;
    }

    /**
     * Closes the connection to the database.
     * @throws DAOException if it could not close this connection.
     */
    public static void closeConnection() throws DAOException {
        if(con!=null){
            try {
                con.close();
                logger.info("Connection to the database closed successfully.");
            } catch (SQLException e) {
                logger.warn(e.getMessage());
                throw new DAOException();
            }
        }
    }

    /**
     * Tries reconnecting if the connection has been lost.
     * @return the connection to the database
     * @throws DAOException
     */
    public static Connection reconnectIfConnectionToDatabaseLost() throws DAOException {
        try {
            if(con!=null && !con.isValid(3)){
                con = null; //set con to null so the getConnection() will try to connect to the database when called again.
                logger.fatal("Connection to database lost.");
            }
        } catch (SQLException e) {
            logger.debug(e.getMessage());
            throw new DAOException(e.getMessage());
        }
        con = JDBCSingletonConnection.getConnection();
        return con;
    }

    /**
     * Executes a script in the database.
     * @param path the path of the script
     */
    public static void reset(String path) throws DAOException{
        try {
            CallableStatement initCall = con.prepareCall("RUNSCRIPT FROM '" + path + "'");
            initCall.execute();
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }

    }
}