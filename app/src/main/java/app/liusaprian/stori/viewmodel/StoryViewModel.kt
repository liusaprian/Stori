package app.liusaprian.stori.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.liusaprian.stori.data.StoriRepository
import app.liusaprian.stori.network.response.Story
import kotlinx.coroutines.launch

class StoryViewModel(private val repository: StoriRepository) : ViewModel() {

    private val _stories = MutableLiveData<List<Story>>()
    val stories: LiveData<List<Story>> = _stories

    fun getStories() = viewModelScope.launch {
        _stories.postValue(repository.getStories())
    }
}