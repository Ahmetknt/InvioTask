package com.ahmetkanat.cat.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmetkanat.cat.R
import com.ahmetkanat.cat.adapter.CatAdapter
import com.ahmetkanat.cat.viewmodel.FeedViewModel
import com.ahmetkanat.cat.model.Cat
import kotlinx.android.synthetic.main.card_tasarim.*
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

        viewModel = ViewModelProvider(this).get(FeedViewModel::class.java)
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
        viewModel.catList.observe(viewLifecycleOwner, Observer { cat ->
            cat?.let {
                Log.e("dsa","${cat}")
                rvFeed.visibility = View.VISIBLE
                Log.e("dsa","dsa")
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
                        viewModel.catList.value?.forEach {
                            if(it.name!!.toLowerCase(Locale.getDefault()).contains(search)){
                                displayList.add(it)
                                displayList[0].name?.let { it1 -> Log.e("x", it1) }
                            }
                        }
                        adapter.updateCatList(displayList)
                    }else{
                        displayList.clear()
                        viewModel.catList.value?.let { displayList.addAll(it) }
                        adapter.updateCatList(displayList)
                    }
                    return true
                }

            })
        }

        super.onCreateOptionsMenu(menu, inflater)
    }
}