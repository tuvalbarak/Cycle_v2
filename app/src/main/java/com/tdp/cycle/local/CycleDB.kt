package com.tdp.cycle.local

import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import com.tdp.cycle.local.daos.UserDao
import com.tdp.cycle.models.cycle_server.User


@Database(
    entities = [
        TempMock::class,
    ],
    version = 1
)
abstract class CycleDB : RoomDatabase() {

    abstract fun userDao(): UserDao

}

@Entity
data class TempMock(
    @PrimaryKey(autoGenerate = true) val id: Long?
)