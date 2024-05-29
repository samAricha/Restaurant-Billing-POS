/*
 * Copyright 2024 Sk Niyaj Ali
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.niyaj.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.niyaj.database.PoposDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesPoposDatabase(
        @ApplicationContext context: Context,
    ): PoposDatabase = Room.databaseBuilder(
        context,
        PoposDatabase::class.java,
        "popos-database",
    ).fallbackToDestructiveMigration()
//        .addCallback(CALLBACK)
        .build()

    private val CALLBACK = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            db.execSQL(
                """
            CREATE TRIGGER[IF NOT EXISTS] product_with_quantity_trigger
            AFTER INSERT, UPDATE, DELETE ON cart
            BEGIN
                REFRESH VIEW product_with_quantity;
            END;
                """.trimIndent(),
            )
        }
    }
}
