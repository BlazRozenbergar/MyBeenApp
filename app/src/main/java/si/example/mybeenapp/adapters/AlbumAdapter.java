package si.example.mybeenapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import si.example.mybeenapp.R;
import si.example.mybeenapp.databinding.RvAlbumListItemBinding;
import si.example.mybeenapp.model.Album;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    private List<Album> mOriginalList;
    private AlbumAdapter.OnClickCallback mOnClickCallback;

    public interface OnClickCallback {
        void onClick(Album album);
    }

    public AlbumAdapter(AlbumAdapter.OnClickCallback onClickCallback) {
        this.mOnClickCallback = onClickCallback;
    }

    public void setList(final List<Album> list) {
        if (this.mOriginalList == null) {
            this.mOriginalList = list;
            if (mOriginalList != null) {
                notifyItemRangeInserted(0, mOriginalList.size());
            }
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mOriginalList.size();
                }

                @Override
                public int getNewListSize() {
                    return list != null ? list.size() : 0;
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    Album newItem = list.get(newItemPosition);
                    Album oldItem = mOriginalList.get(oldItemPosition);
                    return newItem.id == oldItem.id
                            && Objects.equals(newItem.photo, oldItem.photo);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Album newItem = list.get(newItemPosition);
                    Album oldItem = mOriginalList.get(oldItemPosition);
                    return newItem.id == oldItem.id && Objects.equals(newItem.title, oldItem.title)
                            && (newItem.photo != null && oldItem.photo != null && newItem.photo.id == oldItem.photo.id);
                }
            });
            this.mOriginalList = list;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvAlbumListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_album_list_item, parent, false);
        binding.setClickCallback(mOnClickCallback);
        return new AlbumViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        holder.binding.setItem(mOriginalList.get(position));
        if (mOriginalList.get(position).photo != null) {
            holder.binding.setThumbnail(mOriginalList.get(position).photo.thumbnailUrl);
        } else {
            holder.binding.setThumbnail(null);
        }
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mOriginalList != null ? mOriginalList.size() : 0;
    }

    static class AlbumViewHolder extends RecyclerView.ViewHolder {
        final RvAlbumListItemBinding binding;

        private AlbumViewHolder(RvAlbumListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
