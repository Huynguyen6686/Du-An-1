/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.books.entity;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
/**
 *
 * @author ADMIN
 */
public class DoanhThuNhanVien {
    private String tenDangNhap;
    private String hoTen;
    private int soHoaDon;
    private double tongDoanhThu;
}
