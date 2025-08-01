/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.books.dao;

import java.util.Date;
import java.util.List;

import poly.books.entity.DoanhThuNgay;
import poly.books.entity.DoanhThuNhanVien;
import poly.books.entity.DoanhThuQuy;
import poly.books.entity.DoanhThuSanPham;
import poly.books.entity.DoanhThuThang;
import poly.books.entity.DoanhThuTheoNam;
import poly.books.entity.LoiNhuanNam;
import poly.books.entity.ThongKeHomNay;
import poly.books.entity.TongDoanhThu;
import poly.books.entity.TongDoanhThuNamNay;
import poly.books.util.XQuery;

/**
 *
 * @author ADMIN
 */
public class DoanhThuDAO {

    // 1. Tổng doanh thu theo ngày
    String doanhThuTheoNgaySQL = """
                              SELECT 
                                  CONVERT(DATE, NgayThanhToan) AS Ngay,
                                  SUM(TongTien) AS TongDoanhThu,
                                  COUNT(MaHD) AS SoHoaDon
                              FROM HoaDon
                              WHERE TrangThai = 1 -- Đã thanh toán
                              GROUP BY CONVERT(DATE, NgayThanhToan)
                              ORDER BY Ngay DESC;
                              """;
    String doanhThuTheoKhoangNgaySQL = """
                          SELECT 
                              CONVERT(DATE, NgayThanhToan) AS Ngay,
                              SUM(TongTien) AS TongDoanhThu,
                              COUNT(MaHD) AS SoHoaDon
                          FROM HoaDon
                          WHERE TrangThai = 1 
                          AND CONVERT(DATE, NgayThanhToan) >= ?
                          AND CONVERT(DATE, NgayThanhToan) <= ?
                          GROUP BY CONVERT(DATE, NgayThanhToan)
                          ORDER BY Ngay DESC;
                          """;
    // 2. Tổng hóa đơn và doanh thu hôm nay
    String thongKeHomNaySQL = """
                              SELECT 
                                  COUNT(MaHD) AS SoHoaDonHomNay,
                                  SUM(TongTien) AS DoanhThuHomNay
                              FROM HoaDon
                              WHERE CONVERT(DATE, NgayThanhToan) = CONVERT(DATE, GETDATE())
                              AND TrangThai = 1;
                              """;

    // 3. Lợi nhuận năm nay
    String loiNhuanNamSQL = """
                              SELECT 
                                  YEAR(NgayThanhToan) AS Nam,
                                  SUM(TongTien) AS DoanhThu,
                                  SUM(TongTien * 0.3) AS LoiNhuan
                              FROM HoaDon
                              WHERE YEAR(NgayThanhToan) = YEAR(GETDATE())
                              AND TrangThai = 1
                              GROUP BY YEAR(NgayThanhToan);
                              """;

