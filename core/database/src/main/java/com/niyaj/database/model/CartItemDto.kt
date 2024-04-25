package com.niyaj.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.niyaj.model.ProductAndQuantity

data class CartItemDto(
    @Embedded
    val cartOrder: CartOrderEntity,

    @Relation(
        parentColumn = "addressId",
        entityColumn = "addressId",
        entity = AddressEntity::class,
        projection = ["shortName"]
    )
    val customerAddress: String?,

    @Relation(
        parentColumn = "customerId",
        entityColumn = "customerId",
        entity = CustomerEntity::class,
        projection = ["customerPhone"]
    )
    val customerPhone: String?,

    @Relation(
        parentColumn = "orderId",
        entityColumn = "orderId",
        entity = CartEntity::class,
        projection = ["productId", "quantity"]
    )
    val cartItems: List<ProductAndQuantity>,

    @Relation(
        parentColumn = "orderId",
        entityColumn = "orderId",
        entity = CartPriceEntity::class,
        projection = ["totalPrice"]
    )
    val orderPrice: OrderTotalPrice,

    @Relation(
        parentColumn = "orderId",
        entity = AddOnItemEntity::class,
        entityColumn = "itemId",
        associateBy = Junction(CartAddOnItemsEntity::class),
        projection = ["itemId"]
    )
    val addOnItems: List<Int> = emptyList(),


    @Relation(
        parentColumn = "orderId",
        entity = ChargesEntity::class,
        entityColumn = "chargesId",
        associateBy = Junction(CartChargesEntity::class),
        projection = ["chargesId"]
    )
    val charges: List<Int> = emptyList()
)
