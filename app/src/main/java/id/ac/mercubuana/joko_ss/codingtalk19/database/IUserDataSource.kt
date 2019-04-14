package id.ac.mercubuana.joko_ss.codingtalk19.database

import id.ac.mercubuana.joko_ss.codingtalk19.model.User
import io.reactivex.Flowable

interface IUserDataSource {
    val allUsers: Flowable<List<User>>
    fun insertUser(vararg users: User)
    fun updateUser(nama : String, id : Int)
    fun deleteUser(id : Int)
}