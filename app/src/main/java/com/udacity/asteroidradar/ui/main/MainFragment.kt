package com.udacity.asteroidradar.ui.main

import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.shoestore.ui.base.BaseFragment

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main)
    , AsteroidAdapter.OnAsteroidClickListener
    , MenuProvider {

    private val mainViewModel: MainViewModel by viewModels { MainViewModel.Factory }

    override fun onStart() {
        super.onStart()

        binding.viewModel = mainViewModel

        val  adapter= AsteroidAdapter(this)
        binding.rvAsteroid.adapter = adapter

        mainViewModel.navigateToDetailsScreen.observe(viewLifecycleOwner) { asteroid->
            if (null != asteroid) {
                // Must find the NavController from the Fragment
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailsFragment(asteroid))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                mainViewModel.onNavigateToDetailsScreenCompleted()
            }
        }
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_overflow_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.week_asteroid -> mainViewModel.onViewAsteroidsClicked(MenuEnum.WEEK)
            R.id.today_asteroid -> mainViewModel.onViewAsteroidsClicked(MenuEnum.TODAY)
            R.id.save_asteroid -> mainViewModel.onViewAsteroidsClicked(MenuEnum.SAVE)
        }
        return true
    }

    override fun onAsteroidClick(asteroid: Asteroid) {
        mainViewModel.onAsteroidSelected(asteroid)
    }

    enum class MenuEnum {
        WEEK,
        TODAY,
        SAVE
    }
}