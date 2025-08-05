package poly.books.dao;

import java.util.List;
import poly.books.entity.ThongTinSanPham;
import poly.books.util.XQuery;

public class ThongTinSanPhamDAO {

    String getAllSQL = """
             SELECT 
                        s.ISBN,
                        s.TenSach,
                        s.GiaBan,
                        cthd.SoLuong,
                        (cthd.SoLuong * cthd.DonGia) AS ThanhTien
                    FROM HoaDon hd
                    JOIN ChiTietHoaDon cthd ON hd.MaHD = cthd.MaHD
                    JOIN Sach s ON cthd.MaSach = s.MaSach
                    LEFT JOIN PhieuGiamGia pgg ON hd.MaPhieu = pgg.MaPhieu 
                    ORDER BY hd.MaHD, s.TenSach;
                           """;
    String findBySQL = """
                            SELECT 
                                             s.ISBN,
                                             s.TenSach,
                                             s.GiaBan,
                                             cthd.SoLuong,
                                             (cthd.SoLuong * cthd.DonGia) AS ThanhTien
                                         FROM HoaDon hd
                                         JOIN ChiTietHoaDon cthd ON hd.MaHD = cthd.MaHD
                                         JOIN Sach s ON cthd.MaSach = s.MaSach
                                         LEFT JOIN PhieuGiamGia pgg ON hd.MaPhieu = pgg.MaPhieu 
                                         WHERE hd.MaHD = ?
                                         ORDER BY s.TenSach;
                           """;

    public List<ThongTinSanPham> getAll() {
        return XQuery.getBeanList(ThongTinSanPham.class, getAllSQL);
    }

    public List<ThongTinSanPham> findByID(int MaHD) {
        return XQuery.getBeanList(ThongTinSanPham.class, findBySQL, MaHD);
    }
}
