package joalissongomes.dev.youtube.main.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import joalissongomes.dev.youtube.R
import joalissongomes.dev.youtube.common.util.formatted
import joalissongomes.dev.youtube.main.model.Video

class VideoAdapter(private val videos: List<Video>, val onClick: (Video) -> Unit) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder =
        VideoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_video, parent, false)
        )

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videos[position])
    }

    override fun getItemCount(): Int = videos.size

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(video: Video) {
            with(itemView) {
                setOnClickListener {
                    onClick.invoke(video)
                }

                Picasso.get().load(video.thumbnailUrl)
                    .into(findViewById<ImageView>(R.id.video_thumbnail))


                Picasso.get().load(video.publisher.pictureProfileUrl)
                    .into(findViewById<ImageView>(R.id.video_author))

                val imageView = findViewById<ImageView>(R.id.video_author)
                val imageUrl = video.publisher.pictureProfileUrl

                Picasso.get().load(imageUrl).into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        val circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(resources, bitmap)
                        circularBitmapDrawable.isCircular = true
                        imageView.setImageDrawable(circularBitmapDrawable)
                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                })

                findViewById<TextView>(R.id.video_title).text = video.title
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