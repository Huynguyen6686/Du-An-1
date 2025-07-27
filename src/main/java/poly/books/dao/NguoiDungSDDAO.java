/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.books.dao;

import java.util.List;
import poly.books.entity.NguoiDungSD;
import poly.books.util.XJdbc;
import poly.books.util.XQuery;

/**
 *
 * @author Thuy
 */
public class NguoiDungSDDAO {
    String FIND_ALL="""
                    SELECT [TenDangNhap]
                          ,[MatKhau]
                          ,[HoTen]
                          ,[TrangThai]
                          ,[QuanLy]
                          ,[HinhAnh]
                      FROM [dbo].[NguoiDungSD]
                    """;
    String FIND_BY_ID="""
                      SELECT [MatKhau]
                            ,[HoTen]
                            ,[TrangThai]
                            ,[QuanLy]
                            ,[HinhAnh]
                        FROM [dbo].[NguoiDungSD]
                      where [TenDangNhap]=?
                      """;
    String CREATE="""
                  INSERT INTO [dbo].[NguoiDungSD]
                             ([TenDangNhap]
                             ,[MatKhau]
                             ,[HoTen]
                             ,[TrangThai]
                             ,[QuanLy]
                             ,[HinhAnh])
                       VALUES
                             (?,?,?,? ,?,?)
                  """;
    
    String UPDATE="""
                  UPDATE [dbo].[NguoiDungSD]
                     SET [MatKhau] = ?
                        ,[HoTen] = ?
                        ,[TrangThai] = ?
                        ,[QuanLy] = ?
                        ,[HinhAnh] = ?
                   WHERE [TenDangNhap] = ?
                  """;
    String DETELE="""
                  DELETE FROM [dbo].[NguoiDungSD]
                        WHERE [TenDangNhap] = ?
                  """;
    public List<NguoiDungSD> findAll(){
        return XQuery.getBeanList(NguoiDungSD.class, FIND_ALL);
    }
    public NguoiDungSD findById(String TenDangNhap){
        return XQuery.getSingleBean(NguoiDungSD.class, FIND_BY_ID, TenDangNhap);
    }
    public int craete(NguoiDungSD ndsd){
        Object[] rowData={
             ndsd.getTenDangNhap(),
        ndsd.getMatKhau(),
        ndsd.getHoTen(),
        ndsd.isTrangThai(),
        ndsd.isQuanLy(),
        ndsd.getHinhAnh()
        };
        return XJdbc.executeUpdate(CREATE, rowData);
    }
    public int update(NguoiDungSD ndsd){
        Object[] rowData={
             
             ndsd.getMatKhau(),
             ndsd.getHoTen(),
             ndsd.isTrangThai(),
             ndsd.isQuanLy(),
             ndsd.getHinhAnh(),
             ndsd.getTenDangNhap()
        };
        return XJdbc.executeUpdate(UPDATE, rowData);
    }
    public int detele(String TenDangNhap){
        return XJdbc.executeUpdate(DETELE, TenDangNhap);
    }
}
