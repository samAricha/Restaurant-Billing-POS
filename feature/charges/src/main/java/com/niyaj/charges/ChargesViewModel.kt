package com.niyaj.charges

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import com.niyaj.common.result.Resource
import com.niyaj.data.repository.ChargesRepository
import com.niyaj.ui.event.BaseViewModel
import com.niyaj.ui.event.UiState
import com.niyaj.ui.utils.UiEvent
import com.samples.apps.core.analytics.AnalyticsEvent
import com.samples.apps.core.analytics.AnalyticsHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChargesViewModel @Inject constructor(
    private val chargesRepository: ChargesRepository,
    private val analyticsHelper: AnalyticsHelper,
) : BaseViewModel() {

    override var totalItems: List<Int> = emptyList()

    @OptIn(ExperimentalCoroutinesApi::class)
    val charges = snapshotFlow { searchText.value }
        .flatMapLatest { it ->
            chargesRepository.getAllCharges(it)
                .onStart { UiState.Loading }
                .map { items ->
                    totalItems = items.map { it.chargesId }
                    if (items.isEmpty()) {
                        UiState.Empty
                    } else UiState.Success(items)
                }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading,
        )

    override fun deleteItems() {
        super.deleteItems()

        viewModelScope.launch {
            when (chargesRepository.deleteCharges(selectedItems.toList())) {
                is Resource.Error -> {
                    mEventFlow.emit(UiEvent.OnError("Unable to delete charges"))
                }

                is Resource.Success -> {
                    mEventFlow.emit(UiEvent.OnSuccess("${selectedItems.size} charges has been deleted"))
                    analyticsHelper.logDeletedCharges(selectedItems.toList())
                }
            }

            mSelectedItems.clear()
        }
    }

}

internal fun AnalyticsHelper.logDeletedCharges(charges: List<Int>) {
    logEvent(
        event = AnalyticsEvent(
            type = "charges_deleted",
            extras = listOf(
                AnalyticsEvent.Param("charges_deleted", charges.toString()),
            ),
        ),
    )
}
