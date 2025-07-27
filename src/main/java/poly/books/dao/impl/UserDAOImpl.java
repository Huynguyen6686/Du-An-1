/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.books.dao.impl;

import java.util.List;
import poly.books.entity.NguoiDungSD;
import poly.books.util.XJdbc;
import poly.books.util.XQuery;

/**
 *
 * @author HuyNguyen
 */
public class UserDAOImpl implements poly.books.dao.UserDAO {

    String createSql = "INSERT INTO NguoiDungSD(TenDangNhap, MatKhau, TrangThai, HoTen, HinhAnh, QuanLy) VALUES(?, ?, ?, ?, ?, ?)";
    String updateSql = "UPDATE NguoiDungSD SET MatKhau=?, TrangThai=?, HoTen=?, HinhAnh=?, QuanLy=? WHERE TenDangNhap=?";
    String deleteSql = "DELETE FROM NguoiDungSD WHERE TenDangNhap=?";
    String findAllSql = "SELECT * FROM NguoiDungSD";
    String findByIdSql = "SELECT * FROM NguoiDungSD WHERE TenDangNhap=?";

    @Override
    public NguoiDungSD create(NguoiDungSD entity) {
        Object[] values = {
            entity.getTenDangNhap(),
            entity.getMatKhau(),
            entity.isTrangThai(),
            entity.getHoTen(),
            entity.getHinhAnh(),
            entity.isQuanLy()
        };
        XJdbc.executeUpdate(createSql, values);
        return entity;
    }

    @Override
    public void update(NguoiDungSD entity) {
        Object[] values = {
            entity.getMatKhau(),
            entity.isTrangThai(),
            entity.getHoTen(),
            entity.getHinhAnh(),
            entity.isQuanLy(),
            entity.getTenDangNhap()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<NguoiDungSD> findAll() {
        return XQuery.getBeanList(NguoiDungSD.class, findAllSql);
    }

    @Override
    public NguoiDungSD findById(String id) {
        return XQuery.getSingleBean(NguoiDungSD.class, findByIdSql, id);
    }
}
