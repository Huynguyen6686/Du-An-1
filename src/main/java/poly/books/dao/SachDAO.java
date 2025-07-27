/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.books.dao;

import java.util.ArrayList;
import java.util.List;
import poly.books.entity.Sach;
import poly.books.util.XJdbc;
import poly.books.util.XQuery;

/**
 *
 * @author LAPTOP
 */
public class SachDAO {

    // SQL lấy tất cả sách (đã bỏ MaLinhVuc và MaLoaiSach)
    String getAllSQL = """
                       SELECT  [MaSach]
                             ,[TenSach]
                             ,[MaTacGia]
                             ,[MaNXB]
                             ,[NamXuatBan]
                             ,[GiaBan]
                             ,[LanTaiBan]
                             ,[ISBN]
                             ,[Tap]
                             ,[MaNgonNgu]
                             ,[HinhAnh]
                         FROM [QLNhaSachPro].[dbo].[Sach]
                       """;

    // SQL tạo sách mới (đã bỏ MaLinhVuc và MaLoaiSach)
    String createSQL = """
                       INSERT INTO [dbo].[Sach]
                                  ([TenSach]
                                  ,[MaTacGia]
                                  ,[MaNXB]
                                  ,[NamXuatBan]
                                  ,[GiaBan]
                                  ,[LanTaiBan]
                                  ,[ISBN]
                                  ,[Tap]
                                  ,[MaNgonNgu]
                                  ,[HinhAnh])
                            VALUES
                                  (?,?,?,?,?,?,?,?,?,?)
                       """;

    // SQL cập nhật sách (đã bỏ MaLinhVuc và MaLoaiSach)
    String updateSQL = """
                       UPDATE [dbo].[Sach]
                          SET [TenSach] = ?
                             ,[MaTacGia] = ?
                             ,[MaNXB] = ?
                             ,[NamXuatBan] = ?
                             ,[GiaBan] = ?
                             ,[LanTaiBan] = ?
                             ,[ISBN] = ?
                             ,[Tap] = ?
                             ,[MaNgonNgu] = ?
                             ,[HinhAnh] = ?
                        WHERE MaSach = ?
                       """;

    String deleteSQL = """
                       DELETE FROM [dbo].[Sach]
                             WHERE MaSach = ?
                       """;

    // SQL cho các bảng trung gian
    String deleteLinhVucSQL = """
                              DELETE FROM [dbo].[Sach_LinhVuc]
                                    WHERE MaSach = ?
                              """;

    String deleteLoaiSachSQL = """
                               DELETE FROM [dbo].[Sach_LoaiSach]
                                     WHERE MaSach = ?
                               """;

    String insertLinhVucSQL = """
                              INSERT INTO [dbo].[Sach_LinhVuc]
                                         ([MaSach], [MaLinhVuc])
                                   VALUES (?, ?)
                              """;

    String insertLoaiSachSQL = """
                               INSERT INTO [dbo].[Sach_LoaiSach]
                                          ([MaSach], [MaLoaiSach])
                                    VALUES (?, ?)
                               """;
    String gallDanhSachSP = """
                          SELECT s.MaSach, s.TenSach, s.MaTacGia, s.MaNXB, 
                                 s.NamXuatBan, s.GiaBan, s.LanTaiBan, s.ISBN, s.Tap, s.MaNgonNgu, k.SoLuong, s.HinhAnh 
                          FROM Sach s 
                          JOIN Kho k ON s.MaSach = k.MaSach
                          """;

