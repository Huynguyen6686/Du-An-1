package poly.books.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import javax.swing.JPanel;

public class GradientPanel extends JPanel {

    public GradientPanel() {
        // Cho phép panel vẽ nền tùy chỉnh (không bị JPanel mặc định ghi đè)
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Tạo gradient màu từ trên xuống dưới
        int width = getWidth();
        int height = getHeight();
        
        // Màu bắt đầu và kết thúc (tùy chỉnh tại đây)
       Color colorStart = new Color(21,155,209);    // Xanh đậm hơn
        Color colorEnd = new Color(1,23,84);     // Xanh lục đậm hơn

        GradientPaint gp = new GradientPaint(0, 0, colorStart, 0, height, colorEnd);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);

        g2d.dispose();

        // Vẽ các thành phần con như JLabel, button... sau khi vẽ nền
        super.paintComponent(g);
    }
}
