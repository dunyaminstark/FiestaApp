package com.dunyamin.fiestaapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dunyamin.fiestaapp.data.ProfilePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val profilePreferences = ProfilePreferences(application)

    private val _name = MutableStateFlow(profilePreferences.getName() ?: "John Doe")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _bio = MutableStateFlow(profilePreferences.getBio() ?: "Android Developer | Holiday Enthusiast")
    val bio: StateFlow<String> = _bio.asStateFlow()

    fun saveProfile(name: String, bio: String) {
        viewModelScope.launch {
            profilePreferences.saveProfile(name, bio)
            _name.value = name
            _bio.value = bio
        }
        // TODO: Implement actual data saving logic here (e.g., SharedPreferences, Database)
        println("Saving profile: Name = $name, Bio = $bio")
    }
}
