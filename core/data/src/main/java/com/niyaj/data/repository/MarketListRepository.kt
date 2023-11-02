package com.niyaj.data.repository

import com.niyaj.common.result.Resource
import com.niyaj.common.result.ValidationResult
import com.niyaj.model.MarketItemAndQuantity
import com.niyaj.model.MarketItemWithQuantity
import com.niyaj.model.MarketList
import com.niyaj.model.MarketListWithItems
import com.niyaj.model.MarketListWithItemsAndQuantity
import kotlinx.coroutines.flow.Flow

interface MarketListRepository {

    suspend fun getAllMarketLists(searchText: String): Flow<List<MarketListWithItems>>

    suspend fun getMarketListById(marketId: Int): Flow<MarketList?>

    suspend fun getMarketItemsWithQuantityById(marketId: Int, searchText: String): Flow<List<MarketItemWithQuantity>>

    suspend fun getMarketItemsAndQuantity(marketId: Int): Flow<List<MarketItemAndQuantity>>

    suspend fun addOrIgnoreMarketList(newMarketList: MarketList): Resource<Boolean>

    suspend fun updateMarketList(newMarketList: MarketList): Resource<Boolean>

    suspend fun upsertMarketList(newMarketList: MarketList): Resource<Boolean>

    suspend fun deleteMarketList(marketId: Int): Resource<Boolean>

    suspend fun deleteMarketLists(marketIds: List<Int>): Resource<Boolean>

    suspend fun importMarketListsToDatabase(marketLists: List<MarketList>): Resource<Boolean>

    suspend fun addMarketListItem(marketId: Int, itemId: Int): Resource<Boolean>

    suspend fun removeMarketListItem(marketId: Int, itemId: Int): Resource<Boolean>

    suspend fun increaseMarketListItemQuantity(marketId: Int, itemId: Int): Resource<Boolean>

    suspend fun decreaseMarketListItemQuantity(marketId: Int, itemId: Int): Resource<Boolean>

    suspend fun addOrUpdateMarketListItems(listWithItems: MarketListWithItemsAndQuantity): Resource<Boolean>

    fun validateItemQuantity(quantity: String): ValidationResult

    fun validateMarketDate(date: Long): ValidationResult

}