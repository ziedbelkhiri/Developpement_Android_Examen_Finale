package com.example.finalexamapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  // Auto-generate id
    val name: String
)
