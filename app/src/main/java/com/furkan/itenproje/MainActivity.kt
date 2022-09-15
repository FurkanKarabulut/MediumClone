package com.furkan.itenproje

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.furkan.itenproje.article.ArticleAdapter
import com.furkan.itenproje.databinding.ActivityMainBinding
import com.furkan.itenproje.model.Article

class MainActivity : AppCompatActivity() {
    private lateinit var newsRecyclerView: RecyclerView
    lateinit var imageid: Array<Int>
    lateinit var title: Array<String>
    lateinit var profile: Array<Int>
    lateinit var info: Array<String>
    lateinit var date: Array<String>
    lateinit var reason: Array<String>
    lateinit var isFavorite: Array<Boolean>
    lateinit var binding: ActivityMainBinding
    private val articleList = mutableListOf<Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()


        profile = arrayOf(
            R.drawable.google,
            R.drawable.facebook,
            R.drawable.star,
            R.drawable.padlock
        )

        newsRecyclerView = findViewById(R.id.recyclerView)
        newsRecyclerView.layoutManager = LinearLayoutManager(this)
        newsRecyclerView.setHasFixedSize(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            articleList.removeIf { item -> item.date == "9/9/2022" }
        }
        getUserdata()
    }

    private fun setData() {
        articleList.add(
            Article(
                "An Embarrrasing Android App Bug that Can Be Easily Missed",
                "Elye in Mobile App Development Publication",
                R.drawable.fk,
                R.drawable.google,
                "Aug 6 . 6 min read",
                "Based on your reading history",
                true,
                "www.google.com",
                R.drawable.google
            )
        )
        articleList.add(
            Article(
                "Different Ways of Handling Click Events on RecyclerView",
                "Asim Giray Boyraz",
                R.drawable.mentor,
                R.drawable.facebook,
                "Aug 4 . 3 min read",
                "Because you follow Kotlin",
                false,
                "www.google.com",
                R.drawable.facebook
            )
        )
        articleList.add(
            Article(
                "Singleton Desing Pattern in Ansdroid Explained",
                "Prachi Jamdade",
                R.drawable.mastercard_logo,
                R.drawable.star,
                "Aug 7 . 3 min read",
                "Because you follow Kotlin",
                true,
                "www.google.com",
                R.drawable.star,
            )
        )
        articleList.add(
            Article(
                "How do View models know how to survive configuration changes?",
                "Jao Foltran in Codex",
                R.drawable.profile,
                R.drawable.padlock,
                "Aug 3 . 4 min read",
                "Because you follow Kotlin",
                false,
                "www.google.com",
                R.drawable.padlock
            )
        )
    }

    private fun getUserdata() {
        newsRecyclerView.adapter = ArticleAdapter(this, articleList)
    }
}
