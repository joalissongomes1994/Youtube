package joalissongomes.dev.youtube.main.model

import java.util.Date

class VideoBuilder {
    var id: String = ""
    var thumbnailUrl: String = ""
    var title: String = ""
    var publishedAt: Date = Date()
    var viewsCount: Long = 0
    var viewsCountLabel: String = ""
    var duration: Int = 0
    var videoUrl: String = ""
    var publisher: Publisher = PublisherBuilder().build()

    fun build(): Video =
        Video(
            id,
            thumbnailUrl,
            title,
            publishedAt,
            viewsCount,
            viewsCountLabel,
            duration,
            videoUrl,
            publisher
        )

    fun publisher(block: PublisherBuilder.() -> Unit): Publisher =
        PublisherBuilder().apply(block).build()
}