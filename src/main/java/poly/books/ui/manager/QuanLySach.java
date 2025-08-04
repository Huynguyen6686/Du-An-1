/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package poly.books.ui.manager;

import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import poly.books.controller.SachController;
import poly.books.dao.LinhVucDAO;
import poly.books.dao.LoaiSachDAO;
import poly.books.dao.NgonNguDAO;
import poly.books.dao.NhaXuatBanDAO;
import poly.books.dao.SachDAO;
import poly.books.dao.TacGiaDAO;
import poly.books.entity.LinhVuc;
import poly.books.entity.LoaiSach;
import poly.books.entity.NgonNgu;
import poly.books.entity.NhaXuatBan;
import poly.books.entity.Sach;
import poly.books.entity.TacGia;
import poly.books.util.XDialog;
import poly.books.util.XJdbc;

/**
 *
 * @author HuyNguyen
 */
public class QuanLySach extends javax.swing.JPanel implements poly.books.controller.SachController {

    List<Sach> sachList = new ArrayList<>();
    SachDAO sachDAO = new SachDAO();
    List<NhaXuatBan> nhaXuatBanList = new ArrayList<>();
    NhaXuatBanDAO nhaXuatBanDAO = new NhaXuatBanDAO();
    List<NgonNgu> ngonNguList = new ArrayList<>();
    NgonNguDAO ngonNguDAO = new NgonNguDAO();
    List<LinhVuc> linhVucList = new ArrayList<>();
    LinhVucDAO linhVucDAO = new LinhVucDAO();
    List<LoaiSach> loaiSachList = new ArrayList<>();
    LoaiSachDAO loaiSachDAO = new LoaiSachDAO();
    List<TacGia> listTacGia = new ArrayList<>();
    TacGiaDAO TacGiaDAO = new TacGiaDAO();

    private JFrame parentFrame;

