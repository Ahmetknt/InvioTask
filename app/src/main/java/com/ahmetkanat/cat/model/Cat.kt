package com.ahmetkanat.cat.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Cat (
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "origin")
    val origin: String?,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "life_span")
    val life_span  : String?,
    @ColumnInfo(name = "country_code")
    val country_code : String?,
    @ColumnInfo(name = "dog_friendly")
    val dog_friendly : Int?,
    @ColumnInfo(name = "reference_image_id", defaultValue = "null")
    val reference_image_id : String?

):Serializable{
    @PrimaryKey(autoGenerate = true)
    var uuid : Int = 0
}