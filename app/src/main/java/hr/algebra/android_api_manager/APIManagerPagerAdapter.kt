package hr.algebra.android_api_manager

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.squareup.picasso.Picasso
import hr.algebra.android_api_manager.model.Item
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.coroutines.newFixedThreadPoolContext
import org.jetbrains.annotations.NonNls
import java.io.File

class APIManagerPagerAdapter(
    private val context: Context,
    private val items: MutableList<Item>
) :
    RecyclerView.Adapter<APIManagerPagerAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), DefaultLifecycleObserver {
        val yt = itemView.findViewById<YouTubePlayerView>(R.id.youtube_player_view)

       override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
            owner.lifecycle.addObserver(yt)

        }

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
            yt.release()
        }


        private val youtubePath : String = "https://www.youtube.com/watch?v="

        val ivFavourite = itemView.findViewById<ImageView>(R.id.ivFavourite)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvPublishedDate = itemView.findViewById<TextView>(R.id.tvPublishedDate)
        private val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
        fun bind(item: Item) {
            Picasso.get()
                .load(File(item.picturePath))
                .error(R.drawable.empty_picture)
                .transform(RoundedCornersTransformation(50, 5))
            tvTitle.text = item.title
            tvPublishedDate.text = item.publishedDate
            tvDescription.text = item.description
            ivFavourite.setImageResource(if (item.favourite) R.drawable.heart else R.drawable.heart_outline)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.apimanager_pager, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.ivFavourite.setOnClickListener {
            item.favourite = !item.favourite
            val uri = ContentUris.withAppendedId(APIMANAGER_PROVIDER_URI, item._id!!)
            val values = ContentValues().apply {
                put(Item::favourite.name, item.favourite)
            }
            context.contentResolver.update(
                uri,
                values,
                null,
                null
            )
            notifyItemChanged(position)
        }

        holder.yt.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(item.videoPath, 0F)
                youTubePlayer.pause()
            }
        })
        holder.bind(item)
    }

    override fun getItemCount() = items.size
}
