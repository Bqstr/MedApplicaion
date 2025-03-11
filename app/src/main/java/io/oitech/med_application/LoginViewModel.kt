package io.oitech.med_application

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.oitech.med_application.models.MyUser

class LoginViewModel :ViewModel(){
    fun loginByEmailAndPassword(email: String, password: String,onSuccess:() ->Unit) {
        MainActivity.signInWithEmail(email,password, onSuccess = onSuccess)    }

    val user =MutableLiveData<MyUser>()
}