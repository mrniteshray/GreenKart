package appxyz.greenkart.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import appxyz.greenkart.domain.model.User
import appxyz.greenkart.domain.repository.AuthRepository
import appxyz.greenkart.domain.util.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override fun getCurrentUser(): User? {
        val firebaseUser = firebaseAuth.currentUser
        return if (firebaseUser != null) {
            // Note: In a real app we would load phone and address from Firestore here
            // Because FirebaseAuth only stores Email, DisplayName, PhotoUrl
            User(
                id = firebaseUser.uid,
                name = firebaseUser.displayName ?: "",
                email = firebaseUser.email ?: "",
                phone = "",
                address = ""
            )
        } else {
            null
        }
    }

    override suspend fun login(email: String, password: String): Flow<Resource<User>> = callbackFlow {
        trySend(Resource.Loading())
        
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = task.result?.user
                    if (firebaseUser != null) {
                        firestore.collection("users").document(firebaseUser.uid).get()
                            .addOnSuccessListener { document ->
                                if (document != null && document.exists()) {
                                    val user = User(
                                        id = firebaseUser.uid,
                                        name = document.getString("name") ?: firebaseUser.displayName ?: "",
                                        email = document.getString("email") ?: firebaseUser.email ?: email,
                                        phone = document.getString("phone") ?: "",
                                        address = document.getString("address") ?: ""
                                    )
                                    trySend(Resource.Success(user))
                                } else {
                                    // Fallback if no document
                                    val user = User(
                                        id = firebaseUser.uid,
                                        name = firebaseUser.displayName ?: "",
                                        email = firebaseUser.email ?: email,
                                        phone = "",
                                        address = ""
                                    )
                                    trySend(Resource.Success(user))
                                }
                            }
                            .addOnFailureListener {
                                val user = User(
                                    id = firebaseUser.uid,
                                    name = firebaseUser.displayName ?: "",
                                    email = firebaseUser.email ?: email,
                                    phone = "",
                                    address = ""
                                )
                                trySend(Resource.Success(user))
                            }
                    } else {
                        trySend(Resource.Error("User login failed, null user returned"))
                    }
                } else {
                    trySend(Resource.Error(task.exception?.localizedMessage ?: "Login failed"))
                }
            }
        
        awaitClose { /* No explicit cleanup needed */ }
    }

    override suspend fun signup(name: String, email: String, phone: String, address: String, password: String): Flow<Resource<User>> = callbackFlow {
        trySend(Resource.Loading())
        
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    val firebaseUser = authTask.result?.user
                    if (firebaseUser != null) {
                        val user = User(
                            id = firebaseUser.uid,
                            name = name,
                            email = email,
                            phone = phone,
                            address = address
                        )
                        
                        // Save to firestore
                        firestore.collection("users").document(user.id).set(user)
                            .addOnCompleteListener { firestoreTask ->
                                if (firestoreTask.isSuccessful) {
                                    trySend(Resource.Success(user))
                                } else {
                                    trySend(Resource.Error(firestoreTask.exception?.localizedMessage ?: "Failed to save user info to Firestore"))
                                }
                            }
                    } else {
                        trySend(Resource.Error("User data is null after signup"))
                    }
                } else {
                    trySend(Resource.Error(authTask.exception?.localizedMessage ?: "Signup failed"))
                }
            }
            
        awaitClose { /* No explicit cleanup needed */ }
    }

    override suspend fun fetchCurrentUserDetails(): Flow<Resource<User>> = callbackFlow {
        trySend(Resource.Loading())
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            firestore.collection("users").document(firebaseUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val user = User(
                            id = firebaseUser.uid,
                            name = document.getString("name") ?: firebaseUser.displayName ?: "",
                            email = document.getString("email") ?: firebaseUser.email ?: "",
                            phone = document.getString("phone") ?: "",
                            address = document.getString("address") ?: ""
                        )
                        trySend(Resource.Success(user))
                    } else {
                        trySend(Resource.Error("User profile not found."))
                    }
                }
                .addOnFailureListener { e ->
                    trySend(Resource.Error(e.message ?: "Failed to fetch user details"))
                }
        } else {
            trySend(Resource.Error("No user logged in"))
        }
        awaitClose { /* cleanup */ }
    }

    override suspend fun updateUserDetails(phone: String, address: String): Flow<Resource<User>> = callbackFlow {
        trySend(Resource.Loading())
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            val updates = hashMapOf<String, Any>(
                "phone" to phone,
                "address" to address
            )
            firestore.collection("users").document(firebaseUser.uid).update(updates)
                .addOnSuccessListener {
                    // Fetch fresh details
                    firestore.collection("users").document(firebaseUser.uid).get()
                        .addOnSuccessListener { document ->
                            if (document != null && document.exists()) {
                                val user = User(
                                    id = firebaseUser.uid,
                                    name = document.getString("name") ?: firebaseUser.displayName ?: "",
                                    email = document.getString("email") ?: firebaseUser.email ?: "",
                                    phone = document.getString("phone") ?: "",
                                    address = document.getString("address") ?: ""
                                )
                                trySend(Resource.Success(user))
                            } else {
                                trySend(Resource.Error("User profile not found after update."))
                            }
                        }
                        .addOnFailureListener { e ->
                            trySend(Resource.Error(e.message ?: "Failed to fetch updated details"))
                        }
                }
                .addOnFailureListener { e ->
                    trySend(Resource.Error(e.message ?: "Failed to update profile"))
                }
        } else {
            trySend(Resource.Error("No user logged in"))
        }
        awaitClose { /* cleanup */ }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }
}

