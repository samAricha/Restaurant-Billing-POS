/*
 *      Copyright 2024 Sk Niyaj Ali
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *              http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */

package com.niyaj.data.mapper

import com.niyaj.database.model.MarketListWithTypeEntity
import com.niyaj.model.MarketListWithType

fun MarketListWithType.toEntity(marketId: Int) = MarketListWithTypeEntity(
    listWithTypeId = listWithTypeId,
    marketId = marketId,
    typeId = typeId,
    listType = listType,
)

fun List<MarketListWithType>.toExternalModel(marketId: Int): List<MarketListWithTypeEntity> {
    return map {
        MarketListWithTypeEntity(
            marketId = marketId,
            listWithTypeId = it.listWithTypeId,
            typeId = it.typeId,
            listType = it.listType,
        )
    }
}