package org.secfirst.advancedsearch.mvp

import org.secfirst.advancedsearch.util.mvp.ThreadSpec
import rx.Observable
import rx.Subscription
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

abstract class Presenter<in V : Presenter.View> constructor(private val threadSpec: ThreadSpec) {
    private var view : V? = null
    private var viewSubscription: CompositeSubscription? = null
    private val lifetimeSubscription = CompositeSubscription()

    open fun onViewAttached(view: V) {
        this.view = view
        this.viewSubscription = CompositeSubscription()
    }

    open fun onViewDetached(view: V) {
        this.viewSubscription?.unsubscribe()
        this.view = null
    }

    open fun onDestroy() {
        lifetimeSubscription.unsubscribe()
    }

    protected fun unsubscribeOnDetach(subscription: Subscription) {
        viewSubscription?.add(subscription)
    }

    protected fun unsubscribeOnDestroy(subscription: Subscription) {
        lifetimeSubscription.add(subscription)
    }

    protected fun <T> Observable<T>.subscribeUntilDetached(onNext: (T) -> Unit): Subscription =
        subscribe(onNext).apply { unsubscribeOnDetach(this) }
    protected fun <T> Observable<T>.subscribeUntilDetached(onNext: (T) -> Unit, onError: (Throwable) -> Unit): Subscription =
        subscribe(onNext, onError).apply { unsubscribeOnDetach(this) }
    protected fun <T> Observable<T>.bg(): Observable<T> = subscribeOn(Schedulers.io())

    protected fun bg(function: () -> Unit) = threadSpec.bg { function() }
    protected fun ui(function: () -> Unit) = threadSpec.ui { function() }

    interface View
}