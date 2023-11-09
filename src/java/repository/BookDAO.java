package repository;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import model.Book;
import model.Category;
import static repository.ConnectDatabase.conn;
import service.BookBO;
import service.CategoryBO;




public class BookDAO {
	Connection conn = null;
	Statement st = null;
	PreparedStatement preSt = null;
	   CategoryBO categoryBO = new CategoryBO();
	   

	public Book findBook(int id) throws SQLException, ClassNotFoundException {
		if (conn == null)
			conn = ConnectDatabase.getMySQLConnection();
		String sql = "Select * from Book where id=?";
        
		PreparedStatement pstm = (PreparedStatement) conn.prepareStatement(sql);
		pstm.setInt(1, id);

		ResultSet rs = pstm.executeQuery();

		while (rs.next()) {
			String name = rs.getString("name");
			String category_id = rs.getString("category_id");
			Date date = rs.getDate("create_day");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String strDate = dateFormat.format(date);
			Category category = new Category();
			try {
				category = categoryBO.findCategory(category_id);
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String amount = rs.getString("amount");
			String image = rs.getString("image");
                        double price= rs.getDouble("price");
			Book book = new Book();
			book.setId(id);
			book.setName(name);
			book.setCategory(category);
			book.setAmount(amount);
			book.setImage(image);
			book.setDay(strDate);
                        book.price=price;
			return book;
		}
		return null;
	}

	public int insertBook(Book book) throws SQLException, ClassNotFoundException {
		if (conn == null)
			conn = ConnectDatabase.getMySQLConnection();
		try {
			st = (Statement) conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int result = 0;
		String insert = "INSERT INTO Book (name, category_id, amount, image) VALUES (?,?, ?, ?)";
		preSt = (PreparedStatement) conn.prepareStatement(insert);
		preSt.setString(1, book.getName());
		preSt.setString(2, Integer.toString(book.getCategory().getId()));
		preSt.setString(3, book.getAmount());
		preSt.setString(4, book.getImage());
		result = preSt.executeUpdate();
		System.out.println("Ketqua" + result);
		return result;
	}

	public ArrayList<Book> getAllBook() throws ClassNotFoundException, SQLException {
		if (conn == null)
			conn = ConnectDatabase.getMySQLConnection();
		ArrayList<Book> list = new ArrayList<Book>();
		String sql = "Select * from Book where deleted=0 ORDER BY create_day DESC";
		PreparedStatement pstm = (PreparedStatement) conn.prepareStatement(sql);
		ResultSet rs = pstm.executeQuery();

		while (rs.next()) {
			String id = rs.getString("id");
			String name = rs.getString("name");
			String category_id = rs.getString("category_id");
			Date date = rs.getDate("create_day");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String strDate = dateFormat.format(date);
			Category category = new Category();
			try {
				category = categoryBO.findCategory(category_id);
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String amount = rs.getString("amount");
			String image = rs.getString("image");
			Book book = new Book();
			book.setId(Integer.parseInt(id));
			book.setName(name);
			book.setCategory(category);
			book.setAmount(amount);
			book.setImage(image);
			book.setDay(strDate);
			list.add(book);
		}
		return list;
	}
	public ArrayList<Book> getSearchBook(String name_search) throws ClassNotFoundException, SQLException {
		if (conn == null)
			conn = ConnectDatabase.getMySQLConnection();
		System.out.println("Day"+name_search);
		ArrayList<Book> list = new ArrayList<Book>();
		String sql = "Select * from Book where name like '%"+name_search+"%';";
//		String sql = "Select * from Book where name like '%l';";
		PreparedStatement pstm = (PreparedStatement) conn.prepareStatement(sql);
		ResultSet rs = pstm.executeQuery();

		while (rs.next()) {
			String id = rs.getString("id");
			String name = rs.getString("name");
			String category_id = rs.getString("category_id");
			Date date = rs.getDate("create_day");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String strDate = dateFormat.format(date);
			Category category = new Category();
			try {
				category = categoryBO.findCategory(category_id);
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String amount = rs.getString("amount");
			String image = rs.getString("image");
			Book book = new Book();
			book.setId(Integer.parseInt(id));
			book.setName(name);
			book.setCategory(category);
			book.setAmount(amount);
			book.setImage(image);
			book.setDay(strDate);
			list.add(book);
		}
		System.out.println(list);
		return list;
	}

	public int updateBook(Book book) throws SQLException, ClassNotFoundException {
		
		int result = 0;
		if (conn == null)
			conn = ConnectDatabase.getMySQLConnection();
		String sql = "Update Book set name =?,category_id =?,amount =?,image =?  where id=? ";
                
		PreparedStatement pstm = (PreparedStatement) conn.prepareStatement(sql);
		
		pstm.setString(1, book.getName());
		pstm.setString(2, Integer.toString(book.getCategory().getId()));
		pstm.setString(3, book.getAmount());
		pstm.setString(4, book.getImage());
		pstm.setString(5,String.valueOf(book.getId()));
		result = pstm.executeUpdate();
		return result;
	}
<<<<<<< HEAD

public int deleteBook(int id) throws ClassNotFoundException, SQLException {
=======
	
        public int deleteBook(int id) throws ClassNotFoundException, SQLException {
>>>>>>> b5ecd520dbbc9499c4eba6437741d855ff5b27cd
            
                int result = 0;
                if (conn == null)
                    conn = ConnectDatabase.getMySQLConnection();
                try {
<<<<<<< HEAD
                    String delete = "update Book set deleted=1 where id= ?";
=======
                    String delete = "DELETE FROM Book WHERE id = ?";
>>>>>>> b5ecd520dbbc9499c4eba6437741d855ff5b27cd
                    preSt = (PreparedStatement) conn.prepareStatement(delete);
                    preSt.setInt(1, id);
                    result = preSt.executeUpdate();
                    System.out.println("Deleted: " + result);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return result;
            }

<<<<<<< HEAD
=======
	
>>>>>>> b5ecd520dbbc9499c4eba6437741d855ff5b27cd
}
