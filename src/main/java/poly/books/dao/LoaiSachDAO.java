/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.books.dao;

import java.util.List;
import poly.books.entity.LoaiSach;
import poly.books.util.XJdbc;
import poly.books.util.XQuery;

/**
 *
 * @author LAPTOP
 */
public class LoaiSachDAO {

    String getAllSQL = """
                       SELECT  [MaLoaiSach]
                             ,[TenLoaiSach]
                         FROM [QLNhaSachPro].[dbo].[LoaiSach]
                       """;
    String findBySQL = """
                       SELECT * FROM [QLNhaSachPro].[dbo].[LoaiSach] where MaLoaiSach = ?
                       """;
    String create = """
                   INSERT INTO [dbo].[LoaiSach]
                                   ([TenLoaiSach])
                             VALUES (?)
                   """;
    String update = """
                  UPDATE [dbo].[LoaiSach]
                      SET [TenLoaiSach] = ?
                    WHERE MaLoaiSach=?
                   """;
    String delete = """
                   DELETE FROM [dbo].[LoaiSach]
                         WHERE MaLoaiSach=?
                   """;

    public List<LoaiSach> getAll() {
        return XQuery.getBeanList(LoaiSach.class, getAllSQL);
    }

    public LoaiSach findByID(int MaLoaiSach) {
        return XQuery.getSingleBean(LoaiSach.class, findBySQL, MaLoaiSach);
    }

    public int create(LoaiSach n) {
        Object[] rowData = {
            n.getTenLoaiSach()
        };
        return XJdbc.executeUpdate(create, rowData);
    }

    public int update(LoaiSach n) {
        Object[] rowData = {
            n.getTenLoaiSach(),
            n.getMaLoaiSach()

        };
        return XJdbc.executeUpdate(update, rowData);
    }

    public int delete(int id) {
        return XJdbc.executeUpdate(delete, id);
    }
    public List<LoaiSach> findBySachID(int maSach) {
    String sql = "SELECT ls.* FROM LoaiSach ls " +
                 "JOIN Sach_LoaiSach sls ON ls.MaLoaiSach = sls.MaLoaiSach " +
                 "WHERE sls.MaSach = ?";
    return XQuery.getBeanList(LoaiSach.class, sql, maSach);
}
}
