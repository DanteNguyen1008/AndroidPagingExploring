package com.cat.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.cat.presentation.R
import com.cat.presentation.adapters.PostFeedAdapter
import com.cat.presentation.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.list_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ListFragment : Fragment() {
    private val listViewModel: ListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rl_post_list.adapter = PostFeedAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.listViewModel.postsLiveData.observe(viewLifecycleOwner, Observer {
            (rl_post_list.adapter as PostFeedAdapter).submitList(it)
        })

        this.listViewModel.refreshState.observe(viewLifecycleOwner, Observer {
            Timber.d("Get state ${it.status.name}")
        })

        this.listViewModel.onLoad()
    }
}
