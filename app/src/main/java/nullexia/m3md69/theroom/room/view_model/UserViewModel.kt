package nullexia.m3md69.theroom.room.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nullexia.m3md69.theroom.room.database.UserDatabase
import nullexia.m3md69.theroom.room.model.User
import nullexia.m3md69.theroom.room.repository.UserRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<User>>
    private val repository: UserRepository

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        readAllData = repository.readAllData
    }

    fun getUserById(userId: Int): LiveData<User> = repository.getUserById(userId)

    fun addUser(user: User) = viewModelScope.launch(Dispatchers.IO) { repository.addUser(user) }

    fun updateUser(user: User) = viewModelScope.launch(Dispatchers.IO) { repository.updateUser(user) }

    fun deleteUser(user: User) = viewModelScope.launch(Dispatchers.IO) { repository.deleteUser(user) }

    fun deleteAllUsers() = viewModelScope.launch(Dispatchers.IO) { repository.deleteAllUsers() }
}