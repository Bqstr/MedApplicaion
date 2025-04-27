package io.oitech.med_application.utils

import android.graphics.Paint.Align
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import io.oitech.med_application.R

object ComposableUtils {


    @Composable
    fun ColumnScope.Space(dp:Dp){
        Spacer(modifier = Modifier.height(dp))
    }
    @Composable
    fun RowScope.Space(dp:Dp){
        Spacer(modifier = Modifier.width(dp))
    }

    @Composable
    fun RowScope.Space(){
        Spacer(modifier = Modifier.weight(1f))
    }


    @Composable
    fun ColumnScope.Space(){
        Spacer(modifier = Modifier.weight(1f))
    }


    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun FirebaseImageLoader(imagePath: String,modifier: Modifier,contentScale: ContentScale) {
        val storage = Firebase.storage
        val storageRef = storage.reference.child("${imagePath}")

        //Not Sure that this is the right way
        var imageUrl = remember { mutableStateOf<String?>("https://firebasestorage.googleapis.com/v0/b/med-project-4397f.firebasestorage.app/o/${imagePath}?alt=media") }


        // Fetch the image download URL
//        LaunchedEffect(imagePath) {
//            Log.e("slkdjflksjdfkljdsfkl", "doing ${imagePath}")
//
//            storageRef.downloadUrl.addOnSuccessListener { uri ->
//                Log.e("slkdjflksjdfkljdsfkl", "success")
//
//                imageUrl.value = uri.toString()
//            }.addOnFailureListener {
//                Log.e("slkdjflksjdfkljdsfkl", "Error fetching image URL: ${it.message}")
//            }
//        }

        // Display the image if the URL is available
        if (imageUrl != null) {

            GlideSubcomposition(model =imageUrl.value,modifier =modifier ) {
                when(state){
                    is RequestState.Failure ->{
                    }
                    is RequestState.Loading ->{
                        Box(modifier =modifier){
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }

                    }
                    else ->{
                        GlideImage(
                            model =  imageUrl.value,
                            contentDescription = "Firebase Image",
                            modifier = modifier,
                            contentScale = contentScale
                        )
                    }
                }
            }
        } else {
            CircularProgressIndicator()
        }
    }


}