    // 4. Doanh thu từng sản phẩm
    String doanhThuSanPhamSQL = """
                              SELECT 
                                  s.MaSach,
                                  s.TenSach,
                                  SUM(ct.SoLuong) AS SoLuongBan,
                                  SUM(ct.SoLuong * ct.DonGia) AS DoanhThu
                              FROM ChiTietHoaDon ct
                              JOIN Sach s ON ct.MaSach = s.MaSach
                              JOIN HoaDon hd ON ct.MaHD = hd.MaHD
                              WHERE hd.TrangThai = 1
                              GROUP BY s.MaSach, s.TenSach
                              ORDER BY DoanhThu DESC;
                              """;
    String doanhThuSanPhamHomNaySQL = """
    SELECT 
        s.MaSach,
        s.TenSach,
        SUM(ct.SoLuong) AS SoLuongBan,
        SUM(ct.SoLuong * ct.DonGia) AS DoanhThu
    FROM ChiTietHoaDon ct
    JOIN Sach s ON ct.MaSach = s.MaSach
    JOIN HoaDon hd ON ct.MaHD = hd.MaHD
    WHERE hd.TrangThai = 1
    AND CONVERT(DATE, hd.NgayThanhToan) = CONVERT(DATE, GETDATE())
    GROUP BY s.MaSach, s.TenSach
    ORDER BY DoanhThu DESC;
""";

// SQL query cho doanh thu sản phẩm tháng này
    String doanhThuSanPhamThangNaySQL = """
    SELECT 
        s.MaSach,
        s.TenSach,
        SUM(ct.SoLuong) AS SoLuongBan,
        SUM(ct.SoLuong * ct.DonGia) AS DoanhThu
    FROM ChiTietHoaDon ct
    JOIN Sach s ON ct.MaSach = s.MaSach
    JOIN HoaDon hd ON ct.MaHD = hd.MaHD
    WHERE hd.TrangThai = 1
    AND YEAR(hd.NgayThanhToan) = YEAR(GETDATE())
    AND MONTH(hd.NgayThanhToan) = MONTH(GETDATE())
    GROUP BY s.MaSach, s.TenSach
    ORDER BY DoanhThu DESC;
""";

// SQL query cho doanh thu sản phẩm năm nay
    String doanhThuSanPhamNamNaySQL = """
    SELECT 
        s.MaSach,
        s.TenSach,
        SUM(ct.SoLuong) AS SoLuongBan,
        SUM(ct.SoLuong * ct.DonGia) AS DoanhThu
    FROM ChiTietHoaDon ct
    JOIN Sach s ON ct.MaSach = s.MaSach
    JOIN HoaDon hd ON ct.MaHD = hd.MaHD
    WHERE hd.TrangThai = 1
    AND YEAR(hd.NgayThanhToan) = YEAR(GETDATE())
    GROUP BY s.MaSach, s.TenSach
    ORDER BY DoanhThu DESC;
""";

// SQL query cho doanh thu sản phẩm theo khoảng ngày
    String doanhThuSanPhamTheoKhoangNgaySQL = """
    SELECT 
        s.MaSach,
        s.TenSach,
        SUM(ct.SoLuong) AS SoLuongBan,
        SUM(ct.SoLuong * ct.DonGia) AS DoanhThu
    FROM ChiTietHoaDon ct
    JOIN Sach s ON ct.MaSach = s.MaSach
    JOIN HoaDon hd ON ct.MaHD = hd.MaHD
    WHERE hd.TrangThai = 1
    AND CONVERT(DATE, hd.NgayThanhToan) >= ?
    AND CONVERT(DATE, hd.NgayThanhToan) <= ?
    GROUP BY s.MaSach, s.TenSach
    ORDER BY DoanhThu DESC;
""";
    // 5. Doanh thu từng nhân viên
    String doanhThuNhanVienSQL = """
                              SELECT 
                                  nd.TenDangNhap,
                                  nd.HoTen,
                                  COUNT(hd.MaHD) AS SoHoaDon,
                                  SUM(hd.TongTien) AS TongDoanhThu
                              FROM HoaDon hd
                              JOIN NguoiDungSD nd ON hd.TenDangNhap = nd.TenDangNhap
                              WHERE hd.TrangThai = 1
                              GROUP BY nd.TenDangNhap, nd.HoTen
                              ORDER BY TongDoanhThu DESC;
                              """;
    String doanhThuNhanVienTheoNgaySQL = """
                              SELECT 
                                  nd.TenDangNhap,
                                  nd.HoTen,
                                  COUNT(hd.MaHD) AS SoHoaDon,
                                  SUM(hd.TongTien) AS TongDoanhThu
                              FROM HoaDon hd
                              JOIN NguoiDungSD nd ON hd.TenDangNhap = nd.TenDangNhap
                              WHERE hd.TrangThai = 1
                              AND CONVERT(DATE, hd.NgayThanhToan) >= ? 
                              AND CONVERT(DATE, hd.NgayThanhToan) <= ?
                              GROUP BY nd.TenDangNhap, nd.HoTen
                              ORDER BY TongDoanhThu DESC;
                              """;
    String doanhThuNhanVienHomNaySQL = """
                          SELECT 
                              nd.TenDangNhap,
                              nd.HoTen,
                              COUNT(hd.MaHD) AS SoHoaDon,
                              SUM(hd.TongTien) AS TongDoanhThu
                          FROM HoaDon hd
                          JOIN NguoiDungSD nd ON hd.TenDangNhap = nd.TenDangNhap
                          WHERE hd.TrangThai = 1
                          AND CONVERT(DATE, hd.NgayThanhToan) = CONVERT(DATE, GETDATE())
                          GROUP BY nd.TenDangNhap, nd.HoTen
                          ORDER BY TongDoanhThu DESC;
                          """;

