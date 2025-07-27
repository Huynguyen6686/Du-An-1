/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.books.dao;

import java.sql.SQLException;
import java.util.List;
import poly.books.entity.KhachHang;
import poly.books.util.XJdbc;
import poly.books.util.XQuery;

/**
 *
 * @author LAPTOP
 */
public class KhachHangDAO {
    String getAllSQL = """
                       SELECT [MaKH] ,[TenKH],[SDT],[Email],[DiaChi]
                           FROM [QLNhaSachPro].[dbo].[KhachHang]
                       """;
    String findBySQL = """
                       SELECT * FROM [QLNhaSachPro].[dbo].[KhachHang] where MaKH = ?
                       """;
    String create = """
                       INSERT INTO [dbo].[KhachHang]
                                  ([TenKH]
                                    ,[SDT]
                                    ,[Email]
                                    ,[DiaChi])
                            VALUES
                                  (?,?,?,?)
                    SELECT SCOPE_IDENTITY();
                       """;
    String update = """
                   UPDATE [dbo].[KhachHang]
                      SET [TenKH] = ?
                          ,[SDT] = ?
                          ,[Email] = ?
                          ,[DiaChi] = ?
                    WHERE MaKH=?
                   """;
    String delete = """
                   DELETE FROM [dbo].[KhachHang]
                         WHERE MaKH=?
                   """;
    public List<KhachHang> getAll() {
        
        return XQuery.getBeanList(KhachHang.class, getAllSQL);
        
    }
    public KhachHang findbyID(int maLinhVuc) {
        
        return XQuery.getSingleBean(KhachHang.class, findBySQL, maLinhVuc);
    }
   public int create(KhachHang kh) throws SQLException {
        Object[] rowData = {
            kh.getTenKH(),
            kh.getSDT(),
            kh.getEmail(), // Đảm bảo đúng thứ tự cột so với câu lệnh INSERT
            kh.getDiaChi()
        };
        XJdbc.executeUpdate(create, rowData);
        
        // Lấy giá trị ID tự động
        Integer id = XJdbc.getIdentityValue("SELECT SCOPE_IDENTITY()", Integer.class);
        
        // Kiểm tra nếu id là null (nghĩa là không lấy được ID tự động)
        if (id == null) {
            throw new SQLException("Không thể lấy ID tự động sau khi thêm khách hàng.");
        }
        return id;
    }

    public int update(KhachHang kh) {
        Object[] rowData = {
            kh.getTenKH(),
            kh.getSDT(),
            kh.getEmail(),
            kh.getDiaChi(),
            kh.getMaKH()

        };
        return XJdbc.executeUpdate(update, rowData);
    }

    public int delete(int id) {
        return XJdbc.executeUpdate(delete, id);
    }
    
}
