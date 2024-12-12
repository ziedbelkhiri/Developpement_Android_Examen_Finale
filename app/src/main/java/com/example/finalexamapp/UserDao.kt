package com.example.finalexamapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getAllUsers(): LiveData<List<User>>

    @Insert
    fun insertUser(user: User)
}
