package librarymanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

public class Transaction {
    private int id;
    private int memberId;
    private int bookId;
    private Date borrowDate;
    private Date returnDate;

    public Transaction() {
    }

    public Transaction(int id, int memberId, int bookId, Date borrowDate, Date returnDate) {
        this.id = id;
        this.memberId = memberId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public void saveToDatabase() {
        try (Connection connection = DBconnection.getConnection()) {
            String query = "INSERT INTO transactions (memberId, bookId, borrowDate) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, memberId);
                preparedStatement.setInt(2, bookId);
                preparedStatement.setDate(3, borrowDate);
                preparedStatement.executeUpdate();

                // Retrieve the generated ID
                var resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    id = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void returnBook() {
        try (Connection connection = DBconnection.getConnection()) {
            String query = "UPDATE transactions SET returnDate=? WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDate(1, returnDate);
                preparedStatement.setInt(2, id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Other methods as needed
}
