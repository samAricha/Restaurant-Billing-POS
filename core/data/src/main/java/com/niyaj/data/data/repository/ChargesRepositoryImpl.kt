package com.niyaj.data.data.repository

import com.niyaj.common.network.Dispatcher
import com.niyaj.common.network.PoposDispatchers
import com.niyaj.common.result.Resource
import com.niyaj.common.result.ValidationResult
import com.niyaj.data.mapper.toEntity
import com.niyaj.data.repository.ChargesRepository
import com.niyaj.data.repository.validation.ChargesValidationRepository
import com.niyaj.database.model.asExternalModel
import com.niyaj.model.Charges
import com.niyaj.model.searchCharges
import com.niyaj.poposroom.features.charges.data.dao.ChargesDao
import com.niyaj.common.tags.ChargesTestTags
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

class ChargesRepositoryImpl(
    private val chargesDao: ChargesDao,
    @Dispatcher(PoposDispatchers.IO)
    private val ioDispatcher: CoroutineDispatcher
) : ChargesRepository, ChargesValidationRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getAllCharges(searchText: String): Flow<List<Charges>> {
        return withContext(ioDispatcher) {
            chargesDao.getAllCharges().mapLatest { it->
                it.map {
                    it.asExternalModel()
                }.searchCharges(searchText)
            }
        }
    }

    override suspend fun getChargesById(chargesId: Int): Resource<Charges?> {
        return try {
            withContext(ioDispatcher) {
                Resource.Success(chargesDao.getChargesById(chargesId)?.asExternalModel())
            }
        }catch (e: Exception) {
            Resource.Error(e.message)
        }
    }

    override suspend fun addOrIgnoreCharges(newCharges: Charges): Resource<Boolean> {
        return try {
            val validateChargesName = validateChargesName(newCharges.chargesName, newCharges.chargesId)
            val validateChargesPrice = validateChargesPrice(newCharges.chargesPrice)

            val hasError = listOf(validateChargesName, validateChargesPrice).any { !it.successful }

            if (!hasError) {
                withContext(ioDispatcher){
                    val result = chargesDao.insertOrIgnoreCharges(newCharges.toEntity())

                    Resource.Success(result > 0)
                }
            }else{
                Resource.Error( "Unable to create Charges Item")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error creating Charges Item")
        }
    }

    override suspend fun updateCharges(newCharges: Charges): Resource<Boolean> {
        return try {
            val validateChargesName = validateChargesName(newCharges.chargesName, newCharges.chargesId)
            val validateChargesPrice = validateChargesPrice(newCharges.chargesPrice)

            val hasError = listOf(validateChargesName, validateChargesPrice).any { !it.successful }

            if (!hasError) {
                withContext(ioDispatcher){
                    val result = chargesDao.updateCharges(newCharges.toEntity())

                    Resource.Success(result > 0)
                }
            }else{
                Resource.Error( "Unable to update Charges Item")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error updating Charges Item")
        }
    }

    override suspend fun upsertCharges(newCharges: Charges): Resource<Boolean> {
        return try {
            val validateChargesName = validateChargesName(newCharges.chargesName, newCharges.chargesId)
            val validateChargesPrice = validateChargesPrice(newCharges.chargesPrice)

            val hasError = listOf(validateChargesName, validateChargesPrice).any { !it.successful }

            if (!hasError) {
                withContext(ioDispatcher){
                    val result = chargesDao.upsertCharges(newCharges.toEntity())

                    Resource.Success(result > 0)
                }
            }else{
                Resource.Error( "Unable to  or update Charges Item")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error creating Charges Item")
        }
    }

    override suspend fun deleteCharges(chargesId: Int): Resource<Boolean> {
        return try {
            withContext(ioDispatcher){
                val result = chargesDao.deleteCharges(chargesId)

                Resource.Success(result > 0)
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error deleting Charges Item")
        }
    }

    override suspend fun deleteCharges(chargesIds: List<Int>): Resource<Boolean> {
        return try {
            withContext(ioDispatcher){
                val result = chargesDao.deleteCharges(chargesIds)

                Resource.Success(result > 0)
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error deleting Charges Item")
        }
    }

    override suspend fun validateChargesName(
        chargesName: String,
        chargesId: Int?
    ): ValidationResult {
        if(chargesName.isEmpty()) {
            return ValidationResult(
                successful = false,
                errorMessage = ChargesTestTags.CHARGES_NAME_EMPTY_ERROR,
            )
        }

        if(chargesName.length < 5 ){
            return ValidationResult(
                successful = false,
                errorMessage = ChargesTestTags.CHARGES_NAME_LENGTH_ERROR,
            )
        }

        if (chargesName.any { it.isDigit() }){
            return ValidationResult(
                successful = false,
                errorMessage = ChargesTestTags.CHARGES_NAME_DIGIT_ERROR,
            )
        }

        val serverResult = withContext(ioDispatcher) {
            chargesDao.findChargesByName(chargesId, chargesName) != null
        }

        if (serverResult){
            return ValidationResult(
                successful = false,
                errorMessage = ChargesTestTags.CHARGES_NAME_ALREADY_EXIST_ERROR,
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    override fun validateChargesPrice(
        chargesPrice: Int
    ): ValidationResult {
        if(chargesPrice == 0){
            return ValidationResult(
                successful = false,
                errorMessage = ChargesTestTags.CHARGES_PRICE_EMPTY_ERROR
            )
        }

        if(chargesPrice < 10){
            return ValidationResult(
                successful = false,
                errorMessage = ChargesTestTags.CHARGES_PRICE_LESS_THAN_TEN_ERROR
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}