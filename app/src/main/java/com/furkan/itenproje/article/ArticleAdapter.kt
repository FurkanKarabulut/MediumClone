package com.furkan.itenproje.article

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.furkan.itenproje.R
import com.furkan.itenproje.databinding.ItemArticleBinding
import com.furkan.itenproje.model.Article

class ArticleAdapter(private val mContext: Context, private val articleList: MutableList<Article>) :
    RecyclerView.Adapter<ArticleAdapter.MyViewHolder>() {
    private val TITLE = "title"
    private val DESCRIPTION = "description"
    private val DATE = "date"
    private val IMAGE = "image"
    private val LINK = "link"
    lateinit var binding: ItemArticleBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = articleList[position]
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, ArticleDetailActivity::class.java)
            intent.putExtra(TITLE, articleList[position].title)
            intent.putExtra(DESCRIPTION, articleList[position].info)
            intent.putExtra(DATE, articleList[position].date)
            intent.putExtra(IMAGE, articleList[position].image)
            intent.putExtra(LINK, articleList[position].link)
            mContext.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return articleList.size
    }
    class MyViewHolder(private val itemArticleBinding: ItemArticleBinding) : RecyclerView.ViewHolder(itemArticleBinding.root) {
        fun bind(article: Article) {
            itemArticleBinding.title.text = article.title
            itemArticleBinding.userInfo.text = article.info
            itemArticleBinding.suggestionReason.text = article.reason
            itemArticleBinding.date.text = article.date
            itemArticleBinding.image.setImageResource(article.image)
            itemArticleBinding.userPhoto.setImageResource(article.profile)
            if (article.isFavorite) {
                itemArticleBinding.star.setImageResource(R.drawable.star_filled)
            } else {
                itemArticleBinding.star.setImageResource(R.drawable.star_empty)
            }
        }
    }
}