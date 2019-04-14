package id.ac.mercubuana.joko_ss.codingtalk19

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import id.ac.mercubuana.joko_ss.codingtalk19.database.UserRepository
import id.ac.mercubuana.joko_ss.codingtalk19.local.UserDataSource
import id.ac.mercubuana.joko_ss.codingtalk19.local.UserDatabase
import id.ac.mercubuana.joko_ss.codingtalk19.model.User
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_item_list.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var userRepository: UserRepository? = null
    private var compositeDisposable : CompositeDisposable? = null
    lateinit var adapter: ArrayAdapter<*>
    var userList: MutableList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_item_list)

        compositeDisposable = CompositeDisposable()

        val userDatabase = UserDatabase.getIntance(this)
        userRepository = UserRepository.getInstance(UserDataSource.getInstance(userDatabase.userDAO()))

//        val adduser  = User()
//        adduser.nama = "Doni"
//        insertUser(adduser)



        loadData()

    }

    private fun insertUser(user: User){
      val disposable = io.reactivex.Observable.create(ObservableOnSubscribe<Any> {
          e -> userRepository!!.insertUser(user)
          e.onComplete()
      })
          .observeOn(
              AndroidSchedulers.mainThread()
          )
          .subscribeOn(Schedulers.io())
          .subscribe(Consumer {
              //Success
          },
              Consumer{
                      throwable -> Toast.makeText(this, ""+throwable.message,Toast.LENGTH_SHORT).show()
              }, Action {
                  Toast.makeText(this, "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show()
              })
        compositeDisposable!!.addAll(disposable)

    }

    private fun onGetAllUserSuccess(users: List<User>){
        userList.clear()
        userList.addAll(users)
        adapter.notifyDataSetChanged()
    }

    private fun loadData(){
        val disposable = userRepository!!.allUsers
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({users -> showData(users)}){
                throwable -> Toast.makeText(this, ""+throwable.message, Toast.LENGTH_SHORT).show()
            }
        compositeDisposable!!.add(disposable)
    }
//
    private fun showData(items: List<User>?){
        list.adapter = UserRecyclerViewAdapter(items, object : UserRecyclerViewAdapter.OnListFragmentInteractionListener{
            override fun onListFragmentInteraction(item: User?) {
                val kirim = Intent(this@MainActivity, Form::class.java)
                kirim.putExtra("kirim_id", item?.id.toString())
                kirim.putExtra("nama", item?.nama)
                startActivity(kirim)

            }
        })
    }

    override fun onDestroy() {
        compositeDisposable?.clear()
        super.onDestroy()
    }
}
