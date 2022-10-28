package com.example.glacticjourney.ui.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glacticjourney.domain.Repository
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

class GlacticViewModel() : ViewModel() {
    val repository: Repository = Repository()

    private val _passengerQuery = mutableStateOf("")
    val passengerQuery: State<String> = _passengerQuery

    private val _showLoading = mutableStateOf(false)
    val showLoading: State<Boolean> = _showLoading

    private val _glacticState = mutableStateOf(GlacticStarshipState())
    val glacticState: State<GlacticStarshipState> = _glacticState


    private var passengerJob: Job = SupervisorJob()
    fun getStarshipDetails(query: String) {
        _passengerQuery.value = query
        var number = 0
        try {
            number = query.toInt()
            if (query.isBlank() || number <= 0) {
                return
            }
        } catch (e: NumberFormatException) {

        }

        passengerJob?.cancel()
        passengerJob = viewModelScope.launch {
            delay(500L)
            _showLoading.value = true
            repository.returnSpaceships { glacticList ->
                _showLoading.value = false
                if (glacticList.isNotEmpty()) {
                    _glacticState.value = glacticState.value.copy(
                        wordInfoItem = glacticList.map {
                            if (it.passengers.contains(",")) {
                                it.passengers = it.passengers.replace(",", "")
                            }
                            it
                        }.filter { glacticResult ->
                            try {
                                !glacticResult.passengers.equals("n/a", ignoreCase = true)
                                    && glacticResult.passengers.toInt() >= number
                            } catch (e: Exception) {
                                false
                            }
                        },
                    )
                }
            }
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
        data class Loading(val message: String? = null, val isLoading: Boolean = false) : UIEvent()
    }
}