    String doanhThuNhanVienThangNaySQL = """
                          SELECT 
                              nd.TenDangNhap,
                              nd.HoTen,
                              COUNT(hd.MaHD) AS SoHoaDon,
                              SUM(hd.TongTien) AS TongDoanhThu
                          FROM HoaDon hd
                          JOIN NguoiDungSD nd ON hd.TenDangNhap = nd.TenDangNhap
                          WHERE hd.TrangThai = 1
                          AND YEAR(hd.NgayThanhToan) = YEAR(GETDATE())
                          AND MONTH(hd.NgayThanhToan) = MONTH(GETDATE())
                          GROUP BY nd.TenDangNhap, nd.HoTen
                          ORDER BY TongDoanhThu DESC;
                          """;

    String doanhThuNhanVienNamNaySQL = """
                          SELECT 
                              nd.TenDangNhap,
                              nd.HoTen,
                              COUNT(hd.MaHD) AS SoHoaDon,
                              SUM(hd.TongTien) AS TongDoanhThu
                          FROM HoaDon hd
                          JOIN NguoiDungSD nd ON hd.TenDangNhap = nd.TenDangNhap
                          WHERE hd.TrangThai = 1
                          AND YEAR(hd.NgayThanhToan) = YEAR(GETDATE())
                          GROUP BY nd.TenDangNhap, nd.HoTen
                          ORDER BY TongDoanhThu DESC;
                          """;
    // 6. Doanh thu theo tháng
    String doanhThuTheoThangSQL = """
                              SELECT 
                                  YEAR(NgayThanhToan) AS Nam,
                                  MONTH(NgayThanhToan) AS Thang,
                                  SUM(TongTien) AS DoanhThu,
                                  COUNT(MaHD) AS SoHoaDon
                              FROM HoaDon
                              WHERE TrangThai = 1
                              GROUP BY YEAR(NgayThanhToan), MONTH(NgayThanhToan)
                              ORDER BY Nam, Thang;
                              """;

    // Doanh thu theo quý
    String doanhThuTheoQuySQL = """
                              SELECT 
                                  YEAR(NgayThanhToan) AS Nam,
                                  DATEPART(QUARTER, NgayThanhToan) AS Quy,
                                  SUM(TongTien) AS DoanhThu,
                                  COUNT(MaHD) AS SoHoaDon
                              FROM HoaDon
                              WHERE TrangThai = 1
                              GROUP BY YEAR(NgayThanhToan), DATEPART(QUARTER, NgayThanhToan)
                              ORDER BY Nam, Quy;
                              """;

    // Doanh thu theo năm
    String doanhThuTheoNamSQL = """
                              SELECT 
                                  YEAR(NgayThanhToan) AS Nam,
                                  SUM(TongTien) AS DoanhThu,
                                 COUNT(MaHD) AS SoHoaDon
                              FROM HoaDon
                              WHERE TrangThai = 1
                              GROUP BY YEAR(NgayThanhToan)
                              ORDER BY Nam;
                              """;

    // 7. Thống kê tổng hợp hôm nay
    String tongHopHomNaySQL = """
                              SELECT 
                                  COUNT(DISTINCT hd.MaHD) AS SoHoaDon,
                                  SUM(hd.TongTien) AS DoanhThu,
                                  SUM(ct.SoLuong) AS SoLuongSachBan,
                                  COUNT(DISTINCT ct.MaSach) AS SoLoaiSachBan
                              FROM HoaDon hd
                              JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD
                              WHERE CONVERT(DATE, hd.NgayThanhToan) = CONVERT(DATE, GETDATE())
                              AND hd.TrangThai = 1;
                              """;

    // Tổng doanh thu
    String tongDoanhThuSQL = """
                              SELECT 
                                  SUM(TongTien) AS TongDoanhThu,
                                  COUNT(MaHD) AS TongSoHoaDon
                              FROM HoaDon
                              WHERE TrangThai = 1;
                              """;

    public TongDoanhThuNamNay getTongDoanhThuNamNay() {
        String sql = """
    SELECT SUM(TongTien) AS tongDoanhThuNamNay
    FROM HoaDon
    WHERE YEAR(NgayThanhToan) = YEAR(GETDATE())
    AND TrangThai = 1
    """;
        return XQuery.getSingleBean(TongDoanhThuNamNay.class, sql);
    }

