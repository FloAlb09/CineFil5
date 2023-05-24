package fr.epf.projet.cinefil5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.epf.projet.cinefil5.model.ServiceDetailsResult


class MoviesAdapter(var items: List<ServiceDetailsResult>) : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    private  lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
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
        var releaseDate: TextView
        var voteAverage: RatingBar
        var overview: TextView

        init {
            poster = itemView.findViewById(R.id.item_movie_poster)
            title = itemView.findViewById(R.id.item_movie_title)
            releaseDate = itemView.findViewById(R.id.item_movie_release_date)
            voteAverage = itemView.findViewById(R.id.item_movie_vote_average)
            overview = itemView.findViewById(R.id.item_movie_overview)

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

        }

        fun bind(movie: ServiceDetailsResult) {
            title.text = movie.title
            releaseDate.text = movie.releaseDate
            overview.text = movie.overview
            Glide.with(poster).load(`IMAGE-URL` + movie.posterPath).into(poster)
            voteAverage.rating = movie.voteAverage.toFloat()
        }
    }
}