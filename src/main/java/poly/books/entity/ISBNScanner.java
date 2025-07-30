package poly.books.entity;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class ISBNScanner {
    private volatile boolean scanning = false;
    private Thread scanThread;
    private JLabel videoLabel;
    private Consumer<String> isbnCallback;
    private Webcam webcam;

    public ISBNScanner(JLabel videoLabel, Consumer<String> isbnCallback) {
        this.videoLabel = videoLabel;
        this.isbnCallback = isbnCallback;

        // ✅ Tránh khởi tạo webcam khi đang ở chế độ thiết kế NetBeans
        if (!java.beans.Beans.isDesignTime()) {
            this.webcam = Webcam.getDefault();
            if (webcam != null) {
                if (webcam.isOpen()) {
                    webcam.close(); // Đảm bảo webcam chưa mở
                }
                webcam.setViewSize(new java.awt.Dimension(320, 240));
            }
        }
    }

    public void startScanning() {
        if (webcam == null) {
            videoLabel.setText("Không tìm thấy webcam!");
            return;
        }

        scanning = true;
        videoLabel.setText("");
        scanThread = new Thread(() -> {
            try {
                if (!webcam.isOpen()) {
                    webcam.open();
                }
                Reader reader = new MultiFormatReader();

                while (scanning) {
                    BufferedImage image = webcam.getImage();
                    if (image != null) {
                        videoLabel.setIcon(new ImageIcon(image));
                        LuminanceSource source = new BufferedImageLuminanceSource(image);
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                        try {
                            Result result = reader.decode(bitmap);
                            String isbn = result.getText();
                            java.awt.EventQueue.invokeLater(() -> isbnCallback.accept(isbn));
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            // Không tìm thấy mã, tiếp tục quét
                        }
                    }
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                java.awt.EventQueue.invokeLater(() ->
                    videoLabel.setText("Lỗi: " + e.getMessage()));
            } finally {
                if (webcam != null && webcam.isOpen()) {
                    webcam.close();
                }
            }
        });
        scanThread.start();
    }

    public void stopScanning() {
        scanning = false;
        if (scanThread != null) {
            scanThread.interrupt();
        }
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
    }
}
