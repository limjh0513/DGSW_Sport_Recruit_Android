package kr.hs.dgsw.sport_recruit.ui.activity

import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kr.hs.dgsw.data.util.PreferenceManager
import kr.hs.dgsw.sport_recruit.R
import kr.hs.dgsw.sport_recruit.adapter.ApplyListAdapter
import kr.hs.dgsw.sport_recruit.base.BaseActivity
import kr.hs.dgsw.sport_recruit.databinding.ActivityDetailBinding
import kr.hs.dgsw.sport_recruit.util.toast
import kr.hs.dgsw.sport_recruit.viewmodel.DetailViewModel

@AndroidEntryPoint
class DetailActivity : BaseActivity<ActivityDetailBinding, DetailViewModel>() {
    override val mViewModel: DetailViewModel by viewModels()
    override val layoutRes: Int
        get() = R.layout.activity_detail
    private var idx: Int = -1
    private var userIdx: Int = -1
    private lateinit var adaptper: ApplyListAdapter
    private var applyState: Int? = null
    private var applyIdx: Int? = null
    private var hidden: Int? = 0

    override fun observeViewModel() {
        initDetailPost()
        initAdapter()
        with(mViewModel) {
            onSuccessGetDetail.observe(this@DetailActivity, Observer {
                mBinding.post = it
                hidden = it.hidden
                if (it.userIdx == userIdx) {
                    applyState = 2
                    mBinding.applyState = applyState!!
                } else {
                    mViewModel.getMyApply(idx, userIdx)
                }

                mBinding.detailRecyclerView.isVisible = hidden != 1
                mBinding.detailTvHidden.isVisible = hidden != 0
                mBinding.detailApplyBtn.isVisible = it.state != 2

            })

            onSuccessGetApply.observe(this@DetailActivity, Observer {
                if (hidden != 1) {
                    adaptper.submitList(it)
                }
            })

            onSuccessGetMyApply.observe(this@DetailActivity, Observer {
                applyIdx = it.idx
                applyState = applyState ?: it.state
                mBinding.applyState = applyState!!
            })

            onSuccessGetUser.observe(this@DetailActivity, Observer {
                when (applyState) {
                    -1 -> { //?????? ??????
                        mViewModel.postApply(idx, userIdx, it)
                    }
                    0 -> { //?????? ??????
                        mViewModel.putApply(applyIdx!!, 1)
                    }
                    1 -> { // ?????? ??????
                        mViewModel.putApply(applyIdx!!, 0)
                    }
                    2 -> { //?????? ??????
                        mViewModel.putPostEnded(idx)
                    }
                }
            })

            onSuccessPostApply.observe(this@DetailActivity, Observer {
                Log.e("dfafasd", "${it}")

                if (it) {
                    toast("????????? ?????? ??????!")
                    applyState = 0
                    mBinding.applyState = applyState!!
                    getPostApply(idx)
                    getDetailPost(idx)
                } else {
                    toast("????????? ?????? ??????... ????????? ????????? ?????? ???????????????.")
                    afterGetDetail()
                }
            })

            onSuccessPutApply.observe(this@DetailActivity, Observer {
                Log.e("sadfa", "${it}")
                if (it >= 0) {
                    if (applyState != 2) {
                        applyState = it
                    }

                    mBinding.applyState = applyState!!
                    toast("?????? ?????? ?????? ??????!")
                    getPostApply(idx)
                    getDetailPost(idx)
                } else {
                    toast("????????? ?????? ??????... ????????? ????????? ?????? ???????????????.")
                    afterGetDetail()
                }
            })

            onSuccessPutPostEnded.observe(this@DetailActivity, Observer {
                mBinding.detailApplyBtn.isVisible = !it
                toast("????????? ?????? ?????? ??????!")
                getDetailPost(idx)
            })

            onErrorEvent.observe(this@DetailActivity, Observer {
                this@DetailActivity.toast("????????? ??????????????????. ${it.message}")
            })
        }
    }

    fun applyBtnClick() {
        if (userIdx > -1) {
            mViewModel.getUser(userIdx)
        }
    }

    fun initAdapter() {
        adaptper = ApplyListAdapter()
        mBinding.detailRecyclerView.adapter = adaptper

        if (idx > -1) {
            mViewModel.getPostApply(idx)
        } else {
            toast("???????????? ???????????? ???????????????.")
        }
    }

    private fun initDetailPost() {
        idx = intent.getIntExtra("postIdx", -1)
        userIdx = PreferenceManager.getUser(this)

        if (idx > -1 && userIdx != -1) {
            mViewModel.getDetailPost(idx)
        } else {
            toast("????????? idx ?????? ?????? ????????? ???????????? ???????????????.")
        }
    }

    private fun afterGetDetail() {
        mViewModel.getDetailPost(idx)
    }
}