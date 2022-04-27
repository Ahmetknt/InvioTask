package com.ahmetkanat.cat.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ahmetkanat.cat.model.Cat

@Dao
interface CatDAO {

    @Insert
    suspend fun insertAll(vararg cat : Cat) : List<Long>

    @Query("SELECT * FROM Cat")
    suspend fun getAllCat() : List<Cat>

    @Query("SELECT * FROM Cat WHERE uuid=:catId")
    suspend fun getCat(catId : Int) : Cat

    @Query("DELETE FROM Cat")
    suspend fun deleteAllCat()

    /*@Query("DELETE FROM Cat WHERE uuid=:catId")
    suspend fun deleteCat(catId: Int) : Cat*/




}