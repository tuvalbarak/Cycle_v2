package com.tdp.cycle.local.daos

import androidx.room.*
import com.tdp.cycle.models.User

@Dao
abstract class UserDao {

    @Insert
    abstract suspend fun insertUser(user: User)

    @Upsert
    abstract suspend fun upsertUser(user: User)

    @Update
    abstract suspend fun updateUser(user: User)

    @Delete
    abstract suspend fun deleteUser(user: User)

    @Query("SELECT * FROM users")
    abstract suspend fun getUser(): User

}