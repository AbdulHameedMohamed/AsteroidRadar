package com.udacity.asteroidradar.ui.main

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.udacity.asteroidradar.MyApplication
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(private val myRepository: AsteroidRepository,
                    // For Handle Process Death If Needed
                    private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val weekAsteroidList = myRepository.asteroidList
    private val todayAsteroidList = myRepository.todayAsteroidList
    val asteroidList: MediatorLiveData<List<Asteroid>> = MediatorLiveData()

    val pictureOfDay = myRepository.pictureOfDay

    private val _navigateToSelectedProperty = MutableLiveData<Asteroid?>()

    val navigateToDetailsScreen: LiveData<Asteroid?>
        get() = _navigateToSelectedProperty

    init {
        getAsteroidData()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val myRepository = (this[APPLICATION_KEY] as MyApplication).myRepository
                MainViewModel(
                    myRepository = myRepository,
                    savedStateHandle = savedStateHandle
                )
            }
        }
    }

    private fun getAsteroidData() {
        viewModelScope.launch {
            myRepository.getAsteroidList()
            myRepository.getNasaPictureOfDay()
            asteroidList.addSource(todayAsteroidList) {
                asteroidList.value = it
            }
        }
    }

    fun onAsteroidSelected(asteroid: Asteroid) {
        _navigateToSelectedProperty.value = asteroid
    }

    fun onNavigateToDetailsScreenCompleted() {
        _navigateToSelectedProperty.value = null
    }



    fun onViewAsteroidsClicked(enumValue: MainFragment.MenuEnum) {
        removeSource()
        when (enumValue) {
            MainFragment.MenuEnum.WEEK -> {
                asteroidList.addSource(weekAsteroidList) {
                    asteroidList.value = it
                }
            }
            MainFragment.MenuEnum.TODAY -> {
                asteroidList.addSource(todayAsteroidList) {
                    asteroidList.value = it
                }
            }
            MainFragment.MenuEnum.SAVE -> {
                asteroidList.addSource(weekAsteroidList) {
                    asteroidList.value = it
                }
            }
        }
    }

    private fun removeSource() {
        asteroidList.removeSource(todayAsteroidList)
        asteroidList.removeSource(weekAsteroidList)
    }

}