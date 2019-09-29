package id.thearka.womanprotectionsystem;

import java.util.ArrayList;

import id.thearka.womanprotectionsystem.Models.NewsModel;

public class NewsData {
    public static String[][] data = new String[][]{
            {"Arifia", "https://firebasestorage.googleapis.com/v0/b/appel-apperempuan.appspot.com/o/ruu-pks.jpeg?alt=media", "Ribut soal RUU PKS 1.", "https://firebasestorage.googleapis.com/v0/b/appel-apperempuan.appspot.com/o/DSC_0010.JPG?alt=media"},
            {"Kasastra R", "https://firebasestorage.googleapis.com/v0/b/appel-apperempuan.appspot.com/o/DSC_0010.JPG?alt=media", "Ribut soal RUU PKS 2.", "https://firebasestorage.googleapis.com/v0/b/appel-apperempuan.appspot.com/o/ruu-pks.jpeg?alt=media"},
            {"Arifia Kasastra R", "https://firebasestorage.googleapis.com/v0/b/appel-apperempuan.appspot.com/o/DSC_0010.JPG?alt=media", "Ribut soal RUU PKS 3.", ""},
            {"Arifia", "https://firebasestorage.googleapis.com/v0/b/appel-apperempuan.appspot.com/o/ruu-pks.jpeg?alt=media", "Ribut soal RUU PKS 4.", "https://firebasestorage.googleapis.com/v0/b/appel-apperempuan.appspot.com/o/DSC_0010.JPG?alt=media"},
            {"Kasastra R", "https://firebasestorage.googleapis.com/v0/b/appel-apperempuan.appspot.com/o/DSC_0010.JPG?alt=media", "Ribut soal RUU PKS 5.", "https://firebasestorage.googleapis.com/v0/b/appel-apperempuan.appspot.com/o/ruu-pks.jpeg?alt=media"},
            {"Arifia Kasastra R", "https://firebasestorage.googleapis.com/v0/b/appel-apperempuan.appspot.com/o/DSC_0010.JPG?alt=media", "Ribut soal RUU PKS 6.", ""},
            {"Arifia", "https://firebasestorage.googleapis.com/v0/b/appel-apperempuan.appspot.com/o/ruu-pks.jpeg?alt=media", "Ribut soal RUU PKS 7.", "https://firebasestorage.googleapis.com/v0/b/appel-apperempuan.appspot.com/o/DSC_0010.JPG?alt=media"},
            {"Kasastra R", "https://firebasestorage.googleapis.com/v0/b/appel-apperempuan.appspot.com/o/DSC_0010.JPG?alt=media", "Ribut soal RUU PKS 8.", "https://firebasestorage.googleapis.com/v0/b/appel-apperempuan.appspot.com/o/ruu-pks.jpeg?alt=media"},
            {"Arifia Kasastra R", "https://firebasestorage.googleapis.com/v0/b/appel-apperempuan.appspot.com/o/DSC_0010.JPG?alt=media", "Ribut soal RUU PKS 9.", ""},
    };

    public static ArrayList<NewsModel> getListData(){
        NewsModel newsData;
        ArrayList<NewsModel> list = new ArrayList<>();
        for (String[] aData : data) {
            newsData = new NewsModel();
            newsData.setNama(aData[0]);
            newsData.setProfil(aData[1]);
            newsData.setStatus(aData[2]);
            newsData.setFoto(aData[3]);

            list.add(newsData);
        }

        return list;
    }
}