    public List<DoanhThuNgay> getAllDoanhThuNgay() {
        return XQuery.getBeanList(DoanhThuNgay.class, doanhThuTheoNgaySQL);
    }

    public List<DoanhThuNgay> getDoanhThuTheoKhoangNgay(Date tuNgay, Date denNgay) {
        return XQuery.getBeanList(DoanhThuNgay.class, doanhThuTheoKhoangNgaySQL, tuNgay, denNgay);
    }

    public ThongKeHomNay getThongKeHomNay() {
        return XQuery.getSingleBean(ThongKeHomNay.class, thongKeHomNaySQL);
    }

    public List<LoiNhuanNam> getLoiNhuanNam() {
        return XQuery.getBeanList(LoiNhuanNam.class, loiNhuanNamSQL);
    }

    public List<DoanhThuSanPham> getAllDoanhThuSanPham() {
        return XQuery.getBeanList(DoanhThuSanPham.class, doanhThuSanPhamSQL);
    }

    public List<DoanhThuNhanVien> getAllDoanhThuNhanVien() {
        return XQuery.getBeanList(DoanhThuNhanVien.class, doanhThuNhanVienSQL);
    }

    public List<DoanhThuThang> getAllDoanhThuThang() {
        return XQuery.getBeanList(DoanhThuThang.class, doanhThuTheoThangSQL);
    }

    public List<DoanhThuQuy> getAllDoanhThuQuy() {
        return XQuery.getBeanList(DoanhThuQuy.class, doanhThuTheoQuySQL);
    }

    public List<DoanhThuTheoNam> getAllDoanhThuTheoNam() {
        return XQuery.getBeanList(DoanhThuTheoNam.class, doanhThuTheoNamSQL);
    }

    public List<ThongKeHomNay> getTongHopHomNay() {
        return XQuery.getBeanList(ThongKeHomNay.class, tongHopHomNaySQL);
    }

    public List<TongDoanhThu> getAllTongDoanhThu() {
        return XQuery.getBeanList(TongDoanhThu.class, tongDoanhThuSQL);
    }

    // Phương thức mới - Doanh thu nhân viên theo khoảng ngày
    public List<DoanhThuNhanVien> getDoanhThuNhanVienTheoNgay(Date tuNgay, Date denNgay) {
        return XQuery.getBeanList(DoanhThuNhanVien.class, doanhThuNhanVienTheoNgaySQL, tuNgay, denNgay);
    }

    public List<DoanhThuNhanVien> getDoanhThuNhanVienTheoHomNay() {
        return XQuery.getBeanList(DoanhThuNhanVien.class, doanhThuNhanVienHomNaySQL);
    }

    // Phương thức mới - Doanh thu nhân viên theo tháng/năm
    public List<DoanhThuNhanVien> getDoanhThuNhanVienTheoThang() {
        return XQuery.getBeanList(DoanhThuNhanVien.class, doanhThuNhanVienThangNaySQL);
    }

    // Phương thức mới - Doanh thu nhân viên theo năm
    public List<DoanhThuNhanVien> getDoanhThuNhanVienTheoNam() {
        return XQuery.getBeanList(DoanhThuNhanVien.class, doanhThuNhanVienNamNaySQL);
    }

    public List<DoanhThuSanPham> getDoanhThuSanPhamHomNay() {
        return XQuery.getBeanList(DoanhThuSanPham.class, doanhThuSanPhamHomNaySQL);
    }

// Phương thức lấy doanh thu sản phẩm tháng này
    public List<DoanhThuSanPham> getDoanhThuSanPhamThangNay() {
        return XQuery.getBeanList(DoanhThuSanPham.class, doanhThuSanPhamThangNaySQL);
    }

// Phương thức lấy doanh thu sản phẩm năm nay
    public List<DoanhThuSanPham> getDoanhThuSanPhamNamNay() {
        return XQuery.getBeanList(DoanhThuSanPham.class, doanhThuSanPhamNamNaySQL);
    }

// Phương thức lấy doanh thu sản phẩm theo khoảng ngày
    public List<DoanhThuSanPham> getDoanhThuSanPhamTheoKhoangNgay(Date tuNgay, Date denNgay) {
        return XQuery.getBeanList(DoanhThuSanPham.class, doanhThuSanPhamTheoKhoangNgaySQL, tuNgay, denNgay);
    }

}
