/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.util.Map;
import java.util.regex.Pattern;
import model.BorrowRequest;

public class BorrowRequestDAO extends GenerateLibraryDAO {

    Connection conn = null;
    Statement st = null;
    protected BookDAO bookRepository= new BookDAO();
    protected ReaderDAO readerRepository= new ReaderDAO();
    PreparedStatement preSt = null;

    public BorrowRequest findBorrowRequest(int id) throws SQLException, ClassNotFoundException {
        if (conn == null) {
            conn = ConnectDatabase.getMySQLConnection();
        }
        String sql = "SELECT * FROM borrow_request WHERE id=?";

        PreparedStatement pstm = (PreparedStatement) conn.prepareStatement(sql);
        pstm.setInt(1, id);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            int readerId = rs.getInt("reader_id");
            int bookId = rs.getInt("book_id");
            int deleted = rs.getInt("deleted");
            Date dueDate = rs.getDate("due_date");
            Date updated_at= rs.getDate("updated_at");
            BorrowRequest borrowRequest = new BorrowRequest();
            borrowRequest.id = id;
            borrowRequest.reader_id = readerId;
            borrowRequest.book_id = bookId;
            borrowRequest.deleted = deleted;
            borrowRequest.due_date = dueDate;
            borrowRequest.updated_at=updated_at;

            return borrowRequest;
        }
        return null;
    }

    public int insertBorrowRequest(BorrowRequest borrowRequest) throws SQLException, ClassNotFoundException {
        if (conn == null) {
            conn = ConnectDatabase.getMySQLConnection();
        }
        try {
            st = (Statement) conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int result = 0;
        String insert = "INSERT INTO borrow_request (reader_id, book_id, deleted, due_date) VALUES (?, ?, ?, ?)";
        preSt = (PreparedStatement) conn.prepareStatement(insert);
        preSt.setInt(1, borrowRequest.reader_id);
        preSt.setInt(2, borrowRequest.book_id);
        preSt.setInt(3, borrowRequest.deleted);
        preSt.setDate(4, new java.sql.Date(borrowRequest.due_date.getTime()));
        result = preSt.executeUpdate();
        System.out.println("Result: " + result);
        return result;
    }

    public ArrayList<BorrowRequest> getAllBorrowRequests() throws ClassNotFoundException, SQLException {
        if (conn == null) {
            conn = ConnectDatabase.getMySQLConnection();
        }
        ArrayList<BorrowRequest> list = new ArrayList<BorrowRequest>();
        String sql = "SELECT * FROM borrow_request where deleted=0 and status=0 order by updated_at desc ";
        PreparedStatement pstm = (PreparedStatement) conn.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            int readerId = rs.getInt("reader_id");
            int bookId = rs.getInt("book_id");
            int deleted = rs.getInt("deleted");
            Date dueDate = rs.getDate("due_date");
            Date updated_at= rs.getDate("updated_at");
            BorrowRequest borrowRequest = new BorrowRequest();
            borrowRequest.id = id;
            borrowRequest.reader_id = readerId;
            borrowRequest.book_id = bookId;
            borrowRequest.deleted = deleted;
            borrowRequest.due_date = dueDate;
            borrowRequest.updated_at=updated_at;
            try {
               borrowRequest.book= this.bookRepository.findBook(bookId);
               borrowRequest.reader= this.readerRepository.findReader(readerId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            

            list.add(borrowRequest);
        }
        return list;
    }
  
     public ArrayList<BorrowRequest> getHistoryBorrowRequestsByConstraint( ArrayList<String> constraint ) throws ClassNotFoundException, SQLException {
        if (conn == null) {
            conn = ConnectDatabase.getMySQLConnection();
        }
        ArrayList<BorrowRequest> list = new ArrayList<BorrowRequest>();
        String sql="SELECT br.* FROM borrow_request br left join reader r on r.id= br.reader_id   where br.deleted=0   ";
          sql= super.generateSqlWithConstraint(constraint, sql);
          sql += " order by updated_at desc ";
        PreparedStatement pstm = (PreparedStatement) conn.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            int readerId = rs.getInt("reader_id");
            int bookId = rs.getInt("book_id");
            int deleted = rs.getInt("deleted");
            Date dueDate = rs.getDate("due_date");
            Date updated_at= rs.getDate("updated_at");
            BorrowRequest borrowRequest = new BorrowRequest();
            borrowRequest.id = id;
            borrowRequest.reader_id = readerId;
            borrowRequest.book_id = bookId;
            borrowRequest.deleted = deleted;
            borrowRequest.due_date = dueDate;
            borrowRequest.updated_at=updated_at;
            try {
               borrowRequest.book= this.bookRepository.findBook(bookId);
               borrowRequest.reader= this.readerRepository.findReader(readerId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            

            list.add(borrowRequest);
        }
        return list;
    }
    
    // Add methods for updating and deleting borrow requests as needed
    public int updateBorrowRequest(BorrowRequest borrowRequest) throws SQLException, ClassNotFoundException {
        if (conn == null) {
            conn = ConnectDatabase.getMySQLConnection();
        }
        int result = 0;
        String sql = "UPDATE borrow_request SET reader_id=?, book_id=?, deleted=?, due_date=?, status=? WHERE id=?";
        PreparedStatement pstm = (PreparedStatement) conn.prepareStatement(sql);

        pstm.setInt(1, borrowRequest.reader_id);
        pstm.setInt(2, borrowRequest.book_id);
        pstm.setInt(3, borrowRequest.deleted);
        pstm.setDate(4, new java.sql.Date(borrowRequest.due_date.getTime()));
        pstm.setInt(5, borrowRequest.status);
        pstm.setInt(6, borrowRequest.id);

        result = pstm.executeUpdate();
        return result;
    }

    public int deleteBorrowRequest(int id) throws SQLException, ClassNotFoundException {
        if (conn == null) {
            conn = ConnectDatabase.getMySQLConnection();
        }
        int result = 0;
        String sql = "UPDATE borrow_request SET deleted=1 WHERE id=?";
        PreparedStatement pstm = (PreparedStatement) conn.prepareStatement(sql);
        pstm.setInt(1, id);
        result = pstm.executeUpdate();
        return result;
    }

}
