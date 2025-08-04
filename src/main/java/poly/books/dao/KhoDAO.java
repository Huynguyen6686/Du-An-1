package poly.books.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import poly.books.util.XJdbc;

public class KhoDAO {

    public void updateSoLuong(int maSach, int soLuongThayDoi) throws SQLException {
        String sqlSelect = "SELECT SoLuong FROM Kho WHERE MaSach = ?";
        String sqlUpdate = "UPDATE Kho SET SoLuong = ? WHERE MaSach = ?";

        try (Connection conn = XJdbc.openConnection()) {
            // Lấy số lượng hiện tại
            PreparedStatement psSelect = conn.prepareStatement(sqlSelect);
            psSelect.setInt(1, maSach);
            ResultSet rs = psSelect.executeQuery();
            int soLuongHienTai = 0;
            if (rs.next()) {
                soLuongHienTai = rs.getInt("SoLuong");
            } else {
                throw new SQLException("Sản phẩm không tồn tại trong kho!");
            }

            // Tính số lượng mới
            int soLuongMoi = soLuongHienTai + soLuongThayDoi;
            if (soLuongMoi < 0) {
                throw new SQLException("Số lượng tồn kho không được âm!");
            }

            // Cập nhật số lượng
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setInt(1, soLuongMoi);
            psUpdate.setInt(2, maSach);
            psUpdate.executeUpdate();
        }
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
