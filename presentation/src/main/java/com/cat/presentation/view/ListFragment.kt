package com.cat.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.HORIZONTAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.cat.domain.entity.State
import com.cat.presentation.R
import com.cat.presentation.adapters.PostFeedAdapter
import com.cat.presentation.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.list_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel


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

        btn_go.setOnClickListener {
            listViewModel.refresh(edt_sub_name.text.toString())
        }

        val dividerItemDecoration = DividerItemDecoration(
            this.requireContext(),
            (rl_post_list.layoutManager as LinearLayoutManager).orientation
        )
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this.requireContext(), R.drawable.list_devider)!!)

        rl_post_list.addItemDecoration(dividerItemDecoration)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.listViewModel.postsLiveData.observe(viewLifecycleOwner, Observer {
            (rl_post_list.adapter as PostFeedAdapter).submitList(it)
        })

        this.listViewModel.refreshState.observe(viewLifecycleOwner, Observer {
            when (it) {
                State.LOADING -> fl_loading.visibility = View.VISIBLE
                else -> fl_loading.visibility = View.GONE
            }
        })

        this.listViewModel.subNameTriggerLiveData.observe(viewLifecycleOwner, Observer { subName ->
            edt_sub_name.setText(subName)
        })

        this.listViewModel.onLoad()
    }
}
