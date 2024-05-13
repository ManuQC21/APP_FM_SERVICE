package IESTP.FM.Service;

import IESTP.FM.Entity.InventoryItems;
import IESTP.FM.Repository.InventoryItemsRepository;
import IESTP.FM.utils.GenericResponse;
import IESTP.FM.utils.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Optional;

@Service
@Transactional
public class InventoryItemsService {

    @Autowired
    private InventoryItemsRepository inventoryItemsRepository;

    public GenericResponse<InventoryItems> saveInformacion(InventoryItems inventoryItems) {
        try {
            InventoryItems savedInventoryItems = inventoryItemsRepository.save(inventoryItems);
            return new GenericResponse<>("SUCCESS", 1, "Información guardada exitosamente", savedInventoryItems);
        } catch (Exception e) {
            return new GenericResponse<>("ERROR", -1, "Error al guardar la información: " + e.getMessage(), null);
        }
    }
    public GenericResponse<List<InventoryItems>> findAllInformacion() {
        try {
            List<InventoryItems> allInventoryItems = (List<InventoryItems>) inventoryItemsRepository.findAll();
            return new GenericResponse<>(Global.TIPO_DATA, Global.RPTA_OK, "Listado completo de información.", allInventoryItems);
        } catch (Exception e) {
            return new GenericResponse<>(Global.TIPO_DATA, Global.RPTA_ERROR, "Error al obtener el listado: " + e.getMessage(), null);
        }
    }
    public GenericResponse<byte[]> generateBarcodeImageForPatrimonialCode(String codigoPatrimonial) {
        Optional<InventoryItems> informacion = inventoryItemsRepository.findByCodigoPatrimonial(codigoPatrimonial);
        if (informacion.isPresent()) {
            try {
                InventoryItems info = informacion.get();
                String barcodeData = info.getCodigoBarra();  // Use only the barcode number
                return new GenericResponse<>("SUCCESS", 1, "Código de barras generado exitosamente", generateBarcodeImage(barcodeData));
            } catch (Exception e) {
                return new GenericResponse<>("ERROR", -1, "Error al generar el código de barras: " + e.getMessage(), null);
            }
        } else {
            return new GenericResponse<>("ERROR", -1, "No se encontró información para el código patrimonial proporcionado", null);
        }
    }

    private byte[] generateBarcodeImage(String data) throws Exception {
        MultiFormatWriter barcodeWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(data, BarcodeFormat.CODE_128, 300, 100);

        // Convert BitMatrix to BufferedImage
        BufferedImage barcodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // Prepare to add text below barcode and margin at the top
        BufferedImage combinedImage = new BufferedImage(barcodeImage.getWidth(), barcodeImage.getHeight() + 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combinedImage.createGraphics();

        // Fill background with white
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, combinedImage.getWidth(), combinedImage.getHeight());

        // Draw barcode image with top margin
        g.drawImage(barcodeImage, 0, 20, null);
        g.setColor(Color.BLACK);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        // Draw text below barcode
        FontMetrics fontMetrics = g.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(data);
        int textX = (barcodeImage.getWidth() - textWidth) / 2; // Center text horizontally
        int textY = barcodeImage.getHeight() + 40; // Position text below the barcode
        g.drawString(data, textX, textY);

        g.dispose(); // Clean up graphics object

        // Write combined image to output byte array
        try (ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(combinedImage, "PNG", pngOutputStream);
            return pngOutputStream.toByteArray();
        }
    }
}
