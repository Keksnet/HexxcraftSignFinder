package de.neo.signfinder.sql;

import de.neo.signfinder.SignFinder;
import de.neo.signfinder.sign.SignInfo;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignDB {

    private static List<SignInfo> readSign(PreparedStatement st) throws SQLException {
        List<SignInfo> signs = new ArrayList<>();
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            signs.add(new SignInfo(
                    new Location(
                            Bukkit.getWorld(SignFinder.getInstance().getConfig().getString("world")),
                            rs.getDouble("x"),
                            rs.getDouble("y"),
                            rs.getDouble("z")
                    ),
                    Arrays.asList(
                            rs.getString("text_1"),
                            rs.getString("text_2"),
                            rs.getString("text_3"),
                            rs.getString("text_4")
                    )
            ));
        }
        rs.close();
        return signs;
    }

    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + SignFinder.getInstance().getDataFolder().getAbsolutePath() + "/signs.db");
    }

    public static List<SignInfo> getSignsWithText(String text) {
        List<SignInfo> signs = null;
        try {
            Connection con = createConnection();
            PreparedStatement st = con.prepareStatement("SELECT * FROM signs WHERE " +
                    "text_1 LIKE ? " +
                    "OR text_2 LIKE ? " +
                    "OR text_3 LIKE ? " +
                    "OR text_4 LIKE ? ");
            for (int i = 0; i < 4; i++) {
                st.setString(i + 1, text);
            }
            signs = readSign(st);
            st.close();
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return signs;
    }

}
