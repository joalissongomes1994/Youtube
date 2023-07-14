package joalissongomes.dev.youtube.main.model

class PublisherBuilder {
    var id: String = ""
    var name: String = ""
    var pictureProfileUrl: String = ""

    fun build(): Publisher =
        Publisher(id, name, pictureProfileUrl)
}