package com.example.groceryapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.groceryapp.auth.Login
import com.example.groceryapp.databinding.ActivityMain2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private lateinit var uid: String
    private lateinit var listAdapter: ListAdapter
    private lateinit var childEventListener: ChildEventListener
    private var count: String = "0"
    private lateinit var dataBaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main2)
        uid = FirebaseAuth.getInstance().currentUser?.uid!!
        dataBaseReference = FirebaseDatabase.getInstance().reference.child("list/$uid/")
        listAdapter = ListAdapter(this, 1, ArrayList())
        binding.listListView.adapter = listAdapter
        binding.listCreateButton.setOnClickListener() {
            binding.apply {
                listCreateButton.visibility = View.INVISIBLE
                listEditLayout.visibility = View.VISIBLE
            }
        }
        binding.listAddButton.setOnClickListener() {
            val intent: Intent = Intent(this, MainActivity::class.java)
            intent.putExtra("listName",
                if (binding.listAddList.text.toString().isEmpty())
                    "Default List"
                else binding.listAddList.text.toString()
            )
            binding.listAddList.setText("")
            startActivity(intent)
        }
        binding.mainMenu.setOnClickListener { view: View ->
            val popup = PopupMenu(this, view)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.menu_main, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_log_out -> {
                        Firebase.auth.signOut()
                        startActivity(Intent(this, Login::class.java))
                        finish()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            listCreateButton.visibility = View.VISIBLE
            listEditLayout.visibility = View.INVISIBLE
        }
        attachDatabase()
    }

    private fun attachDatabase() {
        childEventListener = object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var shoppingList: ShoppingList = ShoppingList()
                shoppingList.listName = snapshot.key
                shoppingList.listItemCount = snapshot.childrenCount.toString()
                if (shoppingList.listName == "Default List")
                    count = shoppingList.listItemCount.toString()
                listAdapter.add(shoppingList)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

        }
        dataBaseReference.addChildEventListener(childEventListener)
    }

    override fun onStop() {
        super.onStop()
        detachDatabase()
    }

    private fun detachDatabase() {
        dataBaseReference.removeEventListener(childEventListener)
        listAdapter.clear()
    }

}