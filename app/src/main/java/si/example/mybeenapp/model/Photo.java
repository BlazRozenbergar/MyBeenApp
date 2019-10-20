package si.example.mybeenapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.internal.CircularBorderDrawable;

public class Photo implements Parcelable {
    public int albumId;
    public int id;
    public String title;
    public String url;
    public String thumbnailUrl;

    protected Photo(Parcel in) {
        albumId = in.readInt();
        id = in.readInt();
        title = in.readString();
        url = in.readString();
        thumbnailUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(albumId);
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(thumbnailUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).into(view);
    }

    @BindingAdapter("imageRoundedCornersUrl")
    public static void loadRoundedCornersImage(ImageView view, String url) {
        RequestOptions options = new RequestOptions();
        options = options.transform(new CenterCrop(), new RoundedCorners(16));
        Glide.with(view.getContext()).load(url).apply(options).into(view);
        //Glide.with(view.getContext()).load(url).circleCrop().into(view);
    }
}
