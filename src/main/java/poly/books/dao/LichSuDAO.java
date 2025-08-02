
package poly.books.dao;

import java.util.List;
import poly.books.entity.LichSuEntity;
import poly.books.util.XQuery;

public class LichSuDAO {
    String getAllSQL = """
	select hd.MaHD ,ndsd.TenDangNhap,ndsd.HoTen,ndsd.QuanLy,kh.TenKH,hd.NgayThanhToan,hd.PhuongThuc,hd.TrangThai from dbo.HoaDon hd 
                       	join NguoiDungSD ndsd on  hd.TenDangNhap = ndsd.TenDangNhap
                       	join KhachHang kh on kh.MaKH = hd.MaKH
                       	order by hd.MaHD;
                       """;
    public List<LichSuEntity> getAll() {
        return XQuery.getBeanList(LichSuEntity.class, getAllSQL);
    }
}
