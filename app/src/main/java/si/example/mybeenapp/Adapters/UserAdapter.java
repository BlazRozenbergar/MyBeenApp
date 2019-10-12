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
import si.example.mybeenapp.databinding.RvUserListItemBinding;
import si.example.mybeenapp.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> mOriginalList;
    private OnClickCallback mOnClickCallback;

    public interface OnClickCallback {
        void onClick(User user);
    }

    public UserAdapter(OnClickCallback onClickCallback) {
        this.mOnClickCallback = onClickCallback;
    }

    public void setList(List<User> list) {
        if (this.mOriginalList == null) {
            this.mOriginalList = list;
            notifyItemRangeInserted(0, mOriginalList.size());
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
                    return mOriginalList.get(oldItemPosition).id ==
                            list.get(newItemPosition).id;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    User newItem = list.get(newItemPosition);
                    User oldItem = mOriginalList.get(oldItemPosition);
                    return newItem.id == oldItem.id
                            && Objects.equals(newItem.name, oldItem.name)
                            && Objects.equals(newItem.username, oldItem.username);
                }
            });
            this.mOriginalList = list;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvUserListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_user_list_item, parent, false);
        binding.setClickCallback(mOnClickCallback);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setItem(mOriginalList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mOriginalList != null ? mOriginalList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final RvUserListItemBinding binding;

        private ViewHolder(RvUserListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
