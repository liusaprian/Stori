package app.liusaprian.stori.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.liusaprian.stori.data.StoriRepository
import app.liusaprian.stori.network.response.FileUploadResponse
import app.liusaprian.stori.network.response.Story
import app.liusaprian.stori.network.response.StoryResponse
import kotlinx.coroutines.launch
import java.io.File

class StoryViewModel(private val repository: StoriRepository) : ViewModel() {

    private val _stories = MutableLiveData<StoryResponse>()
    val stories: LiveData<StoryResponse> = _stories

    private val _uploadSuccess = MutableLiveData<FileUploadResponse?>()
    val uploadSuccess: LiveData<FileUploadResponse?> = _uploadSuccess

    fun getStories() = viewModelScope.launch {
        _stories.postValue(repository.getStories())
    }

    fun addStory(file: File, desc: String) = viewModelScope.launch {
        _uploadSuccess.postValue(repository.addStory(file, desc))
    }

    fun saveStories(stories: List<Story>) {
        repository.saveStoryList(stories)
    }
}