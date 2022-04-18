package com.ahmetkanat.cat.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ahmetkanat.cat.R
import com.ahmetkanat.cat.util.downloadFromUrl
import com.ahmetkanat.cat.util.placeholderProgressBar
import com.ahmetkanat.cat.viewmodel.CatViewModel
import kotlinx.android.synthetic.main.fragment_cat.*

class CatFragment : Fragment() {

    private lateinit var viewModel : CatViewModel
    private var catUUID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            catUUID = CatFragmentArgs.fromBundle(it).catUUID
        }

        viewModel = ViewModelProviders.of(this).get(CatViewModel::class.java)
        viewModel.getDataFromRoom(catUUID)


        observeLiveData()
    }
    private fun observeLiveData(){
        viewModel.catLiveData.observe(viewLifecycleOwner, Observer { cat ->
            cat?.let {
                descriptionText.text = cat.description
                originText.text = cat.origin
                lifespanText.text = cat.life_span
                countrycodeText.text = cat.country_code
                dogFriendlyText.text = cat.dog_friendly.toString()
                /*context?.let {
                    imageViewDetay.downloadFromUrl(cat.image.url, placeholderProgressBar(it))
                }*/

            }
        })
    }

}