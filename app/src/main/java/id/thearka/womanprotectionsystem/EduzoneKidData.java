package id.thearka.womanprotectionsystem;

import java.util.ArrayList;

import id.thearka.womanprotectionsystem.Models.EduzoneModel;

public class EduzoneKidData {
    public static String[][] data = new String[][]{
            {"SUARA ANAK PENYANDANG DISABILITAS", "https://www.kemenpppa.go.id/lib/uploads/feature_image/ed828-whatsapp-image-2019-04-11-at-12.24.37-pm.jpeg", "https://www.kemenpppa.go.id/index.php/page/read/30/2100/suara-anak-penyandang-disabilitas"},
            {"Rapat Koordinasi Nasional Unit Pengendalian Gratifikasi (Rakornas UPG)", "https://www.kemenpppa.go.id/lib/uploads/feature_image/cd92a-whatsapp-image-2018-11-20-at-15.17.57.jpeg", "https://www.kemenpppa.go.id/index.php/page/read/30/1968/rapat-koordinasi-nasional-unit-pengendalian-gratifikasi-rakornas-upg"},
            {"LINDUNGI ANAK DARI JARINGAN TERORISME DAN PAHAM RADIKALISME", "https://www.kemenpppa.go.id/lib/uploads/feature_image/7d1bc-whatsapp-image-2018-11-16-at-14.53.49.jpeg", "https://www.kemenpppa.go.id/index.php/page/read/30/1963/lindungi-anak-dari-jaringan-terorisme-dan-paham-radikalisme"},
            {"PERLINDUNGAN ANAK MELALUI PENDEKATAN BERBASIS SISTEM", "https://www.kemenpppa.go.id/lib/uploads/feature_image/750d2-whatsapp-image-2018-10-09-at-2.02.54-pm.jpeg", "https://www.kemenpppa.go.id/index.php/page/read/30/1891/perlindungan-anak-melalui-pendekatan-berbasis-sistem"},
            {"PEDOMAN PELAKSANAAN HARi ANAK NASIONAL (HAN) TAHUN 2018", "https://www.kemenpppa.go.id/lib/uploads/feature_image/dde6f-image-2018-07-11-at-16.29.48.jpeg", "https://www.kemenpppa.go.id/index.php/page/read/30/1782/pedoman-pelaksanaan-hari-anak-nasional-han-tahun-2018"}
    };

    public static ArrayList<EduzoneModel> getListData(){
        EduzoneModel eduzone = null;
        ArrayList<EduzoneModel> list = new ArrayList<>();
        for (String[] aData : data) {
            eduzone = new EduzoneModel();
            eduzone.setJudul(aData[0]);
            eduzone.setImage(aData[1]);
            eduzone.setUrl(aData[2]);

            list.add(eduzone);
        }

        return list;
    }
}
