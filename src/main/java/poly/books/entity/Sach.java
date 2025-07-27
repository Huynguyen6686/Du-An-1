/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.books.entity;

import java.util.Date;
import java.util.List;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
/**
 *
 * @author HuyNguyen
 */
public class Sach {

    private int MaSach;
    private String TenSach;
    private int MaTacGia;
    private int MaLinhVuc;
    private int MaLoaiSach;
    private int MaNXB;
    private Date NamXuatBan;
    private double GiaBan;
    private int LanTaiBan;
    private String ISBN;
    private Integer Tap;
    private int MaNgonNgu;
    private String HinhAnh;
    private transient List<Integer> maLinhVucs;
    private transient List<Integer> maLoaiSachs;
    private List<LinhVuc> linhVucList;
    private List<LoaiSach> loaiSachList;
    private int SoLuong;

// Và các phương thức getter/setter tương ứng
    public List<LinhVuc> getLinhVucList() {
        return linhVucList;
    }

    public void setLinhVucList(List<LinhVuc> linhVucList) {
        this.linhVucList = linhVucList;
    }

    public List<LoaiSach> getLoaiSachList() {
        return loaiSachList;
    }

    public void setLoaiSachList(List<LoaiSach> loaiSachList) {
        this.loaiSachList = loaiSachList;
    }
}
