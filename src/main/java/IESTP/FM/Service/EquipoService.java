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
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.util.IOUtils;
import java.io.FileInputStream;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;



@Service
@Transactional
@Slf4j
public class EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    public GenericResponse<Equipo> addEquipo(Equipo equipo) {
        try {
            equipo.setCodigoBarra(generateRandomBarcode(12));
            Equipo savedEquipo = equipoRepository.save(equipo);
            return new GenericResponse<>(Global.TIPO_EXITO, Global.RESPUESTA_OK, "Equipo registrado exitosamente.", savedEquipo);
        } catch (Exception e) {
            log.error("Error al registrar el equipo: ", e);
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RESPUESTA_ERROR, "Error al registrar el equipo: " + e.getMessage(), null);
        }
    }

    private String generateRandomBarcode(int length) {
        return new Random().ints(length, 0, 10)
                .mapToObj(i -> String.valueOf(i))
                .collect(Collectors.joining());
    }

    @Transactional
    public GenericResponse<Equipo> updateEquipo(Equipo equipo) {
        return equipoRepository.findById(equipo.getId())
                .map(existingEquipo -> {
                    updateEquipoData(existingEquipo, equipo);
                    equipoRepository.save(existingEquipo);
                    return new GenericResponse<>(Global.TIPO_EXITO, Global.RESPUESTA_OK, "Equipo actualizado exitosamente.", existingEquipo);
                })
                .orElseGet(() -> new GenericResponse<>(Global.TIPO_ERROR, Global.RESPUESTA_ERROR, "Equipo no encontrado.", null));
    }


    private void updateEquipoData(Equipo existingEquipo, Equipo equipo) {
        existingEquipo.setTipoEquipo(equipo.getTipoEquipo());
        existingEquipo.setDescripcion(equipo.getDescripcion());
        existingEquipo.setEstado(equipo.getEstado());
        existingEquipo.setFechaRevision(equipo.getFechaRevision());
        existingEquipo.setMarca(equipo.getMarca());
        existingEquipo.setModelo(equipo.getModelo());
        existingEquipo.setNombreEquipo(equipo.getNombreEquipo());
        existingEquipo.setNumeroOrden(equipo.getNumeroOrden());
        existingEquipo.setSerie(equipo.getSerie());
        if (equipo.getResponsable() != null && equipo.getResponsable().getId() > 0) {
            existingEquipo.setResponsable(new Empleado(equipo.getResponsable().getId()));
        }
        if (equipo.getUbicacion() != null && equipo.getUbicacion().getId() > 0) {
            existingEquipo.setUbicacion(new Ubicacion(equipo.getUbicacion().getId()));
        }
    }


    public GenericResponse<Void> deleteEquipo(Integer id) {
        return equipoRepository.findById(id)
                .map(equipo -> {
                    equipoRepository.delete(equipo);
                    return new GenericResponse<Void>(Global.TIPO_EXITO, Global.RESPUESTA_OK, "Equipo eliminado exitosamente.", null);
                })
                .orElseGet(() -> {
                    log.error("Intento de eliminar equipo fallido, equipo no encontrado con ID: {}", id);
                    return new GenericResponse<Void>(Global.TIPO_ERROR, Global.RESPUESTA_ERROR, "Equipo no encontrado.", null);
                });
    }

    public GenericResponse<List<Equipo>> findAllEquipos() {
        try {
            List<Equipo> equipos = (List<Equipo>) equipoRepository.findAll();
            return new GenericResponse<>(Global.TIPO_EXITO, Global.RESPUESTA_OK, "Listado completo de equipos.", equipos);
        } catch (Exception e) {
            log.error("Error al obtener el listado de equipos: ", e);
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RESPUESTA_ERROR, "Error al obtener el listado de equipos: " + e.getMessage(), null);
        }
    }

    public GenericResponse<Equipo> scanAndCopyBarcodeData(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                log.error("La imagen cargada es nula o el formato no es compatible.");
                return new GenericResponse<>(Global.TIPO_ERROR, Global.RESPUESTA_ADVERTENCIA, "La imagen cargada es nula o el formato no es compatible.", null);
            }

            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);
            String barcodeText = result.getText().trim();

            log.info("Código de barras decodificado (limpio): '{}'", barcodeText);

            Optional<Equipo> informacion = equipoRepository.findByCodigoBarra(barcodeText);
            if (informacion.isPresent()) {
                log.info("Código de barras encontrado en la base de datos: {}", barcodeText);
                return new GenericResponse<>(Global.TIPO_EXITO, Global.RESPUESTA_OK, "Escaneo de Código de Barras correcto", informacion.get());
            } else {
                log.warn("Código de barras no encontrado en la base de datos: {}", barcodeText);
                return new GenericResponse<>(Global.TIPO_ADVERTENCIA, Global.RESPUESTA_ADVERTENCIA, "Código de barras no encontrado", null);
            }
        } catch (Exception e) {
            log.error("Error al procesar el archivo: ", e);
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RESPUESTA_ERROR, "Error al procesar el archivo: " + e.getMessage(), null);
        }
    }

    public GenericResponse<byte[]> generateBarcodeImageForPatrimonialCode(String codigoPatrimonial) {
        Optional<Equipo> informacion = equipoRepository.findByCodigoPatrimonial(codigoPatrimonial);
        if (!informacion.isPresent()) {
            log.error("No se encontró información para el código patrimonial proporcionado: {}", codigoPatrimonial);
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RESPUESTA_ERROR, "No se encontró información para el código patrimonial proporcionado", null);
        }

        try {
            Equipo info = informacion.get();
            String barcodeData = info.getCodigoBarra(); // Use only the barcode number
            byte[] barcodeImage = generateBarcodeImage(barcodeData);
            return new GenericResponse<>(Global.TIPO_EXITO, Global.RESPUESTA_OK, "Código de barras generado exitosamente", barcodeImage);
        } catch (Exception e) {
            log.error("Error al generar el código de barras: ", e);
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RESPUESTA_ERROR, "Error al generar el código de barras: " + e.getMessage(), null);
        }
    }

    private byte[] generateBarcodeImage(String data) throws Exception {
        MultiFormatWriter barcodeWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(data, BarcodeFormat.CODE_128, 300, 100);
        BufferedImage barcodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        BufferedImage combinedImage = new BufferedImage(barcodeImage.getWidth(), barcodeImage.getHeight() + 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combinedImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, combinedImage.getWidth(), combinedImage.getHeight());
        g.drawImage(barcodeImage, 0, 20, null);
        g.setColor(Color.BLACK);
        g.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 12)); // Fully qualified reference
        FontMetrics fontMetrics = g.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(data);
        int textX = (barcodeImage.getWidth() - textWidth) / 2; // Center text horizontally
        int textY = barcodeImage.getHeight() + 40; // Position text below the barcode
        g.drawString(data, textX, textY);
        g.dispose(); // Clean up graphics object

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

            // Insertar el logo
            FileInputStream inputStream = new FileInputStream("src/main/resources/logo.png");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            inputStream.close();
            CreationHelper helper = workbook.getCreationHelper();
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(0);
            anchor.setRow1(0);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize();

            // Crear un estilo para el título
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 16);
            titleStyle.setFont(titleFont);

            // Agregar título en la fila 3
            Row titleRow = sheet.createRow(2); // Fila 3
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("REPORTE TOTAL DE EQUIPOS");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 15)); // Fusionar celdas para el título

            // Estilo para encabezados
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Crear el encabezado en la fila 6
            String[] columns = {
                    "ID", "Tipo Equipo", "Código Barra", "Código Patrimonial", "Descripción", "Estado",
                    "Fecha Compra", "Marca", "Modelo", "Nombre Equipo", "Número Orden", "Serie",
                    "Responsable - Nombre", "Responsable - Cargo", "Ubicación - Ambiente", "Ubicación - Ubicación Física"
            };
            Row headerRow = sheet.createRow(5); // Fila 6
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(headerStyle);
            }

            // Llenar datos a partir de la fila 7
            int rowIdx = 6;
            for (Equipo equipo : equipos) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(equipo.getId());
                row.createCell(1).setCellValue(equipo.getTipoEquipo());
                row.createCell(2).setCellValue(equipo.getCodigoBarra());
                row.createCell(3).setCellValue(equipo.getCodigoPatrimonial());
                row.createCell(4).setCellValue(equipo.getDescripcion());
                row.createCell(5).setCellValue(equipo.getEstado());
                row.createCell(6).setCellValue(equipo.getFechaRevision().toString());
                row.createCell(7).setCellValue(equipo.getMarca());
                row.createCell(8).setCellValue(equipo.getModelo());
                row.createCell(9).setCellValue(equipo.getNombreEquipo());
                row.createCell(10).setCellValue(equipo.getNumeroOrden());
                row.createCell(11).setCellValue(equipo.getSerie());
                row.createCell(12).setCellValue(equipo.getResponsable() != null ? equipo.getResponsable().getNombre() : "-");
                row.createCell(13).setCellValue(equipo.getResponsable() != null ? equipo.getResponsable().getCargo() : "-");
                row.createCell(14).setCellValue(equipo.getUbicacion() != null ? equipo.getUbicacion().getAmbiente() : "-");
                row.createCell(15).setCellValue(equipo.getUbicacion() != null ? equipo.getUbicacion().getUbicacionFisica() : "-");
            }


            // Ajuste automático de tamaño de columnas
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
                return new GenericResponse<>(Global.TIPO_DATOS, Global.RESPUESTA_OK, "Equipo encontrado exitosamente.", equipo);
            } else {
                log.warn("No se encontró el equipo con ID: {}", id);
                return new GenericResponse<>(Global.TIPO_RESULTADO, Global.RESPUESTA_ADVERTENCIA, "El equipo no existe", null);
            }
        } catch (Exception e) {
            log.error("Error al buscar el equipo con ID: {}", id, e);
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RESPUESTA_ERROR, "Error al buscar el equipo: " + e.getMessage(), null);
        }
    }

    public GenericResponse<List<Equipo>> filtroPorNombre(String nombreEquipo) {
        try {
            List<Equipo> equipos = equipoRepository.findByNombreEquipoContaining(nombreEquipo);
            if (equipos.isEmpty()) {
                log.info("No se encontraron equipos con el nombre: {}", nombreEquipo);
                return new GenericResponse<>(Global.TIPO_ADVERTENCIA, Global.RESPUESTA_ADVERTENCIA, "No se encontraron equipos con ese nombre.", null);
            }
            return new GenericResponse<>(Global.TIPO_DATOS, Global.RESPUESTA_OK, "Equipos encontrados exitosamente.", equipos);
        } catch (Exception e) {
            log.error("Error al buscar equipos por nombre: {}", nombreEquipo, e);
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RESPUESTA_ERROR, "Error al buscar equipos: " + e.getMessage(), null);
        }
    }

    public GenericResponse<List<Equipo>> filtroCodigoPatrimonial(String codigoPatrimonial) {
        try {
            List<Equipo> equipos = equipoRepository.findByCodigoPatrimonialContaining(codigoPatrimonial);
            if (equipos.isEmpty()) {
                log.info("No se encontraron equipos con el código patrimonial: {}", codigoPatrimonial);
                return new GenericResponse<>(Global.TIPO_ADVERTENCIA, Global.RESPUESTA_ADVERTENCIA, "No se encontraron equipos con ese código patrimonial.", null);
            }
            return new GenericResponse<>(Global.TIPO_DATOS, Global.RESPUESTA_OK, "Equipos encontrados exitosamente.", equipos);
        } catch (Exception e) {
            log.error("Error al buscar equipos por código patrimonial: {}", codigoPatrimonial, e);
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RESPUESTA_ERROR, "Error al buscar equipos: " + e.getMessage(), null);
        }
    }

    public GenericResponse<List<Equipo>> filtroFechaRevisionBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            List<Equipo> equipos = equipoRepository.findByFechaRevisionBetween(fechaInicio, fechaFin);
            if (equipos.isEmpty()) {
                log.info("No se encontraron equipos entre las fechas: {} y {}", fechaInicio, fechaFin);
                return new GenericResponse<>(Global.TIPO_ADVERTENCIA, Global.RESPUESTA_ADVERTENCIA, "No se encontraron equipos entre esas fechas.", null);
            }
            return new GenericResponse<>(Global.TIPO_DATOS, Global.RESPUESTA_OK, "Equipos encontrados exitosamente entre las fechas especificadas.", equipos);
        } catch (Exception e) {
            log.error("Error al buscar equipos entre las fechas: {} y {}: {}", fechaInicio, fechaFin, e);
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RESPUESTA_ERROR, "Error al buscar equipos entre esas fechas: " + e.getMessage(), null);
        }
    }
}
