package joalissongomes.dev.youtube.main.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import joalissongomes.dev.youtube.R
import joalissongomes.dev.youtube.common.util.formatTime
import joalissongomes.dev.youtube.common.util.videos
import joalissongomes.dev.youtube.databinding.ActivityMainBinding
import joalissongomes.dev.youtube.main.model.ListVideo
import joalissongomes.dev.youtube.main.model.Video
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var youtubePlayer: YoutubePlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videos = mutableListOf<Video>()
        videoAdapter = VideoAdapter(videos) { video ->
            showOverlayView(video)
        }

        findViewById<View>(R.id.view_layer).alpha = 0f

        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = videoAdapter

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        CoroutineScope(Dispatchers.IO).launch {
            val res = async { getVideos() }
            val listVideo = res.await()

            withContext(Dispatchers.Main) {
                listVideo?.let {
                    videos.clear()
                    videos.addAll(it.data)
                    videoAdapter.notifyDataSetChanged()
                    binding.motionContainer.removeView(binding.progressRecycler)
//                    binding.progressRecycler.visibility = View.GONE
                }
            }
        }

        findViewById<SeekBar>(R.id.seek_bar).setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser)
                    youtubePlayer.seek(progress.toLong())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        preparePlayer()
    }

    override fun onDestroy() {
        youtubePlayer.release()
        super.onDestroy()
    }

    override fun onPause() {
        youtubePlayer.pause()
        super.onPause()
    }

    private fun preparePlayer() {
        youtubePlayer = YoutubePlayer(this)

        youtubePlayer.youtubePlayerListener = object : YoutubePlayer.YoutubePlayerListener {
            override fun onPrepared(duration: Int) {}

            override fun onTrackTime(currentPosition: Long, percent: Long) {
                findViewById<SeekBar>(R.id.seek_bar).progress = percent.toInt()
                findViewById<TextView>(R.id.current_time).text = currentPosition.formatTime()

                Log.i("this:", currentPosition.formatTime())
            }
        }

        findViewById<SurfaceView>(R.id.surface_player).holder.addCallback(youtubePlayer)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_search -> {}
            R.id.main_user -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showOverlayView(video: Video) {
        findViewById<View>(R.id.view_layer).animate().apply {
            duration = 400
            alpha(0.5f)
        }

        binding.motionContainer.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
                println("Transition Trigger $triggerId $positive $progress")
            }

            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                println("Transition Started $startId $endId")
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                println("Transition Change $startId $endId $progress")

                if (progress > 0.5f)
                    findViewById<View>(R.id.view_layer).alpha = 1.0f - progress
                else
                    findViewById<View>(R.id.view_layer).alpha = 0.5f

            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                println("Transition Completed $currentId")
            }
        })

        findViewById<ImageView>(R.id.video_player).visibility = View.GONE
        youtubePlayer.setUrl(video.videoUrl)

        val detailAdapter = VideoDetailAdapter(videos())
        val rvSimilar = findViewById<RecyclerView>(R.id.rv_similar)
        rvSimilar.layoutManager = LinearLayoutManager(this)
        rvSimilar.adapter = detailAdapter

        findViewById<TextView>(R.id.content_channel).text = video.publisher.name
        Picasso.get().load(video.publisher.pictureProfileUrl)
            .into(findViewById<ImageView>(R.id.img_channel))

        detailAdapter.notifyDataSetChanged()
    }

    private fun getVideos(): ListVideo? {
        val client = OkHttpClient.Builder().build()

        val request =
            Request.Builder().get().url("https://tiagoaguiar.co/api/youtube-videos").build()

        return try {
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                GsonBuilder().create().fromJson(response.body()?.string(), ListVideo::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}


