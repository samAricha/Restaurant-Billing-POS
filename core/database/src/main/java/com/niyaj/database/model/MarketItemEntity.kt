package com.niyaj.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.niyaj.model.MarketItem

@Entity(tableName = "market_item")
data class MarketItemEntity(

    @PrimaryKey(autoGenerate = true)
    val itemId: Int = 0,

    val itemType: String,

    val itemName: String,

    val itemPrice: String? = null,

    val itemDescription: String? = null,

    @Embedded
    val itemMeasureUnit: MeasureUnitEntity?,

    val createdAt: Long,

    val updatedAt: Long? = null
)


fun MarketItemEntity.asExternalModel(): MarketItem {
    return MarketItem(
        itemId = itemId,
        itemType = itemType,
        itemName = itemName,
        itemPrice = itemPrice,
        itemDescription = itemDescription,
        itemMeasureUnit = itemMeasureUnit?.asExternalModel(),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}