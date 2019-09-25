package id.thearka.womanprotectionsystem;

import java.util.ArrayList;

import id.thearka.womanprotectionsystem.Models.EduzoneModel;

public class EduzoneData {
    public static String[][] data = new String[][]{
            {"Pernyataan Sikap Komnas Perempuan terhadap Rencana Pengesahan Rancangan Undang-Undang Kitab Undang-Undang Hukum Pidana (RUU KUHP) “Tunda Pengesahan: (Jakarta, 20 September 2019)", "https://www.komnasperempuan.go.id/file/post_image/Lky3ws_post_Image%20Siaran%20Pers.JPG", "https://www.komnasperempuan.go.id/read-news-pernyataan-sikap-komnas-perempuan-terhadap-rencana-pengesahan-rancangan-undang-undang-kitab-undang-undang-hukum-pidana-ruu-kuhp-tunda-pengesahan-jakarta-20-september-2019"},
            {"Siaran Pers Komnas Perempuan “Stop Penyiksaan, Tegakkan Keadilan, Tangguhkan Penahanan Ibu Hamil” (11 September 2019)", "https://www.komnasperempuan.go.id/file/post_image/NSakeN_post_Image%20Siaran%20Pers.JPG", "https://www.komnasperempuan.go.id/read-news-siaran-pers-komnas-perempuan-stop-penyiksaan-tegakkan-keadilan-tangguhkan-penahanan-ibu-hamil-11-september-2019"},
            {"Pengumuman 50 Besar Calon Anggota Komnas Perempuan 2020 - 2024.", "https://www.komnasperempuan.go.id/file/images/noimage.png", "https://www.komnasperempuan.go.id/read-news-pengumuman-50-besar-calon-anggota-komnas-perempuan-2020-2024"},
            {"Siaran Pers Komnas Perempuan Merespon Konflik Lahan di Batanghari Jambi (Jakarta, 11 September 2019)", "https://www.komnasperempuan.go.id/file/post_image/MaiLcT_post_Image%20Siaran%20Pers.JPG", "https://www.komnasperempuan.go.id/read-news-siaran-pers-komnas-perempuan-merespon-konflik-lahan-di-batanghari-jambi-jakarta-11-september-2019"},
            {"Siaran Pers Komnas Perempuan Merespon Situasi di Papua: Kembalikan Rasa Aman yang Sejati bagi Masyarakat Papua dengan Mengedepankan Martabat dan Tanpa Kekerasan Jakarta, 3 September 2019", "https://www.komnasperempuan.go.id/file/post_image/aTOu6e_post_Image%20Siaran%20Pers.JPG", "https://www.komnasperempuan.go.id/read-news-siaran-pers-komnas-perempuan-merespon-situasi-di-papua-kembalikan-rasa-aman-yang-sejati-bagi-masyarakat-papua-dengan-mengedepankan-martabat-dan-tanpa-kekerasan-jakarta-3-september-2019"}
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
