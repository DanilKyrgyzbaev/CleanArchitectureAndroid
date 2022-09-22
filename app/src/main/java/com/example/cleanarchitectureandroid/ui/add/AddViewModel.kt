package com.example.cleanarchitectureandroid.ui.add

import androidx.lifecycle.*
import com.example.cleanarchitectureandroid.model.User
import com.example.cleanarchitectureandroid.room.RoomDao
import kotlinx.coroutines.launch

class AddViewModel(private val roomDao: RoomDao) : ViewModel() {

    val allItems: LiveData<List<User>> = roomDao.getItems().asLiveData()

    //Обновить
    fun updateItem(id: Int, role: String, name: String, lastName: String, age: Int) {
        val updatedItem = getUpdatedItemEntry(id,role, name, lastName, age)
        updateItem(updatedItem)
    }

    //обновить элемент
    fun updateItem(item: User) {
        viewModelScope.launch {
            roomDao.update(item)
        }
    }

    fun sellItem(item: User) {
        if (item.age > 0) {
            // Decrease the quantity by 1
            val newItem = item.copy(age = item.age - 1)
            updateItem(newItem)
        }
    }


    private fun getUpdatedItemEntry(
        id: Int,
        role: String,
        name: String,
        lastName: String,
        age: Int
    ): User {
        return User(
            id = id,
            role = role,
            firstName = name,
            lastName = lastName,
            age = age
        )
    }

    fun addNewItem(role: String, name: String, lastName: String, age: Int) {
        val newItem = getNewItemEntry(role,name, lastName, age)
        insertItem(newItem)
    }

    private fun insertItem(item: User) {
        viewModelScope.launch {
            roomDao.insert(item)
        }
    }

    fun deleteItem(item: User) {
        viewModelScope.launch {
            roomDao.delete(item)
        }
    }

    fun retrieveItem(id: Int): LiveData<User> {
        return roomDao.getItem(id).asLiveData()
    }

    fun isEntryValid( role: String, name: String, lastName: String, age: Int): Boolean {
        if (role.isBlank()||name.isBlank() || lastName.isBlank() || age.toString().isBlank()) {
            return false
        }
        return true
    }


    private fun getNewItemEntry(role: String, name: String, lastName: String, age: Int): User {
        return User(
            role = role,
            firstName = name,
            lastName = lastName,
            age = age
        )
    }
}

class InventoryViewModelFactory(private val itemDao: RoomDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
