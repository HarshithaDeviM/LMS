package librarymanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Book {
    private int id;
    private String title;
    private String author;
    private String ISBN;
    private int quantity;
    public Book() {
    }

    public Book(String title, String author, String ISBN, int quantity) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.quantity=quantity;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void saveToDatabase() {
        try (Connection connection = DBconnection.getConnection()) {
            String query = "INSERT INTO books (title, author, ISBN, quantity) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, author);
                preparedStatement.setString(3, ISBN);
                preparedStatement.setInt(4, quantity);
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

    public static List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try (Connection connection = DBconnection.getConnection()) {
            String query = "SELECT * FROM books";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Book book = new Book();
                    book.setId(resultSet.getInt("id"));
                    book.setTitle(resultSet.getString("title"));
                    book.setAuthor(resultSet.getString("author"));
                    book.setISBN(resultSet.getString("ISBN"));
                    book.setQuantity(resultSet.getInt("quantity"));
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void updateInDatabase() {
        try (Connection connection = DBconnection.getConnection()) {
            String query = "UPDATE books SET title=?, author=?, ISBN=?, quantity=? WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, author);
                preparedStatement.setString(3, ISBN);
                preparedStatement.setInt(4, quantity);
                preparedStatement.setInt(5, id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFromDatabase() {
        try (Connection connection = DBconnection.getConnection()) {
            String query = "DELETE FROM books WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
