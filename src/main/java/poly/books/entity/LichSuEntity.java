
package poly.books.entity;

import java.util.Date;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LichSuEntity {
        private int MaHD;
    private String TenDangNhap;
    private String HoTen;
    private String TenKH;
    private Date NgayThanhToan;
    private int PhuongThuc;
    private double ThanhTien;
    private int Giam;
    private double GiaSauKhiGiam;
    private int TrangThai;
}
