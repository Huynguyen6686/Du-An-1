package poly.books.util;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XDate {

    public static final String PATTERN_FULL = "MM-dd-yyyy HH:mm:ss";
    public static final String PATTERN_SHORT = "MM/dd/yyyy";

    private static final SimpleDateFormat formater = new SimpleDateFormat();

    public static Date now() {
        return new Date();
        
    }

    public static Date parse(String dateTime, String pattern) {
        formater.applyLocalizedPattern(pattern);
        try {
            return formater.parse(dateTime);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parse(String dateTime) {
        return parse(dateTime, PATTERN_SHORT);
    }

    public static String format(Date dateTime, String pattern) {
        if (dateTime == null) {
            return "";
        }
        formater.applyPattern(pattern);
        return formater.format(dateTime);
    }

    public static String format(Date dateTime) {
        return format(dateTime, PATTERN_SHORT);
    }

    public static void main(String[] args) {
        Date date = XDate.parse("Jan 21, 2024", "MM dd, yyyy");
        String text = XDate.format(date, "dd-MM-yyyy");
        System.out.println(text); // => 21-Jan-2024
    }

    public static <T> T getValue(String sql, Class<T> clazz) throws SQLException {
        try (var conn = poly.books.util.XJdbc.openConnection(); var stmt = conn.createStatement(); var rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                if (clazz == Long.class) {
                    return clazz.cast(rs.getLong(1));
                }
                throw new UnsupportedOperationException("Chỉ hỗ trợ Long.class hiện tại");
            }
        }
        return null;
    }
}
