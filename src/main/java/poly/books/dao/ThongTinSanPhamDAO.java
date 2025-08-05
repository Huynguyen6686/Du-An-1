package poly.books.dao;

import java.util.List;
import poly.books.entity.ThongTinSanPham;
import poly.books.util.XQuery;

public class ThongTinSanPhamDAO {

    String getAllSQL = """
    SELECT 
            hd.MaHD,
            s.TenSach,
            s.GiaBan,
            cthd.SoLuong,
            (cthd.SoLuong * cthd.DonGia) AS ThanhTien
        FROM HoaDon hd
        JOIN ChiTietHoaDon cthd ON hd.MaHD = cthd.MaHD
        JOIN Sach s ON cthd.MaSach = s.MaSach
        LEFT JOIN PhieuGiamGia pgg ON hd.MaPhieu = pgg.MaPhieu 
        ORDER BY hd.MaHD, s.TenSach;;
                           """;
    String findBySQL = """
                            SELECT 
                                     hd.MaHD,
                                     s.TenSach,
                                     s.GiaBan,
                                     cthd.SoLuong,
                                     (cthd.SoLuong * cthd.DonGia) AS ThanhTien
                                 FROM HoaDon hd
                                 JOIN ChiTietHoaDon cthd ON hd.MaHD = cthd.MaHD
                                 JOIN Sach s ON cthd.MaSach = s.MaSach
                                 LEFT JOIN PhieuGiamGia pgg ON hd.MaPhieu = pgg.MaPhieu where s.ISBN = ?
                                 ORDER BY hd.MaHD, s.TenSach;
                           """;

    public List<ThongTinSanPham> getAll() {
        return XQuery.getBeanList(ThongTinSanPham.class, getAllSQL);
    }

    public List<ThongTinSanPham> findByISBN(String ISBN) {
        return XQuery.getBeanList(ThongTinSanPham.class, findBySQL, ISBN);
    }
}
