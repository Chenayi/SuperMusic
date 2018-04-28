package com.chenayi.supermusic.mvp.ui.activity

import android.content.Intent
import android.view.ViewGroup
import com.chenayi.supermusic.R
import com.chenayi.supermusic.base.BaseActivity
import com.chenayi.supermusic.base.IPresenter
import com.chenayi.supermusic.databinding.ActivitySplashBinding
import com.chenayi.supermusic.di.component.AppComponent
import com.chenayi.supermusic.utils.RxScheduler
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * Created by Chenwy on 2018/4/23.
 */
class SplashActivity : BaseActivity<IPresenter, ActivitySplashBinding>() {
    private var disposable: Disposable? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun rootLayout(): ViewGroup? {
        return null
    }

    override fun setupComponent(appComponent: AppComponent?) {
    }

    override fun initData() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .compose(RxScheduler.compose())
                .subscribe(object : Observer<Long> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: Long) {
                        if (t == 3L) {
                            disposable()
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            finish()
                        }
                    }

                    override fun onError(e: Throwable) {
                    }

                })
    }

    private fun disposable() {
        if (disposable != null && disposable?.isDisposed == false) {
            disposable?.dispose()
        }
    }

    override fun onDestroy() {
        disposable()
        super.onDestroy()
    }
}