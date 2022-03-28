package com.ravitej.flickrsearch.ui.searchDetail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ravitej.flickrsearch.R
import com.ravitej.flickrsearch.databinding.FragentSearchDetailBinding
import kotlinx.android.synthetic.main.search_detail_item.view.*

//TODO: Create cards to make the content look pretty
class SearchDetailFragment : Fragment() {
    private lateinit var binding: FragentSearchDetailBinding
    private val args: SearchDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragent_search_detail,
            container,
            false
        )

        Glide.with(binding.searchImage)
            .load(args.imageDetails.media.m)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.imageWidth.titleLabel.text = "Width"
                    binding.imageHeight.titleLabel.text = "Height"
                    binding.imageWidth.titleContent.text =
                        "${resource?.intrinsicWidth.toString()}px"
                    binding.imageHeight.titleContent.text =
                        "${resource?.intrinsicHeight.toString()}px"
                    return false
                }

            })
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(binding.searchImage)


        binding.imageTitleContainer.titleContent.text = args.imageDetails.title
        binding.imageTitleContainer.titleLabel.text = "Title"

        binding.imageAuthor.titleContent.text = args.imageDetails.author
        binding.imageAuthor.titleLabel.text = "Author"

        binding.imageDescriptionContainer.titleContent.text =
            Html.fromHtml(args.imageDetails.description)
        binding.imageDescriptionContainer.titleLabel.text = "Description"

        return binding.root
    }
}