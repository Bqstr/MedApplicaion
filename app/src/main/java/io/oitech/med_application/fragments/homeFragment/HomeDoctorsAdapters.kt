package io.oitech.med_application.fragments.homeFragment

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.oitech.med_application.DateOfTheWeek
import io.oitech.med_application.R

class HomeDoctorsAdapters(private val items: List<HomeDoctorUiItem>) :
    RecyclerView.Adapter<HomeDoctorsAdapters.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.home_doctor_item_name)
        //val image: TextView = view.findViewById(R.id.home_doctor_item_image)
        val speciality: TextView = view.findViewById(R.id.home_doctor_item_speciality)
        val rating: TextView = view.findViewById(R.id.home_doctor_item_rating)
        val distance:TextView=view.findViewById(R.id.home_doctor_item_distance)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_doctors_recucler_view_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.speciality.text = item.speciality
        holder.distance.text =item.speciality
        //holder.image.text =item.image
        holder.rating.text =item.rating
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




data class HomeDoctorUiItem(
    val id:Int,
    val name:String,
    val image:String ="",
    val speciality:String,
    val distance:Double,
    val rating:String,
    val description:String ="skibi di doooooop",
    val listOfTimes:List<DateOfTheWeek> = emptyList()
)
