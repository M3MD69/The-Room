package nullexia.m3md69.theroom.room.repository

import androidx.lifecycle.LiveData
import nullexia.m3md69.theroom.room.dao.UserDao
import nullexia.m3md69.theroom.room.model.User

class UserRepository(private val userDao: UserDao) {
    val readAllData: LiveData<List<User>> = userDao.readAllData()
    fun getUserById(userId: Int): LiveData<User> = userDao.getUserById(userId)
    suspend fun addUser(user: User) = userDao.addUser(user)
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
    fun deleteAllUsers() = userDao.deleteAllUsers()
}