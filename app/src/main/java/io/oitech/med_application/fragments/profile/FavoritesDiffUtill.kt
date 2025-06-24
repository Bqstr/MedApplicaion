package io.oitech.med_application.fragments.profile

import androidx.recyclerview.widget.DiffUtil
import io.oitech.med_application.fragments.homeFragment.HomeDoctorUiItem


class FavoritesDiffUtill(
    private val oldList: List<HomeDoctorUiItem>,
    private val newList: List<HomeDoctorUiItem>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Compare unique IDs
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Compare full content if needed
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}