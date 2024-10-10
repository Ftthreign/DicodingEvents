package com.ftthreign.dicodingevents.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.ftthreign.dicodingevents.databinding.ActivityDetailBinding
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    private val viewModel: DetailEventViewModel by viewModels<DetailEventViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getIntExtra(EVENT_ID, 1)
        if (eventId != -1) {
            viewModel.getEventDetail(eventId)
        }

        supportActionBar?.title = "Detail Event"
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.eventDetail.observe(this) {
            it.let { event ->
                binding.eventName.text = event.name
                binding.eventDescription.text = HtmlCompat.fromHtml(
                    event.description, HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                binding.ownerName.text = event.ownerName
                binding.eventStartTime.text = event.beginTime
                binding.eventEndTime.text = event.endTime
                binding.eventQuota.text = event.quota.toString()
                binding.quotaLeft.text = (event.quota - event.registrants).toString()
                Picasso
                    .get()
                    .load(event.mediaCover)
                    .into(binding.ivMediaCoverEvent)
                binding.btnRegister.setOnClickListener {
                    val linkIntent = Intent(Intent.ACTION_VIEW)
                    linkIntent.data = Uri.parse(event.link)
                    startActivity(linkIntent)
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.errorMessage.observe(this){errorMessage->
            if(errorMessage != null) {
                binding.errorMessage.text = errorMessage
                binding.errorMessage.visibility = View.VISIBLE
                binding.progressBar1.visibility = View.GONE
                binding.ivMediaCoverEvent.visibility = View.GONE
                binding.eventName.visibility = View.GONE
                binding.eventDescription.visibility = View.GONE
                binding.titleOwnerName.visibility = View.GONE
                binding.ownerName.visibility = View.GONE
                binding.schedule.visibility = View.GONE
                binding.startEvent.visibility = View.GONE
                binding.endEvent.visibility = View.GONE
                binding.eventStartTime.visibility = View.GONE
                binding.eventEndTime.visibility = View.GONE
                binding.quotaTitle.visibility = View.GONE
                binding.eventQuota.visibility = View.GONE
                binding.quotaLeftTitle.visibility = View.GONE
                binding.quotaLeft.visibility = View.GONE
                binding.btnRegister.visibility = View.GONE
            }
        }
    }

    private fun showLoading(isLoading : Boolean) {
        if(isLoading) {
            binding.progressBar1.visibility = View.VISIBLE
        } else {
            binding.progressBar1.visibility = View.GONE
        }
    }

    companion object {
        const val EVENT_ID = "event_id"
    }
}