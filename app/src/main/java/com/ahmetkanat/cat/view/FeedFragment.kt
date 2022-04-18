package com.ahmetkanat.cat.view

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmetkanat.cat.R
import com.ahmetkanat.cat.adapter.CatAdapter
import com.ahmetkanat.cat.viewmodel.FeedViewModel
import com.ahmetkanat.catapp.model.Cat
import com.bumptech.glide.load.model.stream.MediaStoreImageThumbLoader
import kotlinx.android.synthetic.main.fragment_feed.*
import java.util.*

class FeedFragment() : Fragment() {

    private lateinit var viewModel : FeedViewModel
    private val adapter = CatAdapter(arrayListOf())

    var displayList = ArrayList<Cat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        viewModel.refreshData()

        rvFeed.layoutManager = LinearLayoutManager(context)
        rvFeed.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener {
            rvFeed.visibility = View.GONE
            catError.visibility = View.GONE
            catLoading.visibility = View.VISIBLE
            viewModel.refreshFromAPI()
            swipeRefreshLayout.isRefreshing = false
        }
        observeLiveData()

        heartButton.setOnClickListener {
            val action = FeedFragmentDirections.actionFeedFragmentToFavoriteFragment()
            Navigation.findNavController(view).navigate(action)
        }

    }

    private fun observeLiveData(){
        viewModel.cat.observe(viewLifecycleOwner, Observer { cat ->
            cat?.let {
                rvFeed.visibility = View.VISIBLE
                adapter.updateCatList(cat)
            }

        })

        viewModel.catError.observe(viewLifecycleOwner, Observer {error ->
            error?.let {
                if(it){
                    catError.visibility  =View.VISIBLE
                }else{
                    catError.visibility = View.GONE
                }
            }

        })

        viewModel.catLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if(it){
                    catLoading.visibility = View.VISIBLE
                    rvFeed.visibility  =View.GONE
                    catError.visibility  =View.GONE
                }else{
                    catLoading.visibility = View.GONE
                }
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_item,menu)
        val menuItem = menu.findItem(R.id.search_action)

        if(menuItem != null){
            val searchView = menuItem.actionView as SearchView

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText!!.isNotEmpty()){
                        displayList.clear()
                        val search = newText.toLowerCase(Locale.getDefault())
                        viewModel.cat.value?.forEach {
                            if(it.name.toLowerCase(Locale.getDefault()).contains(search)){
                                displayList.add(it)
                                Log.e("x",displayList[0].name)
                            }
                        }
                        adapter.updateCatList(displayList)
                    }else{
                        displayList.clear()
                        viewModel.cat.value?.let { displayList.addAll(it) }
                        adapter.updateCatList(displayList)
                    }
                    return true
                }

            })
        }

        super.onCreateOptionsMenu(menu, inflater)
    }
}