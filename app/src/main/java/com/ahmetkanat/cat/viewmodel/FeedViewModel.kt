package com.ahmetkanat.cat.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.ahmetkanat.catapp.model.Cat
import com.ahmetkanat.cat.service.CatAPIService
import com.ahmetkanat.cat.service.CatDatabase
import com.ahmetkanat.cat.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : BaseViewModel(application) {
    private val catAPIService = CatAPIService()
    private val disposable = CompositeDisposable()
    private var customPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L

    val cat = MutableLiveData<List<Cat>>()
    val catError = MutableLiveData<Boolean>()
    val catLoading = MutableLiveData<Boolean>()

    fun refreshData(){
        catLoading.value = true
        val updateTime = customPreferences.getTime()
        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime){
            getDataFromSQLite()
        }else{
            getDataFromAPI()
        }
    }

    fun refreshFromAPI(){
        getDataFromAPI()
    }

    private fun getDataFromSQLite(){
        launch {
            val cat = CatDatabase(getApplication()).catDao().getAllCat()
            showCat(cat)
            Toast.makeText(getApplication(),"Cat From SQLite",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getDataFromAPI(){
        catLoading.value = true

        disposable.add(
            catAPIService.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Cat>>(){
                    override fun onSuccess(t: List<Cat>) {
                        storeInSQLite(t)
                        Toast.makeText(getApplication(),"Cat From API",Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(e: Throwable) {
                        catLoading.value = false
                        catError.value = true
                    }
                })
        )

    }
    private fun showCat(catList : List<Cat>){
        cat.value = catList
        catError.value = false
        catLoading.value = false
    }


    private fun storeInSQLite(list : List<Cat>){

        launch {
            val dao = CatDatabase(getApplication()).catDao()
            dao.deleteAllCat()
            val listLong = dao.insertAll(*list.toTypedArray())

            var i = 0
            while (i < list.size){
                list[i].uuid = listLong[i].toInt()
                i += 1
            }
            showCat(list)
        }

        customPreferences.saveTime(System.nanoTime())
    }



    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}