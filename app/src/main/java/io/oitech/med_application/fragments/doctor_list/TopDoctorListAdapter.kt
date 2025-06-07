package io.oitech.med_application.fragments.doctor_list


import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import io.oitech.med_application.R
import io.oitech.med_application.fragments.homeFragment.HomeDoctorUiItem
import io.oitech.med_application.fragments.homeFragment.OnItemClickListener

class TopDoctorListAdapter(private val items: List<HomeDoctorUiItem>, val context: Context,
                           public val listener: OnItemClickListener,) :
    RecyclerView.Adapter<TopDoctorListAdapter.ItemViewHolder>() {



    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.top_doctor_list_item_name)
        val image: ImageView = view.findViewById(R.id.top_doctor_list_item_image)
        val speciality: TextView = view.findViewById(R.id.top_doctor_list_item_speciality)
        val rating: TextView = view.findViewById(R.id.top_doctor_list_item_rating)
        val distance:TextView=view.findViewById(R.id.top_doctor_list_item_distance)
        val loadingImage :FrameLayout =view.findViewById(R.id.top_doctor_list_item_image_loading)
        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.top_doctors_list_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.speciality.text = item.speciality
        holder.distance.text ="${item.distance}m"
        //holder.image.text =item.image
        holder.rating.text =item.rating

        val storage = Firebase.storage
        val storageRef = storage.reference.child(item.image)

        holder.loadingImage.visibility =View.VISIBLE

        Log.d("asdfasdfasdfasdfasdf", "${item.image}")
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            Log.d("asdfasdfasdfasdfasdf", "${uri}    aa")

            //holder.progressBar.visibility = View.GONE
            holder.image.visibility = View.VISIBLE
            Glide.with(context)
                .load(uri)
                .listener(object : RequestListener<Drawable> {

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        // holder.progressBar.visibility = View.GONE
                        holder.image.visibility = View.VISIBLE // Hide the image until it's loaded

                        holder.loadingImage.visibility =View.GONE

                        return false // Let Glide handle setting the image
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.loadingImage.visibility =View.VISIBLE
                        holder.image.visibility =View.GONE


                        return false // Let Glide handle the error drawable
                    }

                })
                .into(holder.image)

            //holder.progressBar.visibility = View.VISIBLE

        }.addOnFailureListener {
            Log.d("asdfasdfasdfasdfasdf", "${it.message}    aa")
            holder.loadingImage.visibility =View.VISIBLE
            holder.image.visibility =View.GONE



        }

    }

    override fun getItemCount() = items.size




}



class StartEndPaddingItemDecoration(
    private val startPadding: Int,  // Padding before the first item
    private val endPadding: Int     // Padding after the last item
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        if (position == RecyclerView.NO_POSITION) return  // Safety check



        // Add padding to the first item (top for vertical, left for horizontal)
        if (position == 0) {
            outRect.left = startPadding
            outRect.right = startPadding

        }
        else{
            outRect.right =startPadding

        }

        // Add padding to the last item (bottom for vertical, right for horizontal)

    }
}





