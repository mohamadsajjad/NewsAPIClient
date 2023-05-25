package com.example.newsapiclient.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.newsapiclient.R
import com.example.newsapiclient.databinding.FragmentInfoBinding
import com.example.newsapiclient.presentation.activity.MainActivity
import com.example.newsapiclient.presentation.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding
    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val args:InfoFragmentArgs by navArgs()
        val article = args.selectedArticle

        binding.webView.apply {
            webViewClient = WebViewClient()
            if (!article.url.isNullOrEmpty()) {
                loadUrl(article.url)
            }
        }

        binding.fabSave.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view,"Saved Successfully",Snackbar.LENGTH_LONG).show()
            binding.fabSave.setImageResource(R.drawable.ic_bookmark_24)
        }
    }

}