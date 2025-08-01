/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package poly.books.ui;

import java.awt.BorderLayout;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import poly.books.dao.DoanhThuDAO;
import poly.books.entity.DoanhThuNgay;
import poly.books.entity.ThongKeHomNay;
import poly.books.entity.TongDoanhThuNamNay;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.ParseException;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import poly.books.entity.DoanhThuNhanVien;
import poly.books.entity.DoanhThuSanPham;
import poly.books.entity.DoanhThuThang;
import poly.books.entity.DoanhThuTheoNam;

/**
 *
 * @author HuyNguyen
 */
public class ThongKe extends javax.swing.JDialog {

    private DoanhThuDAO doanhThuDAO = new DoanhThuDAO();

    /**
     * Creates new form ThongKe
     */
    public ThongKe(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        jpnngay.setVisible(true); // panel ngày

        jPanel11.setVisible(false);
        jPanel12.setVisible(false);
        jPanel13.setVisible(false);
        jPanel14.setVisible(false);
        jPanel15.setVisible(false);
        jPanel16.setVisible(false);
        jPanel17.setVisible(false);
        jPanel18.setVisible(false);
        jpnngay1.setVisible(false);

        filltableAll();

        jpnngay.setVisible(true);

        updatePanelVisibility("Ngày");
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }

        });

    }

    private void filltableAll() {
        loadDoanhThuTheoNgay();
        loadDoanhThuTheoThang();
        loadDoanhThuTheoNam();
        loadDoanhThuNhanVien();
        loadDoanhThuTheoNgay();
        loadDoanhThuNhanVienTheoThang();
        loadDoanhThuNhanVienTheoNam();
        loadDoanhThuNhanVien();
        loadDoanhThuSanPham();
        loadDoanhThuSanPhamHomNay();
        loadDoanhThuSanPhamTheoNam();
        loadDoanhThuSanPhamTheoThang();
        displayTodayRevenue();
        displayAnnualRevenue();
        veBieuDoDoanhThu();
        veBieuDoDoanhThuThang();
        veBieuDoDoanhThuNam();
        veBieuDoDoanhThuNhanVien();
        veBieuDoDoanhThuNhanVienHomNay();
        veBieuDoDoanhThuNhanVienThangNay();
        veBieuDoDoanhThuNhanVienNamNay();
        veBieuDoDoanhThuSanPham();
        veBieuDoDoanhThuSanPhamHomNay();
        veBieuDoDoanhThuSanPhamNamNay();
        veBieuDoDoanhThuSanPhamThangNay();
    }

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {
        int selectedIndex = jTabbedPane1.getSelectedIndex();
        String timeSelection = (String) cbothoigian.getSelectedItem();

        // Ẩn tất cả các panel trước
        resetAllPanels();

        if (selectedIndex == 1) { // Tab Nhân viên
            jpnngay.setVisible(false);
            jpnngay1.setVisible(true);
            jpnngaysp.setVisible(false); // Ẩn panel ngày của sản phẩm

            // Hiển thị panel phù hợp với tab Nhân viên
            switch (timeSelection) {
                case "Ngày":
                    jPanel19.setVisible(true);
                    jPanel27.setVisible(true);
                    break;
                case "Tháng":
                    jPanel20.setVisible(true);
                    jPanel28.setVisible(true);
                    break;
                case "Năm":
                    jPanel21.setVisible(true);
                    jPanel29.setVisible(true);
                    break;
            }
        } else if (selectedIndex == 2) { // Tab Sản phẩm
            jpnngay.setVisible(false);
            jpnngay1.setVisible(false);
            jpnngaysp.setVisible(true); // Hiển thị panel ngày của sản phẩm

            // Hiển thị panel phù hợp với tab Sản phẩm
            switch (timeSelection) {
                case "Ngày":
                    jPanel23.setVisible(true);
                    jPanel34.setVisible(true);
                    break;
                case "Tháng":
                    jPanel24.setVisible(true);
                    jPanel35.setVisible(true);
                    break;
                case "Năm":
                    jPanel25.setVisible(true);
                    jPanel36.setVisible(true);
                    break;
            }
        } else { // Tab Thời gian (selectedIndex == 0)
            jpnngay.setVisible(true);
            jpnngay1.setVisible(false);
            jpnngaysp.setVisible(false); // Ẩn panel ngày của sản phẩm

            // Hiển thị panel phù hợp với tab Thời gian
            switch (timeSelection) {
                case "Ngày":
                    jPanel11.setVisible(true);
                    jPanel14.setVisible(true);
                    break;
                case "Tháng":
                    jPanel12.setVisible(true);
                    jPanel15.setVisible(true);
                    break;
                case "Năm":
                    jPanel13.setVisible(true);
                    jPanel16.setVisible(true);
                    break;
            }
        }
    }

    public JPanel getContentPanel() {
        return ThongKe; // Trả về JPanel Quanlysach chứa toàn bộ giao diện
    }

    private void displayTodayRevenue() {
        try {
            ThongKeHomNay thongke = doanhThuDAO.getThongKeHomNay();
            if (thongke != null) {
                NumberFormat currencyFormat = NumberFormat.getNumberInstance();
                String formattedRevenue = currencyFormat.format(thongke.getDoanhThuHomNay());
                lbdthn.setText(formattedRevenue);

                // Nếu có số đơn hôm nay, hiển thị luôn
                if (thongke.getSoHoaDonHomNay() >= 0) {
                    lbsodon.setText(String.valueOf(thongke.getSoHoaDonHomNay()));
                }
            } else {
                lbdthn.setText("0");
                lbsodon.setText("0");
            }
        } catch (Exception e) {
            lbdthn.setText("Lỗi");
            lbsodon.setText("Lỗi");
            e.printStackTrace();
        }
    }

    private void displayAnnualRevenue() {
        try {
            TongDoanhThuNamNay result = doanhThuDAO.getTongDoanhThuNamNay();
            double annualRevenue = (result != null) ? result.getTongDoanhThuNamNay() : 0.0;
            NumberFormat currencyFormat = NumberFormat.getNumberInstance();
            String formattedRevenue = currencyFormat.format(annualRevenue);
            lbdtcnam.setText(formattedRevenue);
        } catch (Exception e) {
            lbdtcnam.setText("0");
            e.printStackTrace();
        }
    }

    private void loadDoanhThuTheoNgay() {
        try {
            List<DoanhThuNgay> list = doanhThuDAO.getAllDoanhThuNgay();
            DefaultTableModel model = (DefaultTableModel) tbndoanhthungay.getModel();
            model.setRowCount(0); // Xóa dữ liệu cũ

            NumberFormat currencyFormat = NumberFormat.getNumberInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            for (DoanhThuNgay tk : list) {
                model.addRow(new Object[]{
                    dateFormat.format(tk.getNgay()),
                    currencyFormat.format(tk.getTongDoanhThu()),
                    tk.getSoHoaDon()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu doanh thu");
        }
    }

    private void loadDoanhThuTheoThang() {
        try {
            List<DoanhThuThang> list = doanhThuDAO.getAllDoanhThuThang();
            DefaultTableModel model = (DefaultTableModel) tbndoanhthuthang.getModel();
            model.setRowCount(0); // Xóa dữ liệu cũ

            NumberFormat currencyFormat = NumberFormat.getNumberInstance();

            for (DoanhThuThang tk : list) {
                String thangNam = String.format("%02d/%d", tk.getThang(), tk.getNam());
                model.addRow(new Object[]{
                    thangNam,
                    currencyFormat.format(tk.getDoanhThu()),
                    tk.getSoHoaDon()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu doanh thu");
        }
    }

    private void loadDoanhThuTheoNam() {
        try {
            List<DoanhThuTheoNam> list = doanhThuDAO.getAllDoanhThuTheoNam();
            DefaultTableModel model = (DefaultTableModel) tbndoanhthunam.getModel();
            model.setRowCount(0); // Xóa dữ liệu cũ

            NumberFormat currencyFormat = NumberFormat.getNumberInstance();

            for (DoanhThuTheoNam tk : list) {
                String Nam = String.format("%d", tk.getNam());
                model.addRow(new Object[]{
                    Nam,
                    currencyFormat.format(tk.getDoanhThu()),
                    tk.getSoHoaDon()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu doanh thu");
        }
    }

    private void loadDoanhThuNhanVien() {
        try {
            List<DoanhThuNhanVien> list = doanhThuDAO.getAllDoanhThuNhanVien();
            DefaultTableModel model = (DefaultTableModel) tbndoanhthungay1.getModel();
            model.setRowCount(0); // Xóa dữ liệu cũ

            NumberFormat currencyFormat = NumberFormat.getNumberInstance();

            for (DoanhThuNhanVien tk : list) {
                model.addRow(new Object[]{
                    tk.getTenDangNhap(),
                    tk.getHoTen(),
                    currencyFormat.format(tk.getTongDoanhThu()),
                    tk.getSoHoaDon()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu doanh thu");
        }
    }

    private void loadDoanhThuNhanVienHomNay() {
        try {
            List<DoanhThuNhanVien> list = doanhThuDAO.getDoanhThuNhanVienTheoHomNay();
            DefaultTableModel model = (DefaultTableModel) tbndoanhthungay2.getModel();
            model.setRowCount(0); // Xóa dữ liệu cũ

            NumberFormat currencyFormat = NumberFormat.getNumberInstance();

            for (DoanhThuNhanVien tk : list) {
                model.addRow(new Object[]{
                    tk.getTenDangNhap(),
                    tk.getHoTen(),
                    currencyFormat.format(tk.getTongDoanhThu()),
                    tk.getSoHoaDon()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu doanh thu");
        }
    }

    private void loadDoanhThuNhanVienTheoThang() {
        try {
            List<DoanhThuNhanVien> list = doanhThuDAO.getDoanhThuNhanVienTheoThang();
            DefaultTableModel model = (DefaultTableModel) tbndoanhthungay3.getModel();
            model.setRowCount(0);

            NumberFormat currencyFormat = NumberFormat.getNumberInstance();

            for (DoanhThuNhanVien tk : list) {
                model.addRow(new Object[]{
                    tk.getTenDangNhap(),
                    tk.getHoTen(),
                    currencyFormat.format(tk.getTongDoanhThu()),
                    tk.getSoHoaDon()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu doanh thu nhân viên theo tháng");
        }
    }

    private void loadDoanhThuNhanVienTheoNam() {
        try {
            List<DoanhThuNhanVien> list = doanhThuDAO.getDoanhThuNhanVienTheoNam();
            DefaultTableModel model = (DefaultTableModel) tbndoanhthungay4.getModel();
            model.setRowCount(0);

            NumberFormat currencyFormat = NumberFormat.getNumberInstance();

            for (DoanhThuNhanVien tk : list) {
                model.addRow(new Object[]{
                    tk.getTenDangNhap(),
                    tk.getHoTen(),
                    currencyFormat.format(tk.getTongDoanhThu()),
                    tk.getSoHoaDon()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu doanh thu nhân viên theo tháng");
        }
    }

    public void veBieuDoDoanhThuNhanVien() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<DoanhThuNhanVien> list = new DoanhThuDAO().getAllDoanhThuNhanVien();
        for (DoanhThuNhanVien dt : list) {
            dataset.addValue(dt.getTongDoanhThu(), "Doanh thu", dt.getHoTen());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "THỐNG KÊ DOANH THU CỦA NHÂN VIÊN ",
                "Nhân viên",
                "Doanh thu",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);

        // Tùy chỉnh renderer cho cột
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 144, 193));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(jPanel19.getWidth(), jPanel19.getHeight()));

        jPanel19.removeAll();
        jPanel19.setLayout(new BorderLayout());
        jPanel19.add(chartPanel, BorderLayout.CENTER);
        jPanel19.validate();
    }

    public void veBieuDoDoanhThuNhanVienHomNay() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<DoanhThuNhanVien> list = new DoanhThuDAO().getDoanhThuNhanVienTheoHomNay();
        for (DoanhThuNhanVien dt : list) {
            dataset.addValue(dt.getTongDoanhThu(), "Doanh thu", dt.getHoTen());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "THỐNG KÊ DOANH THU CỦA NHÂN VIÊN HÔM NAY",
                "Nhân viên",
                "Doanh thu",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);

        // Tùy chỉnh renderer cho cột
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 144, 193));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(jPanel20.getWidth(), jPanel20.getHeight()));

        jPanel20.removeAll();
        jPanel20.setLayout(new BorderLayout());
        jPanel20.add(chartPanel, BorderLayout.CENTER);
        jPanel20.validate();
    }

    public void veBieuDoDoanhThuNhanVienThangNay() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<DoanhThuNhanVien> list = new DoanhThuDAO().getDoanhThuNhanVienTheoThang();
        for (DoanhThuNhanVien dt : list) {
            dataset.addValue(dt.getTongDoanhThu(), "Doanh thu", dt.getHoTen());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "THỐNG KÊ DOANH THU CỦA NHÂN VIÊN THÁNG NÀY",
                "Nhân viên",
                "Doanh thu",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);

        // Tùy chỉnh renderer cho cột
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 144, 193));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(jPanel21.getWidth(), jPanel21.getHeight()));

        jPanel21.removeAll();
        jPanel21.setLayout(new BorderLayout());
        jPanel21.add(chartPanel, BorderLayout.CENTER);
        jPanel21.validate();
    }

    public void veBieuDoDoanhThuNhanVienNamNay() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<DoanhThuNhanVien> list = new DoanhThuDAO().getDoanhThuNhanVienTheoNam();
        for (DoanhThuNhanVien dt : list) {
            dataset.addValue(dt.getTongDoanhThu(), "Doanh thu", dt.getHoTen());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "THỐNG KÊ DOANH THU CỦA NHÂN VIÊN THÁNG NÀY",
                "Nhân viên",
                "Doanh thu",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);

        // Tùy chỉnh renderer cho cột
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 144, 193));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(jPanel22.getWidth(), jPanel22.getHeight()));

        jPanel22.removeAll();
        jPanel22.setLayout(new BorderLayout());
        jPanel22.add(chartPanel, BorderLayout.CENTER);
        jPanel22.validate();
    }

    public void veBieuDoDoanhThu() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<DoanhThuNgay> list = new DoanhThuDAO().getAllDoanhThuNgay();
        for (DoanhThuNgay dt : list) {
            dataset.addValue(dt.getTongDoanhThu(), "Doanh thu", dt.getNgay());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "THỐNG KÊ DOANH THU",
                "Thời gian",
                "Doanh thu",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);

        // Tùy chỉnh renderer cho cột
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 144, 193));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(jPanel11.getWidth(), jPanel11.getHeight()));

        jPanel11.removeAll();
        jPanel11.setLayout(new BorderLayout());
        jPanel11.add(chartPanel, BorderLayout.CENTER);
        jPanel11.validate();
    }

    public void veBieuDoDoanhThuThang() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<DoanhThuThang> list = new DoanhThuDAO().getAllDoanhThuThang();

        for (DoanhThuThang dt : list) {
            String label = dt.getThang() + "/" + dt.getNam(); // VD: 7/2025
            dataset.addValue(dt.getDoanhThu(), "Doanh thu", label);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "THỐNG KÊ DOANH THU THEO THÁNG",
                "Tháng/Năm",
                "Doanh thu",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);

        // Tùy chỉnh renderer cho cột
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 144, 193));
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(jPanel12.getWidth(), jPanel12.getHeight()));

        jPanel12.removeAll();
        jPanel12.setLayout(new BorderLayout());
        jPanel12.add(chartPanel, BorderLayout.CENTER);
        jPanel12.validate();
    }

    public void veBieuDoDoanhThuNam() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<DoanhThuTheoNam> list = new DoanhThuDAO().getAllDoanhThuTheoNam();

        for (DoanhThuTheoNam dt : list) {
            String label = String.valueOf(dt.getNam());
            dataset.addValue(dt.getDoanhThu(), "Doanh thu", label);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "THỐNG KÊ DOANH THU THEO NĂM",
                "Năm",
                "Doanh thu",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);

        // Tùy chỉnh renderer cho cột
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 144, 193));
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(jPanel13.getWidth(), jPanel13.getHeight()));

        jPanel13.removeAll();
        jPanel13.setLayout(new BorderLayout());
        jPanel13.add(chartPanel, BorderLayout.CENTER);
        jPanel13.validate();
    }

    private void loadDoanhThuTheoKhoangNgay(Date ngayBatDau, Date ngayKetThuc) {
        try {
            List<DoanhThuNgay> list = doanhThuDAO.getDoanhThuTheoKhoangNgay(ngayBatDau, ngayKetThuc);
            DefaultTableModel model = (DefaultTableModel) tbndoanhthutheokhoangngay.getModel();
            model.setRowCount(0); // Xóa dữ liệu cũ

            NumberFormat currencyFormat = NumberFormat.getNumberInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            for (DoanhThuNgay tk : list) {
                model.addRow(new Object[]{
                    dateFormat.format(tk.getNgay()),
                    currencyFormat.format(tk.getTongDoanhThu()),
                    tk.getSoHoaDon()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu doanh thu theo khoảng ngày");
        }
    }

    private void loadDoanhThuNhanVienTheoKhoangNgay(Date tuNgay, Date denNgay) {
        try {
            List<DoanhThuNhanVien> list = doanhThuDAO.getDoanhThuNhanVienTheoNgay(tuNgay, denNgay);
            DefaultTableModel model = (DefaultTableModel) tbndoanhthungay6.getModel();
            model.setRowCount(0); // Xóa dữ liệu cũ

            NumberFormat currencyFormat = NumberFormat.getNumberInstance();

            for (DoanhThuNhanVien tk : list) {
                model.addRow(new Object[]{
                    tk.getTenDangNhap(),
                    tk.getHoTen(),
                    currencyFormat.format(tk.getTongDoanhThu()),
                    tk.getSoHoaDon()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu doanh thu theo khoảng ngày");
        }
    }

    public void veBieuDoDoanhThuTheoKhoangNgay(Date ngayBatDau, Date ngayKetThuc) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<DoanhThuNgay> list = new DoanhThuDAO().getDoanhThuTheoKhoangNgay(ngayBatDau, ngayKetThuc);

        // Kiểm tra nếu danh sách rỗng
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu trong khoảng thời gian này");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (DoanhThuNgay dt : list) {
            dataset.addValue(dt.getTongDoanhThu(), "Doanh thu", dateFormat.format(dt.getNgay()));
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "THỐNG KÊ DOANH THU THEO KHOẢNG NGÀY",
                "Ngày",
                "Doanh thu",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 144, 193));

        ChartPanel chartPanel = new ChartPanel(chart);
        // Đặt kích thước cố định thay vì dựa vào panel (tránh giá trị 0)
        chartPanel.setPreferredSize(new Dimension(900, 200));

        jPanel17.removeAll();
        jPanel17.setLayout(new BorderLayout());
        jPanel17.add(chartPanel, BorderLayout.CENTER);
        jPanel17.validate();
        jPanel17.repaint(); // Thêm dòng này để đảm bảo panel được vẽ lại
    }

    public void veBieuDoDoanhThuNhanVienTheoKhoangNgay(Date tuNgay, Date denNgay) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<DoanhThuNhanVien> list = new DoanhThuDAO().getDoanhThuNhanVienTheoNgay(tuNgay, denNgay);

        // Kiểm tra nếu danh sách rỗng
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu trong khoảng thời gian này");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (DoanhThuNhanVien dt : list) {
            dataset.addValue(dt.getTongDoanhThu(), "Doanh thu", dt.getHoTen());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "THỐNG KÊ DOANH THU NHÂN VIÊN THEO KHOẢNG NGÀY",
                "Tên",
                "Doanh thu",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 144, 193));

        ChartPanel chartPanel = new ChartPanel(chart);
        // Đặt kích thước cố định thay vì dựa vào panel (tránh giá trị 0)
        chartPanel.setPreferredSize(new Dimension(900, 200));

        jPanel31.removeAll();
        jPanel31.setLayout(new BorderLayout());
        jPanel31.add(chartPanel, BorderLayout.CENTER);
        jPanel31.validate();
        jPanel31.repaint(); // Thêm dòng này để đảm bảo panel được vẽ lại
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ThongKe = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbdthn = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lbsodon = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lbdtcnam = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbndoanhthungay = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbndoanhthuthang = new javax.swing.JTable();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbndoanhthunam = new javax.swing.JTable();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbndoanhthutheokhoangngay = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel9 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbndoanhthungay1 = new javax.swing.JTable();
        jPanel28 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tbndoanhthungay2 = new javax.swing.JTable();
        jPanel29 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tbndoanhthungay3 = new javax.swing.JTable();
        jPanel30 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tbndoanhthungay4 = new javax.swing.JTable();
        jPanel32 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tbndoanhthungay6 = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jpntqtg = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jpncttg = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tbndoanhthungay5 = new javax.swing.JTable();
        jPanel35 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        tbndoanhthungay7 = new javax.swing.JTable();
        jPanel36 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tbndoanhthungay8 = new javax.swing.JTable();
        jPanel37 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tbndoanhthungay9 = new javax.swing.JTable();
        jPanel38 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        tbndoanhthungay10 = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        cbothoigian = new javax.swing.JComboBox<>();
        jpnngay = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtngaydau = new javax.swing.JFormattedTextField();
        txtngaycuoi = new javax.swing.JFormattedTextField();
        jButton1 = new javax.swing.JButton();
        btnlammoi = new javax.swing.JButton();
        jpnngay1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtngaydaunv = new javax.swing.JFormattedTextField();
        txtngaycuoinv = new javax.swing.JFormattedTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jpnngaysp = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtngaydaunv1 = new javax.swing.JFormattedTextField();
        txtngaycuoinv1 = new javax.swing.JFormattedTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        ThongKe.setBackground(new java.awt.Color(255, 255, 255));
        ThongKe.setMinimumSize(new java.awt.Dimension(1123, 773));
        ThongKe.setPreferredSize(new java.awt.Dimension(1123, 773));
        ThongKe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ThongKeMouseClicked(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(51, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 144, 193)));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Doanh thu hôm nay");

        lbdthn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbdthn.setForeground(new java.awt.Color(255, 255, 255));
        lbdthn.setText("jLabel1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addGap(26, 26, 26)
                .addComponent(lbdthn)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lbdthn))
                .addContainerGap(63, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 102, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 144, 193)));
        jPanel2.setPreferredSize(new java.awt.Dimension(276, 142));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("số đơn hôm nay");

        lbsodon.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbsodon.setForeground(new java.awt.Color(255, 255, 255));
        lbsodon.setText("jLabel1");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbsodon)
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lbsodon))
                .addContainerGap(64, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 153, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 144, 193)));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Doanh thu cả năm");

        lbdtcnam.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbdtcnam.setForeground(new java.awt.Color(255, 255, 255));
        lbdtcnam.setText("jLabel1");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbdtcnam)
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lbdtcnam))
                .addContainerGap(65, Short.MAX_VALUE))
        );

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setOpaque(true);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
        });

        jTabbedPane2.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane2.setOpaque(true);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 962, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 962, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 962, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 962, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(311, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Tổng quan", jPanel6);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        tbndoanhthungay.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Ngày", "Tổng doanh thu", "Số đơn"
            }
        ));
        jScrollPane1.setViewportView(tbndoanhthungay);
        if (tbndoanhthungay.getColumnModel().getColumnCount() > 0) {
            tbndoanhthungay.getColumnModel().getColumn(2).setHeaderValue("Số đơn");
        }

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 905, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        tbndoanhthuthang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Tháng", "Tổng doanh thu", "Số đơn"
            }
        ));
        jScrollPane2.setViewportView(tbndoanhthuthang);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 905, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        tbndoanhthunam.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Năm", "Tổng doanh thu", "Số đơn"
            }
        ));
        jScrollPane3.setViewportView(tbndoanhthunam);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 905, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        tbndoanhthutheokhoangngay.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Ngày", "Tổng doanh thu", "Số đơn"
            }
        ));
        jScrollPane4.setViewportView(tbndoanhthutheokhoangngay);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 905, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Chi tiết", jPanel7);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Thời gian", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane3.setBackground(new java.awt.Color(255, 255, 255));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 962, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 962, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 962, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 962, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 962, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(510, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Tổng quan", jPanel9);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));

        tbndoanhthungay1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Tên đăng nhập", "Tên", "Doanh thu", "Số đơn"
            }
        ));
        jScrollPane5.setViewportView(tbndoanhthungay1);

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 971, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(164, Short.MAX_VALUE))
        );

        tbndoanhthungay2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Tên đăng nhập", "Tên", "Doanh thu", "Số đơn"
            }
        ));
        jScrollPane6.setViewportView(tbndoanhthungay2);

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 971, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        tbndoanhthungay3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Tên đăng nhập", "Tên", "Doanh thu", "Số đơn"
            }
        ));
        jScrollPane7.setViewportView(tbndoanhthungay3);

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 971, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        tbndoanhthungay4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Tên đăng nhập", "Tên", "Doanh thu", "Số đơn"
            }
        ));
        jScrollPane8.setViewportView(tbndoanhthungay4);

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 971, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        tbndoanhthungay6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Tên đăng nhập", "Tên", "Doanh thu", "Số đơn"
            }
        ));
        jScrollPane10.setViewportView(tbndoanhthungay6);

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 971, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Chi tiết", jPanel10);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Nhân viên", jPanel5);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane4.setBackground(new java.awt.Color(255, 255, 255));

        jpntqtg.setBackground(new java.awt.Color(255, 255, 255));

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 962, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 962, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 962, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 962, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 962, Short.MAX_VALUE)
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jpntqtgLayout = new javax.swing.GroupLayout(jpntqtg);
        jpntqtg.setLayout(jpntqtgLayout);
        jpntqtgLayout.setHorizontalGroup(
            jpntqtgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpntqtgLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpntqtgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpntqtgLayout.createSequentialGroup()
                        .addGap(0, 9, Short.MAX_VALUE)
                        .addGroup(jpntqtgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jpntqtgLayout.createSequentialGroup()
                        .addGroup(jpntqtgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jpntqtgLayout.setVerticalGroup(
            jpntqtgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpntqtgLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(564, Short.MAX_VALUE))
        );

        jTabbedPane4.addTab("Tổng quan", jpntqtg);

        jpncttg.setBackground(new java.awt.Color(255, 255, 255));

        jPanel34.setBackground(new java.awt.Color(255, 255, 255));

        tbndoanhthungay5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã sản phẩm ", "Tên sản phẩm", "Số lượng", "Doanh thu"
            }
        ));
        jScrollPane9.setViewportView(tbndoanhthungay5);

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 959, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        tbndoanhthungay7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã sản phẩm ", "Tên sản phẩm", "Số lượng", "Doanh thu"
            }
        ));
        jScrollPane11.setViewportView(tbndoanhthungay7);

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 959, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        tbndoanhthungay8.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã sản phẩm ", "Tên sản phẩm", "Số lượng", "Doanh thu"
            }
        ));
        jScrollPane12.setViewportView(tbndoanhthungay8);

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 959, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        tbndoanhthungay9.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã sản phẩm ", "Tên sản phẩm", "Số lượng", "Doanh thu"
            }
        ));
        jScrollPane13.setViewportView(tbndoanhthungay9);

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 959, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        tbndoanhthungay10.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã sản phẩm ", "Tên sản phẩm", "Số lượng", "Doanh thu"
            }
        ));
        jScrollPane14.setViewportView(tbndoanhthungay10);

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 959, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jpncttgLayout = new javax.swing.GroupLayout(jpncttg);
        jpncttg.setLayout(jpncttgLayout);
        jpncttgLayout.setHorizontalGroup(
            jpncttgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpncttgLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpncttgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpncttgLayout.setVerticalGroup(
            jpncttgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpncttgLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane4.addTab("Chi tiết", jpncttg);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane4)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane4)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Sản phẩm", jPanel8);

        jLabel7.setText("mốc thơi gian");

        cbothoigian.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ngày", "Tháng", "Năm" }));
        cbothoigian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbothoigianActionPerformed(evt);
            }
        });

        jpnngay.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setText("Từ :");

        jLabel9.setText("Đến");

        jButton1.setText("Tim kiếm");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnngayLayout = new javax.swing.GroupLayout(jpnngay);
        jpnngay.setLayout(jpnngayLayout);
        jpnngayLayout.setHorizontalGroup(
            jpnngayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnngayLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtngaydau, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtngaycuoi, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(9, Short.MAX_VALUE))
        );
        jpnngayLayout.setVerticalGroup(
            jpnngayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnngayLayout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addGroup(jpnngayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(txtngaydau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtngaycuoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)))
        );

        btnlammoi.setText("Làm mới");
        btnlammoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlammoiActionPerformed(evt);
            }
        });

        jpnngay1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setText("Từ :");

        jLabel11.setText("Đến");

        jButton2.setText("Tim kiếm");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Tổng doanh thu");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnngay1Layout = new javax.swing.GroupLayout(jpnngay1);
        jpnngay1.setLayout(jpnngay1Layout);
        jpnngay1Layout.setHorizontalGroup(
            jpnngay1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnngay1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtngaydaunv, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtngaycuoinv, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpnngay1Layout.setVerticalGroup(
            jpnngay1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnngay1Layout.createSequentialGroup()
                .addGap(0, 13, Short.MAX_VALUE)
                .addGroup(jpnngay1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(txtngaydaunv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtngaycuoinv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)))
        );

        jpnngaysp.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setText("Từ :");

        jLabel13.setText("Đến");

        jButton4.setText("Tim kiếm");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Tổng doanh thu");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnngayspLayout = new javax.swing.GroupLayout(jpnngaysp);
        jpnngaysp.setLayout(jpnngayspLayout);
        jpnngayspLayout.setHorizontalGroup(
            jpnngayspLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnngayspLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtngaydaunv1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtngaycuoinv1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpnngayspLayout.setVerticalGroup(
            jpnngayspLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnngayspLayout.createSequentialGroup()
                .addGap(0, 13, Short.MAX_VALUE)
                .addGroup(jpnngayspLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(txtngaydaunv1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtngaycuoinv1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4)
                    .addComponent(jButton5)))
        );

        javax.swing.GroupLayout ThongKeLayout = new javax.swing.GroupLayout(ThongKe);
        ThongKe.setLayout(ThongKeLayout);
        ThongKeLayout.setHorizontalGroup(
            ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThongKeLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ThongKeLayout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 995, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(105, Short.MAX_VALUE))
                    .addGroup(ThongKeLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(114, 114, 114)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32))
                    .addGroup(ThongKeLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbothoigian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addGroup(ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jpnngay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpnngay1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpnngaysp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(59, 59, 59)
                        .addComponent(btnlammoi)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        ThongKeLayout.setVerticalGroup(
            ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThongKeLayout.createSequentialGroup()
                .addGroup(ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ThongKeLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ThongKeLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ThongKeLayout.createSequentialGroup()
                                .addComponent(jpnngay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jpnngay1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jpnngaysp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbothoigian, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ThongKeLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnlammoi)
                        .addGap(60, 60, 60)))
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(96, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(ThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(ThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date ngayBatDau = dateFormat.parse(txtngaydau.getText());
            Date ngayKetThuc = dateFormat.parse(txtngaycuoi.getText());

            if (ngayBatDau.after(ngayKetThuc)) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc");
                return;
            }

            // Chọn tab "Chi tiết" trước khi hiển thị kết quả
            jTabbedPane2.setSelectedIndex(0);

            // Reset và hiển thị panel cần thiết
            resetAllPanels();
            jPanel17.setVisible(true);
            jPanel18.setVisible(true);

            loadDoanhThuTheoKhoangNgay(ngayBatDau, ngayKetThuc);
            veBieuDoDoanhThuTheoKhoangNgay(ngayBatDau, ngayKetThuc);

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ngày đúng định dạng dd/MM/yyyy");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jPanel4MouseClicked

    private void cbothoigianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbothoigianActionPerformed
        String selectedOption = (String) cbothoigian.getSelectedItem();
        updatePanelVisibility(selectedOption);
    }//GEN-LAST:event_cbothoigianActionPerformed

    private void btnlammoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlammoiActionPerformed
        // TODO add your handling code here:
        filltableAll();
        txtngaydau.setText("");
        txtngaycuoi.setText("");
        txtngaycuoinv.setText("");
        txtngaydaunv.setText("");
        txtngaycuoinv1.setText("");
        txtngaydaunv1.setText("");
        jPanel27.setVisible(true);
        jPanel19.setVisible(true);
        jPanel11.setVisible(true);
        jPanel14.setVisible(true);
        jPanel23.setVisible(true);
        jPanel34.setVisible(true);
        jPanel24.setVisible(false);
        jPanel25.setVisible(false);
        jPanel26.setVisible(false);
        jPanel25.setVisible(false);
        jPanel26.setVisible(false);
        jPanel33.setVisible(false);
        jPanel35.setVisible(false);
        jPanel36.setVisible(false);
        jPanel37.setVisible(false);
        jPanel12.setVisible(false);
        jPanel13.setVisible(false);
        jPanel15.setVisible(false);
        jPanel16.setVisible(false);
        jPanel17.setVisible(false);
        jPanel18.setVisible(false);
        jPanel20.setVisible(false);
        jPanel21.setVisible(false);
        jPanel22.setVisible(false);

        jPanel24.setVisible(false);
        jPanel25.setVisible(false);
        jPanel26.setVisible(false);
        jPanel28.setVisible(false);
        jPanel29.setVisible(false);
        jPanel30.setVisible(false);

    }//GEN-LAST:event_btnlammoiActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date ngayBatDau = dateFormat.parse(txtngaydaunv.getText());
            Date ngayKetThuc = dateFormat.parse(txtngaycuoinv.getText());

            if (ngayBatDau.after(ngayKetThuc)) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc");
                return;
            }

            // Chọn tab "Chi tiết" (index 1) thay vì "Tổng quan" (index 0)
            jTabbedPane3.setSelectedIndex(0);

            // Reset tất cả panel
            resetAllPanels();

            // Hiển thị các panel cần thiết
            jpnngay1.setVisible(true); // Hiển thị panel nhập ngày
            jPanel31.setVisible(true); // Hiển thị panel biểu đồ
            jPanel32.setVisible(true); // Hiển thị panel bảng dữ liệu

            // Gọi phương thức load dữ liệu nhân viên theo khoảng ngày
            loadDoanhThuNhanVienTheoKhoangNgay(ngayBatDau, ngayKetThuc);
            veBieuDoDoanhThuNhanVienTheoKhoangNgay(ngayBatDau, ngayKetThuc);

            // Làm mới giao diện
            jPanel31.revalidate();
            jPanel31.repaint();
            jPanel32.revalidate();
            jPanel32.repaint();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ngày đúng định dạng dd/MM/yyyy");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        jPanel27.setVisible(true);
        jPanel19.setVisible(true);
        jPanel20.setVisible(false);
        jPanel21.setVisible(false);
        jPanel22.setVisible(false);
        jPanel28.setVisible(false);
        jPanel29.setVisible(false);
        jPanel30.setVisible(false);

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date ngayBatDau = dateFormat.parse(txtngaydaunv1.getText());
            Date ngayKetThuc = dateFormat.parse(txtngaycuoinv1.getText());

            if (ngayBatDau.after(ngayKetThuc)) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc");
                return;
            }

            // Chọn tab "Chi tiết" trước khi hiển thị kết quả
            jTabbedPane4.setSelectedIndex(0); // Sửa từ jTabbedPane2 thành jTabbedPane3

            // Reset và hiển thị panel cần thiết
            resetAllPanels();
            jPanel38.setVisible(true);
            jPanel33.setVisible(true);// Hiển thị bảng kết quả

            // Gọi phương thức load dữ liệu nhân viên theo khoảng ngày
            loadDoanhThuSanPhamTheoKhoangNgay(ngayBatDau, ngayKetThuc);
            veBieuDoDoanhThuSanPhamTheoKhoangNgay(ngayBatDau, ngayKetThuc);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ngày đúng định dạng dd/MM/yyyy");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        jPanel23.setVisible(true);
        jPanel34.setVisible(true);
        jPanel24.setVisible(false);
        jPanel25.setVisible(false);
        jPanel26.setVisible(false);
        jPanel25.setVisible(false);
        jPanel26.setVisible(false);
        jPanel33.setVisible(false);
        jPanel35.setVisible(false);
        jPanel36.setVisible(false);
        jPanel37.setVisible(false);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
       
       
    }//GEN-LAST:event_jPanel1MouseClicked

    private void ThongKeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ThongKeMouseClicked
       filltableAll();
    }//GEN-LAST:event_ThongKeMouseClicked
    private void updatePanelVisibility(String selectedOption) {
        resetAllPanels();

        int selectedIndex = jTabbedPane1.getSelectedIndex();

        // Debug: In ra index để kiểm tra
        System.out.println("Selected tab index: " + selectedIndex);
        System.out.println("Selected option: " + selectedOption);

        if (selectedIndex == 1) { // Tab Nhân viên
            jpnngay.setVisible(false);
            jpnngay1.setVisible(true);
            jpnngaysp.setVisible(false);
            jPanel27.setVisible(true);
            jPanel19.setVisible(true);

            switch (selectedOption) {
                case "Ngày":
                    jPanel20.setVisible(true);
                    jPanel28.setVisible(true);
                    jPanel27.setVisible(false);
                    jPanel19.setVisible(false);
                    loadDoanhThuNhanVienHomNay();
                    break;
                case "Tháng":
                    jPanel21.setVisible(true);
                    jPanel29.setVisible(true);
                    jPanel27.setVisible(false);
                    jPanel19.setVisible(false);
                    loadDoanhThuNhanVienTheoThang();
                    break;
                case "Năm":
                    jPanel22.setVisible(true);
                    jPanel30.setVisible(true);
                    jPanel27.setVisible(false);
                    jPanel19.setVisible(false);
                    loadDoanhThuNhanVienTheoNam();
                    break;
            }

        } else if (selectedIndex == 2) { // Tab Sản phẩm (sửa từ 3 thành 2)
            jpnngay.setVisible(false);
            jpnngay1.setVisible(false);
            jpnngaysp.setVisible(true); // Hiển thị panel ngày cho sản phẩm
            jPanel34.setVisible(true);
            jPanel23.setVisible(true);
            switch (selectedOption) {
                case "Ngày":
                    jPanel24.setVisible(true);
                    jPanel35.setVisible(true);
                    jPanel34.setVisible(false); // Ẩn panel tổng quan nếu cần
                    jPanel23.setVisible(false);
                    loadDoanhThuSanPhamHomNay();
                    break;
                case "Tháng":
                    jPanel25.setVisible(true);
                    jPanel36.setVisible(true);
                    jPanel34.setVisible(false);
                    jPanel23.setVisible(false);
                    loadDoanhThuSanPhamTheoThang();
                    break;
                case "Năm":
                    jPanel26.setVisible(true);
                    jPanel37.setVisible(true);
                    jPanel34.setVisible(false);
                    jPanel23.setVisible(false);
                    loadDoanhThuSanPhamTheoNam();
                    break;
            }

        } else { // Tab Thời gian (index 0)
            jpnngay.setVisible(true);
            jpnngay1.setVisible(false);
            jpnngaysp.setVisible(false);

            switch (selectedOption) {
                case "Ngày":
                    jPanel11.setVisible(true);
                    jPanel14.setVisible(true);
                    break;
                case "Tháng":
                    jPanel12.setVisible(true);
                    jPanel15.setVisible(true);
                    break;
                case "Năm":
                    jPanel13.setVisible(true);
                    jPanel16.setVisible(true);
                    break;
            }
        }
    }

    private void resetAllPanels() {
        // Ẩn tất cả các panel ngày trước
        jpnngay.setVisible(false);
        jpnngay1.setVisible(false);

        jPanel11.setVisible(false);
        jPanel12.setVisible(false);
        jPanel13.setVisible(false);
        jPanel14.setVisible(false);
        jPanel15.setVisible(false);
        jPanel16.setVisible(false);
        jPanel17.setVisible(false);
        jPanel18.setVisible(false);
        jPanel19.setVisible(false);
        jPanel20.setVisible(false);
        jPanel21.setVisible(false);
        jPanel22.setVisible(false);
        jPanel23.setVisible(false);
        jPanel24.setVisible(false);
        jPanel25.setVisible(false);
        jPanel26.setVisible(false);
        jPanel27.setVisible(false);
        jPanel28.setVisible(false);
        jPanel29.setVisible(false);
        jPanel30.setVisible(false);
        jPanel31.setVisible(false);
        jPanel32.setVisible(false);
        jPanel33.setVisible(false);
        jPanel34.setVisible(false);
        jPanel35.setVisible(false);
        jPanel36.setVisible(false);
        jPanel37.setVisible(false);

    }

    private void loadDoanhThuSanPham() {
        try {
            List<DoanhThuSanPham> list = doanhThuDAO.getAllDoanhThuSanPham();
            DefaultTableModel model = (DefaultTableModel) tbndoanhthungay5.getModel();
            model.setRowCount(0); // Xóa dữ liệu cũ

            NumberFormat currencyFormat = NumberFormat.getNumberInstance();

            for (DoanhThuSanPham tk : list) {
                model.addRow(new Object[]{
                    tk.getMaSach(),
                    tk.getTenSach(),
                    currencyFormat.format(tk.getDoanhThu()),
                    tk.getSoLuongBan()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu doanh thu");
        }
    }

    private void loadDoanhThuSanPhamHomNay() {
        try {
            List<DoanhThuSanPham> list = doanhThuDAO.getDoanhThuSanPhamHomNay();
            DefaultTableModel model = (DefaultTableModel) tbndoanhthungay7.getModel();
            model.setRowCount(0); // Xóa dữ liệu cũ

            NumberFormat currencyFormat = NumberFormat.getNumberInstance();

            for (DoanhThuSanPham tk : list) {
                model.addRow(new Object[]{
                    tk.getMaSach(),
                    tk.getTenSach(),
                    currencyFormat.format(tk.getDoanhThu()),
                    tk.getSoLuongBan()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu doanh thu");
        }
    }

    private void loadDoanhThuSanPhamTheoThang() {
        try {
            List<DoanhThuSanPham> list = doanhThuDAO.getDoanhThuSanPhamThangNay();
            DefaultTableModel model = (DefaultTableModel) tbndoanhthungay8.getModel();
            model.setRowCount(0);

            NumberFormat currencyFormat = NumberFormat.getNumberInstance();

            for (DoanhThuSanPham tk : list) {
                model.addRow(new Object[]{
                    tk.getMaSach(),
                    tk.getTenSach(),
                    currencyFormat.format(tk.getDoanhThu()),
                    tk.getSoLuongBan()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu doanh thu nhân viên theo tháng");
        }
    }

    private void loadDoanhThuSanPhamTheoNam() {
        try {
            List<DoanhThuSanPham> list = doanhThuDAO.getDoanhThuSanPhamNamNay();
            DefaultTableModel model = (DefaultTableModel) tbndoanhthungay9.getModel();
            model.setRowCount(0);

            NumberFormat currencyFormat = NumberFormat.getNumberInstance();

            for (DoanhThuSanPham tk : list) {
                model.addRow(new Object[]{
                    tk.getMaSach(),
                    tk.getTenSach(),
                    currencyFormat.format(tk.getDoanhThu()),
                    tk.getSoLuongBan()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu doanh thu nhân viên theo tháng");
        }
    }

    public void veBieuDoDoanhThuSanPham() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<DoanhThuSanPham> list = new DoanhThuDAO().getAllDoanhThuSanPham();
        for (DoanhThuSanPham dt : list) {
            dataset.addValue(dt.getDoanhThu(), "Doanh thu", dt.getTenSach());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "THỐNG KÊ DOANH THU CỦA SẢN PHẨM ",
                "Sản Phẩm",
                "Doanh thu",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);

        // Tùy chỉnh renderer cho cột
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 144, 193));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(jPanel23.getWidth(), jPanel23.getHeight()));

        jPanel23.removeAll();
        jPanel23.setLayout(new BorderLayout());
        jPanel23.add(chartPanel, BorderLayout.CENTER);
        jPanel23.validate();
    }

    public void veBieuDoDoanhThuSanPhamHomNay() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<DoanhThuSanPham> list = new DoanhThuDAO().getDoanhThuSanPhamHomNay();
        for (DoanhThuSanPham dt : list) {
            dataset.addValue(dt.getDoanhThu(), "Doanh thu", dt.getTenSach());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "THỐNG KÊ DOANH THU CỦA SẢN PHẨM ",
                "Sản Phẩm",
                "Doanh thu",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);

        // Tùy chỉnh renderer cho cột
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 144, 193));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(jPanel24.getWidth(), jPanel24.getHeight()));

        jPanel24.removeAll();
        jPanel24.setLayout(new BorderLayout());
        jPanel24.add(chartPanel, BorderLayout.CENTER);
        jPanel24.validate();
    }

    public void veBieuDoDoanhThuSanPhamThangNay() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<DoanhThuSanPham> list = new DoanhThuDAO().getDoanhThuSanPhamThangNay();
        for (DoanhThuSanPham dt : list) {
            dataset.addValue(dt.getDoanhThu(), "Doanh thu", dt.getTenSach());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "THỐNG KÊ DOANH THU CỦA SẢN PHẨM ",
                "Sản Phẩm",
                "Doanh thu",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);

        // Tùy chỉnh renderer cho cột
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 144, 193));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(jPanel25.getWidth(), jPanel25.getHeight()));

        jPanel25.removeAll();
        jPanel25.setLayout(new BorderLayout());
        jPanel25.add(chartPanel, BorderLayout.CENTER);
        jPanel25.validate();
    }

    public void veBieuDoDoanhThuSanPhamNamNay() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<DoanhThuSanPham> list = new DoanhThuDAO().getDoanhThuSanPhamNamNay();
        for (DoanhThuSanPham dt : list) {
            dataset.addValue(dt.getDoanhThu(), "Doanh thu", dt.getTenSach());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "THỐNG KÊ DOANH THU CỦA SẢN PHẨM ",
                "Sản Phẩm",
                "Doanh thu",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);

        // Tùy chỉnh renderer cho cột
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 144, 193));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(jPanel26.getWidth(), jPanel26.getHeight()));

        jPanel26.removeAll();
        jPanel26.setLayout(new BorderLayout());
        jPanel26.add(chartPanel, BorderLayout.CENTER);
        jPanel26.validate();
    }

    private void loadDoanhThuSanPhamTheoKhoangNgay(Date tuNgay, Date denNgay) {
        try {
            List<DoanhThuSanPham> list = doanhThuDAO.getDoanhThuSanPhamTheoKhoangNgay(tuNgay, denNgay);
            DefaultTableModel model = (DefaultTableModel) tbndoanhthungay10.getModel();
            model.setRowCount(0); // Xóa dữ liệu cũ

            NumberFormat currencyFormat = NumberFormat.getNumberInstance();

            for (DoanhThuSanPham tk : list) {
                model.addRow(new Object[]{
                    tk.getMaSach(),
                    tk.getTenSach(),
                    currencyFormat.format(tk.getDoanhThu()),
                    tk.getSoLuongBan()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu doanh thu theo khoảng ngày");
        }
    }

    public void veBieuDoDoanhThuSanPhamTheoKhoangNgay(Date tuNgay, Date denNgay) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<DoanhThuSanPham> list = new DoanhThuDAO().getDoanhThuSanPhamTheoKhoangNgay(tuNgay, denNgay);

        // Kiểm tra nếu danh sách rỗng
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu trong khoảng thời gian này");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (DoanhThuSanPham dt : list) {
            dataset.addValue(dt.getDoanhThu(), "Doanh thu", dt.getTenSach());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "THỐNG KÊ DOANH THU NHÂN VIÊN THEO KHOẢNG NGÀY",
                "Tên",
                "Doanh thu",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 144, 193));

        ChartPanel chartPanel = new ChartPanel(chart);
        // Đặt kích thước cố định thay vì dựa vào panel (tránh giá trị 0)
        chartPanel.setPreferredSize(new Dimension(900, 200));

        jPanel33.removeAll();
        jPanel33.setLayout(new BorderLayout());
        jPanel33.add(chartPanel, BorderLayout.CENTER);
        jPanel33.validate();
        jPanel33.repaint(); // Thêm dòng này để đảm bảo panel được vẽ lại
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ThongKe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ThongKe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ThongKe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ThongKe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ThongKe dialog = new ThongKe(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ThongKe;
    private javax.swing.JButton btnlammoi;
    private javax.swing.JComboBox<String> cbothoigian;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JPanel jpncttg;
    private javax.swing.JPanel jpnngay;
    private javax.swing.JPanel jpnngay1;
    private javax.swing.JPanel jpnngaysp;
    private javax.swing.JPanel jpntqtg;
    private javax.swing.JLabel lbdtcnam;
    private javax.swing.JLabel lbdthn;
    private javax.swing.JLabel lbsodon;
    private javax.swing.JTable tbndoanhthunam;
    private javax.swing.JTable tbndoanhthungay;
    private javax.swing.JTable tbndoanhthungay1;
    private javax.swing.JTable tbndoanhthungay10;
    private javax.swing.JTable tbndoanhthungay2;
    private javax.swing.JTable tbndoanhthungay3;
    private javax.swing.JTable tbndoanhthungay4;
    private javax.swing.JTable tbndoanhthungay5;
    private javax.swing.JTable tbndoanhthungay6;
    private javax.swing.JTable tbndoanhthungay7;
    private javax.swing.JTable tbndoanhthungay8;
    private javax.swing.JTable tbndoanhthungay9;
    private javax.swing.JTable tbndoanhthuthang;
    private javax.swing.JTable tbndoanhthutheokhoangngay;
    private javax.swing.JFormattedTextField txtngaycuoi;
    private javax.swing.JFormattedTextField txtngaycuoinv;
    private javax.swing.JFormattedTextField txtngaycuoinv1;
    private javax.swing.JFormattedTextField txtngaydau;
    private javax.swing.JFormattedTextField txtngaydaunv;
    private javax.swing.JFormattedTextField txtngaydaunv1;
    // End of variables declaration//GEN-END:variables
}
