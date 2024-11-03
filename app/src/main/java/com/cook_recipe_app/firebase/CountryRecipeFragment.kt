package com.cook_recipe_app.firebase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class CountryRecipeFragment : BaseFragment() {

    companion object {
        fun newInstance(): CountryRecipeFragment {
            return CountryRecipeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_country_recipe, container, false)
    }
}
