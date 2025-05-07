package io.oitech.med_application.fragments.homeFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.oitech.med_application.R
import io.oitech.med_application.chatbot.ChatActivity
import io.oitech.med_application.fragments.MainViewModel
import io.oitech.med_application.utils.Resource


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HomeFragment : Fragment(),OnItemClickListener {
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


    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController =findNavController()

        val recyclerView: RecyclerView = view.findViewById(R.id.home_doctors_recuclerView)


        recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)

        val adapter =HomeDoctorsAdapters(emptyList(),this,requireContext())

        recyclerView.adapter = adapter


        recyclerView.addItemDecoration(StartEndPaddingItemDecoration(50, 50))


        view.findViewById<TextView>(R.id.see_all_text).setOnClickListener{
            navController.navigate(R.id.action_homeFragment_to_doctorsListFragment2)
        }
        view.findViewById<LinearLayout>(R.id.doctor_home_button).setOnClickListener{
            navController.navigate(R.id.action_homeFragment_to_mapFragment)
        }
        view.findViewById<LinearLayout>(R.id.ai_button_home).setOnClickListener{
            val intent = Intent(requireContext(), ChatActivity::class.java)
            startActivity(intent)

        }
        view.findViewById<LinearLayout>(R.id.hospital_home).setOnClickListener{
            navController.navigate(R.id.action_homeFragment_to_hospitalsFragment)
        }

        view.findViewById<LinearLayout>(R.id.map_home).setOnClickListener{
            navController.navigate(R.id.action_homeFragment_to_mapFragment)
        }

        viewModel.getAllDoctors()
        viewModel.doctors.observe(viewLifecycleOwner) { newList ->
            if(newList is Resource.Success ) {
                if(newList.data !=null) {
                    adapter.updateList(newList.data)
                }
            }
        }



    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController =findNavController()
        return when (item.itemId) {
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

    override fun onItemClick(position: Int) {
        Log.d("asdfasdfasdfasdfasdf","here")
        if(viewModel.doctors.value is Resource.Success) {
            val doctor = (viewModel.doctors.value as Resource.Success<List<HomeDoctorUiItem>>).data?.get(position)

            val bundle = Bundle()
            bundle.putParcelable("doctorDetails", doctor)
            findNavController().navigate(R.id.action_homeFragment_to_doctorDetailFragment, bundle)
        }

    }
}