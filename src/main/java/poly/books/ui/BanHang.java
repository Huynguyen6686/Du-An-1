/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package poly.books.ui;

import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import poly.books.dao.ChiTietHoaDonDAO;
import poly.books.dao.HoaDonDAO;
import poly.books.dao.KhachHangDAO;
import poly.books.dao.PhieuGiamGiaDAO;
import poly.books.dao.SachDAO;
import poly.books.entity.ChiTietHoaDon;
import poly.books.entity.HoaDon;
import poly.books.entity.ISBNScanner;
import poly.books.entity.KhachHang;
import poly.books.entity.PhieuGiamGia;
import poly.books.entity.Sach;
import poly.books.ui.manager.DanhSachKhachHang;
import poly.books.ui.manager.DanhSachMaGG;
import poly.books.ui.manager.DanhSachSanPham;
import poly.books.util.XAuth;
import poly.books.util.XDate;
import poly.books.util.XJdbc;

/**
 *
 * @author HuyNguyen
 */
public class BanHang extends javax.swing.JPanel {

    private static final int SO_HOA_DON_CHO_TOI_DA = 10;
    private static final int MA_KHACH_VANG_LAI = 1;
    private static final String TEN_KHACH_VANG_LAI = "Khách vãng lai";
    private static final String DUONG_DAN_ANH = "/imgSach/";
    private static final String TEXT_ANH_MAC_DINH = "ảnh";

    // DAOs
    private final SachDAO sachDAO = new SachDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();
    private final PhieuGiamGiaDAO phieuGiamGiaDAO = new PhieuGiamGiaDAO();

    // Bien instance
    private JFrame parentFrame;
    private int maHoaDonHienTai = -1;
    private ISBNScanner isbnScanner;

    /**
     * Creates new form PolyBooks
     */
    public BanHang() {
        initComponents();
        khoiTaoComponents();
    }

    private void khoiTaoComponents() {
        thieLapThuocTinhBang();
        thieLapThuocTinhField();
        voHieuHoaTatCaButton();
        khoiTaoISBNScanner();
        taiDuLieuBangHoaDon();
    }

    private void thieLapThuocTinhBang() {
        tbSanPham.setDefaultEditor(Object.class, null);
    }

    private void thieLapThuocTinhField() {
        txtMaHD.setEditable(false);
        txtMaNV.setEditable(false);
        txtNgayLap.setEditable(false);
        txtTenKH.setEditable(false);
        txtSDT.setEditable(false);
        txtTrangThai.setEditable(false);
        txtTonKho.setEditable(false);
        txtMaPhieu.setEditable(false);
        txtTenMaGG.setEditable(false);
        txtISBN.setEditable(false);
        txtMaSach.setEditable(false);
        txtTenSach.setEditable(false);
        txtTonKho.setEditable(false);
        txtDonGia.setEditable(false);
    }

    private void khoiTaoISBNScanner() {
        if (!java.beans.Beans.isDesignTime()) {
            isbnScanner = new ISBNScanner(lbQuetISBN, this::xuLyQuetISBN);
            isbnScanner.stopScanning();
        }
    }

    // Phuong thuc validation
    private KetQuaValidation kiemTraTaoHoaDon() {
        if (XAuth.user == null || XAuth.currentTenDangNhap == null) {
            return KetQuaValidation.loi("Người dùng chưa đăng nhập!");
        }

        String tenKhachHang = txtTenKH.getText().trim();
        if (tenKhachHang.isEmpty()) {
            return KetQuaValidation.loi("Vui lòng chọn khách hàng!");
        }

        return KetQuaValidation.thanhCong();
    }

    private KetQuaValidation kiemTraThemSanPham() {
        if (maHoaDonHienTai == -1) {
            return KetQuaValidation.loi("Vui lòng tạo hóa đơn trước!");
        }

        String maSanPham = txtMaSach.getText().trim();
        if (maSanPham.isEmpty()) {
            return KetQuaValidation.loi("Vui lòng chọn một sản phẩm!");
        }

        int soLuong = (Integer) spSoLuong.getValue();
        if (soLuong <= 0) {
            return KetQuaValidation.loi("Số lượng phải lớn hơn 0!");
        }

        return KetQuaValidation.thanhCong();
    }

    private KetQuaValidation kiemTraThanhToan() {
        if (maHoaDonHienTai == -1 || tbSanPham.getRowCount() == 0) {
            return KetQuaValidation.loi("Vui lòng tạo hóa đơn và thêm sản phẩm!");
        }

        if (!rdoTienMat.isSelected() && !rdoTk.isSelected()) {
            return KetQuaValidation.loi("Vui lòng chọn phương thức thanh toán!");
        }

        double tongTienCuoi = tinhTongTienCuoi();
        if (tongTienCuoi < 0) {
            return KetQuaValidation.loi("Thành tiền không được âm!");
        }

        return KetQuaValidation.thanhCong();
    }

    // Quan ly trang thai button
    private void voHieuHoaTatCaButton() {
        DanhSachSanPham.setEnabled(false);
        btnThemSP.setEnabled(false);
        btnHuy.setEnabled(false);
        btnThanhToan.setEnabled(false);
        btnSua.setEnabled(false);
        btnQuetISBN.setEnabled(false);
        btnDungQuet.setEnabled(false);
        btnNhapISNB.setEnabled(false);
    }

    private void kichHoatButtonSauTaoHoaDon() {
        DanhSachSanPham.setEnabled(true);
        btnThemSP.setEnabled(true);
        btnHuy.setEnabled(true);
        btnSua.setEnabled(true);
        btnQuetISBN.setEnabled(true);
        btnDungQuet.setEnabled(true);
        btnNhapISNB.setEnabled(true);
        btnThanhToan.setEnabled(false);
    }

    private void kichHoatButtonThanhToan() {
        btnThanhToan.setEnabled(true);
    }

    // Xu ly su kien
    private void xuLyQuetISBN(String isbn) {
        try {
            Sach sach = sachDAO.findByISBN(isbn);
            if (sach != null) {
                chonSach(sach);
                hienThiDialogChonSanPham(sach);
            } else {
                lbQuetISBN.setText("Không tìm thấy sách với ISBN: " + isbn);
            }
        } catch (Exception e) {
            lbQuetISBN.setText("Lỗi: " + e.getMessage());
            Logger.getLogger(BanHang.class.getName()).log(Level.SEVERE, "Loi quet ISBN", e);
        }
    }

    private void hienThiDialogChonSanPham(Sach sach) {
        DanhSachSanPham dialog = new DanhSachSanPham(parentFrame, true, this);
        dialog.setSelectedSach(sach);
        dialog.setVisible(true);
    }

