package com.ahmetkanat.catapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Image (
  @ColumnInfo(name = "url")
  val url    : String
) : Serializable{

}
