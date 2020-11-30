package com.example.groceryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.groceryapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var uid: String
    private lateinit var listName: String
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var childEventListener: ChildEventListener
    private lateinit var itemId: ArrayList<String?>
    private var count: Int = 0
    private lateinit var dataBaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        listName = intent.extras?.get("listName").toString()
        binding.itemListName.text = listName
        binding.itemProductCount.text = "Produces($count)"
        uid = FirebaseAuth.getInstance().currentUser?.uid!!
        dataBaseReference = FirebaseDatabase.getInstance().reference.child("list/$uid/$listName")
        itemId = ArrayList()
        binding.uploadButton.setOnClickListener { uploadData() }
        itemAdapter = ItemAdapter(this, 0, ArrayList())
        binding.itemListView.adapter = itemAdapter
    }

    private fun uploadData() {
        val product = Product(
            binding.addName.text.toString(),
            binding.addPrise.text.toString(),
            binding.addDescription.text.toString()
        )
        binding.addName.setText("")
        binding.addPrise.setText("")
        binding.addDescription.setText("")
        dataBaseReference.push().setValue(product)
        Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        attachDatabase()
    }

    private fun attachDatabase() {
        childEventListener = object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                var j = 0
                if (previousChildName == null) j = 0
                else
                    for ((i, id) in itemId.withIndex())
                        if (id == previousChildName) j = i + 1

                itemAdapter.remove(itemAdapter.getItem(j))
                itemAdapter.insert(snapshot.getValue(Product::class.java), j)
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.e("TAG", "onChildAdded: ")
                itemId.add(snapshot.key)
                val product: Product? = snapshot.getValue(Product::class.java)
                if (product != null) {
                    product.itemId = snapshot.key
                    product.listName = binding.itemListName.text.toString()
                }
                itemAdapter.add(product)
                count++
                binding.itemProductCount.text = "Produces($count)"
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val i = itemId.indexOf(snapshot.key)
                itemAdapter.remove(itemAdapter.getItem(i))
                itemId.removeAt(i)
                itemAdapter.notifyDataSetChanged()
                count--
                binding.itemProductCount.text = "Produces($count)"
            }
        }
        dataBaseReference.addChildEventListener(childEventListener)
    }

    override fun onPause() {
        super.onPause()
        detachDatabase()
    }

    private fun detachDatabase() {
        dataBaseReference.removeEventListener(childEventListener)
        itemAdapter.clear()
        count = 0
    }
}