/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.books.dao;

import java.util.List;
import poly.books.entity.NhaXuatBan;
import poly.books.util.XJdbc;
import poly.books.util.XQuery;

/**
 *
 * @author LAPTOP
 */
public class NhaXuatBanDAO {

    String getAllSQL = """
                       SELECT  [MaNXB]
                             ,[TenNXB]
                         FROM [QLNhaSachPro].[dbo].[NhaXuatBan]
                       """;
    String findBySQL = """
                       SELECT * FROM [QLNhaSachPro].[dbo].[NhaXuatBan] where MaNXB = ?
                       """;
    String createSQL = """
                        INSERT INTO [dbo].[NhaXuatBan]
                                              ([TenNXB])
                                        VALUES
                                              (?)
                       """;
    String updateSQL = """
                         UPDATE [dbo].[NhaXuatBan]
                            SET [TenNXB] = ?
                          WHERE MaNXB = ?
                       """;
    String deleteSQL = """
                        DELETE FROM [dbo].[NhaXuatBan]
                              WHERE MaNXB = ?
                       """;
    String findByTenSQL = """
                       SELECT * FROM [QLNhaSachPro].[dbo].[NhaXuatBan] where TenNXB = ?
                       """;

    public List<NhaXuatBan> getAll() {
        return XQuery.getBeanList(NhaXuatBan.class, getAllSQL);
    }

    public NhaXuatBan findByID(int MaNXB) {
        return XQuery.getSingleBean(NhaXuatBan.class, findBySQL, MaNXB);
    }

    public NhaXuatBan findByTen(String tenNXB) {
        return XQuery.getSingleBean(NhaXuatBan.class, findByTenSQL, tenNXB);
    }

    public int create(NhaXuatBan nhaXuatBan) {
        Object[] rowData = {
            nhaXuatBan.getTenNXB()
        };
        return XJdbc.executeUpdate(createSQL, rowData);
    }

    public int update(NhaXuatBan nhaXuatBan) {
        Object[] rowData = {
            nhaXuatBan.getTenNXB(),
            nhaXuatBan.getMaNXB()
        };
        return XJdbc.executeUpdate(updateSQL, rowData);
    }

    public int delete(int id) {
        return XJdbc.executeUpdate(deleteSQL, id);
    }
}
