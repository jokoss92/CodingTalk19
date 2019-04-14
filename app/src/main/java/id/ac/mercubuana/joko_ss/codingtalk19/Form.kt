package id.ac.mercubuana.joko_ss.codingtalk19

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import id.ac.mercubuana.joko_ss.codingtalk19.R
import id.ac.mercubuana.joko_ss.codingtalk19.database.UserRepository
import id.ac.mercubuana.joko_ss.codingtalk19.local.UserDataSource
import id.ac.mercubuana.joko_ss.codingtalk19.local.UserDatabase
import id.ac.mercubuana.joko_ss.codingtalk19.model.User
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.fragment_item.*

class Form : AppCompatActivity() {


    private var userRepository: UserRepository? = null
    private var compositeDisposable : CompositeDisposable? = null
    private lateinit var ambil : String
    private lateinit var ambilnama : String
    lateinit var adapter: ArrayAdapter<*>
    var userList: MutableList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        val tangkap = intent
        val ambil = tangkap.getStringExtra("kirim_id")
        val ambilnama = tangkap.getStringExtra("nama")

        compositeDisposable = CompositeDisposable()

        val userDatabase = UserDatabase.getIntance(this)
        userRepository = UserRepository.getInstance(UserDataSource.getInstance(userDatabase.userDAO()))

        tvEditId.text = ambil
        etNama.setText(ambilnama)

        btnEdit.setOnClickListener{
//            val user = User()
//            user.nama = etNama.text.toString()
            updateUser(etNama.text.toString(),ambil.toInt())
            finish()
        }

        btnDelete.setOnClickListener{

            deleteUser(ambil.toInt())
            finish()
        }
    }

    private fun deleteUser(id: Int){
        val disposable = io.reactivex.Observable.create(ObservableOnSubscribe<Any> {
                e -> userRepository!!.deleteUser(id)
            e.onComplete()
        })
            .observeOn(
                AndroidSchedulers.mainThread()
            )
            .subscribeOn(Schedulers.io())
            .subscribe(
                Consumer {
                    //Success
                },
                Consumer{
                        throwable -> Toast.makeText(this, ""+throwable.message, Toast.LENGTH_SHORT).show()
                }, Action {
                    Toast.makeText(this, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                })
        compositeDisposable!!.addAll(disposable)


    }

    private fun updateUser(nama: String, id: Int){
        val disposable = io.reactivex.Observable.create(ObservableOnSubscribe<Any> {
                e -> userRepository!!.updateUser(nama, id)
            e.onComplete()
        })
            .observeOn(
                AndroidSchedulers.mainThread()
            )
            .subscribeOn(Schedulers.io())
            .subscribe(
                Consumer {
                //Success
            },
                Consumer{
                        throwable -> Toast.makeText(this, ""+throwable.message, Toast.LENGTH_SHORT).show()
                }, Action {
                    Toast.makeText(this, "Data Berhasil Diupdate", Toast.LENGTH_SHORT).show()
                })
        compositeDisposable!!.addAll(disposable)


    }
    override fun onDestroy() {
        compositeDisposable?.clear()
        super.onDestroy()
    }
}
