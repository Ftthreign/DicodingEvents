package com.ftthreign.dicodingevents.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.ftthreign.dicodingevents.R
import com.ftthreign.dicodingevents.data.local.entity.EventEntity
import com.ftthreign.dicodingevents.databinding.ActivityDetailBinding
import com.ftthreign.dicodingevents.ui.viewModels.MainViewModel
import com.ftthreign.dicodingevents.ui.viewModels.ViewModelFactory
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val data = intent.getParcelableExtra<EventEntity>("EXTRA_EVENT")
        data?.let { observeViewModel(it) }
        supportActionBar?.title = "Detail Event"
    }

    private fun observeViewModel(event: EventEntity) {
        binding.apply {
            changeFavouriteIcon(event.isFavourite)
            progressBar1.visibility = View.GONE
            eventName.text = event.name
            eventDescription.text = HtmlCompat.fromHtml(
                event.description, HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            ownerName.text = event.ownerName
            eventStartTime.text = event.beginTime
            eventEndTime.text = event.endTime
            eventQuota.text = event.quota.toString()
            quotaLeft.text = (event.quota - event.registrants).toString()
            Picasso
                .get()
                .load(event.mediaCover)
                .into(ivMediaCoverEvent)
            btnRegister.setOnClickListener {
                val linkIntent = Intent(Intent.ACTION_VIEW)
                linkIntent.data = Uri.parse(event.link)
                startActivity(linkIntent)
            }
            favoriteFab.setOnClickListener {
                event.isFavourite = !event.isFavourite
                changeFavouriteIcon(event.isFavourite)
                if(event.isFavourite) {
                    viewModel.addEventToFavourite(event)
                    Toast.makeText(this@DetailActivity, "Berhasil menambahkan event ke Favorit", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.removeEventFromFavourite(event)
                    Toast.makeText(this@DetailActivity, "Berhasil menghapus event dari Favorit", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun changeFavouriteIcon(isFavourite : Boolean) {
        binding.favoriteFab.setImageResource(
            if (isFavourite) {
                R.drawable.baseline_favorite_24
            } else {
                R.drawable.ic_border_favorite
            }
        )
    }
}