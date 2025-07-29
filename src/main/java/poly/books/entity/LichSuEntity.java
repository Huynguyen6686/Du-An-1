
package poly.books.entity;

import java.util.Date;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LichSuEntity {
    private int STT;
    private String TenDangNhap;
    private String HoTen;
    private boolean QuanLy;
    private String TenKH;
    private Date NgayThanhToan;
    private int PhuongThuc;
    private int TrangThai;
}
