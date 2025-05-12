package io.oitech.med_application.fragments.map

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import io.oitech.med_application.R
import io.oitech.med_application.databinding.FragmentMapBinding
import io.oitech.med_application.fragments.MainViewModel
import io.oitech.med_application.utils.Resource

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters


    val viewModel: MainViewModel by activityViewModels()


    private lateinit var mGoogleMap: GoogleMap
    private lateinit var binding: FragmentMapBinding
    val MY_PERMISSIONS_REQUEST_LOCATION = 99

    lateinit var mLocationRequest: LocationRequest
    var mLastLocation: Location? = null

    var mFusedLocationClient: FusedLocationProviderClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(layoutInflater)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment =
            getChildFragmentManager().findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //val myLocationText = view?.findViewById<TextView>(R.id.my_address)




        binding.goBackFromMap.setOnClickListener{
            Log.d("asdfasdfasdfasdfasdf","go back")
            findNavController().popBackStack()
        }

        binding.confirmLocation.setOnClickListener{
            Log.d("asdfasdfasdfasdfasdf","go back")
            findNavController().popBackStack()
        }






        viewModel.addressFromGeocode.observe(viewLifecycleOwner) { newAddress ->
            Log.d("sdafasdfadsfasdfsadf", newAddress.toString())
            if (newAddress is Resource.Success) {
                Log.d("sdafasdfadsfasdfsadf", "${newAddress}    whaaat")
                view?.findViewById<TextView>(R.id.my_address)?.text = newAddress.data
            }
        }




        binding.btnMyLocation.setOnClickListener {
            mLastLocation?.let { location ->
//                val geocoder = Geocoder(requireContext())
//                val addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
//
//                if (addressList != null && addressList.isNotEmpty()) {
//                    val address = addressList[0]
//                    val fullAddress = address.getAddressLine(0)
//
//                    viewModel.addressFromGeocode.value =Resource.Success(fullAddress)
//                    Toast.makeText(requireContext(), fullAddress, Toast.LENGTH_LONG).show()
//                }

                assignLocation(location)


                val latLng = LatLng(location.latitude, location.longitude)
                val cameraPosition = CameraPosition.Builder()
                    .target(latLng)
                    .zoom(18f)
                    .build()
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        mLocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 50)
            .setWaitForAccurateLocation(false)
            .setIntervalMillis(60)
            .build()


        Log.d("sadfasdfasdfasdfasdf", "map ready")


        mLocationRequest!!.priority =
            com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //Location Permission already granted

            mFusedLocationClient?.lastLocation?.addOnSuccessListener { loca ->
                assignLocation(loca)

            }


//            mFusedLocationClient!!.lastLocation(
//                mLocationRequest, mLocationCallback, Looper.myLooper()
//            )
                //mGoogleMap.setMyLocationEnabled(true)


        } else {
            //Request Location Permission
            checkLocationPermission()
        }

        mGoogleMap.uiSettings.isMyLocationButtonEnabled = false

    }


    private var hasCenteredMap = false


    var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                val location = locationList.last()
                mLastLocation = location
                val latLng = LatLng(location.latitude, location.longitude)


                val geocoder = Geocoder(requireContext())
                val addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

                if (addressList != null && addressList.isNotEmpty()) {
                    val address = addressList[0]
                    val fullAddress = address.getAddressLine(0)

                    Toast.makeText(requireContext(), fullAddress, Toast.LENGTH_LONG).show()
                }

                // Optionally update marker or do other logic...

                if (!hasCenteredMap) {
                    val cameraPosition = CameraPosition.Builder()
                        .target(latLng)
                        .zoom(18f)
                        .build()
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    hasCenteredMap = true
                }
            }
        }
    }


    fun assignLocation(location: Location?) {
        if (location != null) {
            mLastLocation = location
            val latLng = LatLng(location.latitude, location.longitude)

            // Clear existing markers if needed
            mGoogleMap.clear()

            // Use vector icon for marker
            val markerIcon = bitmapDescriptorFromVector(R.drawable.my_location_green)
            mGoogleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .icon(markerIcon)
                    .title("You are here")
            )

            // Optional: Get and display address
            val geocoder = Geocoder(requireContext())
            val addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            if (addressList != null && addressList.isNotEmpty()) {
                val address = addressList[0]
                val fullAddress = address.getAddressLine(0)

                Log.d("LocationAddress", address.toString())
                viewModel.addressFromGeocode.value = Resource.Success(fullAddress)
                Toast.makeText(requireContext(), fullAddress, Toast.LENGTH_LONG).show()
            }

            // Move camera once
            if (!hasCenteredMap) {
                val cameraPosition = CameraPosition.Builder()
                    .target(latLng)
                    .zoom(18f)
                    .build()
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                hasCenteredMap = true
            }
        }
    }

    private fun bitmapDescriptorFromVector(vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(requireContext(), vectorResId)!!
        vectorDrawable.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(requireContext()).setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, i -> //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(
                            this@MapFragment.requireActivity(),
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }.create().show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }

}