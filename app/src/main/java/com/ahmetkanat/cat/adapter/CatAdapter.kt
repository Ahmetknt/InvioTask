package com.ahmetkanat.cat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ahmetkanat.cat.R
import com.ahmetkanat.cat.databinding.CardTasarimBinding
import com.ahmetkanat.cat.util.downloadFromUrl
import com.ahmetkanat.cat.util.placeholderProgressBar
import com.ahmetkanat.cat.view.FeedFragmentDirections
import com.ahmetkanat.catapp.model.Cat
import kotlinx.android.synthetic.main.card_tasarim.view.*

class CatAdapter(private val catList : ArrayList<Cat>) : RecyclerView.Adapter<CatAdapter.CatHolder>() {

    inner class CatHolder(val cardTasarimBinding: CardTasarimBinding) : RecyclerView.ViewHolder(cardTasarimBinding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardTasarimBinding.inflate(layoutInflater,parent,false)
        return CatHolder(binding)

    }

    override fun onBindViewHolder(holder: CatHolder, position: Int) {
        val context = holder.itemView.context
        val cat = catList[position]
        //val url = cat.image.url

        holder.cardTasarimBinding.apply {
            nameText.text = cat.name
            //favoriImage.downloadFromUrl(url, placeholderProgressBar(holder.itemView.context))
        }
        holder.cardTasarimBinding.cardView.setOnClickListener {
            val action = FeedFragmentDirections.actionFeedFragmentToCatFragment(cat.uuid)
            Navigation.findNavController(it).navigate(action)
        }

        //bu kontrol boolean ile de sağlanabilirdi fakat card yapısı olduğu için boolean yapısı olmadı favorilere ekleme çıkarmaya böyle bir çözüm buldum.
        var display = R.drawable.star_off

       holder.cardTasarimBinding.starButton.setOnClickListener {
            if(display == R.drawable.star_off){
                holder.cardTasarimBinding.starButton.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.star
                    ))
                display = R.drawable.star
            }
            else if(display == R.drawable.star){
                holder.cardTasarimBinding.starButton.setImageDrawable(
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
