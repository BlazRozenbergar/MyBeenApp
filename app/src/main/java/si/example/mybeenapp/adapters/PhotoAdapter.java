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
import si.example.mybeenapp.databinding.PhotoItemBinding;
import si.example.mybeenapp.model.Photo;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private List<Photo> mOriginalList;
    private PhotoAdapter.OnClickCallback mOnClickCallback;

    public interface OnClickCallback {
        void onClick(Photo album);
    }

    public PhotoAdapter(PhotoAdapter.OnClickCallback onClickCallback) {
        this.mOnClickCallback = onClickCallback;
    }

    public void setList(List<Photo> list) {
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
                    return mOriginalList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mOriginalList.get(oldItemPosition).id == list.get(newItemPosition).id;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Photo newItem = list.get(newItemPosition);
                    Photo oldItem = mOriginalList.get(oldItemPosition);
                    return newItem.id == oldItem.id && Objects.equals(newItem.title, oldItem.title);
                }
            });
            this.mOriginalList = list;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PhotoItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.photo_item, parent, false);
        binding.setClickCallback(mOnClickCallback);
        return new PhotoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        holder.binding.setItem(mOriginalList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mOriginalList != null ? mOriginalList.size() : 0;
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        final PhotoItemBinding binding;

        private PhotoViewHolder(PhotoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
