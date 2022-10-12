package hr.algebra.android_api_manager.model

data class Item(
    var _id: Long?,
    val title: String,
    val description: String,
    val picturePath: String,
    val videoPath: String,
    val publishedDate: String,
    var favourite: Boolean
)
