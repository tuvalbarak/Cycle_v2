package com.tdp.cycle.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey
    val id: String,
    val email: String?,
    val name: String?,
    val phoneNumber: String?,
    val photoUrl: String?,
)

data class UserServer(
    val email: String?,
    val first_name: String?,
    val last_ame: String?,
    val usermame: String?,
    val password: String?
)