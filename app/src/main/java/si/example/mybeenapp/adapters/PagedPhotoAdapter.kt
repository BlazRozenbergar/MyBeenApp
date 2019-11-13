package si.example.mybeenapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import si.example.mybeenapp.R
import si.example.mybeenapp.databinding.PhotoItemBinding
import si.example.mybeenapp.model.Photo

class PagedPhotoAdapter(clickCallback: OnClickCallback) : PagedListAdapter<Photo, PagedPhotoAdapter.PagedPhotoViewHolder>(PhotoDiffCallback) {
    private val mClickCallback: OnClickCallback = clickCallback

    interface OnClickCallback {
        fun onClick(photo: Photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagedPhotoViewHolder {
        return PagedPhotoViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.photo_item, parent, false))
    }

    override fun onBindViewHolder(holder: PagedPhotoViewHolder, position: Int) {
        holder.binding.item = getItem(position)
        holder.binding.clickCallback = mClickCallback
    }

    companion object {
        val PhotoDiffCallback = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return newItem.id == oldItem.id && newItem.title == oldItem.title
            }
        }
    }

    class PagedPhotoViewHolder constructor(val binding: PhotoItemBinding) : RecyclerView.ViewHolder(binding.root)
}