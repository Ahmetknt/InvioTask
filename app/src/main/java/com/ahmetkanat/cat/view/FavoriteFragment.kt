package com.ahmetkanat.cat.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmetkanat.cat.R
import com.ahmetkanat.cat.adapter.CatAdapter
import com.ahmetkanat.cat.adapter.FavoriteAdapter
import com.ahmetkanat.cat.viewmodel.CatViewModel
import com.ahmetkanat.cat.viewmodel.FavoriteViewModel
import com.ahmetkanat.cat.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.card_tasarim.*
import kotlinx.android.synthetic.main.fragment_cat.*
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.fragment_feed.*

class FavoriteFragment : Fragment() {

    private lateinit var viewModel : FavoriteViewModel
    private val adapter = FavoriteAdapter(arrayListOf())
    private var favoriCatId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            favoriCatId = FavoriteFragmentArgs.fromBundle(it).favoriCatID
        }

        viewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        viewModel.getDataFromRoom()

        RV_favorite.layoutManager = LinearLayoutManager(context)
        RV_favorite.adapter = adapter


        observeLiveData()

    }

    private fun observeLiveData(){
        viewModel.catLiveData.observe(viewLifecycleOwner, Observer { cat ->
            cat?.let {
                RV_favorite.visibility = View.VISIBLE
                adapter.updateCatList(cat)
            }

        })
    }

}