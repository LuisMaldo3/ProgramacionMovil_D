package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.huellitasvet.data.repository.VetRepository

/** [ENTREGADO] Agrega los otros ViewModels en los siguientes pasos. */
class VetViewModelFactory(private val repo: VetRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(ServiciosViewModel::class.java) -> ServiciosViewModel(repo) as T
        else -> throw IllegalArgumentException("ViewModel no registrado: ${modelClass.name}")
    }
}
