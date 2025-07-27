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
public class LoaiSach {
      private int MaLoaiSach;
    private String TenLoaiSach;
    @Override
    public String toString() {
        return TenLoaiSach;
    }
}
