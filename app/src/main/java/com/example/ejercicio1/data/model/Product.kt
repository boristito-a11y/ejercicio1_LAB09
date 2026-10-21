package com.example.ejercicio1.data.model

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("products") val products: List<Product>,
    @SerializedName("total") val total: Int,
    @SerializedName("skip") val skip: Int,
    @SerializedName("limit") val limit: Int
)

data class Product(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("price") val price: Double,
    @SerializedName("discountPercentage") val discountPercentage: Double,
    @SerializedName("rating") val rating: Double,
    @SerializedName("stock") val stock: Int,
    @SerializedName("tags") val tags: List<String>?,
    @SerializedName("brand") val brand: String?,
    @SerializedName("warrantyInformation") val warrantyInformation: String?,
    @SerializedName("shippingInformation") val shippingInformation: String?,
    @SerializedName("availabilityStatus") val availabilityStatus: String?,
    @SerializedName("returnPolicy") val returnPolicy: String?,
    @SerializedName("reviews") val reviews: List<Review>?,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("images") val images: List<String>?
) {
    // Precio original calculado a partir del descuento
    val originalPrice: Double
        get() = price / (1 - discountPercentage / 100)
}

data class Review(
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("date") val date: String,
    @SerializedName("reviewerName") val reviewerName: String
)