package kr.hs.dgsw.data.network.remote

import io.reactivex.rxjava3.core.Single
import kr.hs.dgsw.data.base.BaseRemote
import kr.hs.dgsw.data.network.response.ApplyResponse
import kr.hs.dgsw.data.network.response.MyAllApplyResponse
import kr.hs.dgsw.data.network.response.MyApplyResponse
import kr.hs.dgsw.data.network.service.ApplyService
import kr.hs.dgsw.domain.request.ApplyRequest
import retrofit2.http.Path
import javax.inject.Inject

class ApplyRemote @Inject constructor(override val service: ApplyService) :
    BaseRemote<ApplyService>() {
    fun getApply(idx: Int): Single<List<ApplyResponse>> =
        service.getApply(idx).map(getResponseData())

    fun getMyApply(idx: Int): Single<List<MyAllApplyResponse>> =
        service.getMyApply(idx).map(getResponseData())

    fun getPostMyApply(postIdx: Int, userIdx: Int): Single<MyApplyResponse> =
        service.getPostMyApply(postIdx, userIdx).map(getResponseData())

    fun postApply(request: ApplyRequest): Single<Boolean> =
        service.postApply(request).map(getResponseData())

    fun putApply(applyIdx: Int, state: Int): Single<Int> =
        service.putApply(applyIdx, state).map(getResponseData())
}