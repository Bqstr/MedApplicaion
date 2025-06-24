package io.oitech.med_application.fragments.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.google.android.material.progressindicator.CircularProgressIndicator
import io.oitech.med_application.R
import io.oitech.med_application.fragments.MainViewModel
import io.oitech.med_application.fragments.homeFragment.OnItemClickListener
import io.oitech.med_application.utils.Resource

/**
 * A simple [Fragment] subclass.
 * Use the [SavedItemsFragments.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedItemsFragments : Fragment(),OnItemClickListener {

    lateinit var adapter :FavoritesListAdapter
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val viewModel :MainViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_items_fragments, container, false)
    }
    init {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFavoriteDoctors()
        adapter =FavoritesListAdapter(context =requireContext(), listener = this)
        view.findViewById<RecyclerView>(R.id.saved_doctors_recyclerVeiw).layoutManager = LinearLayoutManager(requireContext())
        view.findViewById<RecyclerView>(R.id.saved_doctors_recyclerVeiw).adapter =adapter

        val listLoaded ={
            view.findViewById<RecyclerView>(R.id.saved_doctors_recyclerVeiw).visibility =View.GONE
            view.findViewById<CircularProgressIndicator>(R.id.saved_doctors_progress_indicator).visibility =View.VISIBLE
        }
        val loadingComplete ={
            view.findViewById<RecyclerView>(R.id.saved_doctors_recyclerVeiw).visibility =View.VISIBLE
            view.findViewById<CircularProgressIndicator>(R.id.saved_doctors_progress_indicator).visibility =View.GONE
        }

        viewModel.favoriteDoctors.observe(viewLifecycleOwner){
            Log.d("damnnnnnn","damn   $it")
            if(it is Resource.Success && it.data?.isEmpty() == false){
                loadingComplete()
                Log.d("damnnnnnn","cringe ${it.data}")

                val list =it.data
                adapter.setList(list)
            }
            else if(it is Resource.Loading){
                listLoaded()
            }


        }



    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SavedItemsFragments.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SavedItemsFragments().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onItemClick(position: Int) {
        val doctorItem =adapter.getDoctorInstance(position)
        val bundle  =Bundle()
        bundle.putParcelable("doctorDetails",doctorItem)
        findNavController().navigate(R.id.action_savedItemsFragments_to_doctorDetailFragment,bundle)
    }
}