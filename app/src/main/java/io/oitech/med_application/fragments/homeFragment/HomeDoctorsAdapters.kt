package io.oitech.med_application.fragments.homeFragment

import android.graphics.Rect
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.oitech.med_application.R
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface OnItemClickListener {
    fun onItemClick(position: Int)
}

class HomeDoctorsAdapters(
    private var items: List<HomeDoctorUiItem>,
    public val listener: OnItemClickListener
) :
    RecyclerView.Adapter<HomeDoctorsAdapters.ItemViewHolder>() {

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.home_doctor_item_name)
        val speciality: TextView = view.findViewById(R.id.home_doctor_item_speciality)
        val rating: TextView = view.findViewById(R.id.home_doctor_item_rating)
        val distance: TextView = view.findViewById(R.id.home_doctor_item_distance)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
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
        holder.distance.text = item.speciality
        holder.rating.text = item.rating
    }

    override fun getItemCount() = items.size

    // Function to update the list manually
    fun updateList(newItems: List<HomeDoctorUiItem>) {
        items = newItems
        notifyDataSetChanged() // This refreshes the RecyclerView
    }
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



@Parcelize
data class HomeDoctorUiItem(
    val id:Int ,
    val name:String,
    val image:String ="",
    val speciality:String,
    val distance:Double,
    val rating:String,
    val description:String ="skibi di doooooop",
    val listOfTimes:List<DateOfTheWeek> = emptyList()
) : Parcelable {
}





data class HomeDoctorUiItemWithout(
    val id:Int =0 ,
    val name:String ="",
    val image:String ="",
    val speciality:String="",
    val distance:Double=0.0,
    val rating:Double =5.0,
    val description:String ="skibi di doooooop",
){
    constructor() : this(0, "", "", "", 0.0, 1.0, "skibi di doooooop")

}



@Parcelize
data class DateOfTheWeek(
    val dateTime: String = "", // Store LocalDateTime as String
    val dateNumber: Int = 0,
    val dateName: String = "",
    val listOfDates: List<TimeSlot> = emptyList()
):Parcelable{
    constructor(localDateTime: LocalDateTime, dateNumber: Int, dateName: String, listOfDates: List<TimeSlot>) :
            this(localDateTime.format(DateTimeFormatter.ISO_DATE_TIME), dateNumber, dateName, listOfDates)

    // Convert String back to LocalDateTime when reading from Firebase
//    fun getLocalDateTime(): LocalDateTime {
//        return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME)
//    }
}

@Parcelize
data class TimeSlot(
    val dateTime: String = "", // Store LocalDateTime as String
    val time: String = "",
    val available: Boolean = false
):Parcelable

