package io.oitech.med_application.fragments.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.oitech.med_application.MainActivity
import io.oitech.med_application.models.MyUser

class LoginViewModel :ViewModel(){



    val user =MutableLiveData<MyUser>()
}