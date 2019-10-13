package si.example.mybeenapp.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class Photo {
    public int albumId;
    public int id;
    public String title;
    public String url;
    public String thumbnailUrl;

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).into(view);
    }
}
