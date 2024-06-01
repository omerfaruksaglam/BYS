package com.baskent.Nursapp.Report;

import com.baskent.Nursapp.DataAccessLayer.*;
import com.baskent.Nursapp.Entity.Bolum;
import com.baskent.Nursapp.Entity.SecondGrader;
import com.baskent.Nursapp.Entity.SecondGraderSinif.SecondGraderSinif;
import com.baskent.Nursapp.Entity.Skills;
import com.baskent.Nursapp.Entity.Subskills;
import com.baskent.Nursapp.Teacher.DTO.SubSkillwithGrades;
import com.baskent.Nursapp.Teacher.Service.TeacherService;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.BaseDirection;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PDFService {
    private final BolumRepository bolumRepository;
    private final TeacherService teacherService;
    private final UserRepository userRepository;
    private final SkillsRespository skillRepository;
    private final SubSkillRepository subSkillRepository;
    public static final String LOGO = "src/main/resources/img.png";
    public static SecondGraderRepository secondGraderRepository;
    private final SecondGraderSinifRepository secondGraderSinifRepository;

    public PDFService(BolumRepository bolumRepository, TeacherService teacherService, UserRepository userRepository, SkillsRespository skillRepository, SubSkillRepository subSkillRepository, SecondGraderSinifRepository secondGraderSinifRepository,SecondGraderRepository secondGraderRepository) throws MalformedURLException {
        this.bolumRepository = bolumRepository;
        this.teacherService = teacherService;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.subSkillRepository = subSkillRepository;
        this.secondGraderSinifRepository = secondGraderSinifRepository;
        this.secondGraderRepository = secondGraderRepository;
    }

    public byte[] createPDF(Long studentId, Long bolumId) throws IOException {
        Image BaskentImage = new Image(ImageDataFactory.create(LOGO)).setWidth(30);

        Bolum bolum = bolumRepository.getBolum(bolumId);
        SecondGrader secondGrader;
        String secondGraderName = "NoN";

        List<SecondGraderSinif> siniflar = new ArrayList<>();
        if(bolum.getSecondGraderId() != 0){
            siniflar = secondGraderSinifRepository.getAllSecondGraderSinifFromId(bolum.getSecondGraderId());
            secondGrader = secondGraderRepository.getSecondGraderFromId(bolum.getSecondGraderId());
            secondGraderName = secondGrader.getSecondGraderName().substring(0,1).toUpperCase() + "G";
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);

        PdfDocument pdf = new PdfDocument(new PdfWriter(byteArrayOutputStream));
        Document document = new Document(pdf, PageSize.A4.rotate());
        Color baskentBlue = new DeviceRgb(224, 234, 244);
        Color renk = new DeviceRgb(217, 217, 217);
        Color headergray = new DeviceRgb(191, 191, 191);

        List<Color> colors = new ArrayList<>();
        colors.add(new DeviceRgb(225, 255, 255));
        colors.add(new DeviceRgb(255, 225, 204));
        colors.add(new DeviceRgb(226, 239, 217));
        colors.add(new DeviceRgb(251, 229, 213));
        colors.add(new DeviceRgb(224, 234, 244));
        int o = 0;
        int fontSize = 7;

        // Türkçe karakter destekli font yükle
        FontProgram fontProgram = FontProgramFactory.createFont("src/main/resources/fonts/TR McLaughlin Bold.TTF");
        PdfFont font = PdfFontFactory.createFont(fontProgram, "Identity-H", true);

        // Başlık
        Paragraph title = new Paragraph();
        title.add(BaskentImage);
        title.add("\nBaşkent Üniversitesi\nSağlık Bilimleri Fakültesi\nHemşirelik Bölümü")
                .setFont(font)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.LEFT);
        document.add(title);

        Paragraph title2 = new Paragraph();
        title2.add("Öğrenci Beceri Karnesi")
                .setFont(font)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(3);
        document.add(title2);

        // Öğrenci Bilgileri
        Table table1 = new Table(UnitValue.createPercentArray(new float[]{4, 3, 2})).useAllAvailableWidth().setFontSize(fontSize);
        table1.addCell(new Cell().add(new Paragraph("Öğrenci Adı Soyadı: " + userRepository.getKullaniciByIdandBolum(studentId, bolumId).getName() +" "+ userRepository.getKullaniciByIdandBolum(studentId, bolumId).getSurname()).setFont(font)));
        table1.addCell(new Cell().add(new Paragraph("Öğrenci Numarası: " + studentId).setFont(font)));
        table1.addCell(new Cell().add(new Paragraph("Yarıyıl : .............. ").setFont(font)));
        document.add(table1);

        Table table2 = new Table(UnitValue.createPercentArray(new float[]{1, 3})).useAllAvailableWidth().setFontSize(fontSize);
        table2.addCell(new Cell().add(new Paragraph("Uygulama Puanları :  ").setFont(font)));
        table2.addCell(new Cell().add(new Paragraph(" ").setFont(font)));
        document.add(table2);

        Table table3 = new Table(UnitValue.createPercentArray(new float[]{2, 1, 3})).useAllAvailableWidth().setFontSize(fontSize).setMarginBottom(10);
        table3.addCell(new Cell().add(new Paragraph("0 puan: Hiç karşılaşılmadı\n" +
                "1 puan: Tamamen yetersiz.\n" +
                "2 puan: Yetersiz, geliştirilebilir\n" +
                "3 puan: Yeterli, ama eksikleri var.\n" +
                "4 puan: Tamamen yeterli işlemi kendi başına uygulayabilir.").setFont(font)));
        table3.addCell(new Cell().add(new Paragraph("KB: Kendi başına\n" +
                "ÖEG: Öğretim Elemanı gözetiminde\n" +
                "HG: Hemşire Gözetiminde ").setFont(font)));
        table3.addCell(new Cell().add(new Paragraph("Beceri, laboratuvarda yapılıp gözlemlendiyse kırmızı renkli kalem ile puanlayınız,\n" +
                "Beceri, hastane/diğer uygulama alanlarında yapıldıysa mavi renkli kalem ile puanlayınız. ").setFont(font)));
        document.add(table3);



        // Beceri Tablosu
        

        //öğrenci not verebilir mi cart curt nested table.

        List<Float> tableColumns = new ArrayList<>();

        List<Integer> counts = new ArrayList<>();
        for(int i = 0; i < bolum.getYilSayisi(); i++) {
            int count = 1;
            if (bolum.getOgrenciNotVerebilirmi()) {
                count++;
            }
            //tableColumns.add(1f);
            for(SecondGraderSinif sinif : siniflar){
                if(sinif.getSinifNumarasi() == i+1){
                    count++;
                }
            }
            counts.add(count);
        }
        tableColumns.add(1f);
        for(int i = 0; i < counts.size(); i++){
            for(int j = 0; j < counts.get(i); j++){
                tableColumns.add(1f/counts.get(i));

            }
        }


        Float[] floatArray2 = tableColumns.toArray(new Float[0]);
        float[] columnWidths2 = new float[floatArray2.length];

        for (int i = 0; i < floatArray2.length; i++) {
            columnWidths2[i] = floatArray2[i]; // Autoboxing ile Float'tan float'a otomatik dönüşüm yapılır.
        }


        //Table header = new Table(UnitValue.createPercentArray(baslikColumnWidths)).useAllAvailableWidth().setFontSize(fontSize).setBackgroundColor(headergray);
        Table table = new Table(UnitValue.createPercentArray(columnWidths2)).useAllAvailableWidth().setFontSize(fontSize);


        Table header = new Table(UnitValue.createPercentArray(columnWidths2)).setFontSize(fontSize).setBackgroundColor(headergray);

        header.addCell(new Cell(2,1).add(new Paragraph("FONKSİYONEL SAĞLIK ÖRÜNTÜLERİ DOĞRULTUSUNDA YAPILAN UYGULAMALAR/BECERİLER")).setBorderBottom(Border.NO_BORDER));
        header.setBorderBottom(Border.NO_BORDER);

        // Tablo Başlıkları
        for (int i = 0; i < bolum.getYilSayisi(); i++) {
            int count = 1;
            if (bolum.getOgrenciNotVerebilirmi()) {
                count++;
            }
            for (SecondGraderSinif sinif : siniflar) {
                if (sinif.getSinifNumarasi() == i + 1) {
                    count++;
                }
            }

            // Üst hücre
            //Cell cellClass = new Cell(1, 3);
            header.addCell(new Cell(1, count).add(new Paragraph((i + 1) + ". Sınıf").setTextAlignment(TextAlignment.CENTER)).setFont(font));
            //header.addCell(cellClass);

            // Alt satır hücreleri

        }
        for (int i = 0; i < bolum.getYilSayisi(); i++) {
            int count = 1;
            if (bolum.getOgrenciNotVerebilirmi()) {
                count++;
            }
            for (SecondGraderSinif sinif : siniflar) {
                if (sinif.getSinifNumarasi() == i + 1) {
                    count++;
                }
            }
            if (bolum.getOgrenciNotVerebilirmi()) {
                header.addCell(new Cell(1, 1).add(new Paragraph("KB").setTextAlignment(TextAlignment.CENTER).setBorderBottom(Border.NO_BORDER)));
            }
            header.addCell(new Cell(1, 1).add(new Paragraph("ÖEG").setTextAlignment(TextAlignment.CENTER).setBorderBottom(Border.NO_BORDER)));
            for (SecondGraderSinif sinif : siniflar) {
                if (sinif.getSinifNumarasi() == i + 1) {
                    header.addCell(new Cell(1, 1).add(new Paragraph(secondGraderName).setTextAlignment(TextAlignment.CENTER).setBorderBottom(Border.NO_BORDER)));
                }
            }
        }

        /*for(int i = 0; i < bolum.getYilSayisi(); i++) {

            int count = 1;
            if(bolum.getOgrenciNotVerebilirmi()){
                //header.addCell(cellKB);
                count++;
            }
            for(SecondGraderSinif sinif : siniflar){
                if(sinif.getSinifNumarasi() == i+1){
                   count++;
                }
            }
            // header.addCell(new Cell().add(new Paragraph((i+1) + ". Sınıf ").setFont(font)));
            Cell cellClass = new Cell(1, count);  // 1 satır, 3 sütun kaplayacak
            cellClass.add(new Paragraph("Sınıf"));
            header.addCell(cellClass);
            // Alt satır: üç ayrı hücrede "KB", "ÖEG", "HG"
            Cell cellKB = new Cell().add(new Paragraph("KB"));

            Cell cellOEG = new Cell().add(new Paragraph("ÖEG"));
            Cell cellHG = new Cell().add(new Paragraph("HG"));

            // Hücreleri tabloya ekliyoruz
            if(bolum.getOgrenciNotVerebilirmi()){
                header.addCell(cellKB);
            }
            header.addCell(cellOEG);
            for(SecondGraderSinif sinif : siniflar){
                if(sinif.getSinifNumarasi() == i+1){
                    header.addCell(cellHG);
                }
            }
            Cell cellClass = new Cell(1, 3);  // 1 satır, 3 sütun kaplayacak
            cellClass.add(new Paragraph("Sınıf"));
            header.addCell(cellClass);
            // Alt satır: üç ayrı hücrede "KB", "ÖEG", "HG"
            Cell cellKB = new Cell().add(new Paragraph("KB"));

            Cell cellOEG = new Cell().add(new Paragraph("ÖEG"));
            Cell cellHG = new Cell().add(new Paragraph("HG"));
            header.addCell(cellKB);
            header.addCell(cellOEG);
            header.addCell(cellHG);
            //header.addCell(new Cell(1, 3).add(new Paragraph(i+1+".sınıf")));
            //header.addCell(new Cell(2,2).add(new Paragraph("kb")));
            //header.addCell(new Cell(2,2).add(new Paragraph("kb")));
            //header.addCell(new Cell(2,2).add(new Paragraph("hg")));
        }
        */

        //table.addHeaderCell(new Cell().add(new Paragraph("1. Sınıf").setFont(font)));
        //table.addHeaderCell(new Cell().add(new Paragraph("2. Sınıf").setFont(font)));
        //table.addHeaderCell(new Cell().add(new Paragraph("3. Sınıf").setFont(font)));
        //table.addHeaderCell(new Cell().add(new Paragraph("4. Sınıf").setFont(font)));

        // Beceri Verileri (Örnek)
        List<Skills> skillsList = skillRepository.getAllSkillsFromBolumId(bolumId);


        for (Skills skill : skillsList) {
            o++;
            Color temp = colors.get(o % 5);
            table.addCell(new Cell().add(new Paragraph(skill.getNamee()).setFont(font)).setBackgroundColor(renk));
            for(int l = 0; l < tableColumns.size()-1; l++){
                table.addCell(new Cell().add(new Paragraph(" ").setFont(font)).setBackgroundColor(renk));
            }
            List<List<SubSkillwithGrades>> aq = new ArrayList<>();

            for(int j = 0; j < bolum.getYilSayisi(); j++){
                aq.add(teacherService.getSubskillWithGrades(studentId.intValue(), bolumId.intValue(), j+1, skill.getSkillId().intValue()));
            }



            for (Subskills subskill : subSkillRepository.getAllSubSkillsFromSkillId(skill.getSkillId())) {
                table.addCell(new Cell().add(new Paragraph(subskill.getName()).setFont(font)).setBackgroundColor(temp));



                for(int i = 0; i < bolum.getYilSayisi(); i++) {
                    if(aq.get(i).get(subskill.getStepNo()-1).getStudentGrade() != null && bolum.getOgrenciNotVerebilirmi()){

                           table.addCell(new Cell().add(new Paragraph(aq.get(i).get(subskill.getStepNo()-1).getStudentGrade().toString()).setFont(font).setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(temp));
                        //table.addCell(table2);
                    }else{

                            if(bolum.getOgrenciNotVerebilirmi()){
                                table.addCell(new Cell().add(new Paragraph(" " ).setFont(font).setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(temp));
                            }

                    }
                    if(aq.get(i).get(subskill.getStepNo()-1).getTeacherGrade() != null){

                        table.addCell(new Cell().add(new Paragraph(aq.get(i).get(subskill.getStepNo()-1).getTeacherGrade().toString()).setFont(font).setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(temp));
                        //table.addCell(table2);
                    }else{

                        table.addCell(new Cell().add(new Paragraph(" " ).setFont(font).setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(temp));

                    }
                    if(aq.get(i).get(subskill.getStepNo()-1).getNurseGrade() != null && bolum.getSecondGraderId() != 0){

                        table.addCell(new Cell().add(new Paragraph(aq.get(i).get(subskill.getStepNo()-1).getNurseGrade().toString()).setFont(font).setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(temp));
                        //table.addCell(table2);
                    }else {
                        for (SecondGraderSinif sinif : siniflar) {
                            if (sinif.getSinifNumarasi() == i + 1) {
                                table.addCell(new Cell().add(new Paragraph(" " ).setFont(font).setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(temp));
                            }
                        }

                    }
                }

            }


        }
        document.add(header);
        document.add(table);
        Table notlar = new Table(UnitValue.createPercentArray(new float[] {1})).useAllAvailableWidth().setHeight(50).addCell(new Cell(3, columnWidths2.length).add(new Paragraph("Notlar:").setFontSize(fontSize)));
        document.add(notlar);
        document.close();
        return byteArrayOutputStream.toByteArray();
    }
}
