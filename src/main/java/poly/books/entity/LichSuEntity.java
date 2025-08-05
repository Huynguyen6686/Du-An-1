
package poly.books.entity;

import java.util.Date;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LichSuEntity {
    private String ISBN;
    private String TenDangNhap;
    private String HoTen;
    private boolean QuanLy;
    private String TenKH;
    private Date NgayThanhToan;
    private int PhuongThuc;
    private double ThanhTien;
    private int Giam;
    private double GiaSauKhiGiam;
    private int TrangThai;
}
