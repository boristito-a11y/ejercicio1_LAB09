package com.example.ejercicio1.data.remote

import com.example.ejercicio1.data.model.Product
import com.example.ejercicio1.data.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {
    @GET("products")
    suspend fun getProducts(@Query("limit") limit: Int = 100): ProductResponse

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Product

    @GET("products/search")
    suspend fun searchProducts(@Query("q") query: String): ProductResponse

    @GET("products/category-list")
    suspend fun getCategories(): List<String>

    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): ProductResponse
}