package poly.books.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ThongTinSanPham {
    private int MaHD;
    private String TenSach;
    private double GiaBan;
    private int SoLuong;
    private Double ThanhTien;
}
