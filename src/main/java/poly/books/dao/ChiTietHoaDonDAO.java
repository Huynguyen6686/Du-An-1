/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.books.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import poly.books.entity.ChiTietHoaDon;
import poly.books.util.XJdbc;
import poly.books.util.XQuery;

/**
 *
 * @author LAPTOP
 */
public class ChiTietHoaDonDAO {

    String getAllSQL = """
                       SELECT TOP (1000) [MaHD]
                             ,[MaSach]
                             ,[SoLuong]
                             ,[DonGia]
                         FROM [QLNhaSachPro].[dbo].[ChiTietHoaDon]
                       """;
    String updateSQL = """
                      UPDATE [dbo].[ChiTietHoaDon]
                          SET 
                             [MaSach] = ?
                             ,[SoLuong] = ?
                             ,[DonGia] = ?
                        WHERE [MaHD] = ?
                       """;
    String deleteSQL = """
                      DELETE FROM [dbo].[ChiTietHoaDon]
                             WHERE MaHD = ?
                       """;
    String findBySQL = """
                       SELECT * FROM [QLNhaSachPro].[dbo].[ChiTietHoaDon] where MaHD = ?
                       """;

    public List<ChiTietHoaDon> getAll() {
        return XQuery.getBeanList(ChiTietHoaDon.class, getAllSQL);
    }

    public List<ChiTietHoaDon> findByID(int MaHD) {
        return XQuery.getBeanList(ChiTietHoaDon.class, findBySQL, MaHD);
    }

    public int update(ChiTietHoaDon chiTietHoaDon) {
        Object[] values = {
            chiTietHoaDon.getMaSach(),
            chiTietHoaDon.getSoLuong(),
            chiTietHoaDon.getDonGia(),
            chiTietHoaDon.getMaHD()
        };
        return XJdbc.executeUpdate(updateSQL, values);
    }

    public int delete(int MaHD) {
        return XJdbc.executeUpdate(deleteSQL, MaHD);
    }

    public void insert(ChiTietHoaDon chiTiet) throws SQLException {
        String sql = "INSERT INTO ChiTietHoaDon (MaHD, MaSach, SoLuong, DonGia) VALUES (?, ?, ?, ?)";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, chiTiet.getMaHD());
            ps.setInt(2, chiTiet.getMaSach());
            ps.setInt(3, chiTiet.getSoLuong());
            ps.setDouble(4, chiTiet.getDonGia());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Error in ChiTietHoaDonDAO.insert: " + e.getMessage());
            throw e;
        }
    }

    public int Delete(int maHD, int maSach) {
        String deleteSQL = """
                      DELETE FROM [dbo].[ChiTietHoaDon]
                      WHERE MaHD = ? AND MaSach = ?
                      """;
        try {
            int rowsAffected = XJdbc.executeUpdate(deleteSQL, maHD, maSach);
            System.out.println("Deleted ChiTietHoaDon: maHD=" + maHD + ", maSach=" + maSach + ", rowsAffected=" + rowsAffected);
            return rowsAffected;
        } catch (Exception e) {
            System.out.println("Error deleting ChiTietHoaDon: " + e.getMessage());
            throw e;
        }
    }
}
