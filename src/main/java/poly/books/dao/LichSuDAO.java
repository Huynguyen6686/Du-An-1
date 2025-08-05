package poly.books.dao;

import java.util.List;
import poly.books.entity.LichSuEntity;
import poly.books.util.XQuery;

public class LichSuDAO {

    String getAllSQL = """
SELECT DISTINCT
    hd.MaHD,
    ndsd.TenDangNhap,
    ndsd.HoTen,
    kh.TenKH,
    hd.NgayThanhToan,
    hd.PhuongThuc,
   hd.TongTien as ThanhTien,
    ISNULL(pgg.GiaTri, 0) AS Giam,
    (hd.TongTien- ISNULL(pgg.GiaTri, 0)) AS GiaSauKhiGiam,
    hd.TrangThai
FROM HoaDon hd
JOIN NguoiDungSD ndsd ON hd.TenDangNhap = ndsd.TenDangNhap
JOIN KhachHang kh ON kh.MaKH = hd.MaKH
LEFT JOIN ChiTietHoaDon cthd ON hd.MaHD = cthd.MaHD
LEFT JOIN Sach s ON cthd.MaSach = s.MaSach
LEFT JOIN PhieuGiamGia pgg ON hd.MaPhieu = pgg.MaPhieu;
                       """;

    public List<LichSuEntity> getAll() {
        return XQuery.getBeanList(LichSuEntity.class, getAllSQL);
    }
}
