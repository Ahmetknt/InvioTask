package com.ahmetkanat.cat.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.ahmetkanat.cat.service.CatDatabase
import com.ahmetkanat.catapp.model.Cat
import kotlinx.coroutines.launch
import java.util.*

class FavoriteViewModel(application: Application) : BaseViewModel(application) {

    val catLiveData = MutableLiveData<List<Cat>>()

    fun getDataFromRoom(){

        launch {
            val dao  = CatDatabase(getApplication()).catDao().getAllCat()
            catLiveData.value = dao
        }

    }

}