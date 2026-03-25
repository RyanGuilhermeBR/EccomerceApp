package com.example.eccomerceapp.data.model

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val originalPrice: Double? = null, // Preço original (sem desconto)
    val discountPercentage: Int? = null, // Porcentagem de desconto
    val installments: Int = 10, // Número de parcelas (padrão 10x)
    val imageUrl: String,
    val category: String,
    val stock: Int,
    val rating: Float = 0f
) {
    // Calcula o valor da parcela
    fun getInstallmentValue(): Double = price / installments

    // Verifica se tem desconto
    fun hasDiscount(): Boolean = originalPrice != null && originalPrice > price
}

