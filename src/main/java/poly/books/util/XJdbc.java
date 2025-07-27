package poly.books.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp tiện ích hỗ trợ làm việc với CSDL quan hệ
 *
 * @author NghiemN
 * @version 1.0
 */
public class XJdbc {

    private static Connection connection;

    /**
     * Mở kết nối nếu chưa mở hoặc đã đóng
     *
     * @return Kết nối đã sẵn sàng
     */
    public static Connection openConnection() {
        var driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
       var dburl = "jdbc:sqlserver://localhost:1433;databaseName=QLNhaSachPro;encrypt=true;trustServerCertificate=true;";
        var username = "sa";
        var password = "123";
        try {
            if (!XJdbc.isReady()) {
                Class.forName(driver);
                connection = DriverManager.getConnection(dburl, username, password);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
public static <T> T getIdentityValue(String sql, Class<T> type) throws SQLException {
        try (var conn = poly.books.util.XJdbc.openConnection(); var stmt = conn.createStatement(); var rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                if (type == Long.class) {
                    return type.cast(rs.getLong(1));
                } else if (type == Integer.class) { // Thêm xử lý cho Integer.class
                    return type.cast(rs.getInt(1));
                }
                throw new UnsupportedOperationException("Chỉ hỗ trợ Long.class và Integer.class hiện tại");
            }
        }
        return null; // Trả về null nếu không có kết quả
    }
    /**
     * Đóng kết nối
     */
    public static void closeConnection() {
        try {
            if (XJdbc.isReady()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Kiểm tra kết nối đã sẵn sàng hay chưa
     *
     * @return true nếu kết nối đã được mở
     */
    public static boolean isReady() {
        try {
            return (connection != null && !connection.isClosed());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Thao tác dữ liệu
     *
     * @param sql câu lệnh SQL (INSERT, UPDATE, DELETE)
     * @param values các giá trị cung cấp cho các tham số trong SQL
     * @return số lượng bản ghi đã thực hiện
     * @throws RuntimeException không thực thi được câu lệnh SQL
     */
    public static int executeUpdate(String sql, Object... values) {
        try {
            var stmt = XJdbc.getStmt(sql, values);
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Truy vấn dữ liệu
     *
     * @param sql câu lệnh SQL (SELECT)
     * @param values các giá trị cung cấp cho các tham số trong SQL
     * @return tập kết quả truy vấn
     * @throws RuntimeException không thực thi được câu lệnh SQL
     */
    public static ResultSet executeQuery(String sql, Object... values) {
        try {
            var stmt = XJdbc.getStmt(sql, values);
            return stmt.executeQuery();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Truy vấn một giá trị
     *
     * @param <T> kiểu dữ liệu kết quả
     * @param sql câu lệnh SQL (SELECT)
     * @param values các giá trị cung cấp cho các tham số trong SQL
     * @return giá trị truy vấn hoặc null
     * @throws RuntimeException không thực thi được câu lệnh SQL
     */
    public static <T> T getValue(String sql, Object... values) {
        try {
            var resultSet = XJdbc.executeQuery(sql, values);
            if (resultSet.next()) {
                return (T) resultSet.getObject(1);
            }
            return null;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Tạo PreparedStatement từ câu lệnh SQL/PROC
     *
     * @param sql câu lệnh SQL/PROC
     * @param values các giá trị cung cấp cho các tham số trong SQL/PROC
     * @return đối tượng đã tạo
     * @throws SQLException không tạo được PreparedStatement
     */
    private static PreparedStatement getStmt(String sql, Object... values) throws SQLException {
        var conn = XJdbc.openConnection();
        var stmt = sql.trim().startsWith("{") ? conn.prepareCall(sql) : conn.prepareStatement(sql);
        for (int i = 0; i < values.length; i++) {
            stmt.setObject(i + 1, values[i]);
        }
        return stmt;
    }

    public static void main(String[] args) {
        demo1();
        demo2();
        demo3();
    }

    private static void demo1() {
        String sql = "SELECT * FROM Drinks WHERE UnitPrice BETWEEN ? AND ?";
        var rs = XJdbc.executeQuery(sql, 1.5, 5.0);
    }

    private static void demo2() {
        String sql = "SELECT max(UnitPrice) FROM Drinks WHERE UnitPrice > ?";
        var maxPrice = XJdbc.getValue(sql, 1.5);
    }

    private static void demo3() {
        String sql = "DELETE FROM Drinks WHERE UnitPrice < ?";
        var count = XJdbc.executeUpdate(sql, 0.0);
    }

    public static <T> List<T> getBeanList(Class<T> clazz, String sql, Object... values) {
        try {
            ResultSet rs = executeQuery(sql, values);
            List<T> list = new ArrayList<>();
            var metadata = rs.getMetaData();
            while (rs.next()) {
                T bean = clazz.getDeclaredConstructor().newInstance();
                for (int i = 1; i <= metadata.getColumnCount(); i++) {
                    String column = metadata.getColumnLabel(i);
                    Object value = rs.getObject(i);
                    try {
                        var field = clazz.getDeclaredField(column);
                        field.setAccessible(true);
                        field.set(bean, value);
                    } catch (NoSuchFieldException ignored) {
                    }
                }
                list.add(bean);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

// Truy vấn 1 dòng và ánh xạ sang 1 Bean
    public static <T> T getSingleBean(Class<T> clazz, String sql, Object... values) {
        List<T> list = getBeanList(clazz, sql, values);
        return list.isEmpty() ? null : list.get(0);
    }
}
