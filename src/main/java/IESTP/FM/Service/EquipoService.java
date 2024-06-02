package IESTP.FM.Service;

import IESTP.FM.Entity.Empleado;
import IESTP.FM.Entity.Equipo;
import IESTP.FM.Entity.Ubicacion;
import IESTP.FM.Repository.EquipoRepository;
import IESTP.FM.utils.GenericResponse;
import IESTP.FM.utils.Global;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
@Transactional
public class EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    public GenericResponse<Equipo> addEquipo(Equipo equipo) {
        try {
            equipo.setCodigoBarra(generateRandomBarcode(12));
            Equipo savedEquipo = equipoRepository.save(equipo);
            return new GenericResponse<>(Global.TIPO_CORRECTO, Global.RPTA_OK, "Equipo registrado exitosamente.", savedEquipo);
        } catch (Exception e) {
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RPTA_ERROR, "Error al registrar el equipo: " + e.getMessage(), null);
        }
    }

    private String generateRandomBarcode(int length) {
        String characters = "0123456789";
        Random rng = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(rng.nextInt(characters.length())));
        }
        return sb.toString();
    }

    public GenericResponse<Equipo> updateEquipo(Equipo equipo) {
        if (equipo.getId() == 0) {
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RPTA_ERROR, "ID de equipo no proporcionado.", null);
        }

        Optional<Equipo> existingEquipo = equipoRepository.findById(equipo.getId());
        if (!existingEquipo.isPresent()) {
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RPTA_ERROR, "Equipo no encontrado.", null);
        }

        try {
            Equipo updatedEquipo = existingEquipo.get();

            // Actualizando solo los campos permitidos
            updatedEquipo.setTipoEquipo(equipo.getTipoEquipo());
            updatedEquipo.setDescripcion(equipo.getDescripcion());
            updatedEquipo.setEstado(equipo.getEstado());
            updatedEquipo.setFechaCompra(equipo.getFechaCompra()); // Asumiendo que ya es LocalDate
            updatedEquipo.setMarca(equipo.getMarca());
            updatedEquipo.setModelo(equipo.getModelo());
            updatedEquipo.setNombreEquipo(equipo.getNombreEquipo());
            updatedEquipo.setNumeroOrden(equipo.getNumeroOrden());
            updatedEquipo.setSerie(equipo.getSerie());

            // Asignar responsable y ubicación si se proporcionan
            if (equipo.getResponsable() != null && equipo.getResponsable().getId() > 0) {
                updatedEquipo.setResponsable(new Empleado(equipo.getResponsable().getId()));
            }
            if (equipo.getUbicacion() != null && equipo.getUbicacion().getId() > 0) {
                updatedEquipo.setUbicacion(new Ubicacion(equipo.getUbicacion().getId()));
            }

            equipoRepository.save(updatedEquipo);
            return new GenericResponse<>(Global.TIPO_CORRECTO, Global.RPTA_OK, "Equipo actualizado exitosamente.", updatedEquipo);
        } catch (Exception e) {
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RPTA_ERROR, "Error al actualizar el equipo: " + e.getMessage(), null);
        }
    }

    public GenericResponse<Void> deleteEquipo(Integer id) {
        if (!equipoRepository.existsById(id)) {
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RPTA_ERROR, "Equipo no encontrado.", null);
        }
        try {
            equipoRepository.deleteById(id);
            return new GenericResponse<>(Global.TIPO_CORRECTO, Global.RPTA_OK, "Equipo eliminado exitosamente.", null);
        } catch (Exception e) {
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RPTA_ERROR, "Error al eliminar el equipo: " + e.getMessage(), null);
        }
    }

    public GenericResponse<List<Equipo>> findAllEquipos() {
        try {
            List<Equipo> equipos = (List<Equipo>) equipoRepository.findAll();
            return new GenericResponse<>(Global.TIPO_CORRECTO, Global.RPTA_OK, "Listado completo de equipos.", equipos);
        } catch (Exception e) {
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RPTA_ERROR, "Error al obtener el listado de equipos: " + e.getMessage(), null);
        }
    }

    public GenericResponse<Equipo> scanAndCopyBarcodeData(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                return new GenericResponse<>(Global.TIPO_ERROR, Global.RPTA_WARNING, "La imagen cargada es nula o el formato no es compatible.", null);
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);
            String barcodeText = result.getText();

            Optional<Equipo> informacion = equipoRepository.findByCodigoBarra(barcodeText);
            if (informacion.isPresent()) {
                return new GenericResponse<>(Global.TIPO_CORRECTO, Global.RPTA_OK, "Escaneo de Código de Barras correcto", informacion.get());
            } else {
                return new GenericResponse<>(Global.TIPO_CUIDADO, Global.RPTA_WARNING, "Código de barras no encontrado", null);
            }
        } catch (Exception e) {
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RPTA_ERROR, "Error al procesar el archivo: " + e.getMessage(), null);
        }
    }

    public GenericResponse<byte[]> generateBarcodeImageForPatrimonialCode(String codigoPatrimonial) {
        Optional<Equipo> informacion = equipoRepository.findByCodigoPatrimonial(codigoPatrimonial);
        if (informacion.isPresent()) {
            try {
                Equipo info = informacion.get();
                String barcodeData = info.getCodigoBarra();  // Use only the barcode number
                return new GenericResponse<>("SUCCESS", Global.RPTA_OK, "Código de barras generado exitosamente", generateBarcodeImage(barcodeData));
            } catch (Exception e) {
                return new GenericResponse<>("ERROR", Global.RPTA_ERROR, "Error al generar el código de barras: " + e.getMessage(), null);
            }
        } else {
            return new GenericResponse<>("ERROR", Global.RPTA_ERROR, "No se encontró información para el código patrimonial proporcionado", null);
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
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0, 0, combinedImage.getWidth(), combinedImage.getHeight());

        // Draw barcode image with top margin
        g.drawImage(barcodeImage, 0, 20, null);
        g.setColor(Color.BLACK);
        g.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, Font.PLAIN, 12));

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
    // Método para generar el Excel
    public byte[] generateExcelReport() throws Exception {
        List<Equipo> equipos = (List<Equipo>) equipoRepository.findAll();
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Equipos");

            // Crear el encabezado
            String[] columns = {"ID", "Tipo Equipo", "Código Barra", "Código Patrimonial", "Descripción", "Estado", "Fecha Compra", "Marca", "Modelo", "Nombre Equipo", "Número Orden", "Serie"};
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
            }

            // Llenar datos
            int rowIdx = 1;
            for (Equipo equipo : equipos) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(equipo.getId());
                row.createCell(1).setCellValue(equipo.getTipoEquipo());
                row.createCell(2).setCellValue(equipo.getCodigoBarra());
                row.createCell(3).setCellValue(equipo.getCodigoPatrimonial());
                row.createCell(4).setCellValue(equipo.getDescripcion());
                row.createCell(5).setCellValue(equipo.getEstado());
                row.createCell(6).setCellValue(equipo.getFechaCompra().toString());
                row.createCell(7).setCellValue(equipo.getMarca());
                row.createCell(8).setCellValue(equipo.getModelo());
                row.createCell(9).setCellValue(equipo.getNombreEquipo());
                row.createCell(10).setCellValue(equipo.getNumeroOrden());
                row.createCell(11).setCellValue(equipo.getSerie());
            }

            // Auto-size all columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new Exception("Error al generar el reporte: " + e.getMessage());
        }
    }

    public GenericResponse<Equipo> getEquipoById(int id) {
        try {
            Equipo equipo = equipoRepository.findByIdWithDetails(id);
            if (equipo != null) {
                return new GenericResponse<>(Global.TIPO_DATA, Global.RPTA_OK, Global.OPERACION_CORRECTA, equipo);
            } else {
                return new GenericResponse<>(Global.TIPO_RESULT, Global.RPTA_WARNING, "El equipo no existe", null);
            }
        } catch (Exception e) {
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RPTA_ERROR, Global.OPERACION_ERRONEA, null);
        }
    }

    public GenericResponse<List<Equipo>> filtroPorNombre(String nombreEquipo) {
        try {
            List<Equipo> equipos = equipoRepository.findByNombreEquipoContaining(nombreEquipo);
            return new GenericResponse<>(Global.TIPO_DATA, Global.RPTA_OK, Global.OPERACION_CORRECTA, equipos);
        } catch (Exception e) {
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RPTA_ERROR, Global.OPERACION_ERRONEA, null);
        }
    }

    public GenericResponse<List<Equipo>> filtroCodigoPatrimonial(String codigoPatrimonial) {
        try {
            List<Equipo> equipos = equipoRepository.findByCodigoPatrimonialContaining(codigoPatrimonial);
            return new GenericResponse<>(Global.TIPO_DATA, Global.RPTA_OK, Global.OPERACION_CORRECTA, equipos);
        } catch (Exception e) {
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RPTA_ERROR, Global.OPERACION_ERRONEA, null);
        }
    }

    public GenericResponse<List<Equipo>> filtroFechaCompraBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            List<Equipo> equipos = equipoRepository.findByFechaCompraBetween(fechaInicio, fechaFin);
            return new GenericResponse<>(Global.TIPO_DATA, Global.RPTA_OK, Global.OPERACION_CORRECTA, equipos);
        } catch (Exception e) {
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RPTA_ERROR, Global.OPERACION_ERRONEA, null);
        }
    }
}
