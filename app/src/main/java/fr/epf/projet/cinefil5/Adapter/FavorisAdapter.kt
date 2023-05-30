package fr.epf.projet.cinefil5.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.epf.projet.cinefil5.R
import fr.epf.projet.cinefil5.model.Favoris

class FavorisAdapter(var items: List<Favoris>) : RecyclerView.Adapter<FavorisAdapter.MoviesViewHolder>() {

    private  lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_favoris, parent, false)
        return MoviesViewHolder(itemView, mListener)

    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movie = items[position]
        holder.bind(movie)
    }

    override fun getItemCount() = items.size

    inner class MoviesViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        private val `IMAGE-URL` = "https://image.tmdb.org/t/p/w500"
        var poster: ImageView
        var title: TextView
        var originalTitle: TextView
        var releaseDate: TextView
        var voteAverage: RatingBar
        var overview: TextView

        init {
            poster = itemView.findViewById(R.id.item_favoris_poster)
            title = itemView.findViewById(R.id.item_favoris_title)
            originalTitle = itemView.findViewById(R.id.item_favoris_original_title)
            releaseDate = itemView.findViewById(R.id.item_favoris_release_date)
            voteAverage = itemView.findViewById(R.id.item_favoris_vote_average)
            overview = itemView.findViewById(R.id.item_favoris_overview)

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

        }

        fun bind(favoris: Favoris) {
            title.text = favoris.title
            originalTitle.text = favoris.originalTitle
            releaseDate.text = favoris.releaseDate
            overview.text = favoris.overview
            Log.i("--------------------FavorisAdapter", favoris.overview)
            Glide.with(poster).load(`IMAGE-URL` + favoris.posterPath).into(poster)
            voteAverage.rating = favoris.voteAverage
        }
    }
}