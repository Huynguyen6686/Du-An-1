package poly.books.dao;

import java.util.List;
import poly.books.entity.LichSuEntity;
import poly.books.util.XQuery;

public class LichSuDAO {

    String getAllSQL = """
SELECT 
                         s.ISBN,
                         ndsd.TenDangNhap,
                         ndsd.HoTen,
                         ndsd.QuanLy,
                         kh.TenKH,
                         hd.NgayThanhToan,
                     	hd.PhuongThuc,
                         (cthd.SoLuong * cthd.DonGia) AS ThanhTien,
                         ISNULL(pgg.GiaTri, 0) AS Giam,
                         (cthd.SoLuong * cthd.DonGia - ISNULL(pgg.GiaTri, 0)) AS GiaSauKhiGiam,
                         hd.TrangThai
                     FROM HoaDon hd
                     JOIN NguoiDungSD ndsd ON hd.TenDangNhap = ndsd.TenDangNhap
                     JOIN KhachHang kh ON kh.MaKH = hd.MaKH
                     JOIN ChiTietHoaDon cthd ON hd.MaHD = cthd.MaHD
                     JOIN Sach s ON cthd.MaSach = s.MaSach
                     LEFT JOIN PhieuGiamGia pgg ON hd.MaPhieu = pgg.MaPhieu;
                       """;

    public List<LichSuEntity> getAll() {
        return XQuery.getBeanList(LichSuEntity.class, getAllSQL);
    }
}
