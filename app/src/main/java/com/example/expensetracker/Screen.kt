package com.example.expensetracker


/*
import backend.model.RepositoryResponse
import backend.model.User
import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.http.*
import kotlinx.coroutines.flow.firstOrNull

class ProfileRepository(database: MongoDatabase) {

    private val userCollection = database.getCollection<User>("users")

    suspend fun getProfile(email: String): RepositoryResponse<User> {
        val user = userCollection.find(Filters.eq("email", email)).firstOrNull()
        return if (user == null) {
            RepositoryResponse(status = 404, message = "User not found")
        } else {
            RepositoryResponse(status = 200, message = "Profile fetched", data = user)
        }
    }

    suspend fun deleteProfile(email: String): RepositoryResponse<Boolean> {

        val user = userCollection.findOneAndDelete(Filters.eq("email", email))

        return if (user == null) {
            RepositoryResponse(status = 404, message = "User not found")
        } else {
            RepositoryResponse(status = 200, message = "Profile Deleted", data = true)
        }

    }

    suspend fun updateProfileName(email: String, name: String): RepositoryResponse<User> {

        if (name.isEmpty()) {
            return RepositoryResponse(
                message = "No fields to update",
                status = HttpStatusCode.BadRequest.value
            )
        }

        val updatedUser = userCollection.findOneAndUpdate(
            Filters.eq("email", email),
            update = Updates.set("name", name),
            options = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
        )

        return if (updatedUser != null) {
            RepositoryResponse(
                data = updatedUser,
                message = "Profile Updated Successfully",
                status = HttpStatusCode.OK.value
            )
        } else {
            RepositoryResponse(
                data = null,
                message = "User not found",
                status = HttpStatusCode.NotFound.value
            )
        }
    }

}

 */