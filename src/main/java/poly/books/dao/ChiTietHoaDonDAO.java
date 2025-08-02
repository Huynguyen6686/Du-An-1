/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.books.dao;

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
