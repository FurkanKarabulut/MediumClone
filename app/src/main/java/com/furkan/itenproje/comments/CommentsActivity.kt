package com.furkan.itenproje.comments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.furkan.itenproje.MainActivity
import com.furkan.itenproje.databinding.ActivityCommentsBinding
import com.furkan.itenproje.util.EncryptedPreferenceManager

class CommentsActivity : AppCompatActivity() {
    private val preferenceManager: EncryptedPreferenceManager by lazy {
        EncryptedPreferenceManager(
            this
        )
    }
    private lateinit var binding: ActivityCommentsBinding
    private val SELF_COMMENT = "self_comment"
    private val COMMENT = "comment"
    private val COMMENT_COUNT = "comment_count"
    private var commentCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeCommentLayoutVisibility()
        setData()
        setListener()
    }

    private fun setListener() {
        binding.send.setOnClickListener {
            val commentCount = preferenceManager.getValueInt(COMMENT_COUNT)
            preferenceManager.save(SELF_COMMENT, true)
            preferenceManager.save(COMMENT_COUNT, commentCount.plus(1))
            preferenceManager.save(COMMENT, binding.enterComment.text.toString())
            val intent = Intent(this@CommentsActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setData() {
        commentCount = preferenceManager.getValueInt(COMMENT_COUNT)
        val comment = preferenceManager.getValueString(COMMENT)
        binding.comtNumber.text = commentCount.toString()
        comment?.let {
            binding.comment.text = it

        }
    }

    private fun changeCommentLayoutVisibility() {
        if (preferenceManager.getValueBoolean(SELF_COMMENT)) {
            binding.enterComment.visibility = View.INVISIBLE
            binding.send.visibility = View.INVISIBLE
            binding.commentLine.visibility = View.INVISIBLE
            binding.comment.visibility = View.VISIBLE
            binding.comment.text = preferenceManager.getValueString(COMMENT)
        } else {
            binding.enterComment.visibility = View.VISIBLE
            binding.send.visibility = View.VISIBLE
            binding.commentLine.visibility = View.VISIBLE
            binding.comment.visibility = View.INVISIBLE
        }
    }
}
