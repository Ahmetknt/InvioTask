package com.ahmetkanat.cat.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahmetkanat.cat.service.CatDatabase
import com.ahmetkanat.catapp.model.Cat
import com.ahmetkanat.catapp.model.Image
import kotlinx.coroutines.launch

class CatViewModel(application: Application) : BaseViewModel(application) {

    val catLiveData = MutableLiveData<Cat>()

    fun getDataFromRoom(uuid : Int){

        launch {
            val dao  = CatDatabase(getApplication()).catDao().getCat(uuid)
            catLiveData.value = dao
        }

    }

}