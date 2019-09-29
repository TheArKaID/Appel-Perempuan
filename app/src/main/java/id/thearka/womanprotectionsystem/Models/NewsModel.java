package id.thearka.womanprotectionsystem.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsModel implements Parcelable {
    private String profil, nama, status, foto;

    public NewsModel(){

    }

    private NewsModel(Parcel in) {
        profil = in.readString();
        nama = in.readString();
        status = in.readString();
        foto = in.readString();
    }

    public static final Creator<NewsModel> CREATOR = new Creator<NewsModel>() {
        @Override
        public NewsModel createFromParcel(Parcel in) {
            return new NewsModel(in);
        }

        @Override
        public NewsModel[] newArray(int size) {
            return new NewsModel[size];
        }
    };

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(profil);
        dest.writeString(nama);
        dest.writeString(status);
        dest.writeString(foto);
    }
}