    // Phuong thuc nghiep vu chinh
    private int taoHoaDon() {
        KetQuaValidation ketQua = kiemTraTaoHoaDon();
        if (!ketQua.isValid()) {
            JOptionPane.showMessageDialog(this, ketQua.getMessage());
            return -1;
        }

        try {
            if (!coTheTaoHoaDonMoi()) {
                return -1;
            }

            int maKhachHang = layMaKhachHang();
            if (maKhachHang == -1) {
                return -1;
            }

            if (!khachHangCoTheTaoHoaDon(maKhachHang)) {
                return -1;
            }

            Integer maPhieuGiamGia = layMaPhieuGiamGiaHopLe();
            if (maPhieuGiamGia == null && !txtMaPhieu.getText().trim().isEmpty()) {
                return -1; // Ma giam gia khong hop le
            }

            return taoVaLuuHoaDon(maKhachHang, maPhieuGiamGia);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tạo hóa đơn: " + e.getMessage());
            Logger.getLogger(BanHang.class.getName()).log(Level.SEVERE, "Loi tao hoa don", e);
            return -1;
        }
    }

    private boolean coTheTaoHoaDonMoi() throws SQLException {
        String sql = "SELECT COUNT(*) FROM HoaDon WHERE TrangThai = 0";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next() && rs.getInt(1) >= SO_HOA_DON_CHO_TOI_DA) {
                JOptionPane.showMessageDialog(this,
                        "Đã đạt giới hạn " + SO_HOA_DON_CHO_TOI_DA + " hóa đơn đang chờ thanh toán!");
                return false;
            }
        }
        return true;
    }

    private int layMaKhachHang() throws SQLException {
        String tenKhachHang = txtTenKH.getText().trim();
        String soDienThoai = txtSDT.getText().trim();

        String sql;
        if (TEN_KHACH_VANG_LAI.equals(tenKhachHang)) {
            sql = "SELECT MaKH FROM KhachHang WHERE TenKH = ? AND SDT IS NULL";
            try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, tenKhachHang);
                return layMaKhachHangTuKetQua(ps.executeQuery());
            }
        } else {
            sql = "SELECT MaKH FROM KhachHang WHERE TenKH = ? AND (SDT = ? OR SDT IS NULL)";
            try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, tenKhachHang);
                ps.setString(2, soDienThoai.isEmpty() ? null : soDienThoai);
                return layMaKhachHangTuKetQua(ps.executeQuery());
            }
        }
    }

    private int layMaKhachHangTuKetQua(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return rs.getInt("MaKH");
        }
        JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng!");
        return -1;
    }

    private boolean khachHangCoTheTaoHoaDon(int maKhachHang) throws SQLException {
        // Bo qua kiem tra cho khach vang lai
        if (maKhachHang == MA_KHACH_VANG_LAI) {
            return true;
        }

        String sql = "SELECT COUNT(*) FROM HoaDon WHERE MaKH = ? AND TrangThai = 0";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maKhachHang);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Khách hàng này đã có một hóa đơn đang chờ thanh toán!");
                return false;
            }
        }
        return true;
    }

    private Integer layMaPhieuGiamGiaHopLe() throws SQLException {
        String maPhieuText = txtMaPhieu.getText().trim();
        if (maPhieuText.isEmpty()) {
            return null;
        }

        try {
            int maPhieu = Integer.parseInt(maPhieuText);
            String sql = "SELECT MaPhieu FROM PhieuGiamGia WHERE MaPhieu = ? AND TrangThai = 1";
            try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, maPhieu);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return maPhieu;
                } else {
                    JOptionPane.showMessageDialog(this, "Mã giảm giá không hợp lệ!");
                    return null;
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Mã phiếu giảm giá không hợp lệ!");
            return null;
        }
    }

    private int taoVaLuuHoaDon(int maKhachHang, Integer maPhieuGiamGia) throws SQLException {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaKH(maKhachHang);
        hoaDon.setTenDangNhap(XAuth.currentTenDangNhap.trim());
        hoaDon.setNgayLap(new java.util.Date());
        hoaDon.setTongTien(0.0);
        hoaDon.setPhuongThuc(1);
        hoaDon.setTrangThai(0);
        hoaDon.setMaPhieu(maPhieuGiamGia);

        int maHoaDonMoi = hoaDonDAO.create(hoaDon);
        if (maHoaDonMoi > 0) {
            maHoaDonHienTai = maHoaDonMoi;
            capNhatThongTinHoaDonTrenForm();
            return maHoaDonMoi;
        }
        return -1;
    }

    private void capNhatThongTinHoaDonTrenForm() {
        txtNgayLap.setText(XDate.format(new java.util.Date(), "dd-MM-yyyy"));
        txtMaNV.setText(XAuth.currentTenDangNhap.trim());
        txtTrangThai.setText("Chờ thanh toán");
        capNhatGiamGia();
    }

    private void themSanPhamVaoHoaDon() {
        KetQuaValidation ketQua = kiemTraThemSanPham();
        if (!ketQua.isValid()) {
            JOptionPane.showMessageDialog(this, ketQua.getMessage());
            return;
        }

        try {
            int maSach = Integer.parseInt(txtMaSach.getText().trim());
            Sach sach = sachDAO.findByID(maSach);
            if (sach == null) {
                JOptionPane.showMessageDialog(this, "Sản phẩm không tồn tại!");
                return;
            }

            int soLuong = (Integer) spSoLuong.getValue();
            int tonKho = laySoLuongTonKho(maSach);

            if (!kiemTraTonKho(maSach, soLuong, tonKho)) {
                return;
            }

            if (sanPhamDaTonTaiTrongGio(maSach)) {
                capNhatSoLuongSanPhamTrongGio(maSach, soLuong, tonKho);
            } else {
                themSanPhamMoiVaoGio(sach, soLuong);
            }

            capNhatTonKhoSach(maSach, -soLuong);

            capNhatTonKhoTrenUI(maSach);

            capNhatGiaTriHoaDon();
            kichHoatButtonThanhToan();
             JOptionPane.showMessageDialog(this, " Thêm sản phẩm vào giỏ hàng thành công ");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm sản phẩm: Vui lòng chọn hóa đơn để thêm sản phẩm ");
            Logger.getLogger(BanHang.class.getName()).log(Level.SEVERE, "Loi them san pham", e);
        }
    }

    private int laySoLuongTonKho(int maSach) throws SQLException {
        String sql = "SELECT SoLuong FROM Sach WHERE MaSach = ?";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSach);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("SoLuong");
            }
            return 0;
        }
    }

    private boolean kiemTraTonKho(int maSach, int soLuongCanThem, int tonKho) {
        if (tonKho < soLuongCanThem) {
            JOptionPane.showMessageDialog(this,
                    "Số lượng tồn kho không đủ (" + tonKho + ")!");
            return false;
        }

        // Kiem tra neu san pham da co trong gio
        DefaultTableModel model = (DefaultTableModel) tbSanPham.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (Integer.parseInt(model.getValueAt(i, 0).toString()) == maSach) {
                int soLuongHienTai = Integer.parseInt(model.getValueAt(i, 3).toString());
                int tongSoLuong = soLuongHienTai + soLuongCanThem;
                if (tongSoLuong > tonKho) {
                    JOptionPane.showMessageDialog(this,
                            "Tổng số lượng vượt quá tồn kho (" + tonKho + ")!");
                    return false;
                }
            }
        }

        return true;
    }

    private boolean sanPhamDaTonTaiTrongGio(int maSach) {
        DefaultTableModel model = (DefaultTableModel) tbSanPham.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (Integer.parseInt(model.getValueAt(i, 0).toString()) == maSach) {
                return true;
            }
        }
        return false;
    }

    private void capNhatSoLuongSanPhamTrongGio(int maSach, int soLuongThem, int tonKho) throws SQLException {
        DefaultTableModel model = (DefaultTableModel) tbSanPham.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (Integer.parseInt(model.getValueAt(i, 0).toString()) == maSach) {
                int soLuongCu = Integer.parseInt(model.getValueAt(i, 3).toString());
                int soLuongMoi = soLuongCu + soLuongThem;

                model.setValueAt(soLuongMoi, i, 3);

                String sql = "UPDATE ChiTietHoaDon SET SoLuong = ? WHERE MaHD = ? AND MaSach = ?";
                try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, soLuongMoi);
                    ps.setInt(2, maHoaDonHienTai);
                    ps.setInt(3, maSach);
                    ps.executeUpdate();
                }
                capNhatTonKhoTrenUI(maSach);
                break;
            }
        }
    }

    private void themSanPhamMoiVaoGio(Sach sach, int soLuong) throws SQLException {
        DefaultTableModel model = (DefaultTableModel) tbSanPham.getModel();
        model.insertRow(0, new Object[]{
            sach.getMaSach(),
            sach.getISBN(),
            sach.getTenSach(),
            soLuong,
            sach.getGiaBan()
        });

        String sql = "INSERT INTO ChiTietHoaDon (MaHD, MaSach, SoLuong, DonGia) VALUES (?, ?, ?, ?)";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHoaDonHienTai);
            ps.setInt(2, sach.getMaSach());
            ps.setInt(3, soLuong);
            ps.setDouble(4, sach.getGiaBan());
            ps.executeUpdate();
        }
    }

    private void capNhatTonKhoSach(int maSach, int soLuongThayDoi) throws SQLException {
        String sql = "UPDATE Sach SET SoLuong = SoLuong + ? WHERE MaSach = ?";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuongThayDoi);
            ps.setInt(2, maSach);
            ps.executeUpdate();
        }
    }

    private void thanhToan() {
        KetQuaValidation ketQua = kiemTraThanhToan();
        if (!ketQua.isValid()) {
            JOptionPane.showMessageDialog(this, ketQua.getMessage());
            return;
        }

        try {
            double tongTienTruocGiam = tinhTongTienTruocGiam();
            double giamGia = tinhGiamGia(tongTienTruocGiam);
            double thanhTien = tongTienTruocGiam - giamGia;

            int phuongThuc = rdoTienMat.isSelected() ? 1 : 2;

            capNhatHoaDonThanhToan(thanhTien, phuongThuc, giamGia);
            hienThiThongBaoThanhCong(tongTienTruocGiam, giamGia, thanhTien);
            resetForm();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi thanh toán: " + e.getMessage());
            Logger.getLogger(BanHang.class.getName()).log(Level.SEVERE, "Loi thanh toan", e);
        }
    }

    private void capNhatHoaDonThanhToan(double thanhTien, int phuongThuc, double giamGia) throws SQLException {
        HoaDon hoaDon = hoaDonDAO.findById(maHoaDonHienTai);
        if (hoaDon != null) {
            hoaDon.setTongTien(thanhTien);
            hoaDon.setPhuongThuc(phuongThuc);
            hoaDon.setNgayThanhToan(new java.util.Date());
            hoaDon.setTrangThai(1);

            String maPhieuText = txtMaPhieu.getText().trim();
            if (!maPhieuText.isEmpty()) {
                hoaDon.setMaPhieu(Integer.parseInt(maPhieuText));
            }

            hoaDonDAO.update(hoaDon);
        }
    }

    private void hienThiThongBaoThanhCong(double tongTien, double giamGia, double thanhTien) {
        txtTrangThai.setText("Đã thanh toán");
        txtTanhtien.setText(String.format("%.2f", thanhTien));
        txtGiamGia.setText(String.format("%.2f", giamGia));
        txtTongtien.setText(String.format("%.2f", tongTien));

        JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
        taiDuLieuBangHoaDon();
    }

    // Phuong thuc tinh toan
    private double tinhTongTienTruocGiam() {
        DefaultTableModel model = (DefaultTableModel) tbSanPham.getModel();
        double tongTien = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                int soLuong = Integer.parseInt(model.getValueAt(i, 3).toString());
                double donGia = Double.parseDouble(model.getValueAt(i, 4).toString());
                tongTien += soLuong * donGia;
            } catch (Exception e) {
                Logger.getLogger(BanHang.class.getName()).log(Level.WARNING, "Loi tinh tong tien", e);
            }
        }
        return tongTien;
    }

    private double tinhGiamGia(double tongTien) {
        String maPhieuText = txtMaPhieu.getText().trim();
        if (maPhieuText.isEmpty()) {
            return 0;
        }

        try {
            int maPhieu = Integer.parseInt(maPhieuText);
            String sql = "SELECT GiaTri, DieuKienApDung FROM PhieuGiamGia WHERE MaPhieu = ? AND TrangThai = 1";

            try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, maPhieu);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    double giaTri = rs.getDouble("GiaTri");
                    int dieuKien = rs.getInt("DieuKienApDung");

                    if (tongTien >= dieuKien) {
                        return Math.min(giaTri, tongTien);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                String.format("Tổng tiền %.2f không đủ để áp dụng mã giảm giá (yêu cầu tối thiểu %.2f)!",
                                        tongTien, (double) dieuKien));
                        xoaThongTinGiamGia();
                        return 0;
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(BanHang.class.getName()).log(Level.WARNING, "Loi tinh giam gia", e);
        }

        xoaThongTinGiamGia();
        return 0;
    }

    private double tinhTongTienCuoi() {
        double tongTienTruocGiam = tinhTongTienTruocGiam();
        double giamGia = tinhGiamGia(tongTienTruocGiam);
        return tongTienTruocGiam - giamGia;
    }

    private void capNhatGiaTriHoaDon() {
        capNhatGiamGia();
        if (maHoaDonHienTai != -1) {
            try {
                double tongTien = tinhTongTienCuoi();
                HoaDon hoaDon = hoaDonDAO.findById(maHoaDonHienTai);
                if (hoaDon != null) {
                    hoaDon.setTongTien(tongTien);
                    hoaDonDAO.update(hoaDon);
                    taiDuLieuBangHoaDon();
                }
            } catch (Exception e) {
                Logger.getLogger(BanHang.class.getName()).log(Level.WARNING, "Loi cap nhat gia tri hoa don", e);
            }
        }
    }

    private void capNhatGiamGia() {
        double tongTienTruocGiam = tinhTongTienTruocGiam();
        double giamGia = tinhGiamGia(tongTienTruocGiam);
        double thanhTien = tongTienTruocGiam - giamGia;

        txtTongtien.setText(String.format("%.2f", tongTienTruocGiam));
        txtTanhtien.setText(String.format("%.2f", thanhTien));
        txtGiamGia.setText(String.format("%.2f", giamGia));
    }

    // Phuong thuc hien thi du lieu
    private void taiDuLieuBangHoaDon() {
        DefaultTableModel model = (DefaultTableModel) tblHoaDon.getModel();
        model.setRowCount(0);

        try {
            String sql = "SELECT DISTINCT hd.MaHD, hd.NgayLap, kh.TenKH, hd.TenDangNhap, "
                    + "pg.TenPhieu, hd.TongTien, hd.PhuongThuc, hd.TrangThai "
                    + "FROM HoaDon hd "
                    + "JOIN KhachHang kh ON hd.MaKH = kh.MaKH "
                    + "LEFT JOIN PhieuGiamGia pg ON hd.MaPhieu = pg.MaPhieu "
                    + "WHERE hd.TrangThai = 0";

            try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("MaHD"),
                        XDate.format(rs.getDate("NgayLap"), "dd-MM-yyyy"),
                        rs.getString("TenKH"),
                        rs.getString("TenDangNhap"),
                        rs.getString("TenPhieu") != null ? rs.getString("TenPhieu") : "",
                        rs.getDouble("TongTien"),
                        rs.getInt("PhuongThuc") == 1 ? "Tiền mặt" : "Chuyển khoản",
                        rs.getInt("TrangThai") == 0 ? "Chờ thanh toán" : "Đã thanh toán"
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách hóa đơn: " + e.getMessage());
            Logger.getLogger(BanHang.class.getName()).log(Level.SEVERE, "Loi tai du lieu bang hoa don", e);
        }
    }

    private void hienThiThongTinHoaDon(HoaDon hoaDon) {
        maHoaDonHienTai = hoaDon.getMaHD();
        txtMaHD.setText(String.valueOf(hoaDon.getMaHD()));
        txtMaNV.setText(hoaDon.getTenDangNhap());
        txtNgayLap.setText(XDate.format(hoaDon.getNgayLap(), "dd-MM-yyyy"));
        txtMaPhieu.setText(hoaDon.getMaPhieu() != null ? String.valueOf(hoaDon.getMaPhieu()) : "");
        txtTrangThai.setText(hoaDon.getTrangThai() == 0 ? "Chờ thanh toán" : "Đã thanh toán");
        txtTanhtien.setText(String.format("%.2f", hoaDon.getTongTien()));

        rdoTienMat.setSelected(hoaDon.getPhuongThuc() == 1);
        rdoTk.setSelected(hoaDon.getPhuongThuc() == 2);

        hienThiThongTinKhachHang(hoaDon.getMaKH());
        hienThiThongTinPhieuGiamGia(hoaDon.getMaPhieu());
        taiChiTietHoaDon();
    }

    private void hienThiThongTinKhachHang(int maKhachHang) {
        try {
            KhachHang khachHang = khachHangDAO.findbyID(maKhachHang);
            if (khachHang != null) {
                txtTenKH.setText(khachHang.getTenKH());
                txtSDT.setText(khachHang.getSDT());
            }
        } catch (Exception e) {
            Logger.getLogger(BanHang.class.getName()).log(Level.WARNING, "Loi hien thi thong tin khach hang", e);
        }
    }

    private void hienThiThongTinPhieuGiamGia(Integer maPhieu) {
        if (maPhieu != null) {
            try {
                PhieuGiamGia phieu = phieuGiamGiaDAO.findByID(maPhieu);
                if (phieu != null) {
                    txtTenMaGG.setText(phieu.getTenPhieu());
                } else {
                    txtTenMaGG.setText("");
                }
            } catch (Exception e) {
                txtTenMaGG.setText("");
                Logger.getLogger(BanHang.class.getName()).log(Level.WARNING, "Loi hien thi phieu giam gia", e);
            }
        } else {
            txtTenMaGG.setText("");
        }
    }

    private void taiChiTietHoaDon() {
        DefaultTableModel model = (DefaultTableModel) tbSanPham.getModel();
        model.setRowCount(0);

        try {
            List<ChiTietHoaDon> danhSachChiTiet = chiTietHoaDonDAO.findByID(maHoaDonHienTai);
            for (ChiTietHoaDon chiTiet : danhSachChiTiet) {
                Sach sach = sachDAO.findByID(chiTiet.getMaSach());
                if (sach != null) {
                    model.addRow(new Object[]{
                        sach.getMaSach(),
                        sach.getISBN(),
                        sach.getTenSach(),
                        chiTiet.getSoLuong(),
                        chiTiet.getDonGia()
                    });
                }
            }
            capNhatGiamGia();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải chi tiết hóa đơn: " + e.getMessage());
            Logger.getLogger(BanHang.class.getName()).log(Level.SEVERE, "Loi tai chi tiet hoa don", e);
        }
    }

    // Phuong thuc set du lieu tu dialog
    public void chonSach(Sach sach) {
        txtMaSach.setText(String.valueOf(sach.getMaSach()));
        txtTenSach.setText(sach.getTenSach());
        txtDonGia.setText(String.valueOf(sach.getGiaBan()));
        txtISBN.setText(sach.getISBN());
        txtTonKho.setText(String.valueOf(sach.getSoLuong()));
        spSoLuong.setValue(1);

        hienThiAnhSach(sach);
    }

    private void hienThiAnhSach(Sach sach) {
        lbAnh.setIcon(null);
        lbAnh.setText("");

        if (sach.getHinhAnh() != null && !sach.getHinhAnh().isEmpty()) {
            lbAnh.setToolTipText(sach.getHinhAnh());
            java.net.URL duongDanAnh = getClass().getResource(DUONG_DAN_ANH + sach.getHinhAnh());
            if (duongDanAnh != null) {
                ImageIcon icon = new ImageIcon(duongDanAnh);
                Image img = icon.getImage().getScaledInstance(
                        lbAnh.getWidth(), lbAnh.getHeight(), Image.SCALE_SMOOTH);
                lbAnh.setIcon(new ImageIcon(img));
            }
        }
    }

    public void chonKhachHang(KhachHang khachHang) {
        txtTenKH.setText(khachHang.getTenKH());
        txtSDT.setText(khachHang.getSDT());
    }

    public void chonPhieuGiamGia(PhieuGiamGia phieuGiamGia) {
        if (maHoaDonHienTai == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng tạo hóa đơn trước khi chọn mã giảm giá!");
            return;
        }

        // Kiểm tra trạng thái
        if (phieuGiamGia.getTrangThai() != 1) {
            JOptionPane.showMessageDialog(this, "Mã giảm giá này đã bị vô hiệu hóa!");
            return;
        }

        // Kiểm tra ngày hết hạn
        java.util.Date ngayHienTai = new java.util.Date();
        if (phieuGiamGia.getNgayKetThuc() != null && phieuGiamGia.getNgayKetThuc().before(ngayHienTai)) {
            JOptionPane.showMessageDialog(this, "Mã giảm giá này đã hết hạn!");
            return;
        }

        // Kiểm tra điều kiện áp dụng trước khi cập nhật
        double tongTienTruocGiam = tinhTongTienTruocGiam();
        if (tongTienTruocGiam < phieuGiamGia.getDieuKienApDung()) {
            JOptionPane.showMessageDialog(this,
                    String.format("Tổng tiền %.2f không đủ để áp dụng mã giảm giá này (yêu cầu tối thiểu %.2f)!",
                            tongTienTruocGiam, (double) phieuGiamGia.getDieuKienApDung()));
            return;
        }

        txtMaPhieu.setText(String.valueOf(phieuGiamGia.getMaPhieu()));
        txtTenMaGG.setText(phieuGiamGia.getTenPhieu());

        try {
            HoaDon hoaDon = hoaDonDAO.findById(maHoaDonHienTai);
            if (hoaDon != null) {
                hoaDon.setMaPhieu(phieuGiamGia.getMaPhieu());
                double tongTien = tinhTongTienCuoi();
                hoaDon.setTongTien(tongTien);
                hoaDonDAO.update(hoaDon);
                taiDuLieuBangHoaDon();
            }
        } catch (Exception e) {
            Logger.getLogger(BanHang.class.getName()).log(Level.WARNING, "Loi cap nhat phieu giam gia", e);
        }
        capNhatGiamGia();
    }

    // Phuong thuc xu ly su kien sua san pham
    private void suaSanPhamTrongGio() {
        int dongDuocChon = tbSanPham.getSelectedRow();
        if (dongDuocChon == -1 || maHoaDonHienTai == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để sửa!");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tbSanPham.getModel();
        int maSach = Integer.parseInt(model.getValueAt(dongDuocChon, 0).toString());

        try {
            Sach sach = sachDAO.findByID(maSach);
            if (sach != null) {
                chonSach(sach);
                int soLuongTrongGio = Integer.parseInt(model.getValueAt(dongDuocChon, 3).toString());
                spSoLuong.setValue(soLuongTrongGio);

                suaSoLuongSanPham(dongDuocChon, maSach, model);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa sản phẩm: " + e.getMessage());
            Logger.getLogger(BanHang.class.getName()).log(Level.SEVERE, "Loi sua san pham", e);
        }
    }

    private void suaSoLuongSanPham(int dongDuocChon, int maSach, DefaultTableModel model) {
        try {
            int soLuongCu = Integer.parseInt(model.getValueAt(dongDuocChon, 3).toString());
            String tenSach = model.getValueAt(dongDuocChon, 2).toString();
            int tonKhoHienTai = laySoLuongTonKho(maSach);

            String thongBao = String.format(
                    "Sản phẩm: %s\n"
                    + "Số lượng hiện tại trong giỏ: %d\n"
                    + "Tồn kho hiện tại: %d\n"
                    + "Tồn kho khả dụng: %d\n\n"
                    + "Nhập số lượng mới (0 = xóa khỏi giỏ):",
                    tenSach, soLuongCu, tonKhoHienTai, tonKhoHienTai + soLuongCu
            );

            String soLuongMoiStr = JOptionPane.showInputDialog(this, thongBao, soLuongCu);
            if (soLuongMoiStr == null) {
                return;
            }

            int soLuongMoi = Integer.parseInt(soLuongMoiStr);
            if (soLuongMoi < 0) {
                JOptionPane.showMessageDialog(this, "Số lượng không được âm!");
                return;
            }

            if (soLuongMoi > soLuongCu) {
                int soLuongCanThem = soLuongMoi - soLuongCu;
                if (soLuongCanThem > tonKhoHienTai) {
                    JOptionPane.showMessageDialog(this,
                            "Không đủ tồn kho!\n"
                            + "Tồn kho hiện tại: " + tonKhoHienTai + "\n"
                            + "Cần thêm: " + soLuongCanThem);
                    return;
                }
            }

            if (soLuongMoi == 0) {
                xoaSanPhamKhoiGio(maSach, soLuongCu, model, dongDuocChon);
            } else {
                capNhatSoLuongMoiSanPham(maSach, soLuongCu, soLuongMoi, model, dongDuocChon);
            }

            capNhatGiaTriHoaDon();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            Logger.getLogger(BanHang.class.getName()).log(Level.SEVERE, "Loi sua so luong", e);
        }
    }

    private void xoaSanPhamKhoiGio(int maSach, int soLuongCu, DefaultTableModel model, int dongDuocChon) throws SQLException {
        // Hoàn lại tồn kho
        capNhatTonKhoSach(maSach, soLuongCu);

        // Xóa khỏi chi tiết hóa đơn
        String sql = "DELETE FROM ChiTietHoaDon WHERE MaHD = ? AND MaSach = ?";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHoaDonHienTai);
            ps.setInt(2, maSach);
            ps.executeUpdate();
        }

        model.removeRow(dongDuocChon);
        JOptionPane.showMessageDialog(this, "Đã xóa sản phẩm khỏi giỏ hàng!");

        // Cập nhật txtTonKho nếu đang hiển thị sản phẩm này
        capNhatTonKhoTrenUI(maSach);

        // Xóa thông tin sản phẩm nếu đang hiển thị
        if (!txtMaSach.getText().trim().isEmpty()
                && Integer.parseInt(txtMaSach.getText().trim()) == maSach) {
            xoaThongTinSanPham();
        }
    }

    private void capNhatSoLuongMoiSanPham(int maSach, int soLuongCu, int soLuongMoi,
            DefaultTableModel model, int dongDuocChon) throws SQLException {
        int chenhLech = soLuongMoi - soLuongCu;

        // Cập nhật tồn kho
        capNhatTonKhoSach(maSach, -chenhLech);

        // Cập nhật chi tiết hóa đơn
        String sql = "UPDATE ChiTietHoaDon SET SoLuong = ? WHERE MaHD = ? AND MaSach = ?";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuongMoi);
            ps.setInt(2, maHoaDonHienTai);
            ps.setInt(3, maSach);
            ps.executeUpdate();
        }

        model.setValueAt(soLuongMoi, dongDuocChon, 3);

        // Cập nhật form nếu đang hiển thị sản phẩm này
        if (!txtMaSach.getText().trim().isEmpty()
                && Integer.parseInt(txtMaSach.getText().trim()) == maSach) {
            spSoLuong.setValue(soLuongMoi);
            // Cập nhật txtTonKho
            capNhatTonKhoTrenUI(maSach);
        }

        JOptionPane.showMessageDialog(this, "Đã cập nhật số lượng!");
    }

    // Phuong thuc huy hoa don
    private void huyHoaDon() {
        if (maHoaDonHienTai == -1) {
            JOptionPane.showMessageDialog(this, "Không có hóa đơn để hủy!");
            return;
        }

        try {
            // Lưu mã sách hiện tại đang hiển thị để cập nhật tồn kho
            int maSachHienTai = -1;
            if (!txtMaSach.getText().trim().isEmpty()) {
                try {
                    maSachHienTai = Integer.parseInt(txtMaSach.getText().trim());
                } catch (NumberFormatException e) {
                    // Ignore
                }
            }

            // Hoàn lại tồn kho cho tất cả sản phẩm trong giỏ
            if (tbSanPham.getRowCount() > 0) {
                for (int i = 0; i < tbSanPham.getRowCount(); i++) {
                    int maSach = Integer.parseInt(tbSanPham.getValueAt(i, 0).toString());
                    int soLuong = Integer.parseInt(tbSanPham.getValueAt(i, 3).toString());
                    capNhatTonKhoSach(maSach, soLuong);
                }
            }

            // Xóa chi tiết hóa đơn
            String sqlXoaChiTiet = "DELETE FROM ChiTietHoaDon WHERE MaHD = ?";
            try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sqlXoaChiTiet)) {
                ps.setInt(1, maHoaDonHienTai);
                ps.executeUpdate();
            }

            // Xóa hóa đơn
            hoaDonDAO.delete(maHoaDonHienTai);

            JOptionPane.showMessageDialog(this, "Hủy hóa đơn thành công!");

            // Cập nhật txtTonKho nếu có sản phẩm đang hiển thị
            if (maSachHienTai != -1) {
                capNhatTonKhoTrenUI(maSachHienTai);
            }

            resetForm();
            taiDuLieuBangHoaDon();
            voHieuHoaTatCaButton();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi hủy hóa đơn: " + e.getMessage());
            Logger.getLogger(BanHang.class.getName()).log(Level.SEVERE, "Loi huy hoa don", e);
        }
    }

    private void capNhatTonKhoTrenUI(int maSach) {
        try {
            // Chỉ cập nhật nếu đang hiển thị sản phẩm này
            if (!txtMaSach.getText().trim().isEmpty()
                    && Integer.parseInt(txtMaSach.getText().trim()) == maSach) {
                int tonKhoMoi = laySoLuongTonKho(maSach);
                txtTonKho.setText(String.valueOf(tonKhoMoi));
            }
        } catch (Exception e) {
            Logger.getLogger(BanHang.class.getName()).log(Level.WARNING, "Loi cap nhat ton kho UI", e);
        }
    }

    // Phuong thuc xu ly ISBN thu cong
    private void nhapISBNThuCong() {

        String isbn = JOptionPane.showInputDialog(this, "Nhập ISBN:", "Nhập ISBN", JOptionPane.PLAIN_MESSAGE);
        if (isbn == null) {
            JOptionPane.showMessageDialog(this, "Đã hủy nhập ISBN");
            return;
        }

        isbn = isbn.trim();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng không để trống ISBN");
            return;
        }
        if (!(isbn.length() == 13)) {
            JOptionPane.showMessageDialog(this, "Độ dài ISBN không hợp lệ! ISBN phải có 13 ký tự.");
            return;
        }
        if (!isbn.matches("\\d{13}")) {
            JOptionPane.showMessageDialog(this, "ISBN chỉ được chứa 13 chữ số (không được có ký tự khác).");
            return;
        }
        xuLyQuetISBN(isbn);
        JOptionPane.showMessageDialog(this, "Thêm sản phẩm vào giỏ hàng thành công");

    }

    // Phuong thuc utility
    private void xoaThongTinGiamGia() {
        txtMaPhieu.setText("");
        txtTenMaGG.setText("");
        txtGiamGia.setText("0");
    }

    private void xoaThongTinSanPham() {
        txtMaSach.setText("");
        txtTenSach.setText("");
        txtDonGia.setText("");
        txtISBN.setText("");
        txtTonKho.setText("");
        spSoLuong.setValue(0);
        lbAnh.setIcon(null);
        lbAnh.setText(TEXT_ANH_MAC_DINH);
    }

    private void resetForm() {
        xoaThongTinSanPham();
        txtMaHD.setText("");
        txtMaNV.setText("");
        txtNgayLap.setText("");
        txtTenKH.setText("");
        txtMaPhieu.setText("");
        txtTrangThai.setText("");
        txtTanhtien.setText("");
        txtGiamGia.setText("0");
        txtSDT.setText("");
        txtTenMaGG.setText("");
        txtTongtien.setText("");

        buttonGroup1.clearSelection();
        ((DefaultTableModel) tbSanPham.getModel()).setRowCount(0);
        maHoaDonHienTai = -1;
    }

    // Setters cho parent frame va webcam
    public void setParentFrame(JFrame parent) {
        this.parentFrame = parent;
    }

    public void stopWebcam() {
        if (isbnScanner != null) {
            isbnScanner.stopScanning();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        BanHang = new javax.swing.JPanel();
        txtTieuDe = new javax.swing.JLabel();
        TaoHD = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtMaHD = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtMaPhieu = new javax.swing.JTextField();
        btnMaGG = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        txtNgayLap = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtTenKH = new javax.swing.JTextField();
        btnChonKH = new javax.swing.JButton();
        txtTrangThai = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        btnTaoHD = new javax.swing.JButton();
        lbQuetISBN = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        btnDungQuet = new javax.swing.JButton();
        btnQuetISBN = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        txtTenMaGG = new javax.swing.JTextField();
        btnNhapISNB = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        HoaDon = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        TTHD = new javax.swing.JPanel();
        lbAnh = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtMaSach = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTenSach = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        spSoLuong = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        txtDonGia = new javax.swing.JTextField();
        DanhSachSanPham = new javax.swing.JButton();
        btnThemSP = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtISBN = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbSanPham = new javax.swing.JTable();
        txtTanhtien = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtGiamGia = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtTongtien = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        rdoTienMat = new javax.swing.JRadioButton();
        rdoTk = new javax.swing.JRadioButton();
        btnThanhToan = new javax.swing.JButton();
        btnHuy = new javax.swing.JButton();
        txtTonKho = new javax.swing.JTextField();
        btnSua = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(1123, 773));
        setPreferredSize(new java.awt.Dimension(1123, 773));
        setLayout(new java.awt.BorderLayout());

        BanHang.setBackground(new java.awt.Color(255, 255, 255));
        BanHang.setMinimumSize(new java.awt.Dimension(1123, 773));
        BanHang.setPreferredSize(new java.awt.Dimension(1123, 773));
        BanHang.setLayout(new java.awt.BorderLayout());

        txtTieuDe.setBackground(new java.awt.Color(0, 143, 193));
        txtTieuDe.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtTieuDe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTieuDe.setText("Bán Hàng");
        txtTieuDe.setMinimumSize(new java.awt.Dimension(1123, 50));
        txtTieuDe.setOpaque(true);
        txtTieuDe.setPreferredSize(new java.awt.Dimension(1123, 50));
        BanHang.add(txtTieuDe, java.awt.BorderLayout.PAGE_START);

        TaoHD.setBackground(new java.awt.Color(255, 255, 255));
        TaoHD.setMinimumSize(new java.awt.Dimension(250, 723));
        TaoHD.setPreferredSize(new java.awt.Dimension(250, 723));

        jLabel9.setText("Mã Hóa đơn:");

        jLabel13.setText("Mã Phiếu GG:");

        btnMaGG.setText("...");
        btnMaGG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMaGGActionPerformed(evt);
            }
        });

        jLabel10.setText("Thời điểm lập:");

        txtNgayLap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNgayLapActionPerformed(evt);
            }
        });

        jLabel12.setText("Mã Nhân viên");

        jLabel1.setText("Tên Khách Hàng:");

        btnChonKH.setText("...");
        btnChonKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonKHActionPerformed(evt);
            }
        });

        jLabel16.setText("Trạng thái:");

        btnTaoHD.setText("Tạo Hóa Đơn");
        btnTaoHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoHDActionPerformed(evt);
            }
        });

        lbQuetISBN.setBackground(new java.awt.Color(255, 255, 255));
        lbQuetISBN.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbQuetISBN.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lbQuetISBN.setOpaque(true);

        jLabel19.setText("SĐT:");

        btnDungQuet.setText("Dừng quét");
        btnDungQuet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDungQuetActionPerformed(evt);
            }
        });

        btnQuetISBN.setText("Quét ISBN");
        btnQuetISBN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuetISBNActionPerformed(evt);
            }
        });

        jLabel20.setText("Tên Mã GG:");

        btnNhapISNB.setText("Nhập ISBN");
        btnNhapISNB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNhapISNBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout TaoHDLayout = new javax.swing.GroupLayout(TaoHD);
        TaoHD.setLayout(TaoHDLayout);
        TaoHDLayout.setHorizontalGroup(
            TaoHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TaoHDLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnTaoHD, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
            .addGroup(TaoHDLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TaoHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TaoHDLayout.createSequentialGroup()
                        .addGroup(TaoHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(TaoHDLayout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(TaoHDLayout.createSequentialGroup()
                                .addGroup(TaoHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGroup(TaoHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(TaoHDLayout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnChonKH, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(TaoHDLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(14, 14, 14))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TaoHDLayout.createSequentialGroup()
                        .addGroup(TaoHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(TaoHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(TaoHDLayout.createSequentialGroup()
                                .addComponent(txtTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5))
                            .addComponent(txtSDT))))
                .addContainerGap())
            .addGroup(TaoHDLayout.createSequentialGroup()
                .addGroup(TaoHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TaoHDLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(TaoHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(TaoHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(TaoHDLayout.createSequentialGroup()
                                .addComponent(txtMaPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(btnMaGG, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(TaoHDLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lbQuetISBN, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(TaoHDLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2))
                    .addGroup(TaoHDLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(btnQuetISBN, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDungQuet))
                    .addGroup(TaoHDLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtTenMaGG, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TaoHDLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnNhapISNB)
                .addGap(80, 80, 80))
        );
        TaoHDLayout.setVerticalGroup(
            TaoHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TaoHDLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(lbQuetISBN, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(TaoHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnQuetISBN)
                    .addComponent(btnDungQuet))
                .addGap(18, 18, 18)
                .addComponent(btnNhapISNB)
                .addGap(15, 15, 15)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(TaoHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(TaoHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtMaPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMaGG))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(TaoHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtTenMaGG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(TaoHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(TaoHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(TaoHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChonKH))
                .addGap(9, 9, 9)
                .addGroup(TaoHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(TaoHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addComponent(btnTaoHD, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(95, 95, 95))
        );

        BanHang.add(TaoHD, java.awt.BorderLayout.LINE_END);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setMinimumSize(new java.awt.Dimension(873, 723));
        jPanel7.setPreferredSize(new java.awt.Dimension(873, 723));
        jPanel7.setLayout(new java.awt.BorderLayout());

        HoaDon.setBackground(new java.awt.Color(255, 255, 255));
        HoaDon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 144, 193)));
        HoaDon.setMinimumSize(new java.awt.Dimension(810, 180));
        HoaDon.setPreferredSize(new java.awt.Dimension(810, 180));

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã hoá đơn", "Ngày lập", "Tên khách hàng", "Tên đăng nhập", "Tên phiếu GG", "Tổng tiền", "Phương thức", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDon.setMinimumSize(new java.awt.Dimension(810, 80));
        tblHoaDon.setPreferredSize(new java.awt.Dimension(810, 80));
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblHoaDon);

        javax.swing.GroupLayout HoaDonLayout = new javax.swing.GroupLayout(HoaDon);
        HoaDon.setLayout(HoaDonLayout);
        HoaDonLayout.setHorizontalGroup(
            HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HoaDonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
                .addContainerGap())
        );
        HoaDonLayout.setVerticalGroup(
            HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HoaDonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );

        jPanel7.add(HoaDon, java.awt.BorderLayout.PAGE_START);

        TTHD.setBackground(new java.awt.Color(255, 255, 255));
        TTHD.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 144, 193), 1, true));
        TTHD.setForeground(new java.awt.Color(0, 144, 193));

        lbAnh.setText("ảnh");
        lbAnh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setText("Mã Sách");

        jLabel4.setText("Tên sách:");

        jLabel5.setText("Số Lượng");

        jLabel6.setText("Đơn giá:");

        DanhSachSanPham.setText("Danh sách sản phẩm");
        DanhSachSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DanhSachSanPhamActionPerformed(evt);
            }
        });

        btnThemSP.setText("Thêm vào giỏ hàng");
        btnThemSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSPActionPerformed(evt);
            }
        });

        jLabel7.setText("ISBN:");

        jLabel11.setText("Tồn kho: ");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Giỏ hàng", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18))); // NOI18N

        tbSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã Sách", "ISBN", "Tên Sách", "Số Lượng", "Đơn giá"
            }
        ));
        tbSanPham.setGridColor(new java.awt.Color(204, 204, 204));
        tbSanPham.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tbSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbSanPhamMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbSanPham);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel14.setText("Thành tiền:");

        jLabel15.setText("Giảm giá:");

        jLabel17.setText("Tổng tiền:");

        jLabel18.setText("Phương thức thanh toán:");

        buttonGroup1.add(rdoTienMat);
        rdoTienMat.setText("Tiền mặt");

        buttonGroup1.add(rdoTk);
        rdoTk.setText("Chuyển khoản");

        btnThanhToan.setText("Thanh Toán");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        btnHuy.setText("Hủy");
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });

        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout TTHDLayout = new javax.swing.GroupLayout(TTHD);
        TTHD.setLayout(TTHDLayout);
        TTHDLayout.setHorizontalGroup(
            TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TTHDLayout.createSequentialGroup()
                .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, TTHDLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(lbAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(TTHDLayout.createSequentialGroup()
                        .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, TTHDLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(TTHDLayout.createSequentialGroup()
                                        .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel5))
                                        .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(TTHDLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(spSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TTHDLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(txtDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(16, 16, 16))))
                                    .addGroup(TTHDLayout.createSequentialGroup()
                                        .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(TTHDLayout.createSequentialGroup()
                                                .addComponent(jLabel11)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtTonKho, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel8))
                                            .addGroup(TTHDLayout.createSequentialGroup()
                                                .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(txtMaSach, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(txtTenSach, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(TTHDLayout.createSequentialGroup()
                                                .addComponent(jLabel7)
                                                .addGap(18, 18, 18)
                                                .addComponent(txtISBN, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(TTHDLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(DanhSachSanPham)))
                        .addGap(33, 33, 33)))
                .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(TTHDLayout.createSequentialGroup()
                        .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(TTHDLayout.createSequentialGroup()
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(txtTongtien, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TTHDLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(30, 30, 30)
                                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtTanhtien, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(TTHDLayout.createSequentialGroup()
                                        .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(TTHDLayout.createSequentialGroup()
                                                .addGap(29, 29, 29)
                                                .addComponent(rdoTk, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TTHDLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnHuy)
                                                .addGap(22, 22, 22)))
                                        .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(8, 8, 8))))
                            .addGroup(TTHDLayout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rdoTienMat, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnThemSP, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8))
                    .addGroup(TTHDLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSua)
                        .addContainerGap())))
        );
        TTHDLayout.setVerticalGroup(
            TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TTHDLayout.createSequentialGroup()
                .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TTHDLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(TTHDLayout.createSequentialGroup()
                                .addComponent(lbAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(txtISBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(txtMaSach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(txtTenSach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(TTHDLayout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(btnSua)))
                .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TTHDLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtTonKho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(spSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(TTHDLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(txtTongtien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15)
                            .addComponent(jLabel14)
                            .addComponent(txtTanhtien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(TTHDLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel18)
                                    .addComponent(rdoTienMat)
                                    .addComponent(rdoTk)))
                            .addGroup(TTHDLayout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnHuy))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(TTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DanhSachSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemSP, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(83, 83, 83))
        );

        jPanel7.add(TTHD, java.awt.BorderLayout.CENTER);

        BanHang.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(BanHang, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnMaGGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMaGGActionPerformed
        DanhSachMaGG dialog = new DanhSachMaGG(parentFrame, true, this);
        dialog.setVisible(true);

    }//GEN-LAST:event_btnMaGGActionPerformed

    private void txtNgayLapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgayLapActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgayLapActionPerformed

    private void btnChonKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonKHActionPerformed
        DanhSachKhachHang dialog = new DanhSachKhachHang(parentFrame, true, this);
        dialog.setVisible(true);
    }//GEN-LAST:event_btnChonKHActionPerformed

    private void btnTaoHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoHDActionPerformed
        maHoaDonHienTai = taoHoaDon();
        if (maHoaDonHienTai > 0) {
            txtMaHD.setText(String.valueOf(maHoaDonHienTai));
            JOptionPane.showMessageDialog(this, "Tạo hóa đơn thành công!");
            taiDuLieuBangHoaDon();
            kichHoatButtonSauTaoHoaDon();
        }
    }//GEN-LAST:event_btnTaoHDActionPerformed

    private void DanhSachSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DanhSachSanPhamActionPerformed
        DanhSachSanPham dialog = new DanhSachSanPham(parentFrame, true, this);
        dialog.setVisible(true);
    }//GEN-LAST:event_DanhSachSanPhamActionPerformed

    private void btnThemSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemSPActionPerformed
        themSanPhamVaoHoaDon();
    }//GEN-LAST:event_btnThemSPActionPerformed

    private void tbSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbSanPhamMouseClicked
        int dongDuocChon = tbSanPham.getSelectedRow();
        if (dongDuocChon == -1 || maHoaDonHienTai == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm trong giỏ hàng!");
            return;
        }

        try {
            DefaultTableModel model = (DefaultTableModel) tbSanPham.getModel();
            int maSach = Integer.parseInt(model.getValueAt(dongDuocChon, 0).toString());
            Sach sach = sachDAO.findByID(maSach);
            if (sach != null) {
                chonSach(sach);
                int soLuong = Integer.parseInt(model.getValueAt(dongDuocChon, 3).toString());
                spSoLuong.setValue(soLuong);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi hiển thị thông tin sản phẩm: " + e.getMessage());
            Logger.getLogger(BanHang.class.getName()).log(Level.WARNING, "Loi hien thi san pham", e);
        }
    }//GEN-LAST:event_tbSanPhamMouseClicked

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        thanhToan();
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void btnDungQuetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDungQuetActionPerformed
        if (isbnScanner != null) {
            isbnScanner.stopScanning();
        }
    }//GEN-LAST:event_btnDungQuetActionPerformed

    private void btnQuetISBNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuetISBNActionPerformed
        if (isbnScanner != null) {
            isbnScanner.startScanning();
        }
    }//GEN-LAST:event_btnQuetISBNActionPerformed

    private void tblHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMouseClicked
        kichHoatButtonSauTaoHoaDon();
        int dongDuocChon = tblHoaDon.getSelectedRow();
        if (dongDuocChon == -1) {
            return;
        }

        kichHoatButtonSauTaoHoaDon();
        btnThanhToan.setEnabled(true);

        int maHoaDonDuocChon = Integer.parseInt(tblHoaDon.getValueAt(dongDuocChon, 0).toString());
        HoaDon hoaDon = hoaDonDAO.findById(maHoaDonDuocChon);

        if (hoaDon != null) {
            hienThiThongTinHoaDon(hoaDon);
        }
    }//GEN-LAST:event_tblHoaDonMouseClicked

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        huyHoaDon();
    }//GEN-LAST:event_btnHuyActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        suaSanPhamTrongGio();

    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnNhapISNBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNhapISNBActionPerformed
        nhapISBNThuCong();
    }//GEN-LAST:event_btnNhapISNBActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JPanel BanHang;
    private javax.swing.JButton DanhSachSanPham;
    public javax.swing.JPanel HoaDon;
    public javax.swing.JPanel TTHD;
    public javax.swing.JPanel TaoHD;
    private javax.swing.JButton btnChonKH;
    private javax.swing.JButton btnDungQuet;
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnMaGG;
    private javax.swing.JButton btnNhapISNB;
    private javax.swing.JButton btnQuetISBN;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnTaoHD;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnThemSP;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbAnh;
    private javax.swing.JLabel lbQuetISBN;
    private javax.swing.JRadioButton rdoTienMat;
    private javax.swing.JRadioButton rdoTk;
    private javax.swing.JSpinner spSoLuong;
    private javax.swing.JTable tbSanPham;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTextField txtDonGia;
    private javax.swing.JTextField txtGiamGia;
    private javax.swing.JTextField txtISBN;
    private javax.swing.JTextField txtMaHD;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtMaPhieu;
    private javax.swing.JTextField txtMaSach;
    private javax.swing.JTextField txtNgayLap;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTanhtien;
    private javax.swing.JTextField txtTenKH;
    private javax.swing.JTextField txtTenMaGG;
    private javax.swing.JTextField txtTenSach;
    private javax.swing.JLabel txtTieuDe;
    private javax.swing.JTextField txtTonKho;
    private javax.swing.JTextField txtTongtien;
    private javax.swing.JTextField txtTrangThai;
    // End of variables declaration//GEN-END:variables
private static class KetQuaValidation {

        private final boolean valid;
        private final String message;

        private KetQuaValidation(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public static KetQuaValidation thanhCong() {
            return new KetQuaValidation(true, "");
        }

        public static KetQuaValidation loi(String message) {
            return new KetQuaValidation(false, message);
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }
}
