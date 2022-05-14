package app.liusaprian.stori.ui.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import app.liusaprian.stori.databinding.StoryItemBinding
import app.liusaprian.stori.network.response.Story
import app.liusaprian.stori.ui.detail.DetailActivity
import com.bumptech.glide.Glide

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    private var listData = ArrayList<Story>()

    fun setData(newListData: List<Story>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    inner class StoryViewHolder(private val binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Story) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(data.photoUrl)
                    .into(photoIv)
                photoIv.contentDescription = data.name + " photo"
                nameTv.text = data.name
                itemView.setOnClickListener {
                    val toDetail = Intent(itemView.context, DetailActivity::class.java)
                    toDetail.putExtra(DetailActivity.STORY_DETAIL, data)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(photoIv, "photo"),
                            Pair(nameTv, "name")
                        )

                    itemView.context.startActivity(toDetail, optionsCompat.toBundle())
                }
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