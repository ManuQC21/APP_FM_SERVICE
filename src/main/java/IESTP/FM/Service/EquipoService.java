package IESTP.FM.Service;

import IESTP.FM.Entity.Equipo;
import IESTP.FM.Entity.InventoryItems;
import IESTP.FM.Repository.EquipoRepository;
import IESTP.FM.Repository.InventoryItemsRepository;
import IESTP.FM.utils.GenericResponse;
import IESTP.FM.utils.Global;
import com.google.zxing.MultiFormatReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.util.List;
@Service
@Transactional
public class EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private InventoryItemsRepository inventoryItemsRepository;
    public GenericResponse<Equipo> addEquipo(Equipo equipo) {
        try {
            equipo.setCodigoBarra(generateRandomBarcode(12));
            Equipo savedEquipo = equipoRepository.save(equipo);
            return new GenericResponse<>("SUCCESS", 1, "Equipo registrado exitosamente.", savedEquipo);
        } catch (Exception e) {
            return new GenericResponse<>("ERROR", 0, "Error al registrar el equipo: " + e.getMessage(), null);
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
        if (equipo.getId() == 0 || !equipoRepository.existsById(equipo.getId())) {
            return new GenericResponse<>("ERROR", 0, "Equipo no encontrado.", null);
        }
        try {
            equipo.setFechaCompra(LocalDate.now());
            Equipo updatedEquipo = equipoRepository.save(equipo);
            return new GenericResponse<>("SUCCESS", 1, "Equipo actualizado exitosamente.", updatedEquipo);
        } catch (Exception e) {
            return new GenericResponse<>("ERROR", 0, "Error al actualizar el equipo: " + e.getMessage(), null);
        }
    }

    public GenericResponse<Void> deleteEquipo(Integer id) {
        if (!equipoRepository.existsById(id)) {
            return new GenericResponse<>("ERROR", 0, "Equipo no encontrado.", null);
        }
        try {
            equipoRepository.deleteById(id);
            return new GenericResponse<>("SUCCESS", 1, "Equipo eliminado exitosamente.", null);
        } catch (Exception e) {
            return new GenericResponse<>("ERROR", 0, "Error al eliminar el equipo: " + e.getMessage(), null);
        }
    }

    public GenericResponse<List<Equipo>> findAllEquipos() {
        try {
            List<Equipo> equipos = (List<Equipo>) equipoRepository.findAll();
            return new GenericResponse<>("SUCCESS", 1, "Listado completo de equipos.", equipos);
        } catch (Exception e) {
            return new GenericResponse<>("ERROR", 0, "Error al obtener el listado de equipos: " + e.getMessage(), null);
        }
    }
    public GenericResponse<Equipo> findEquipoByCodigoPatrimonial(String codigoPatrimonial) {
        Optional<Equipo> equipo = equipoRepository.findByCodigoPatrimonial(codigoPatrimonial);
        if (equipo.isPresent()) {
            return new GenericResponse<>(Global.TIPO_DATA, Global.RPTA_OK, "Equipo encontrado", equipo.get());
        } else {
            return new GenericResponse<>(Global.TIPO_DATA, Global.RPTA_WARNING, "Equipo no encontrado", null);
        }
    }
    public GenericResponse<Equipo> scanAndCopyBarcodeData(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                throw new IllegalArgumentException("La imagen cargada es nula o el formato no es compatible.");
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            MultiFormatReader reader = new MultiFormatReader(); // Soporta múltiples formatos
            String barcodeText = reader.decode(bitmap).getText();

            Optional<InventoryItems> informacion = inventoryItemsRepository.findByCodigoBarra(barcodeText);
            if (informacion.isPresent()) {
                InventoryItems info = informacion.get();
                Equipo nuevoEquipo = new Equipo(info);
                Equipo savedEquipo = equipoRepository.save(nuevoEquipo);
                return new GenericResponse<>("SUCCESS", 1, "Escaneo de Código de Barras correcto", savedEquipo);
            } else {
                return new GenericResponse<>("WARNING", 0, "Código de barras no encontrado", null);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el stack trace para depuración
            return new GenericResponse<>("ERROR", -1, "Error al procesar el archivo: " + e.toString(), null);
        }
    }

    // Método para generar el Excel
    public byte[] generateExcelReport() throws Exception {
        List<Equipo> equipos = (List<Equipo>) equipoRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
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



}
