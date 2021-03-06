package kr.hs.dgsw.sport_recruit.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kr.hs.dgsw.sport_recruit.R
import kr.hs.dgsw.sport_recruit.adapter.PostListAdapter
import kr.hs.dgsw.sport_recruit.base.BaseFragment
import kr.hs.dgsw.sport_recruit.databinding.FragmentHomeBinding
import kr.hs.dgsw.sport_recruit.util.toast
import kr.hs.dgsw.sport_recruit.viewmodel.HomeViewModel

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override val mViewModel: HomeViewModel by viewModels()
    override val layoutRes: Int
        get() = R.layout.fragment_home

    lateinit var adapter: PostListAdapter
    var currentPosition = 0

    override fun observerViewModel() {
        setTabLayout()
        setRecyclerView()
        with(mViewModel) {
            onSuccessGetAllPost.observe(this@HomeFragment, Observer {
                adapter.submitList(it.toMutableList())
            })
            onSuccessGetStatePost.observe(this@HomeFragment, Observer {
                adapter.submitList(it.toMutableList())
            })
            onSuccessGetCategoryPost.observe(this@HomeFragment, Observer {
                adapter.submitList(it.toMutableList())

            })
            onErrorEvent.observe(this@HomeFragment, Observer {
                this@HomeFragment.toast(requireContext(), "오류 발생 ${it.message}")
            })
        }
    }

    override fun onResume() {
        super.onResume()
        getItemList(currentPosition)
    }

    private fun setRecyclerView() {
        adapter = PostListAdapter(requireActivity())
        mBinding.homeRecycler.adapter = adapter
    }

    private fun setTabLayout() {
        mBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                getItemList(tab!!.position)
                currentPosition = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun getItemList(position: Int){
        when (position) {
            0 -> mViewModel.getAllPost()
            1 -> mViewModel.getStatePost(1)
            2 -> mViewModel.getCategoryPost(0)
            3 -> mViewModel.getCategoryPost(1)
            4 -> mViewModel.getCategoryPost(2)
            5 -> mViewModel.getCategoryPost(3)
        }
    }

}