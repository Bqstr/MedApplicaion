package io.oitech.med_application.fragments.doctor_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.oitech.med_application.R
import io.oitech.med_application.fragments.homeFragment.HomeDoctorUiItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DoctorsListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DoctorsListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctors_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recyclerView: RecyclerView = view.findViewById(R.id.top_doctor_recucler_view)

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )

        val doctorsList = listOf(
            HomeDoctorUiItem(
                name = "Dr. Smith",
                image = "https://example.com/image1.jpg",
                speciality = "Cardiologist",
                distance = 6000.0,
                rating = "5.0",
                id = 0,
                hospitalId = 0,
                price = 10000
            ),
            HomeDoctorUiItem(
                name = "Dr. Jane",
                image = "https://example.com/image2.jpg",
                speciality = "Dentist",
                distance = 5000.0,
                rating = "4.5",
                id = 1,
                hospitalId = 0,
                price = 6000
            )
        )

        recyclerView.adapter = TopDoctorListAdapter(doctorsList)


        //recyclerView.addItemDecoration(StartEndPaddingItemDecoration(50, 50)) // 50px padding at start & end

        Log.d("HomeFragment", "RecyclerView item count: ${doctorsList.size}")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DoctorsListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DoctorsListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}