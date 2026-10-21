package com.example.ejercicio1.data.repository

import com.example.ejercicio1.data.model.Product
import com.example.ejercicio1.data.remote.ProductApiService
import com.example.ejercicio1.data.remote.RetrofitClient

class ProductRepository(
    private val api: ProductApiService = RetrofitClient.servicio
) {
    suspend fun getProducts(): List<Product> = api.getProducts().products

    suspend fun getProduct(id: Int): Product = api.getProductById(id)

    suspend fun search(query: String): List<Product> = api.searchProducts(query).products

    suspend fun getCategories(): List<String> = api.getCategories()

    suspend fun getByCategory(category: String): List<Product> =
        api.getProductsByCategory(category).products
}