    public List<Sach> getAllDanhSachSP() {
        return XQuery.getBeanList(Sach.class, gallDanhSachSP);
    }
public Sach findByISBN(String isbn) {
    String sql = getAllSQL + " WHERE ISBN = ?";
    return XQuery.getSingleBean(Sach.class, sql, isbn);
}
    public List<Sach> getAll() {
        return XQuery.getBeanList(Sach.class, getAllSQL);
    }
public Sach findByID(int maSach) {
        String sql = gallDanhSachSP + " WHERE s.MaSach = ?";
        return XQuery.getSingleBean(Sach.class, sql, maSach);
    }
    /**
     * Tạo sách mới (không bao gồm lĩnh vực và loại sách)
     */
    public int create(Sach sach) {
        Object[] values = {
            sach.getTenSach(),
            sach.getMaTacGia(),
            sach.getMaNXB(),
            sach.getNamXuatBan(),
            sach.getGiaBan(),
            sach.getLanTaiBan(),
            sach.getISBN(),
            sach.getTap(),
            sach.getMaNgonNgu(),
            sach.getHinhAnh()
        };
        return XJdbc.executeUpdate(createSQL, values);
    }

    /**
     * Cập nhật thông tin sách (không bao gồm lĩnh vực và loại sách)
     */
    public int update(Sach sach) {
        Object[] values = {
            sach.getTenSach(),
            sach.getMaTacGia(),
            sach.getMaNXB(),
            sach.getNamXuatBan(),
            sach.getGiaBan(),
            sach.getLanTaiBan(),
            sach.getISBN(),
            sach.getTap(),
            sach.getMaNgonNgu(),
            sach.getHinhAnh(),
            sach.getMaSach()
        };
        return XJdbc.executeUpdate(updateSQL, values);
    }

    /**
     * Xóa sách và tất cả liên kết với lĩnh vực và loại sách
     */
    public int delete(int maSach) {
        // Xóa liên kết với lĩnh vực
        XJdbc.executeUpdate(deleteLinhVucSQL, maSach);
        // Xóa liên kết với loại sách
        XJdbc.executeUpdate(deleteLoaiSachSQL, maSach);
        // Xóa sách
        return XJdbc.executeUpdate(deleteSQL, maSach);
    }

    /**
     * Thêm lĩnh vực cho sách
     */
    public int addLinhVuc(int maSach, int maLinhVuc) {
        // Kiểm tra xem MaLinhVuc có tồn tại trong bảng LinhVuc
        String checkSql = "SELECT COUNT(*) FROM LinhVuc WHERE MaLinhVuc = ?";
        int count = (int) XJdbc.getValue(checkSql, maLinhVuc);
        if (count == 0) {
            throw new IllegalArgumentException("MaLinhVuc " + maLinhVuc + " không tồn tại trong bảng LinhVuc");
        }
        return XJdbc.executeUpdate(insertLinhVucSQL, maSach, maLinhVuc);
    }

    public int addLoaiSach(int maSach, int maLoaiSach) {
        // Kiểm tra xem MaLoaiSach có tồn tại trong bảng LoaiSach
        String checkSql = "SELECT COUNT(*) FROM LoaiSach WHERE MaLoaiSach = ?";
        int count = (int) XJdbc.getValue(checkSql, maLoaiSach);
        if (count == 0) {
            throw new IllegalArgumentException("MaLoaiSach " + maLoaiSach + " không tồn tại trong bảng LoaiSach");
        }
        return XJdbc.executeUpdate(insertLoaiSachSQL, maSach, maLoaiSach);
    }

    /**
     * Thêm nhiều lĩnh vực cho sách
     */
    public void addLinhVucList(int maSach, List<Integer> danhSachLinhVuc) {
        for (Integer maLinhVuc : danhSachLinhVuc) {
            addLinhVuc(maSach, maLinhVuc);
        }
    }

    /**
     * Thêm loại sách cho sách
     */
    /**
     * Thêm nhiều loại sách cho sách
     */
    public void addLoaiSachList(int maSach, List<Integer> danhSachLoaiSach) {
        for (Integer maLoaiSach : danhSachLoaiSach) {
            addLoaiSach(maSach, maLoaiSach);
        }
    }

