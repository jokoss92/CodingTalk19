package id.ac.mercubuana.joko_ss.codingtalk19.local

import android.arch.persistence.room.*
import id.ac.mercubuana.joko_ss.codingtalk19.model.User
import io.reactivex.Flowable

@Dao
interface UserDao {
    @get:Query("SELECT * FROM users")
    val allUsers: Flowable<List<User>>

    @Insert
    fun insertUser(vararg users: User)

    @Query(" UPDATE  users SET nama =:nama WHERE id=:id")
    fun updateUser(nama : String, id : Int)

    @Query(" DELETE FROM  users WHERE id=:id")
    fun deleteUser(id : Int)
}