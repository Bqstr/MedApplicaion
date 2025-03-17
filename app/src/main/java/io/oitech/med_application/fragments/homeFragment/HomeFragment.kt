package io.oitech.med_application.fragments.homeFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.oitech.med_application.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController =findNavController()
        Log.d("HomeFragment", "onViewCreated called") // Debugging log

        val recyclerView: RecyclerView = view.findViewById(R.id.home_doctors_recuclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)

        val doctorsList = listOf(
            HomeDoctorUiItem(name ="Dr. Smith", image ="https://example.com/image1.jpg", speciality ="Cardiologist", distance =6000.0, rating ="5.0",id =0),
            HomeDoctorUiItem(name ="Dr. Jane", image ="https://example.com/image2.jpg",speciality = "Dentist", distance =5000.0,rating = "4.5",id =1)
        )

        recyclerView.adapter = HomeDoctorsAdapters(doctorsList)


        recyclerView.addItemDecoration(StartEndPaddingItemDecoration(50, 50)) // 50px padding at start & end


        view.findViewById<TextView>(R.id.see_all_text).setOnClickListener{
            navController.navigate(R.id.action_homeFragment_to_doctorsListFragment2)
        }



        Log.d("HomeFragment", "RecyclerView item count: ${doctorsList.size}")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController =findNavController()
        return when (item.itemId) {
            R.id.exit -> {








                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}