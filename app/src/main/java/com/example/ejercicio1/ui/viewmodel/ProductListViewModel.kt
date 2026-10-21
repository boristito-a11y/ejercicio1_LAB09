package com.example.ejercicio1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ejercicio1.data.model.Product
import com.example.ejercicio1.data.repository.ProductRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductListUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val products: List<Product> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val query: String = ""
)

class ProductListViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadCategories()
        loadProducts()
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query, selectedCategory = null) }
        // Espera a que el usuario deje de escribir antes de consultar el API
        loadProducts(debounceMs = 400)
    }

    fun onCategorySelected(category: String?) {
        _uiState.update { it.copy(selectedCategory = category, query = "") }
        loadProducts()
    }

    fun retry() {
        if (_uiState.value.categories.isEmpty()) loadCategories()
        loadProducts()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            runCatching { repository.getCategories() }
                .onSuccess { categories -> _uiState.update { it.copy(categories = categories) } }
        }
    }

    private fun loadProducts(debounceMs: Long = 0) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            if (debounceMs > 0) delay(debounceMs)
            _uiState.update { it.copy(isLoading = true, error = null) }
            val state = _uiState.value
            try {
                val products = when {
                    state.query.isNotBlank() -> repository.search(state.query)
                    state.selectedCategory != null -> repository.getByCategory(state.selectedCategory)
                    else -> repository.getProducts()
                }
                _uiState.update { it.copy(isLoading = false, products = products) }
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) throw e
                _uiState.update {
                    it.copy(isLoading = false, error = "No se pudieron cargar los productos")
                }
            }
        }
    }
}