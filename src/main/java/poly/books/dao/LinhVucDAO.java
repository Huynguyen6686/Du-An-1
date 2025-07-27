
package poly.books.dao;

import java.util.List;
import poly.books.entity.LinhVuc;
import poly.books.util.XJdbc;
import poly.books.util.XQuery;

public class LinhVucDAO {
    String getAllSQL = """
                       SELECT [MaLinhVuc]
                               ,[TenLinhVuc]
                           FROM [QLNhaSachPro].[dbo].[LinhVuc]
                       """;
    String findBySQL = """
                       SELECT * FROM [QLNhaSachPro].[dbo].[LinhVuc] where MaLinhVuc = ?
                       """;
    String create = """
                   INSERT INTO [dbo].[LinhVuc]
                              ([TenLinhVuc])
                        VALUES (?)
                   """;
    String update = """
                   UPDATE [dbo].[LinhVuc]
                      SET [TenLinhVuc] = ?
                    WHERE MaLinhVuc=?
                   """;
    String delete = """
                   DELETE FROM [dbo].[LinhVuc]
                         WHERE MaLinhVuc=?
                   """;
    public List<LinhVuc> getAll() {
        
        return XQuery.getBeanList(LinhVuc.class, getAllSQL);
        
    }
    public LinhVuc findbyID(int maLinhVuc) {
        return XQuery.getSingleBean(LinhVuc.class, findBySQL, maLinhVuc);
    }
    public int create(LinhVuc lv) {
        Object[] rowData = {
            lv.getTenLinhVuc()
        };
        return XJdbc.executeUpdate(create, rowData);
    }

    public int update(LinhVuc lv) {
        Object[] rowData = {
            lv.getTenLinhVuc(),
            lv.getMaLinhVuc()

        };
        return XJdbc.executeUpdate(update, rowData);
    }

    public int delete(int id) {
        return XJdbc.executeUpdate(delete, id);
    }
    public List<LinhVuc> findByLinhVucID(int maLinhVuc) {
    String sql = "SELECT lv.* FROM LinhVuc lv " +
                 "JOIN Sach_LinhVuc slv ON lv.MaLinhVuc = slv.MaLinhVuc " +
                 "WHERE slv.MaSach = ?";
    return XQuery.getBeanList(LinhVuc.class, sql, maLinhVuc);
}    
}
