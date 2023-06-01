package com.niyaj.poposroom.features.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.niyaj.poposroom.features.addon_item.dao.AddOnItemDao
import com.niyaj.poposroom.features.addon_item.domain.model.AddOnItem
import com.niyaj.poposroom.features.address.dao.AddressDao
import com.niyaj.poposroom.features.address.domain.model.Address
import com.niyaj.poposroom.features.charges.dao.ChargesDao
import com.niyaj.poposroom.features.charges.domain.model.Charges
import com.niyaj.poposroom.features.common.utils.TimestampConverters

@Database(
    entities = [
        AddOnItem::class,
        Address::class,
        Charges::class,
    ],
    version = 1,
    autoMigrations = [],
    exportSchema = true,
)
@TypeConverters(TimestampConverters::class)
abstract class PoposDatabase : RoomDatabase() {
    abstract fun addOnItemDao(): AddOnItemDao
    abstract fun addressDao(): AddressDao
    abstract fun chargesDao(): ChargesDao
}