    /**
     * Creates new form QuanLySach1
     */
    public QuanLySach() {
        initComponents();

        cboLinhVuc.setModel(new DefaultComboBoxModel<>());
        cboTheLoai.setModel(new DefaultComboBoxModel<>());
        btnxoalv.addActionListener(e -> removeSelectedLinhVuc());
        btnxoatl.addActionListener(e -> removeSelectedTheLoai());
        fillToTablelv();
        fillToTabletl();
        fillToTablenxb();
        fillToTabletg();
        fillToTableng();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                fillToTable();
            }

        });
        tbllv.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbllvMouseClicked(evt);
            }
        });

        tbltl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbltlMouseClicked(evt);
            }
        });
    }

    public void refreshLinhVucTable() {
        fillToTablelv(); // Gọi lại phương thức fillToTablelv để cập nhật dữ liệu
    }

    public void showQuanLyLinhVuc(JFrame frame) {
        SachController.super.showQuanLyLinhVuc(frame); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody

    }

    public void refresLoaiSachTable() {
        fillToTabletl();
    }

    private void removeSelectedLinhVuc() {
        int index = cboLinhVuc.getSelectedIndex();
        if (index >= 0) {
            DefaultComboBoxModel model = (DefaultComboBoxModel) cboLinhVuc.getModel();
            model.removeElementAt(index);
            JOptionPane.showMessageDialog(this, "Đã xóa lĩnh vực khỏi combobox");
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lĩnh vực cần xóa");
        }
    }

    private void removeSelectedTheLoai() {
        int index = cboTheLoai.getSelectedIndex();
        if (index >= 0) {
            DefaultComboBoxModel model = (DefaultComboBoxModel) cboTheLoai.getModel();
            model.removeElementAt(index);
            JOptionPane.showMessageDialog(this, "Đã xóa thể loại khỏi combobox");
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thể loại cần xóa");
        }
    }

    // Thêm phương thức fill combobox
    public void fillCboLinhVuc() {
        DefaultComboBoxModel model = new DefaultComboBoxModel<>();
        cboLinhVuc.setModel(model);
        linhVucList = linhVucDAO.getAll();
        for (LinhVuc lv : linhVucList) {
            model.addElement(lv);
        }
    }

    public void fillCboTheLoai() {
        DefaultComboBoxModel model = new DefaultComboBoxModel<>();
        cboTheLoai.setModel(model);
        loaiSachList = loaiSachDAO.getAll();
        for (LoaiSach ls : loaiSachList) {
            model.addElement(ls);
        }
    }

    private String getTenTacGia(int maTacGia) {
        TacGia tacGia = TacGiaDAO.findByID(maTacGia);
        return tacGia != null ? tacGia.getTenTacGia() : "Không xác định";
    }

    private String getTenNXB(int maNXB) {
        NhaXuatBan nxb = nhaXuatBanDAO.findByID(maNXB);
        return nxb != null ? nxb.getTenNXB() : "Không xác định";
    }

    private String getTenNgonNgu(int maNgonNgu) {
        NgonNgu ngonNgu = ngonNguDAO.findByID(maNgonNgu);
        return ngonNgu != null ? ngonNgu.getTenNgonNgu() : "Không xác định";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Quanlysach = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtMaSach = new javax.swing.JTextField();
        txtTenSach = new javax.swing.JTextField();
        cboTheLoai = new javax.swing.JComboBox<>();
        cboLinhVuc = new javax.swing.JComboBox<>();
        txtTap = new javax.swing.JTextField();
        txtLanTaiBan = new javax.swing.JTextField();
        txtgia = new javax.swing.JTextField();
        txtISBN = new javax.swing.JTextField();
        lblanh = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnTimKiem = new javax.swing.JButton();
        txtTimKiem = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbSach = new javax.swing.JTable();
        jLabel25 = new javax.swing.JLabel();
        txtNamSX = new javax.swing.JTextField();
        tabPane = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbllv = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbltl = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbltg = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblnxb = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblng = new javax.swing.JTable();
        btnxoalv = new javax.swing.JButton();
        btnxoatl = new javax.swing.JButton();
        txtnxb = new javax.swing.JTextField();
        txtngonngu = new javax.swing.JTextField();
        txttacgia = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        btnNXB2 = new javax.swing.JButton();
        btnTacgia2 = new javax.swing.JButton();
        btnTheLoai2 = new javax.swing.JButton();
        btnLinhVuc2 = new javax.swing.JButton();
        btnNgonNgu2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(1123, 773));
        setPreferredSize(new java.awt.Dimension(1123, 773));
        setLayout(new java.awt.BorderLayout());

        Quanlysach.setBackground(new java.awt.Color(255, 255, 255));
        Quanlysach.setMinimumSize(new java.awt.Dimension(1123, 773));
        Quanlysach.setPreferredSize(new java.awt.Dimension(1123, 773));

        jLabel1.setText("Mã Sách");

        jLabel2.setText("Tên sách");

        jLabel3.setText("Tác giả");

        jLabel4.setText("Lĩnh vực");

        jLabel5.setText("Thể loại");

        jLabel6.setText("Nhà xuất bản");

        jLabel11.setText("Ngôn ngữ");

        jLabel14.setText("Tập");

        jLabel15.setText("Lần tái bản");

        jLabel16.setText("Giá bán");

        jLabel17.setText("ISBN");

        txtTenSach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenSachActionPerformed(evt);
            }
        });

        cboTheLoai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboTheLoai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboTheLoaiMouseClicked(evt);
            }
        });

        cboLinhVuc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLinhVuc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboLinhVucMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cboLinhVucMouseEntered(evt);
            }
        });

        txtgia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtgiaActionPerformed(evt);
            }
        });

        lblanh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_supplier_25px.png"))); // NOI18N
        lblanh.setText("ảnh");
        lblanh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        lblanh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblanhMouseClicked(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 153, 153));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_25px_5.png"))); // NOI18N
        jButton1.setText("Thêm");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 255, 204));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_edit_25px.png"))); // NOI18N
        jButton2.setText("Sửa");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(51, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_delete_25px_1.png"))); // NOI18N
        jButton3.setText("Xóa");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 204, 204));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_reset_25px_1.png"))); // NOI18N
        jButton4.setText("Làm mới");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm Kiếm", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(102, 102, 102))); // NOI18N

        btnTimKiem.setBackground(new java.awt.Color(102, 102, 255));
        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_search_25px.png"))); // NOI18N
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(74, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiem))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        tbSach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "MaSach", "TenSach", "MaTacGia", "MaLinhVuc", "MaLoaiSach", "MaNXB", "NamXuatBan", "GiaBan", "LanTaiBan", "ISBN", "Tap", "MaNgonNgu", "Số Lượng", "HinhAnh"
            }
        ));
        tbSach.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbSachMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbSach);

        jLabel25.setText("Năm sản xuất");

        tabPane.setBackground(new java.awt.Color(255, 255, 255));
        tabPane.setOpaque(true);
        tabPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabPaneMouseClicked(evt);
            }
        });

        tbllv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Lĩnh vực"
            }
        ));
        tbllv.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbllvMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbllv);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabPane.addTab("Lĩnh vực", jPanel8);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        tbltl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Thể loại"
            }
        ));
        tbltl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbltlMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tbltl);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabPane.addTab("Thể loại", jPanel9);

        tbltg.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Tác giả"
            }
        ));
        tbltg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbltgMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tbltg);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabPane.addTab("Tác giả", jPanel2);

        tblnxb.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Nhà xuất bản"
            }
        ));
        tblnxb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblnxbMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblnxb);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabPane.addTab("Nhà xuất bản", jPanel3);

        tblng.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Ngôn ngữ"
            }
        ));
        tblng.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblngMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tblng);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabPane.addTab("ngôn ngữ", jPanel5);

        btnxoalv.setText("xóa");

        btnxoatl.setText("xóa");

        txtnxb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnxbActionPerformed(evt);
            }
        });

        txtngonngu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtngonnguActionPerformed(evt);
            }
        });

        txttacgia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttacgiaActionPerformed(evt);
            }
        });

        jLabel7.setText("Số Lượng:");

        btnNXB2.setText("Nhà xuất bản");
        btnNXB2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNXB2btnNXBActionPerformed(evt);
            }
        });

        btnTacgia2.setText("Tác giả");
        btnTacgia2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTacgia2btnTacgiaActionPerformed(evt);
            }
        });

        btnTheLoai2.setText("Thể loại");
        btnTheLoai2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTheLoai2btnTheLoaiActionPerformed(evt);
            }
        });

        btnLinhVuc2.setText("Lĩnh vực");
        btnLinhVuc2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLinhVuc2btnLinhVucActionPerformed(evt);
            }
        });

        btnNgonNgu2.setText("Ngôn ngữ");
        btnNgonNgu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNgonNgu2btnNgonNguActionPerformed(evt);
            }
        });

        jLabel8.setBackground(new java.awt.Color(0, 144, 193));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Quản Lý Sách");
        jLabel8.setMaximumSize(new java.awt.Dimension(115, 50));
        jLabel8.setMinimumSize(new java.awt.Dimension(115, 50));
        jLabel8.setOpaque(true);
        jLabel8.setPreferredSize(new java.awt.Dimension(115, 50));

        javax.swing.GroupLayout QuanlysachLayout = new javax.swing.GroupLayout(Quanlysach);
        Quanlysach.setLayout(QuanlysachLayout);
        QuanlysachLayout.setHorizontalGroup(
            QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(QuanlysachLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnTacgia2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNgonNgu2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTheLoai2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNXB2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLinhVuc2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(QuanlysachLayout.createSequentialGroup()
                        .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(QuanlysachLayout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtMaSach))
                            .addGroup(QuanlysachLayout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cboLinhVuc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(QuanlysachLayout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cboTheLoai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(QuanlysachLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtnxb, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(QuanlysachLayout.createSequentialGroup()
                                .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txttacgia, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTenSach, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnxoalv, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnxoatl, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(QuanlysachLayout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtSoLuong))
                            .addGroup(QuanlysachLayout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addGap(10, 10, 10)
                                .addComponent(txtNamSX))
                            .addGroup(QuanlysachLayout.createSequentialGroup()
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtLanTaiBan, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(QuanlysachLayout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtngonngu))
                            .addGroup(QuanlysachLayout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtTap, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(QuanlysachLayout.createSequentialGroup()
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtISBN))
                            .addGroup(QuanlysachLayout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtgia, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(QuanlysachLayout.createSequentialGroup()
                        .addComponent(lblanh, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(QuanlysachLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1016, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        QuanlysachLayout.setVerticalGroup(
            QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, QuanlysachLayout.createSequentialGroup()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(QuanlysachLayout.createSequentialGroup()
                        .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtngonngu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(txtTap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(txtLanTaiBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(txtgia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(txtISBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtNamSX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25))
                        .addGap(18, 18, 18)
                        .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(QuanlysachLayout.createSequentialGroup()
                        .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtMaSach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtTenSach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(QuanlysachLayout.createSequentialGroup()
                                .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(txttacgia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cboLinhVuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnxoalv)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(cboTheLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnxoatl))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(txtnxb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(QuanlysachLayout.createSequentialGroup()
                                .addComponent(btnNXB2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnTacgia2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnTheLoai2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnLinhVuc2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnNgonNgu2, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(QuanlysachLayout.createSequentialGroup()
                        .addGroup(QuanlysachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(QuanlysachLayout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4))
                            .addComponent(lblanh, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79))
        );

        add(Quanlysach, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnNXB2btnNXBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNXB2btnNXBActionPerformed
        this.showQuanLyNhaXuatBan(parentFrame);
    }//GEN-LAST:event_btnNXB2btnNXBActionPerformed

    private void btnTacgia2btnTacgiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTacgia2btnTacgiaActionPerformed
        this.showQuanLyTacGia(parentFrame);
    }//GEN-LAST:event_btnTacgia2btnTacgiaActionPerformed

    private void btnTheLoai2btnTheLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTheLoai2btnTheLoaiActionPerformed
        this.showQuanLyTheLoai(parentFrame);
    }//GEN-LAST:event_btnTheLoai2btnTheLoaiActionPerformed

    private void btnLinhVuc2btnLinhVucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLinhVuc2btnLinhVucActionPerformed
        this.showQuanLyLinhVuc(parentFrame);
    }//GEN-LAST:event_btnLinhVuc2btnLinhVucActionPerformed

    private void btnNgonNgu2btnNgonNguActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNgonNgu2btnNgonNguActionPerformed
        this.showQuanLyNgonNgu(parentFrame);
    }//GEN-LAST:event_btnNgonNgu2btnNgonNguActionPerformed

    private void txtTenSachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenSachActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenSachActionPerformed

    private void cboTheLoaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboTheLoaiMouseClicked

    }//GEN-LAST:event_cboTheLoaiMouseClicked

    private void cboLinhVucMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboLinhVucMouseClicked

    }//GEN-LAST:event_cboLinhVucMouseClicked

    private void cboLinhVucMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboLinhVucMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_cboLinhVucMouseEntered

    private void txtgiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtgiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtgiaActionPerformed

    private void lblanhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblanhMouseClicked
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName();
            File destDir = new File("src/main/resources/imgSach"); // Thư mục tài nguyên
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            File dest = new File(destDir, fileName);
            try {
                Files.copy(selectedFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Không thể sao chép ảnh: " + ex.getMessage());
                return;
            }
            lblanh.setToolTipText(fileName); // Lưu tên file
            lblanh.setText("");

            // Tải và thu nhỏ ảnh theo kích thước của lblanh
            java.net.URL imageUrl = getClass().getResource("/imgSach/" + fileName);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                Image img = icon.getImage().getScaledInstance(lblanh.getWidth(), lblanh.getHeight(), Image.SCALE_SMOOTH);
                lblanh.setIcon(new ImageIcon(img));
            } else {
                lblanh.setText("Không tải được ảnh");
            }
        }
    }//GEN-LAST:event_lblanhMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.create();
        fillToTable();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.update();
        fillToTable();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.delete();
        fillToTable();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.clear();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        String timkiem = txtTimKiem.getText().trim().toLowerCase();
        DefaultTableModel defaultTableModel = (DefaultTableModel) tbSach.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(defaultTableModel);
        tbSach.setRowSorter(sorter);

        if (timkiem.isEmpty()) {
            // Nếu chuỗi tìm kiếm trống, reset bộ lọc và làm mới bảng
            sorter.setRowFilter(null);
            fillToTable(); // Gọi lại dữ liệu ban đầu (nếu bạn có phương thức này)
            return;
        }

        // Áp dụng lọc theo cột 1 (ví dụ: cột thể loại)
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + timkiem, 1));
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void tbSachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbSachMouseClicked

        int index = tbSach.getSelectedRow();
        if (index >= 0) {
            Sach sach = sachList.get(index);
            this.setForm(sach);
        }
    }//GEN-LAST:event_tbSachMouseClicked

    private void tbllvMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbllvMouseClicked
        int row = tbllv.getSelectedRow();
        if (row >= 0) {
            String tenLinhVuc = (String) tbllv.getValueAt(row, 0);
            LinhVuc lv = linhVucList.stream()
                    .filter(x -> x.getTenLinhVuc().equals(tenLinhVuc))
                    .findFirst()
                    .orElse(null);

            if (lv != null) {
                DefaultComboBoxModel model = (DefaultComboBoxModel) cboLinhVuc.getModel();
                boolean exists = false;
                for (int i = 0; i < model.getSize(); i++) {
                    LinhVuc item = (LinhVuc) model.getElementAt(i);
                    if (item.getMaLinhVuc() == lv.getMaLinhVuc()) { // So sánh bằng MaLinhVuc
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    model.addElement(lv);
                    cboLinhVuc.setSelectedItem(lv);
                    JOptionPane.showMessageDialog(this, "Đã thêm lĩnh vực vào combobox");
                } else {
                    JOptionPane.showMessageDialog(this, "Lĩnh vực này đã có trong combobox");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lĩnh vực không hợp lệ!");
            }
        }
    }//GEN-LAST:event_tbllvMouseClicked

    private void tbltlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbltlMouseClicked
        int row = tbltl.getSelectedRow();
        if (row >= 0) {
            String tenLoaiSach = (String) tbltl.getValueAt(row, 0);
            LoaiSach ls = loaiSachList.stream()
                    .filter(x -> x.getTenLoaiSach().equals(tenLoaiSach))
                    .findFirst()
                    .orElse(null);

            if (ls != null) {
                DefaultComboBoxModel model = (DefaultComboBoxModel) cboTheLoai.getModel();
                boolean exists = false;
                for (int i = 0; i < model.getSize(); i++) {
                    LoaiSach item = (LoaiSach) model.getElementAt(i);
                    if (item.getMaLoaiSach() == ls.getMaLoaiSach()) { // So sánh bằng MaLoaiSach
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    model.addElement(ls);
                    cboTheLoai.setSelectedItem(ls);
                    JOptionPane.showMessageDialog(this, "Đã thêm thể loại vào combobox");
                } else {
                    JOptionPane.showMessageDialog(this, "Thể loại này đã có trong combobox");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Thể loại không hợp lệ!");
            }
        }
    }//GEN-LAST:event_tbltlMouseClicked

    private void tbltgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbltgMouseClicked
        // TODO add your handling code here:
        int row = tbltg.getSelectedRow();
        if (row >= 0) {
            String tenTacGia = (String) tbltg.getValueAt(row, 0);
            txttacgia.setText(tenTacGia);
        }
    }//GEN-LAST:event_tbltgMouseClicked

    private void tblnxbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblnxbMouseClicked
        // TODO add your handling code here:
        int row = tblnxb.getSelectedRow();
        if (row >= 0) {
            String tenNXB = (String) tblnxb.getValueAt(row, 0);
            txtnxb.setText(tenNXB);
        }
    }//GEN-LAST:event_tblnxbMouseClicked

    private void tblngMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblngMouseClicked
        // TODO add your handling code here:
        int row = tblng.getSelectedRow();
        if (row >= 0) {
            String tenNgonNgu = (String) tblng.getValueAt(row, 0);
            txtngonngu.setText(tenNgonNgu);
        }
    }//GEN-LAST:event_tblngMouseClicked

    private void txtnxbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnxbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnxbActionPerformed

    private void txtngonnguActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtngonnguActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtngonnguActionPerformed

    private void txttacgiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttacgiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttacgiaActionPerformed

    private void tabPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabPaneMouseClicked
        fillToTablelv();
        fillToTabletl();
        fillToTablenxb();
        fillToTabletg();
        fillToTableng();
    }//GEN-LAST:event_tabPaneMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JPanel Quanlysach;
    private javax.swing.JButton btnLinhVuc2;
    private javax.swing.JButton btnNXB2;
    private javax.swing.JButton btnNgonNgu2;
    private javax.swing.JButton btnTacgia2;
    private javax.swing.JButton btnTheLoai2;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnxoalv;
    private javax.swing.JButton btnxoatl;
    private javax.swing.JComboBox<String> cboLinhVuc;
    private javax.swing.JComboBox<String> cboTheLoai;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JLabel lblanh;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTable tbSach;
    private javax.swing.JTable tbllv;
    private javax.swing.JTable tblng;
    private javax.swing.JTable tblnxb;
    private javax.swing.JTable tbltg;
    private javax.swing.JTable tbltl;
    private javax.swing.JTextField txtISBN;
    private javax.swing.JTextField txtLanTaiBan;
    private javax.swing.JTextField txtMaSach;
    private javax.swing.JTextField txtNamSX;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTap;
    private javax.swing.JTextField txtTenSach;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txtgia;
    private javax.swing.JTextField txtngonngu;
    private javax.swing.JTextField txtnxb;
    private javax.swing.JTextField txttacgia;
    // End of variables declaration//GEN-END:variables
    public void refreshCboLinhVuc() {
        fillCboLinhVuc();
    }

    public void refreshCboTheLoai() {
        fillCboTheLoai();
    }

    public void fillToTablelv() {
        DefaultTableModel model = (DefaultTableModel) tbllv.getModel();
        model.setRowCount(0);
        linhVucList = linhVucDAO.getAll();
        for (LinhVuc lv : linhVucList) {
            Object[] rowData = {
                lv.getTenLinhVuc()
            };
            model.addRow(rowData);
        }

    }

    public void fillToTabletl() {
        DefaultTableModel model = (DefaultTableModel) tbltl.getModel();
        model.setRowCount(0);
        loaiSachList = loaiSachDAO.getAll();
        for (LoaiSach n : loaiSachList) {
            Object[] rowData = {
                n.getTenLoaiSach()
            };
            model.addRow(rowData);
        }

    }

    public void fillToTabletg() {
        DefaultTableModel defaultTableModel = (DefaultTableModel) tbltg.getModel();
        defaultTableModel.setRowCount(0);
        listTacGia = TacGiaDAO.getAll();
        for (TacGia tacGia : listTacGia) {
            Object[] rowData = {
                tacGia.getTenTacGia(),};
            defaultTableModel.addRow(rowData);
        }
    }

    public void fillToTablenxb() {
        DefaultTableModel defaultTableModel = (DefaultTableModel) tblnxb.getModel();
        defaultTableModel.setRowCount(0);
        nhaXuatBanList = nhaXuatBanDAO.getAll();
        for (NhaXuatBan nhaXuatBan : nhaXuatBanList) {
            Object[] rowData = {
                nhaXuatBan.getTenNXB()
            };
            defaultTableModel.addRow(rowData);
        }
    }

    public void fillToTableng() {
        DefaultTableModel model = (DefaultTableModel) tblng.getModel();
        model.setRowCount(0);
        ngonNguList = ngonNguDAO.getAll();
        for (NgonNgu n : ngonNguList) {
            Object[] rowData = {
                n.getTenNgonNgu()
            };
            model.addRow(rowData);
        }

    }

    @Override
    public void showJDialog(JDialog dialog) {
        SachController.super.showJDialog(dialog); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public void showQuanLyTacGia(JFrame frame) {
        SachController.super.showQuanLyTacGia(frame); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public void showQuanLyTheLoai(JFrame frame) {
        SachController.super.showQuanLyTheLoai(frame); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody

    }

    @Override
    public void showQuanLyNgonNgu(JFrame frame) {
        SachController.super.showQuanLyNgonNgu(frame); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public void showQuanLyNhaXuatBan(JFrame frame) {
        SachController.super.showQuanLyNhaXuatBan(frame); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public void open() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void loadLinhVucVaTheLoai(int maSach) {
        // Load LinhVuc for the book
        List<LinhVuc> linhVucs = linhVucDAO.findByLinhVucID(maSach);
        if (!linhVucs.isEmpty()) {
            cboLinhVuc.setSelectedItem(linhVucs.get(0));
        }

        // Load LoaiSach for the book
        List<LoaiSach> loaiSachs = loaiSachDAO.findBySachID(maSach);
        if (!loaiSachs.isEmpty()) {
            cboTheLoai.setSelectedItem(loaiSachs.get(0));
        }
    }

    private void updateCboLinhVuc(List<LinhVuc> linhVucs) {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboLinhVuc.getModel();
        model.removeAllElements();
        for (LinhVuc lv : linhVucs) {
            model.addElement(lv);
        }
        if (!linhVucs.isEmpty()) {
            cboLinhVuc.setSelectedItem(linhVucs.get(0));
        }
    }

    private void updateCboTheLoai(List<LoaiSach> loaiSachs) {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboTheLoai.getModel();
        model.removeAllElements();
        for (LoaiSach ls : loaiSachs) {
            model.addElement(ls);
        }
        if (!loaiSachs.isEmpty()) {
            cboTheLoai.setSelectedItem(loaiSachs.get(0));
        }
    }

    @Override
    public void setForm(Sach entity) {
        try {
            txtMaSach.setText(String.valueOf(entity.getMaSach()));
            txtTenSach.setText(entity.getTenSach());
            txtTap.setText(entity.getTap() != null ? String.valueOf(entity.getTap()) : "");
            txtLanTaiBan.setText(String.valueOf(entity.getLanTaiBan()));
            txtgia.setText(String.valueOf(entity.getGiaBan()));
            txtISBN.setText(entity.getISBN());
            txtNamSX.setText(new SimpleDateFormat("yyyy").format(entity.getNamXuatBan()));
            txttacgia.setText(getTenTacGia(entity.getMaTacGia()));
            txtnxb.setText(getTenNXB(entity.getMaNXB()));
            txtngonngu.setText(getTenNgonNgu(entity.getMaNgonNgu()));
            txtSoLuong.setText(String.valueOf(entity.getSoLuong())); // Thêm dòng này

            lblanh.setIcon(null);
            lblanh.setText("");
            if (entity.getHinhAnh() != null && !entity.getHinhAnh().isEmpty()) {
                lblanh.setToolTipText(entity.getHinhAnh());
                java.net.URL imageUrl = getClass().getResource("/imgSach/" + entity.getHinhAnh());
                if (imageUrl != null) {
                    ImageIcon icon = new ImageIcon(imageUrl);
                    Image img = icon.getImage().getScaledInstance(lblanh.getWidth(), lblanh.getHeight(), Image.SCALE_SMOOTH);
                    lblanh.setIcon(new ImageIcon(img));
                }
            }

            // Tải toàn bộ danh sách lĩnh vực và thể loại
            List<LinhVuc> linhVucs = linhVucDAO.findByLinhVucID(entity.getMaSach());
            updateCboLinhVuc(linhVucs);

            List<LoaiSach> loaiSachs = loaiSachDAO.findBySachID(entity.getMaSach());
            updateCboTheLoai(loaiSachs);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu sách: " + e.getMessage());
        }
    }

    @Override
    public Sach getForm() {
        try {
            Sach sach = new Sach();

            // Xử lý mã sách
            if (!txtMaSach.getText().isEmpty()) {
                sach.setMaSach(Integer.parseInt(txtMaSach.getText()));
            }

            // Xử lý tên sách
            if (txtTenSach.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Tên sách không được để trống");
            }
            sach.setTenSach(txtTenSach.getText());

            // Xử lý tập
            if (!txtTap.getText().isEmpty()) {
                sach.setTap(Integer.parseInt(txtTap.getText()));
            } else {
                sach.setTap(0);
            }

            // Xử lý lần tái bản
            if (!txtLanTaiBan.getText().isEmpty()) {
                sach.setLanTaiBan(Integer.parseInt(txtLanTaiBan.getText()));
            } else {
                sach.setLanTaiBan(1);
            }

            // Xử lý giá bán
            if (!txtgia.getText().isEmpty()) {
                double giaBan = Double.parseDouble(txtgia.getText());
                if (giaBan <= 0) {
                    throw new IllegalArgumentException("Giá bán phải lớn hơn 0");
                }
                sach.setGiaBan(giaBan);
            } else {
                throw new IllegalArgumentException("Giá bán không được để trống");
            }

            // Xử lý ISBN
            if (txtISBN.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("ISBN không được để trống");
            }
            sach.setISBN(txtISBN.getText());

            // Xử lý năm xuất bản
            if (!txtNamSX.getText().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                sdf.setLenient(false);
                sach.setNamXuatBan(sdf.parse(txtNamSX.getText()));
            } else {
                throw new IllegalArgumentException("Năm xuất bản không được để trống");
            }

            // Xử lý tác giả
            String tenTacGia = txttacgia.getText();
            TacGia tacGia = TacGiaDAO.findByTen(tenTacGia);
            if (tacGia == null) {
                throw new IllegalArgumentException("Tác giả không tồn tại: " + tenTacGia);
            }
            sach.setMaTacGia(tacGia.getMaTacGia());

            // Xử lý nhà xuất bản
            String tenNXB = txtnxb.getText();
            NhaXuatBan nxb = nhaXuatBanDAO.findByTen(tenNXB);
            if (nxb == null) {
                throw new IllegalArgumentException("Nhà xuất bản không tồn tại: " + tenNXB);
            }
            sach.setMaNXB(nxb.getMaNXB());

            // Xử lý ngôn ngữ
            String tenNgonNgu = txtngonngu.getText();
            NgonNgu ngonNgu = ngonNguDAO.findByTen(tenNgonNgu);
            if (ngonNgu == null) {
                throw new IllegalArgumentException("Ngôn ngữ không tồn tại: " + tenNgonNgu);
            }
            sach.setMaNgonNgu(ngonNgu.getMaNgonNgu());

            // Xử lý số lượng
            if (!txtSoLuong.getText().isEmpty()) {
                int soLuong = Integer.parseInt(txtSoLuong.getText());
                if (soLuong < 0) {
                    throw new IllegalArgumentException("Số lượng không được âm");
                }
                sach.setSoLuong(soLuong); // Gán số lượng vào đối tượng Sach
            } else {
                throw new IllegalArgumentException("Số lượng không được để trống");
            }

            // Xử lý hình ảnh
            String imageName = lblanh.getToolTipText();
            if (imageName != null && !imageName.isEmpty()) {
                sach.setHinhAnh(imageName);
            } else {
                sach.setHinhAnh("default.png");
            }

            return sach;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số vào các trường số liệu", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Năm xuất bản phải đúng định dạng yyyy", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu từ form: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    @Override
    public void fillToTable() {
        DefaultTableModel model = (DefaultTableModel) tbSach.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        sachList = sachDAO.getAllDanhSachSP(); // Sử dụng getAllDanhSachSP để lấy cả số lượng

        for (Sach sach : sachList) {
            String tenTacGia = TacGiaDAO.findByID(sach.getMaTacGia()).getTenTacGia();
            String tenNgonNgu = ngonNguDAO.findByID(sach.getMaNgonNgu()).getTenNgonNgu();
            String tenNXB = nhaXuatBanDAO.findByID(sach.getMaNXB()).getTenNXB();

            List<LinhVuc> linhVucs = linhVucDAO.findByLinhVucID(sach.getMaSach());
            List<LoaiSach> loaiSachs = loaiSachDAO.findBySachID(sach.getMaSach());

            String dsLinhVuc = linhVucs.stream()
                    .map(LinhVuc::getTenLinhVuc)
                    .collect(Collectors.joining(", "));
            String dsLoaiSach = loaiSachs.stream()
                    .map(LoaiSach::getTenLoaiSach)
                    .collect(Collectors.joining(", "));

            model.addRow(new Object[]{
                sach.getMaSach(),
                sach.getTenSach(),
                tenTacGia,
                dsLinhVuc,
                dsLoaiSach,
                tenNXB,
                sach.getNamXuatBan(),
                String.format("%,.0f VND", sach.getGiaBan()),
                sach.getLanTaiBan(),
                sach.getISBN(),
                sach.getTap() != null && sach.getTap() > 0 ? "Tập " + sach.getTap() : "",
                tenNgonNgu,
                sach.getSoLuong(), // Thêm số lượng từ bảng Kho
                sach.getHinhAnh()
            });
        }

        tbSach.setRowHeight(25);
        tbSach.getColumnModel().getColumn(7).setPreferredWidth(100);
    }

    @Override
    public void create() {
        Sach sach = getForm();
        if (sach == null) {
            return;
        }
        try {
            // Lấy danh sách lĩnh vực từ combobox
            List<Integer> linhVucIds = new ArrayList<>();
            DefaultComboBoxModel lvModel = (DefaultComboBoxModel) cboLinhVuc.getModel();
            for (int i = 0; i < lvModel.getSize(); i++) {
                LinhVuc lv = (LinhVuc) lvModel.getElementAt(i);
                linhVucIds.add(lv.getMaLinhVuc());
            }

            // Lấy danh sách thể loại từ combobox
            List<Integer> loaiSachIds = new ArrayList<>();
            DefaultComboBoxModel lsModel = (DefaultComboBoxModel) cboTheLoai.getModel();
            for (int i = 0; i < lsModel.getSize(); i++) {
                LoaiSach ls = (LoaiSach) lsModel.getElementAt(i);
                loaiSachIds.add(ls.getMaLoaiSach());
            }

            // Kiểm tra dữ liệu bắt buộc
            if (linhVucIds.isEmpty()) {
                XDialog.alert("Vui lòng chọn ít nhất một lĩnh vực!");
                return;
            }
            if (loaiSachIds.isEmpty()) {
                XDialog.alert("Vui lòng chọn ít nhất một thể loại!");
                return;
            }
            if (txtSoLuong.getText().trim().isEmpty()) {
                XDialog.alert("Vui lòng nhập số lượng!");
                return;
            }

            // Thêm sách và các quan hệ
            int result = sachDAO.createSachHoanChinh(sach, linhVucIds, loaiSachIds);
            if (result > 0) {
                // Thêm vào bảng Kho
                int soLuong = Integer.parseInt(txtSoLuong.getText());
                XJdbc.executeUpdate("INSERT INTO Kho (MaSach, SoLuong) VALUES (?, ?)", result, soLuong);

                this.fillToTable();
                this.clear();
                XDialog.alert("Thêm sách thành công!");
            } else {
                XDialog.alert("Thêm sách thất bại! Vui lòng kiểm tra dữ liệu hoặc kết nối cơ sở dữ liệu.");
            }
        } catch (Exception ex) {
            XDialog.alert("Lỗi khi thêm sách: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void update() {
        Sach sach = getForm();
        if (sach == null) {
            return;
        }
        try {
            List<Integer> linhVucIds = new ArrayList<>();
            DefaultComboBoxModel lvModel = (DefaultComboBoxModel) cboLinhVuc.getModel();
            for (int i = 0; i < lvModel.getSize(); i++) {
                LinhVuc lv = (LinhVuc) lvModel.getElementAt(i);
                linhVucIds.add(lv.getMaLinhVuc());
            }

            List<Integer> loaiSachIds = new ArrayList<>();
            DefaultComboBoxModel lsModel = (DefaultComboBoxModel) cboTheLoai.getModel();
            for (int i = 0; i < lsModel.getSize(); i++) {
                LoaiSach ls = (LoaiSach) lsModel.getElementAt(i);
                loaiSachIds.add(ls.getMaLoaiSach());
            }

            if (linhVucIds.isEmpty()) {
                XDialog.alert("Vui lòng chọn ít nhất một lĩnh vực!");
                return;
            }
            if (loaiSachIds.isEmpty()) {
                XDialog.alert("Vui lòng chọn ít nhất một thể loại!");
                return;
            }

            int result = sachDAO.updateSachHoanChinh(sach, linhVucIds, loaiSachIds);
            if (result > 0) {
                this.fillToTable();
                XDialog.alert("Cập nhật sách thành công!");
            } else {
                XDialog.alert("Cập nhật sách thất bại! Vui lòng kiểm tra dữ liệu hoặc kết nối cơ sở dữ liệu.");
            }
        } catch (Exception ex) {
            XDialog.alert("Lỗi khi cập nhật sách: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void delete() {
        if (XDialog.confirm("Bạn thực sự muốn xóa?")) {
            int selectedRow = tbSach.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < sachList.size()) {
                Sach entity = sachList.get(selectedRow);
                int id = entity.getMaSach();
                try {
                    // Kiểm tra xem sách có trong hóa đơn không
                    String sql = "SELECT COUNT(*) FROM ChiTietHoaDon WHERE MaSach = ?";
                    int count = (int) XJdbc.getValue(sql, id);
                    if (count > 0) {
                        XDialog.alert("Không thể xóa sách vì đã tồn tại trong hóa đơn!");
                        return;
                    }

                    // Xóa các bản ghi liên quan
                    XJdbc.executeUpdate("DELETE FROM Sach_LinhVuc WHERE MaSach = ?", id);
                    XJdbc.executeUpdate("DELETE FROM Sach_LoaiSach WHERE MaSach = ?", id);
                    XJdbc.executeUpdate("DELETE FROM Kho WHERE MaSach = ?", id);

                    // Xóa sách
                    sachDAO.delete(id);
                    this.fillToTable();
                    this.clear();
                    XDialog.alert("Xóa sách thành công!");
                } catch (RuntimeException ex) {
                    XDialog.alert("Lỗi: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } else {
                XDialog.alert("Vui lòng chọn một sách để xóa!");
            }
        }
    }

    @Override
    public void clear() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có muốn làm mới không ?", "Confirm question", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            lblanh.setText("");
            txtISBN.setText("");
            txtLanTaiBan.setText("");
            txtMaSach.setText("");
            txtNamSX.setText("");
            txttacgia.setText("");
            txtTap.setText("");
            txtTenSach.setText("");
            txtgia.setText("");
            cboLinhVuc.setSelectedIndex(-1);
            txtnxb.setText("");
            txtngonngu.setText("");
            cboTheLoai.setSelectedIndex(-1);

            JOptionPane.showMessageDialog(this, "Làm mới thành công");
        } else {
            return;
        }
    }

    @Override
    public void setEditable(boolean editable) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
