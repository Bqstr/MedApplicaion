package io.oitech.med_application.fragments.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.oitech.med_application.MainActivity
import io.oitech.med_application.models.MyUser

class LoginViewModel :ViewModel(){

    val isEmailAuthorization  =MutableLiveData<Boolean>(true)
    fun loginByEmailAndPassword(email: String, password: String,onSuccess:() ->Unit) {
        MainActivity.signInWithEmail(email, password, onSuccess = onSuccess)
    }

    val user =MutableLiveData<MyUser>()
}