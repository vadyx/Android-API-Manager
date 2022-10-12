package hr.algebra.android_api_manager

import android.app.AlertDialog
import android.content.ContentUris
import android.content.Context
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.android_api_manager.framework.startActivity
import hr.algebra.android_api_manager.model.Item
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class ItemAdapter(private val context: Context, private val items: MutableList<Item>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
        private val tvItem = itemView.findViewById<TextView>(R.id.tvItem)
        fun bind(item: Item) {
            Picasso.get()
                .load(File(item.picturePath))
                .error(R.drawable.empty_picture)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)
            tvItem.text = item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            context.startActivity<APIManagerPagerActivity>(ITEM_POSITION, position)
        }
        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(context).apply {
                setTitle(R.string.delete)
                setMessage(context.getString(R.string.deleteConfirm) + " '${items[position].title}'?")
                setIcon(R.drawable.delete)
                setCancelable(true)
                setNegativeButton(context.getString(R.string.no), null)
                setPositiveButton(R.string.confirm) { _, _ -> deleteItem(position) }
                show()
            }
            true
        }
        holder.bind(items[position])
    }

    private fun deleteItem(position: Int) {
        val item = items[position]
        context.contentResolver.delete(
            ContentUris.withAppendedId( APIMANAGER_PROVIDER_URI, item._id!!),
            null,
            null)
        File(item.picturePath).delete()
        items.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size
}
