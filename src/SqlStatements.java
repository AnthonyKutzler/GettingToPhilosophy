public interface SqlStatements {

    String TABLE_NAME = "gettingToPhilosophy";

    String SELECT_URL = "SELECT * FROM " + TABLE_NAME + " WHERE startingPage = ?";
    String INSERT_URL = "INSERT INTO " + TABLE_NAME + " (startingPage, path) VALUES (?, ?)";

}
