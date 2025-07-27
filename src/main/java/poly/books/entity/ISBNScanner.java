/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
        this.webcam = Webcam.getDefault();
        if (webcam != null) {
            webcam.setViewSize(new java.awt.Dimension(320, 240));
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
            webcam.open();
            Reader reader = new MultiFormatReader();
            while (scanning) {
                try {
                    BufferedImage image = webcam.getImage();
                    if (image != null) {
                        videoLabel.setIcon(new ImageIcon(image));
                        LuminanceSource source = new BufferedImageLuminanceSource(image);
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                        try {
                            Result result = reader.decode(bitmap);
                            String isbn = result.getText();
                            java.awt.EventQueue.invokeLater(() -> isbnCallback.accept(isbn));
                            Thread.sleep(1000); // Đợi 1 giây trước khi quét tiếp
                        } catch (Exception e) {
                            // Không tìm thấy mã, tiếp tục quét
                        }
                    }
                    Thread.sleep(100); // Cập nhật video
                } catch (Exception e) {
                    java.awt.EventQueue.invokeLater(() ->
                        videoLabel.setText("Lỗi: " + e.getMessage()));
                    break;
                }
            }
            webcam.close();
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
        videoLabel.setIcon(null);
        videoLabel.setText("Đã dừng quét");
    }
}