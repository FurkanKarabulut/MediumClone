package com.furkan.itenproje.article

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.furkan.itenproje.comments.CommentsActivity
import com.furkan.itenproje.R
import com.furkan.itenproje.databinding.ActivityArticleDetailBinding
import com.furkan.itenproje.util.EncryptedPreferenceManager
import org.w3c.dom.Comment

class ArticleDetailActivity : AppCompatActivity() {
    private val preferenceManager: EncryptedPreferenceManager by lazy {
        EncryptedPreferenceManager(
            this
        )
    }
    private lateinit var binding: ActivityArticleDetailBinding
    private val CLAPS_COUNT = "claps_count"
    private val COMMENT_COUNT = "comment_count"
    private var clapsCount = 0
    private var title: String? = null
    private var description: String? = null
    private var date: String? = null
    private var image: Int? = null
    private var link: String? = null
    private var isBookmark: Boolean? = false
    private val TITLE = "title"
    private val DESCRIPTION = "description"
    private val DATE = "date"
    private val IMAGE = "image"
    private  val LINK ="link"
    private  val IS_BOOKMARK ="is_bookmark"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleDetailBinding.inflate(layoutInflater)
        getDataFromBundle()
        setContentView(binding.root)
        setupListener()
        setData()
    }

    private fun getDataFromBundle() {
        title = if (!intent.getStringExtra(TITLE).isNullOrEmpty()) {
            intent.getStringExtra(TITLE)
        } else {
            resources.getString(R.string.title)
        }
        description = if (!intent.getStringExtra(DESCRIPTION).isNullOrEmpty()) {
            intent.getStringExtra(DESCRIPTION)
        } else {
            resources.getString(R.string.info)
        }

        date = if (!intent.getStringExtra(DATE).isNullOrEmpty()) {
            intent.getStringExtra(DATE)
        } else {
            resources.getString(R.string.date)
        }

        link = if (!intent.getStringExtra(LINK).isNullOrEmpty()) {
            intent.getStringExtra(LINK)
        } else {
            resources.getString(R.string.link)
        }

        isBookmark = preferenceManager.getValueBoolean(IS_BOOKMARK)

        if (isBookmark == true) {
            binding.bookmarkButton.setImageResource(R.drawable.bookmark_filled)
        } else {
            binding.bookmarkButton.setImageResource(R.drawable.bookmark)
        }

        image = intent.getIntExtra(IMAGE, R.mipmap.ic_launcher_round)
    }

    private fun setData() {
        val commentCount = preferenceManager.getValueInt(COMMENT_COUNT)
        clapsCount = preferenceManager.getValueInt(CLAPS_COUNT)
        binding.commentCount.text = commentCount.toString()
        binding.claps.text = clapsCount.toString()
        binding.title.text = title
        binding.infoArea.text = description
        binding.dateArea.text = date
        image?.let {
            binding.imageFk.setImageResource(it)
        } ?: run {
            binding.imageFk.setImageResource(R.mipmap.ic_launcher_round)
        }
    }

    private fun setupListener() {
        binding.createComment.setOnClickListener {
            val intent = Intent(this@ArticleDetailActivity, CommentsActivity::class.java)
            startActivity(intent)
        }
        binding.btnClaps.setOnClickListener {
            clapsCount = (clapsCount.plus(1))
            preferenceManager.save(CLAPS_COUNT, clapsCount)
            binding.claps.text = clapsCount.toString()
        }
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        binding.shareButton.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = SHARE_INTENT_TYPE
            shareIntent.putExtra(Intent.EXTRA_TEXT, link)
            startActivity(Intent.createChooser(shareIntent, resources.getString(R.string.select_an_app)))
        }
        binding.bookmarkButton.setOnClickListener {
            if (isBookmark == true) {
                preferenceManager.save(IS_BOOKMARK, false)
                binding.bookmarkButton.setImageResource(R.drawable.bookmark)
            } else {
                preferenceManager.save(IS_BOOKMARK, true)
                binding.bookmarkButton.setImageResource(R.drawable.bookmark_filled)
            }
        }

    }

    companion object {
        const val SHARE_INTENT_TYPE = "text/plain"
    }
}