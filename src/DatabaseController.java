import org.DB.DatabaseConnection;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

class DatabaseController extends DatabaseConnection implements SqlStatements {

    //SQL Statements
    private Connection connection;
    private PreparedStatement statement;


    DatabaseController() {
        super("Philosophy");
        connection = super.connection;
    }

    /**
     * Get List if startingUrl was run before
     *
     * @param startingURL accepts full URL or Title portion of URL, parses based on startsWith("http")
     * @return List of pages, without Titles
     * @throws SQLException
     */
    List<WikiPage> getPreviousPath(String startingURL) throws SQLException{
        statement = connection.prepareStatement(SELECT_URL);
        //if startingURL starts with http, subString title portion of link
        if(startingURL.startsWith("http") && !startingURL.endsWith("/"))
            startingURL = startingURL.substring(startingURL.lastIndexOf("/") + 1, startingURL.length());
        statement.setString(1, startingURL);
        ResultSet rs = statement.executeQuery();
        if(rs.first()){
            String[] pages = rs.getString("path").split("/");
            List<WikiPage> wikiPages = new LinkedList<>();
            for(String page : pages){
                if(!page.contains("Found") && !page.contains("Reached"))
                    wikiPages.add(new WikiPage("http://en.wikipedia.org/wiki/" + page, null));
                else
                    //Add message(Found loop, Reached Philosophy.., max links,..) to end
                    wikiPages.add(new WikiPage(page, null));
            }
            statement.close();
            connection.close();
            return wikiPages;
        }
        return new ArrayList<>();
    }

    /**
     *
     * @param startingURL full Path to URL, use subString to insert title
     * @param path List<WikiPage> parsed to title seperated by '/'
     * @throws SQLException catch at a higher level
     */
    void insertPath(String startingURL, List<WikiPage> path) throws SQLException{
        statement = connection.prepareStatement(SELECT_URL);
        statement.setString(1, startingURL);
        if(statement.executeQuery().first()){
            return;
        }
        StringBuilder fullPathBuilder = new StringBuilder();
        for(WikiPage page : path){
            fullPathBuilder.append(page.getTitle());
            fullPathBuilder.append("/");
        }
        String allTitles = fullPathBuilder.toString();
        //insert fullPath.substring(0, fullPath.length -1);
        statement = connection.prepareStatement(INSERT_URL);
        statement.setString(1, startingURL.substring(startingURL.lastIndexOf("/") + 1, startingURL.length()));
        statement.setString(2, allTitles.substring(0, allTitles.length() - 1));
        statement.executeUpdate();
        statement.close();
        connection.close();
    }
}
