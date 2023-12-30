package librarymanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Member {
    private int id;
    private String name;
    private String contactNum;

    // Constructors
    public Member() {
    }

    public Member(String name, String contactNum) {
        this.name = name;
        this.contactNum= contactNum;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }
// Database Operations

    public void saveToDatabase() {
        try (Connection connection = DBconnection.getConnection()) {
            String query = "INSERT INTO members (name, contactNum) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, contactNum);
                preparedStatement.executeUpdate();

                // Retrieve the generated ID
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    id = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        try (Connection connection = DBconnection.getConnection()) {
            String query = "SELECT * FROM members";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Member member = new Member();
                    member.setId(resultSet.getInt("id"));
                    member.setName(resultSet.getString("name"));
                    member.setContactNum(resultSet.getString("contactNum"));
                    members.add(member);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    public void updateInDatabase() {
        try (Connection connection = DBconnection.getConnection()) {
            String query = "UPDATE members SET name=?, contactNum=? WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, contactNum);
                preparedStatement.setInt(3, id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFromDatabase() {
        try (Connection connection = DBconnection.getConnection()) {
            String query = "DELETE FROM members WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