    /**
     * Xóa tất cả lĩnh vực của một sách
     */
    public int removeLinhVuc(int maSach) {
        return XJdbc.executeUpdate(deleteLinhVucSQL, maSach);
    }

    /**
     * Xóa tất cả loại sách của một sách
     */
    public int removeLoaiSach(int maSach) {
        return XJdbc.executeUpdate(deleteLoaiSachSQL, maSach);
    }

    /**
     * Cập nhật lĩnh vực cho sách (xóa cũ, thêm mới)
     */
    public void updateLinhVuc(int maSach, List<Integer> danhSachLinhVuc) {
        removeLinhVuc(maSach);
        if (danhSachLinhVuc != null) {
            for (Integer maLinhVuc : danhSachLinhVuc) {
                try {
                    addLinhVuc(maSach, maLinhVuc);
                } catch (IllegalArgumentException ex) {
                    throw new RuntimeException("Lỗi khi thêm lĩnh vực: " + ex.getMessage(), ex);
                }
            }
        }
    }

    public void updateLoaiSach(int maSach, List<Integer> danhSachLoaiSach) {
        removeLoaiSach(maSach);
        if (danhSachLoaiSach != null) {
            for (Integer maLoaiSach : danhSachLoaiSach) {
                try {
                    addLoaiSach(maSach, maLoaiSach);
                } catch (IllegalArgumentException ex) {
                    throw new RuntimeException("Lỗi khi thêm loại sách: " + ex.getMessage(), ex);
                }
            }
        }
    }

    /**
     * Lấy MaSach theo ISBN (dùng để lấy ID sau khi tạo)
     */
    public int getMaSachByISBN(String isbn) {
        String sql = "SELECT MaSach FROM Sach WHERE ISBN = ?";
        // Thay XQuery bằng class database utility của bạn
        Object result = XJdbc.getValue(sql, isbn); // hoặc tên class khác
        return result != null ? (Integer) result : 0;
    }

    /**
     * Tạo sách hoàn chỉnh với lĩnh vực và loại sách
     */
    public int createSachHoanChinh(Sach sach, List<Integer> danhSachLinhVuc, List<Integer> danhSachLoaiSach) {
        try {
            // Tạo sách trước
            int result = create(sach);
            if (result <= 0) {
                throw new RuntimeException("Không thể tạo sách mới");
            }

            // Lấy MaSach vừa tạo
            int maSach = getMaSachByISBN(sach.getISBN());
            if (maSach <= 0) {
                throw new RuntimeException("Không thể lấy MaSach sau khi tạo");
            }

            // Thêm lĩnh vực
            if (danhSachLinhVuc != null && !danhSachLinhVuc.isEmpty()) {
                addLinhVucList(maSach, danhSachLinhVuc);
            }

            // Thêm loại sách
            if (danhSachLoaiSach != null && !danhSachLoaiSach.isEmpty()) {
                addLoaiSachList(maSach, danhSachLoaiSach);
            }

            return maSach;
        } catch (Exception ex) {
            throw new RuntimeException("Lỗi khi tạo sách hoàn chỉnh: " + ex.getMessage(), ex);
        }
    }

    public int updateSachHoanChinh(Sach sach, List<Integer> danhSachLinhVuc, List<Integer> danhSachLoaiSach) {
        try {
            // Cập nhật thông tin sách
            int result = update(sach);
            if (result <= 0) {
                throw new RuntimeException("Không thể cập nhật thông tin sách");
            }

            // Cập nhật lĩnh vực
            if (danhSachLinhVuc != null) {
                updateLinhVuc(sach.getMaSach(), danhSachLinhVuc);
            }

            // Cập nhật loại sách
            if (danhSachLoaiSach != null) {
                updateLoaiSach(sach.getMaSach(), danhSachLoaiSach);
            }

            return result;
        } catch (Exception ex) {
            throw new RuntimeException("Lỗi khi cập nhật sách hoàn chỉnh: " + ex.getMessage(), ex);
        }
    }

}
