package id.thearka.womanprotectionsystem;

import java.util.ArrayList;

import id.thearka.womanprotectionsystem.Models.EduzoneModel;

public class EduzoneHumanData {
    public static String[][] data = new String[][]{
            {"Jenguk Aktivis di Rutan Mako Brimob, Komnas HAM Sarankan Presiden Jokowi ke Papua", "https://www.komnasham.go.id/files/20190924-jenguk-aktivis-di-rutan-mako-brimob-$UORFD.jpeg", "https://www.komnasham.go.id/index.php/news/2019/09/24/1169/jenguk-aktivis-di-rutan-mako-brimob-komnas-ham-sarankan-presiden-jokowi-ke-papua.html?utm_source=beranda"},
            {"Demi Eksistensi Pendidikan Tunanetra, Komnas HAM Mediasi Kemensos-BRSPDSN", "https://www.komnasham.go.id/files/20190923-demi-eksistensi-pendidikan-tunanetra-$88K34RC.jpeg", "https://www.komnasham.go.id/index.php/news/2019/09/23/1167/demi-eksistensi-pendidikan-tunanetra-komnas-ham-mediasi-kemensos-brspdsn.html?utm_source=beranda"},
            {"Komnas HAM Bantu Penyelesaian Sengketa Tanah Masyarakat Adat Tukan", "https://www.komnasham.go.id/files/20190923-komnas-ham-bantu-penyelesaian--$FR2FOQ.jpeg", "https://www.komnasham.go.id/index.php/news/2019/09/23/1166/komnas-ham-bantu-penyelesaian-sengketa-tanah-masyarakat-adat-tukan.html?utm_source=beranda"},
            {"Komnas HAM Minta Pengesahan RKUHP Ditunda", "https://www.komnasham.go.id/files/20190920-komnas-ham-minta-pengesahan-rkuhp-$PUZ8A.jpg", "https://www.komnasham.go.id/index.php/news/2019/09/20/1165/komnas-ham-minta-pengesahan-rkuhp-ditunda.html?utm_source=beranda"},
            {"Penghormatan Terakhir Komnas HAM Bagi H. S Dillon", "https://www.komnasham.go.id/files/20190920-penghormatan-terakhir-komnas-ham-$4D.jpeg", "https://www.komnasham.go.id/index.php/news/2019/09/20/1164/penghormatan-terakhir-komnas-ham-bagi-h-s-dillon.html?utm_source=beranda"}
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
