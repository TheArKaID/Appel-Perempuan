package id.thearka.womanprotectionsystem.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class EduzoneModel implements Parcelable {

    private String judul, image, url;

    public EduzoneModel(Parcel in) {
        judul = in.readString();
        image = in.readString();
        url = in.readString();
    }
    public EduzoneModel() {

    }

    public static final Creator<EduzoneModel> CREATOR = new Creator<EduzoneModel>() {
        @Override
        public EduzoneModel createFromParcel(Parcel in) {
            return new EduzoneModel(in);
        }

        @Override
        public EduzoneModel[] newArray(int size) {
            return new EduzoneModel[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.image);
        dest.writeString(this.judul);
        dest.writeString(this.url);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
