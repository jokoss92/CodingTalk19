package id.ac.mercubuana.joko_ss.codingtalk19.local

import id.ac.mercubuana.joko_ss.codingtalk19.database.IUserDataSource
import id.ac.mercubuana.joko_ss.codingtalk19.model.User
import io.reactivex.Flowable

class UserDataSource(private val userDao: UserDao): IUserDataSource {
    override fun updateUser(nama: String, id: Int) {
        userDao.updateUser(nama, id)
    }

    override fun deleteUser(id: Int) {
    userDao.deleteUser(id)
    }

    override val allUsers: Flowable<List<User>>
        get() = userDao.allUsers

    override fun insertUser(vararg users: User) {
        userDao.insertUser(*users)
    }



    companion object {
        private var mInstance: UserDataSource? = null
        fun getInstance(userDao: UserDao): UserDataSource{
            if (mInstance == null){
                mInstance = UserDataSource(userDao)
            }
            return mInstance!!
        }
    }
}