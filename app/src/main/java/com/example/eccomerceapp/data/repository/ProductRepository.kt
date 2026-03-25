package com.example.eccomerceapp.data.repository

import com.example.eccomerceapp.data.api.ProductApi
import com.example.eccomerceapp.data.model.Product
import com.example.eccomerceapp.data.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(private val productApi: ProductApi) {

    suspend fun getProducts(page: Int = 1, limit: Int = 20): Result<List<Product>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = productApi.getProducts(page, limit)
                if (response.isSuccessful && response.body() != null) {
                    Result.Success(response.body()!!)
                } else {
                    Result.Error("Erro ao buscar produtos: ${response.message()}")
                }
            } catch (e: Exception) {
                Result.Error("Erro de conexão: ${e.message}", e)
            }
        }
    }

    suspend fun getProductById(id: String): Result<Product> {
        return withContext(Dispatchers.IO) {
            try {
                val response = productApi.getProductById(id)
                if (response.isSuccessful && response.body() != null) {
                    Result.Success(response.body()!!)
                } else {
                    Result.Error("Erro ao buscar produto: ${response.message()}")
                }
            } catch (e: Exception) {
                Result.Error("Erro de conexão: ${e.message}", e)
            }
        }
    }

    suspend fun getProductsByCategory(category: String): Result<List<Product>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = productApi.getProductsByCategory(category)
                if (response.isSuccessful && response.body() != null) {
                    Result.Success(response.body()!!)
                } else {
                    Result.Error("Erro ao buscar produtos: ${response.message()}")
                }
            } catch (e: Exception) {
                Result.Error("Erro de conexão: ${e.message}", e)
            }
        }
    }
}
