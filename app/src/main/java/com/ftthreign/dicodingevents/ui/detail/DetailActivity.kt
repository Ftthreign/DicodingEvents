package com.ftthreign.dicodingevents.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
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
            it?.let { event ->
                binding.apply {
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
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.errorMessage.observe(this) { errMessage ->
            errMessage?.let {
                binding.apply {
                    errorMessage.text = it
                    errorMessage.visibility = View.VISIBLE
                    progressBar1.visibility = View.GONE
                    ivMediaCoverEvent.visibility = View.GONE
                    eventName.visibility = View.GONE
                    eventDescription.visibility = View.GONE
                    titleOwnerName.visibility = View.GONE
                    ownerName.visibility = View.GONE
                    schedule.visibility = View.GONE
                    startEvent.visibility = View.GONE
                    endEvent.visibility = View.GONE
                    eventStartTime.visibility = View.GONE
                    eventEndTime.visibility = View.GONE
                    quotaTitle.visibility = View.GONE
                    eventQuota.visibility = View.GONE
                    quotaLeftTitle.visibility = View.GONE
                    quotaLeft.visibility = View.GONE
                    btnRegister.visibility = View.GONE
                }
            }
        }
    }

    private fun showLoading(isLoading : Boolean) {
        binding.progressBar1.visibility = if(isLoading) View.VISIBLE else View.GONE
    }


    companion object {
        const val EVENT_ID = "event_id"
    }
}