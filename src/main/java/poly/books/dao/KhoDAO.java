package poly.books.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import poly.books.util.XJdbc;

public class KhoDAO {

    public int updateSoLuong(int maSach, int soLuong) {
        String sql = "UPDATE [dbo].[Kho] SET SoLuong = SoLuong + ? WHERE MaSach = ?";
        return XJdbc.executeUpdate(sql, soLuong, maSach);
    }

    public int getSoLuong(int maSach) {
        String sql = "SELECT SoLuong FROM [dbo].[Kho] WHERE MaSach = ?";
        Integer result = (Integer) XJdbc.getValue(sql, maSach);
        return result != null ? result : 0;
    }
    public void updateSoLuongWithConnection(Connection conn, int maSach, int soLuong) throws SQLException {
    String sql = "UPDATE Kho SET SoLuong = SoLuong + ? WHERE MaSach = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, soLuong);
        ps.setInt(2, maSach);
        ps.executeUpdate();
    }
}
}
