package app.liusaprian.stori.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.liusaprian.stori.databinding.StoryItemBinding
import app.liusaprian.stori.network.response.Story
import com.bumptech.glide.Glide

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    private var listData = ArrayList<Story>()

    fun setData(newListData: List<Story>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
    }

    inner class StoryViewHolder(private val binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Story) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(data.photoUrl)
                    .into(photoIv)
                nameTv.text = data.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        StoryViewHolder(
            StoryItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount() = listData.size

}