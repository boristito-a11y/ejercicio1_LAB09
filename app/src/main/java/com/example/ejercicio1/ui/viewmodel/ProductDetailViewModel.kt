package com.example.ejercicio1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ejercicio1.data.model.Product
import com.example.ejercicio1.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ProductDetailUiState {
    data object Loading : ProductDetailUiState
    data class Success(val product: Product) : ProductDetailUiState
    data class Error(val message: String) : ProductDetailUiState
}

class ProductDetailViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    fun load(id: Int) {
        _uiState.value = ProductDetailUiState.Loading
        viewModelScope.launch {
            _uiState.value = try {
                ProductDetailUiState.Success(repository.getProduct(id))
            } catch (e: Exception) {
                ProductDetailUiState.Error("No se pudo cargar el producto")
            }
        }
    }
}