package io.oitech.med_application.fragments.profile

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.runtime.toMutableStateList
import androidx.recyclerview.widget.RecyclerView
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

class FavoritesListAdapter(
    public val listener: OnItemClickListener,
    private val context: android.content.Context
    ): RecyclerView.Adapter<FavoritesListAdapter.ItemViewHolder>() {

    private var items = mutableListOf<HomeDoctorUiItem>()



    inner class ItemViewHolder(view: View):RecyclerView.ViewHolder(view){
        val name: TextView = view.findViewById(R.id.favorites_doctor_item_name)
        val speciality: TextView = view.findViewById(R.id.favorites_doctor_item_speciality)
        val rating: TextView = view.findViewById(R.id.favorites_doctor_item_rating)
        val distance: TextView = view.findViewById(R.id.favorites_doctor_item_distance)
        val image: ImageView = view.findViewById(R.id.favorites_doctor_item_image)
        // val progressBar = view.findViewById<ProgressBar>(R.id.home_doctor_item_progress)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }


    fun setList(list:List<HomeDoctorUiItem>){
        items =list.toMutableList()
        notifyDataSetChanged() // <- THIS tells the adapter to refresh
    }
    fun clearList(){
        items.clear()
        notifyDataSetChanged() // <- THIS tells the adapter to refresh

    }

    fun addItem(item:HomeDoctorUiItem){
        items.add(item)
        notifyDataSetChanged() // <- THIS tells the adapter to refresh

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritesListAdapter.ItemViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_doctor_item, parent, false)

        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int =items.size
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item =items[position]
        holder.name.text =item.name
        holder.speciality.text =item.speciality
        holder.distance.text =item.distance
        holder.rating.text =item.rating

        val storage = Firebase.storage
        val storageRef = storage.reference.child(item.image)

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

                        return false // Let Glide handle setting the image
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        //holder.progressBar.visibility = View.GONE
                        return false // Let Glide handle the error drawable
                    }

                })
                .into(holder.image)

            //holder.progressBar.visibility = View.VISIBLE

        }.addOnFailureListener {
            Log.d("asdfasdfasdfasdfasdf", "${it.message}    aa")
            holder.image.setImageResource(R.drawable.doctor_mock_image)
        }

    }

    fun getDoctorInstance(position: Int): HomeDoctorUiItem {
        return items[position]
    }


}