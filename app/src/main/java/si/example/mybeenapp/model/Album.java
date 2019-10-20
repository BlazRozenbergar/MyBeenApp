package si.example.mybeenapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Album implements Parcelable {
    public int userId;
    public int id;
    public String title;
    public Photo photo;

    protected Album(Parcel in) {
        userId = in.readInt();
        id = in.readInt();
        title = in.readString();
        photo = in.readParcelable(Photo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeParcelable(photo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };
}
