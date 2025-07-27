/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.books.dao;

import java.util.List;
import poly.books.entity.NgonNgu;
import poly.books.util.XJdbc;
import poly.books.util.XQuery;

/**
 *
 * @author LAPTOP
 */
public class NgonNguDAO {

    String getAllSQL = """
                       SELECT [MaNgonNgu]
                             ,[TenNgonNgu]
                         FROM [QLNhaSachPro].[dbo].[NgonNgu]
                       """;
    String findBySQL = """
                       SELECT * FROM [QLNhaSachPro].[dbo].[NgonNgu] where MaNgonNgu = ?
                       """;
    String create = """
                   INSERT INTO [dbo].[NgonNgu]
                              ([TenNgonNgu])
                        VALUES (?)
                   """;
    String update = """
                   UPDATE [dbo].[NgonNgu]
                      SET [TenNgonNgu] = ?
                    WHERE MaNgonNgu=?
                   """;
    String delete = """
                   DELETE FROM [dbo].[NgonNgu]
                         WHERE MaNgonNgu=?
                   """;
    String findByTenSQL = """
                       SELECT * FROM [QLNhaSachPro].[dbo].[NgonNgu] where TenNgonNgu = ?
                       """;
    public List<NgonNgu> getAll() {
        return XQuery.getBeanList(NgonNgu.class, getAllSQL);
    }
    public NgonNgu findByTen(String tenNgonNgu) {
        return XQuery.getSingleBean(NgonNgu.class, findByTenSQL, tenNgonNgu);
    }
    public NgonNgu findByID(int MaNgonNgu) {
        return XQuery.getSingleBean(NgonNgu.class, findBySQL, MaNgonNgu);
    }

    public int create(NgonNgu n) {
        Object[] rowData = {
            n.getTenNgonNgu()
        };
        return XJdbc.executeUpdate(create, rowData);
    }

    public int update(NgonNgu n) {
        Object[] rowData = {
            n.getTenNgonNgu(),
            n.getMaNgonNgu()

        };
        return XJdbc.executeUpdate(update, rowData);
    }

    public int delete(int id) {
        return XJdbc.executeUpdate(delete, id);
    }
}
