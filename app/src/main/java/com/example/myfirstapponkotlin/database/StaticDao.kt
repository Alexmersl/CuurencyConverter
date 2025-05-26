package com.example.myfirstapponkotlin.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StaticDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewStatisticData(vararg listOfCurrencies: ListOfCurrencies)

    @Query("SELECT currencyName, value FROM list_of_currencies;")
    fun getAllStatisticData(): List<StatisticInfoTuple>

    @Query("DELETE FROM list_of_currencies WHERE currencyName = :currencyNameId")
    fun deleteStatisticDataById(currencyNameId: String)

}