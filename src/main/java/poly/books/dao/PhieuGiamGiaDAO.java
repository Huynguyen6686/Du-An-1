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
                       SELECT  [MaPhieu]
                             ,[GiaTri]
                             ,[DieuKienApDung]
                             ,[TrangThai]
                         FROM [QLNhaSachPro].[dbo].[PhieuGiamGia]
                       """;
    String createSQL = """
                       INSERT INTO [dbo].[PhieuGiamGia]
                                  ([GiaTri]
                                  ,[DieuKienApDung]
                                  ,[TrangThai])
                            VALUES
                                  (?,?,?,?)
                       """;

    String updateSQL = """
                       UPDATE [dbo].[PhieuGiamGia]
                          SET [GiaTri] = ?
                             ,[DieuKienApDung] = ?
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
            phieuGiamGia.getGiaTri(),
            phieuGiamGia.getDieuKienApDung(),
            phieuGiamGia.getTrangThai()
        };
        return XJdbc.executeUpdate(createSQL, values);
    }

    public int update(PhieuGiamGia phieuGiamGia) {
        Object[] values = {
            phieuGiamGia.getGiaTri(),
            phieuGiamGia.getDieuKienApDung(),
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
