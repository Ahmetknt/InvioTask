package com.ahmetkanat.cat.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ahmetkanat.cat.service.CatDatabase
import com.ahmetkanat.cat.model.Cat
import kotlinx.coroutines.launch
import java.util.*

class FavoriteViewModel(application: Application) : BaseViewModel(application) {

    val catLiveData = MutableLiveData<Cat>()

    fun getDataFromRoom(catId : Int){

        launch {
            val dao  = CatDatabase(getApplication()).catDao().getCat(catId)
            catLiveData.value = dao

        }

    }

}