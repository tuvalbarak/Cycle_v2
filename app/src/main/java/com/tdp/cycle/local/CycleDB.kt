package com.tdp.cycle.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tdp.cycle.local.daos.UserDao
import com.tdp.cycle.models.User


@Database(
    entities = [
        User::class,
    ],
    version = 1
)
abstract class CycleDB : RoomDatabase() {

    abstract fun userDao(): UserDao

}