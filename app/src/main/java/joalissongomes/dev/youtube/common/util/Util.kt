package joalissongomes.dev.youtube.common.util

import android.util.Log
import joalissongomes.dev.youtube.main.model.Video
import joalissongomes.dev.youtube.main.model.VideoBuilder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.formatted(): String =
    SimpleDateFormat("d MMM yyyy", Locale("pt", "BR")).format(this)

fun Long.formatTime(): String {
    val minutes: Long = this / 1000 / 60
    val seconds: Long = this / 1000 % 60

    return String.format("%02d:%02d", minutes, seconds)
}


fun String.toDate(): Date =
    SimpleDateFormat("yyyy-mm-dd", Locale("pt", "BR")).parse(this)

//DSL
fun video(block: VideoBuilder.() -> Unit): Video =
    VideoBuilder().apply(block).build()

fun videos(): List<Video> {
    return arrayListOf(
        video {
            id = "UVpKBHO2fMg"
            thumbnailUrl = "https://img.youtube.com/vi/UVpKBHO2fMg/maxresdefault.jpg"
            title = "Entrevista com Marlon Wayans | The Noite (14/08/19)"
            publishedAt = "2019-08-15".toDate()
            viewsCount = 742497
            viewsCountLabel = "7M"
            duration = 1886
            videoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
            publisher = publisher {
                id = "sbtthenoite"
                name = "The Noite com Danilo Gentili"
                pictureProfileUrl =
                    "https://yt3.ggpht.com/a/AGF-l7_3BYlSlp94WOjGe1UECUCdb73qRJVFH_t9Tw=s48-c-k-c0xffffffff-no-rj-mo"
            }
        },
        video {
            id = "PlYUZU0H5go"
            thumbnailUrl = "https://img.youtube.com/vi/cuau8E6t2QU/maxresdefault.jpg"
            title = "Relembrando Steve Jobs"
            viewsCount = 1703
            publishedAt = "2014-08-15".toDate()
            viewsCountLabel = "1k"
            duration = 194
            videoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
            publisher = publisher {
                id = "UCrWWMZ6GVOM5zqYAUI44XXg"
                name = "Tiago Aguiar"
                pictureProfileUrl =
                    "https://yt3.ggpht.com/ytc/AKedOLT2VtZ3n30tTpDyjAoZGl44EfHhajN1Zy5LYm3iiA=s88-c-k-c0x00ffffff-no-rj"
            }
        },
    )
}