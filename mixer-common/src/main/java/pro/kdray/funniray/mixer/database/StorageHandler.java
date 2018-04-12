package pro.kdray.funniray.mixer.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class StorageHandler {
    private static final String TOKENS_CREATE = "CREATE TABLE IF NOT EXISTS token (uuid VARCHAR(36), accessToken VARCHAR(36), refreshToken VARCHAR(72), expiresAt FLOAT());";

    private static final String USER_TOKENS_GET = "SELECT accessToken,refreshToken,expiresAt from token WHERE uuid=?";
    private static final String USER_TOKENS_REMOVE = "DELETE from token WHERE uuid=?";
    private static final String USER_TOKENS_INSERT = "INSERT INTO token VALUES(?,?,?,?)";
    private static final String USER_TOKENS_UPDATE = "UPDATE token SET accessToken = ?, refreshToken = ?, expiresAt = ? WHERE uuid = ?";

    private DataSource dataSource;

    public StorageHandler(DataSource dataSource){

        this.dataSource = dataSource;

        try {
            Connection connection = this.dataSource.getConnection();
            if (connection == null)
                return;
            PreparedStatement statement = connection.prepareStatement(TOKENS_CREATE);
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PlayerToken getToken(UUID uuid){
        try {
            Connection connection = this.dataSource.getConnection();
            if (connection == null)
                return null;
            PreparedStatement statement = connection.prepareStatement(USER_TOKENS_GET);
            statement.setString(1,uuid.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                PlayerToken returnvalue = new PlayerToken(uuid, rs.getString("accessToken"), rs.getString("refreshToken"), rs.getFloat("expiresAt"));
                connection.close();
                return returnvalue;
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeToken(UUID uuid){
        try {
            Connection connection = this.dataSource.getConnection();
            if (connection == null)
                return;
            PreparedStatement statement = connection.prepareStatement(USER_TOKENS_REMOVE);
            statement.setString(1,uuid.toString());
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addToken(PlayerToken token){
        try {
            Connection connection = this.dataSource.getConnection();
            if (connection == null)
                return;
            PreparedStatement statement = connection.prepareStatement(USER_TOKENS_INSERT);
            statement.setString(1,token.getUuid().toString());
            statement.setString(2,token.getAccessToken());
            statement.setString(3,token.getRefreshToken());
            statement.setFloat(4,token.getExpiresAt());
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateToken(PlayerToken token){
        try {
            Connection connection = this.dataSource.getConnection();
            if (connection == null)
                return;
            PreparedStatement statement = connection.prepareStatement(USER_TOKENS_UPDATE);
            statement.setString(1,token.getAccessToken());
            statement.setString(2,token.getRefreshToken());
            statement.setFloat(3,token.getExpiresAt());
            statement.setString(4,token.getUuid().toString());
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        this.dataSource.close();
    }
}
