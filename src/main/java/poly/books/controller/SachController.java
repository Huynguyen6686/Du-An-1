/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package poly.books.controller;

import javax.swing.JDialog;
import javax.swing.JFrame;
import poly.books.entity.Sach;
import poly.books.ui.manager.DanhSachSanPham;
import poly.books.ui.manager.QuanLyLinhVuc;
import poly.books.ui.manager.QuanLyNgonNgu;
import poly.books.ui.manager.QuanLyNhaXuatBan;
import poly.books.ui.manager.QuanLyTacGia;
import poly.books.ui.manager.QuanLyTheLoai;
import poly.books.ui.BanHang;
/**
 *
 * @author ADMIN
 */
public interface SachController extends CrudController<Sach> {

    default void showJDialog(JDialog dialog) {
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    default void showQuanLyLinhVuc(JFrame frame) {
        this.showJDialog(new QuanLyLinhVuc(frame, true, null));
    }

    default void showQuanLyTacGia(JFrame frame) {
        this.showJDialog(new QuanLyTacGia(frame, true,null));
    }

    default void showQuanLyTheLoai(JFrame frame) {
        this.showJDialog(new QuanLyTheLoai(frame, true, null));
    }

    default void showQuanLyNgonNgu(JFrame frame) {
        this.showJDialog(new QuanLyNgonNgu(frame, true,null));
    }

    default void showQuanLyNhaXuatBan(JFrame frame) {
        this.showJDialog(new QuanLyNhaXuatBan(frame, true,null));
    }

    default void showDanhSachSanPham(JFrame frame, BanHang banHang) {
        this.showJDialog(new DanhSachSanPham(frame, true, banHang));
    }

}
