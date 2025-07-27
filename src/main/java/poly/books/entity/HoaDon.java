/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.books.entity;

import java.util.Date;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
/**
 *
 * @author HuyNguyen
 */
public class HoaDon {
     private int MaHD;
    private Date NgayLap;
    private int MaKH;
    private String TenDangNhap;
    private Integer MaPhieu; // Nullable
    private double TongTien;
    private int PhuongThuc;
    private Date NgayThanhToan;
    private int TrangThai;
}
