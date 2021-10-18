package com.ait.minesweeper

import android.app.ProgressDialog.show
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ait.minesweeper.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnReset.setOnClickListener{
            binding.MinesweeperUI.resetGame()
        }

    }

    fun isTryMode() : Boolean {
        return binding.tgbtnIsTry.isChecked()
    }
    fun showTextMessage(msg: String) {
        binding.mainText.text = msg
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
    }
}