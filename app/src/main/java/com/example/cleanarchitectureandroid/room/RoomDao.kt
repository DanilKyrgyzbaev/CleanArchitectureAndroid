package com.example.cleanarchitectureandroid.room

import androidx.room.*
import com.example.cleanarchitectureandroid.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    @Query("SELECT * from user ORDER BY first_name ASC")
    fun getItems(): Flow<List<User>>

    @Query("SELECT * from user WHERE id = :id")
    fun getItem(id: Int): Flow<User>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: User)

    @Update
    suspend fun update(item: User)

    @Delete
    suspend fun delete(item: User)
}
