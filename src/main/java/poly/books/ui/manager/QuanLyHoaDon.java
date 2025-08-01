/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package poly.books.ui.manager;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import poly.books.dao.ChiTietHoaDonDAO;
import poly.books.dao.HoaDonDAO;
import poly.books.entity.ChiTietHoaDon;
import poly.books.entity.HoaDon;
import poly.books.ui.Book;
import poly.books.util.XDate;
import poly.books.util.XDialog;
import poly.books.util.XJdbc;

/**
 *
 * @author LAPTOP
 */
public class QuanLyHoaDon extends javax.swing.JPanel implements poly.books.controller.QLHoaDonController {

    List<HoaDon> hoaDonList = new ArrayList<>();
    HoaDonDAO hoaDonDAO = new HoaDonDAO();
    List<ChiTietHoaDon> chiTietHoaDonList = new ArrayList<>();
    ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();

    /**
     * Creates new form QuanLyHoaDon
     */
    public QuanLyHoaDon() {
        initComponents();

        init();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                fillToTable();
            }

        });
    }
    private boolean ViewAllHDCT = false;

    public void init() {
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    // Xử lý single-click
                    ViewAllHDCT = false;
                    loadHDCT();
                } else if (evt.getClickCount() == 2) {
                    // Xử lý double-click (giữ nguyên mã hiện tại)

                    int selectedRow = tblHoaDon.getSelectedRow();
                    if (selectedRow != -1) {
                        int maHD = (int) tblHoaDon.getValueAt(selectedRow, 0);
                        ViewAllHDCT = false;
                        loadHDCT();

                        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(QuanLyHoaDon.this);
                        if (frame instanceof Book) {
                            Book bookFrame = (Book) frame;
                            if (bookFrame.cardLayout != null && bookFrame.QuanLy != null && bookFrame.banHang != null) {
                                bookFrame.cardLayout.show(bookFrame.QuanLy, "card2");

                                HoaDonDAO dao = new HoaDonDAO();
                                HoaDon hoaDon = dao.findById(maHD);
                                if (hoaDon != null) {
                                    bookFrame.banHang.setHoaDon(hoaDon);
                                } else {
                                    JOptionPane.showMessageDialog(QuanLyHoaDon.this, "Hóa đơn không tồn tại: " + maHD);
                                }
                            } else {
                                JOptionPane.showMessageDialog(QuanLyHoaDon.this, "Lỗi: Một hoặc nhiều thành phần null.");
                            }
                        }
                    }
                }
            }
        });
    }

    public void formHDCT() {
        initComponents();
    }

    public void refreshTable() {
        fillToTable();
    }

    public void loadHDCT() {
        DefaultTableModel defaultTableModel = (DefaultTableModel) tblHDCT.getModel();
        defaultTableModel.setRowCount(0);

        int totalQuantity = 0;
        double totalPrice = 0;

        if (ViewAllHDCT) {
            chiTietHoaDonList = chiTietHoaDonDAO.getAll();
            txtMaHDCT.setText("");
        } else {
            int selectedRow = tblHoaDon.getSelectedRow();
            if (selectedRow != -1) {
                int maHD = (int) tblHoaDon.getValueAt(selectedRow, 0);
                chiTietHoaDonList = chiTietHoaDonDAO.findByID(maHD);
                txtMaHDCT.setText(String.valueOf(maHD));
            } else {
                return;
            }
        }

        for (ChiTietHoaDon chiTietHoaDon : chiTietHoaDonList) {
            // Lấy thông tin giảm giá từ HoaDon
            HoaDon hoaDon = hoaDonDAO.findById(chiTietHoaDon.getMaHD());
            Double giamGia = 0.0;
            if (hoaDon != null && hoaDon.getMaPhieu() != null) {
                String sql = "SELECT GiaTri FROM PhieuGiamGia WHERE MaPhieu = ?";
                try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, hoaDon.getMaPhieu());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        giamGia = rs.getDouble("GiaTri");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi lấy giảm giá: " + e.getMessage());
                }
            }

            defaultTableModel.addRow(new Object[]{
                chiTietHoaDon.getMaHD(),
                chiTietHoaDon.getMaSach(),
                chiTietHoaDon.getSoLuong(),
                chiTietHoaDon.getDonGia(),
                String.format("%.2f", giamGia)
            });

            totalQuantity += chiTietHoaDon.getSoLuong();
            // Không tính totalPrice từ ChiTietHoaDon, lấy trực tiếp từ HoaDon
            if (!ViewAllHDCT && hoaDon != null) {
                totalPrice = hoaDon.getTongTien(); // Lấy TongTien từ HoaDon (đã trừ giảm giá)
            }
        }

        // Nếu ViewAllHDCT, tính tổng tiền từ tất cả hóa đơn
        if (ViewAllHDCT) {
            for (ChiTietHoaDon chiTiet : chiTietHoaDonList) {
                HoaDon hoaDon = hoaDonDAO.findById(chiTiet.getMaHD());
                if (hoaDon != null) {
                    totalPrice += hoaDon.getTongTien();
                }
            }
        }

        txtTongSoSanPham.setText(String.valueOf(totalQuantity));
        txtTongTienHDCT.setText(String.format("%.2f", totalPrice));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        QuanLyHD = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtTimKiemHoaDon = new javax.swing.JTextField();
        btnTimKiemHoaDon = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        txtSoLuongHoaDon = new javax.swing.JTextField();
        txtTongTienCacHoaDon = new javax.swing.JLabel();
        txtNgayTT = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtMaHDCT = new javax.swing.JTextField();
        txtTongTienHDCT = new javax.swing.JTextField();
        txtTongSoSanPham = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHDCT = new javax.swing.JTable();
        btnTatCaChiTietHoaDon = new javax.swing.JButton();
        btnHuy = new javax.swing.JButton();
        btnThanhToan = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(1123, 773));
        setPreferredSize(new java.awt.Dimension(1123, 773));
        setLayout(new java.awt.BorderLayout());

        QuanLyHD.setBackground(new java.awt.Color(255, 255, 255));
        QuanLyHD.setMinimumSize(new java.awt.Dimension(1123, 773));
        QuanLyHD.setPreferredSize(new java.awt.Dimension(1123, 773));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm hoá đơn", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        btnTimKiemHoaDon.setBackground(new java.awt.Color(102, 102, 255));
        btnTimKiemHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/book/icons8_search_25px.png"))); // NOI18N
        btnTimKiemHoaDon.setText("Tìm kiếm");
        btnTimKiemHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemHoaDonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(218, 218, 218)
                .addComponent(txtTimKiemHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnTimKiemHoaDon)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimKiemHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiemHoaDon))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jLabel19.setText("Số lượng hoá đơn");

        txtTongTienCacHoaDon.setText("Ngày Thanh toán");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chi tiết hoá đơn", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        jLabel21.setText("Mã Hoá đơn");

        jLabel22.setText("Tổng số sản phẩm");

        jLabel23.setText("Tổng tiền hoá đơn");

        tblHDCT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã hoá đơn", "Mã Sách", "Số lượng ", "Đơn giá", "Giảm giá"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblHDCT);

        btnTatCaChiTietHoaDon.setText("Tất cả chi tiết hoá đơn");
        btnTatCaChiTietHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTatCaChiTietHoaDonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMaHDCT, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22)
                .addGap(18, 18, 18)
                .addComponent(txtTongSoSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTongTienHDCT, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(btnTatCaChiTietHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                .addGap(66, 66, 66))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 984, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22)
                    .addComponent(txtMaHDCT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTongSoSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(txtTongTienHDCT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTatCaChiTietHoaDon))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btnHuy.setText("Hủy");
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });

        btnThanhToan.setText("Thanh Toán");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã hoá đơn", "Ngày lập", "Mã khách hàng", "Tên đăng nhập", "Mã phiếu", "Tổng tiền", "Phương thức", "Ngày thanh toán", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblHoaDon);

        jLabel1.setBackground(new java.awt.Color(0, 144, 193));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Quản Lý Hóa Đơn");
        jLabel1.setMaximumSize(new java.awt.Dimension(150, 50));
        jLabel1.setMinimumSize(new java.awt.Dimension(150, 50));
        jLabel1.setOpaque(true);
        jLabel1.setPreferredSize(new java.awt.Dimension(150, 50));

        javax.swing.GroupLayout QuanLyHDLayout = new javax.swing.GroupLayout(QuanLyHD);
        QuanLyHD.setLayout(QuanLyHDLayout);
        QuanLyHDLayout.setHorizontalGroup(
            QuanLyHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(QuanLyHDLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(QuanLyHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(QuanLyHDLayout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(jLabel19)
                        .addGap(18, 18, 18)
                        .addComponent(txtSoLuongHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtTongTienCacHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNgayTT, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80)
                        .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(104, 104, 104))
                    .addGroup(QuanLyHDLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        QuanLyHDLayout.setVerticalGroup(
            QuanLyHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, QuanLyHDLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(QuanLyHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtSoLuongHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTongTienCacHoaDon)
                    .addComponent(txtNgayTT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHuy)
                    .addComponent(btnThanhToan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        add(QuanLyHD, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTimKiemHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemHoaDonActionPerformed

        String timkiem = txtTimKiemHoaDon.getText().trim().toLowerCase();
        if (timkiem.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên thể loại để tìm kiếm!");
            fillToTable();
            return;
        }
        DefaultTableModel defaultTableModel = (DefaultTableModel) tblHoaDon.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(defaultTableModel);
        tblHoaDon.setRowSorter(sorter);
        if (timkiem.isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + timkiem, 0));
    }//GEN-LAST:event_btnTimKiemHoaDonActionPerformed

    private void tblHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMouseClicked
        ViewAllHDCT = false;
        loadHDCT();
    }//GEN-LAST:event_tblHoaDonMouseClicked

    private void btnTatCaChiTietHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTatCaChiTietHoaDonActionPerformed
        ViewAllHDCT = true;
        loadHDCT();
    }//GEN-LAST:event_btnTatCaChiTietHoaDonActionPerformed

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        huyHD();
    }//GEN-LAST:event_btnHuyActionPerformed

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        // TODO add your code here:
        ThanhToan();
    }//GEN-LAST:event_btnThanhToanActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel QuanLyHD;
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnTatCaChiTietHoaDon;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnTimKiemHoaDon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblHDCT;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTextField txtMaHDCT;
    private javax.swing.JTextField txtNgayTT;
    private javax.swing.JTextField txtSoLuongHoaDon;
    private javax.swing.JTextField txtTimKiemHoaDon;
    private javax.swing.JTextField txtTongSoSanPham;
    private javax.swing.JLabel txtTongTienCacHoaDon;
    private javax.swing.JTextField txtTongTienHDCT;
    // End of variables declaration//GEN-END:variables

    public void huyHD() {
        int selectedRow = tblHoaDon.getSelectedRow();
        if (selectedRow == -1) {
            XDialog.alert("Vui lòng chọn hóa đơn để hủy!");
            return;
        }
        int maHD = (int) tblHoaDon.getValueAt(selectedRow, 0);
        if (XDialog.confirm("Bạn có chắc muốn hủy hóa đơn này?")) {
            try {
                // Lấy danh sách chi tiết hóa đơn
                List<ChiTietHoaDon> chiTietList = chiTietHoaDonDAO.findByID(maHD);
                Connection conn = XJdbc.openConnection();
                String sqlKho = "UPDATE Kho SET SoLuong = SoLuong + ? WHERE MaSach = ?";

                // Khôi phục số lượng trong kho
                for (ChiTietHoaDon chiTiet : chiTietList) {
                    try (PreparedStatement ps = conn.prepareStatement(sqlKho)) {
                        ps.setInt(1, chiTiet.getSoLuong());
                        ps.setInt(2, chiTiet.getMaSach());
                        ps.executeUpdate();
                    }
                }

                // Xóa chi tiết hóa đơn và hóa đơn
                chiTietHoaDonDAO.delete(maHD);
                hoaDonDAO.delete(maHD);
                XDialog.alert("Hủy hóa đơn thành công!");
                fillToTable();
                loadHDCT();
            } catch (Exception e) {
                XDialog.alert("Lỗi khi hủy hóa đơn: " + e.getMessage());
            }
        }
    }

    public void ThanhToan() {
        
        if(tblHDCT.getRowCount()==0){
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm trước khi thanh toán");
            return;
        }
        
        int selectedRow = tblHoaDon.getSelectedRow();
        if (selectedRow == -1) {
            XDialog.alert("Vui lòng chọn hóa đơn để thanh toán!");
            return;
        }
        int maHD = (int) tblHoaDon.getValueAt(selectedRow, 0);
        HoaDon hoaDon = hoaDonDAO.findById(maHD);
        if (hoaDon != null && hoaDon.getTrangThai() == 0) {
            hoaDon.setTrangThai(1);
            hoaDon.setNgayThanhToan(new java.util.Date());
            hoaDonDAO.update(hoaDon);
            txtNgayTT.setText(XDate.format(XDate.now(), "dd-MM-yyyy HH:mm:ss"));
            XDialog.alert("Thanh toán hóa đơn thành công!");
            fillToTable();
            loadHDCT();
        } else {
            XDialog.alert("Hóa đơn không hợp lệ hoặc đã thanh toán!");
        }
    }

    @Override
    public void open() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setForm(HoaDon entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public HoaDon getForm() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void fillToTable() {
        DefaultTableModel defaultTableModel = (DefaultTableModel) tblHoaDon.getModel();
        defaultTableModel.setRowCount(0);
        hoaDonList = hoaDonDAO.getAll();
        int totalOfQuantity = 0;
        int totalOfPrice = 0;
        for (HoaDon hoaDon : hoaDonList) {
            // Chỉ lấy hóa đơn có trạng thái = 0 (chưa thanh toán)
            if (hoaDon.getTrangThai() == 0) {
                Object[] rowData = {
                    hoaDon.getMaHD(),
                    hoaDon.getNgayLap(),
                    hoaDon.getMaKH(),
                    hoaDon.getTenDangNhap(),
                    hoaDon.getMaPhieu(),
                    hoaDon.getTongTien(),
                    hoaDon.getPhuongThuc() == 1 ? "Tiền mặt" : "Chuyển khoản",
                    hoaDon.getNgayThanhToan(),
                    "Chờ thanh toán"
                };
                totalOfQuantity++;
                totalOfPrice += (hoaDon.getTongTien());
                defaultTableModel.addRow(rowData);
                txtSoLuongHoaDon.setText(String.valueOf(totalOfQuantity));

            }
        }
    }

    @Override
    public void create() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setEditable(boolean editable) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
