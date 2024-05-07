package com.minhduc202.musicapp.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.storage.storage
import com.minhduc202.musicapp.R
import com.minhduc202.musicapp.constant.Constants
import com.minhduc202.musicapp.databinding.ActivityAdminHomeBinding
import com.minhduc202.musicapp.databinding.ItemMusicBinding
import com.minhduc202.musicapp.epoxy.MyEpoxyController
import com.minhduc202.musicapp.epoxy.ViewBindingKotlinModel
import com.minhduc202.musicapp.model.MusicItem
import com.minhduc202.musicapp.util.MediaPlayerUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminHomeBinding
    private var listMusic = ArrayList<MusicItem>()
    private lateinit var myController: MyEpoxyController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        setupView()
        handleEvent()
    }

    private fun handleEvent() {
        binding.btnAdd.setOnClickListener {
            val intent = Intent()
            intent.setType("audio/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            pickMusicLauncher.launch(intent)
        }
    }

    private val pickMusicLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri: Uri? = result.data?.data
                selectedImageUri?.let {
                    saveMusic(it)
                }
            }
        }

    private fun saveMusic(uri: Uri) {
        val storage = Firebase.storage
        val ref = storage.reference.child("music_data/test.mp3")
        val uploadTask = ref.putFile(uri)

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                Toast.makeText(this@AdminHomeActivity, "Cập nhật thất bại", Toast.LENGTH_SHORT).show()
                task.exception?.let {
                    Log.e("ABC", it.message.toString())
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                Toast.makeText(this@AdminHomeActivity, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                MediaPlayerUtil.playByUrl(this@AdminHomeActivity, downloadUri)
            } else {
                //
            }
        }
    }

    private fun setupView() {
    }

    private fun initData() {
        CoroutineScope(Dispatchers.IO).launch {
            val database = Firebase.database
            val myRef = database.reference.child(Constants.CHILD_MUSICS)
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val tempList = dataSnapshot.getValue<List<MusicItem>>()
                    tempList?.let {
                        listMusic.addAll(it)
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        setupRecyclerView()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    private fun setupRecyclerView() {
        binding.rvMusics.withModels {
            listMusic.forEach {
                MusicItemForAdminModel(this@AdminHomeActivity, it) {
                    onMusicClicked()
                }.id(it.id).addTo(this)
            }
        }
    }

    private fun onMusicClicked() {

    }

    data class MusicItemForAdminModel(
        val context: Context,
        val musicItem: MusicItem,
        val onClickItem: (MusicItem) -> Unit
    ) : ViewBindingKotlinModel<ItemMusicBinding>(R.layout.item_music) {
        override fun ItemMusicBinding.bind() {
            tvName.text = musicItem.name
            tvAuthor.text = musicItem.author
            tvName.isSelected = true
            tvAuthor.isSelected = true
            Glide.with(context).load(musicItem.image).error(R.drawable.img_error).into(imgSong)
            root.setOnClickListener {
                onClickItem(musicItem)
            }
        }
    }
}