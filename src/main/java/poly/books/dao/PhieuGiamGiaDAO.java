/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.books.dao;

import java.util.List;
import poly.books.entity.PhieuGiamGia;
import poly.books.util.XJdbc;
import poly.books.util.XQuery;

/**
 *
 * @author LAPTOP
 */
public class PhieuGiamGiaDAO {

    String getAllSQL = """
                       SELECT [MaPhieu]
                                 ,[TenPhieu]      
                                 ,[GiaTri]
                                 ,[DieuKienApDung]
                                 ,[NgayBatDau]
                                 ,[NgayKetThuc]
                                 ,[TrangThai]
                             FROM [dbo].[PhieuGiamGia]
                       """;
    String createSQL = """
                       INSERT INTO [dbo].[PhieuGiamGia]
                                   ([TenPhieu]
                                  ,[GiaTri]
                                  ,[DieuKienApDung]
                                  ,[NgayBatDau]
                                  ,[NgayKetThuc]
                                  ,[TrangThai])
                            VALUES
                                  (?,?,?,?,?,?)
                       """;

    String updateSQL = """
                       UPDATE [dbo].[PhieuGiamGia]
                          SET [TenPhieu] = ?
                             ,[GiaTri] = ?
                             ,[DieuKienApDung] = ?
                             ,[NgayBatDau] = ?
                             ,[NgayKetThuc] = ?
                             ,[TrangThai] = ?
                        WHERE MaPhieu = ?
                       """;
    String deleteSQL = """
                       DELETE FROM [dbo].[PhieuGiamGia]
                             WHERE MaPhieu = ?
                       """;
    String findBySQL = """
                       SELECT *FROM [QLNhaSachPro].[dbo].[PhieuGiamGia] where MaPhieu = ?
                       """;

    public List<PhieuGiamGia> getAll() {
        return XQuery.getBeanList(PhieuGiamGia.class, getAllSQL);
    }

    public int create(PhieuGiamGia phieuGiamGia) {
        Object[] values = {
            phieuGiamGia.getTenPhieu(), 
            phieuGiamGia.getGiaTri(),
            phieuGiamGia.getDieuKienApDung(),
            phieuGiamGia.getNgayBatDau(),
            phieuGiamGia.getNgayKetThuc(),
            phieuGiamGia.getTrangThai()
        };
        return XJdbc.executeUpdate(createSQL, values);
    }

    public int update(PhieuGiamGia phieuGiamGia) {
        Object[] values = {
            phieuGiamGia.getTenPhieu(),
            phieuGiamGia.getGiaTri(),
            phieuGiamGia.getDieuKienApDung(),
            phieuGiamGia.getNgayBatDau(),
            phieuGiamGia.getNgayKetThuc(),
            phieuGiamGia.getTrangThai(),
            phieuGiamGia.getMaPhieu()
        };
        return XJdbc.executeUpdate(updateSQL, values);
    }

    public int delete(int MaPhieu) {
        return XJdbc.executeUpdate(deleteSQL, MaPhieu);
    }

    public List<PhieuGiamGia> findByID(int MaPhieu) {
        return XQuery.getBeanList(PhieuGiamGia.class, findBySQL, MaPhieu);
    }
}
