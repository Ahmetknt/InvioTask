package com.ahmetkanat.cat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ahmetkanat.cat.R
import com.ahmetkanat.cat.databinding.CardFavoriteBinding
import com.ahmetkanat.cat.util.downloadFromUrl
import com.ahmetkanat.cat.util.placeholderProgressBar
import com.ahmetkanat.cat.view.FavoriteFragmentDirections
import com.ahmetkanat.catapp.model.Cat

class FavoriteAdapter(private val catList : ArrayList<Cat>) : RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>() {



    inner class FavoriteHolder(val favoriteBinding: CardFavoriteBinding) : RecyclerView.ViewHolder(favoriteBinding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardFavoriteBinding.inflate(layoutInflater,parent,false)
        return FavoriteHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        val context = holder.itemView.context
        val cat = catList[position]
        //val url = cat.image.url

        holder.favoriteBinding.apply {
            nameText.text = cat.name
            //favoriImage.downloadFromUrl(url, placeholderProgressBar(holder.itemView.context))
        }
        holder.favoriteBinding.cardView.setOnClickListener {
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToCatFragment(cat.uuid)
            Navigation.findNavController(it).navigate(action)
        }

        //bu kontrol boolean ile de sağlanabilirdi fakat card yapısı olduğu için boolean yapısı olmadı favorilere ekleme çıkarmaya böyle bir çözüm buldum.
        var display = R.drawable.star_off

        holder.favoriteBinding.starButton.setOnClickListener {
            if(display == R.drawable.star_off){
                holder.favoriteBinding.starButton.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.star
                    ))
                display = R.drawable.star
            }
            else if(display == R.drawable.star){
                holder.favoriteBinding.starButton.setImageDrawable(
                    ContextCompat.getDrawable(context,R.drawable.star_off
                    ))
                display = R.drawable.star_off
            }
        }
    }

    override fun getItemCount(): Int {
        return catList.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateCatList(newCatList : List<Cat>){
        catList.clear()
        catList.addAll(newCatList)
        notifyDataSetChanged()
    }
}