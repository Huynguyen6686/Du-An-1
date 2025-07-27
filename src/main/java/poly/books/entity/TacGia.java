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
public class TacGia {
    private int MaTacGia;
    private String TenTacGia;
    private String QueQuan;
    @Override
    public String toString() {
        return TenTacGia;
    }
}
