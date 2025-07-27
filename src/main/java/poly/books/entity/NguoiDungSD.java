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
 * @author HuyNguyen
 */
public class NguoiDungSD {
    private String TenDangNhap;
    private String MatKhau;
    private String HoTen;
    private boolean TrangThai;
    private boolean QuanLy;
    private String HinhAnh;

}
