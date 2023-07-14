package joalissongomes.dev.youtube.main.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import joalissongomes.dev.youtube.R
import joalissongomes.dev.youtube.common.util.formatted
import joalissongomes.dev.youtube.main.model.Video

class VideoDetailAdapter(private val videos: List<Video>) :
    RecyclerView.Adapter<VideoDetailAdapter.VideoDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoDetailViewHolder =
        VideoDetailViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.video_detail_list_item_video, parent, false)
        )

    override fun onBindViewHolder(holder: VideoDetailViewHolder, position: Int) {
        holder.bind(videos[position])
    }

    override fun getItemCount(): Int = videos.size

    inner class VideoDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(video: Video) {
            with(itemView) {

                Picasso.get().load(video.thumbnailUrl)
                    .into(findViewById<ImageView>(R.id.video_thumbnail))

                findViewById<TextView>(R.id.video_title).text = video.title

                Log.i("Testando", "$video")

                findViewById<TextView>(R.id.video_info).text = context.getString(
                    R.string.info,
                    video.publisher.name,
                    video.viewsCountLabel,
                    video.publishedAt.formatted()
                )
            }
        }
    }
}