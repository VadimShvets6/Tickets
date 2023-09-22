@file:Suppress("UNCHECKED_CAST")

package com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.coreutills.extentions.tryNull
import com.example.coreutills.utils.AsyncFlexLayoutInflater
import com.flexeiprata.novalles.interfaces.Instructor
import com.flexeiprata.novalles.interfaces.Novalles
import com.flexeiprata.novalles.interfaces.UIModelHelper
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.launchUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import okhttp3.internal.toImmutableList
import java.lang.ref.WeakReference
import kotlin.reflect.KClass

class DelegateAdapter private constructor(
    private val delegates: List<ItemDelegateBase<BaseUiModel, DelegateViewHolder<BaseUiModel>>>,
    private val prefetchPolicy: PrefetchPolicy
) : ListAdapter<BaseUiModel, DelegateViewHolder<BaseUiModel>>(DefaultDiffUtil(delegates)) {

    private var fragmentRef: WeakReference<Fragment> = WeakReference(null)

    override fun getItemViewType(position: Int): Int {
        return delegates.find { it.uiModelClass == getItem(position)::class }?.let { delegates.indexOf(it) }
            ?: throw Exception("Unregistered viewHolder")
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        when (prefetchPolicy) {
            PrefetchPolicy.Enabled -> {
                prefetchedDelegates.forEach {
                    it.prefetchViews(recyclerView)
                }
                prefetchedNestedDelegates.forEach {
                    it.onAttachParent(recyclerView)
                }
            }

            PrefetchPolicy.Disabled -> Unit
        }

        delegates.forEach {
            it.sharedPools = this.sharedPools
            it.fragmentRef = fragmentRef
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        prefetchedDelegates.forEach {
            it.clearPrefetch()
        }
    }

    fun attachController(controller: RecyclerViewHoldersController) {
        delegates.forEach {
            it.controllers.add(controller)
        }
    }

    fun submitListChunked(items: List<BaseUiModel>, coroutineScope: CoroutineScope) {
        coroutineScope.launchUI {
            val chunkedList = mutableListOf<BaseUiModel>()
            items.forEach {
                chunkedList.add(it)
                submitList(chunkedList.toImmutableList())
                delay(24)
            }
        }
    }

    fun releaseResources() {
        delegates.forEach {
            it.onRelease()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DelegateViewHolder<BaseUiModel> {
        return delegates[viewType].getOrCreateViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: DelegateViewHolder<BaseUiModel>, position: Int) {
        onBindViewHolder(holder, position, mutableListOf())
    }

    override fun onBindViewHolder(holder: DelegateViewHolder<BaseUiModel>, position: Int, payloads: MutableList<Any>) {
        val item = getItem(position)
        delegates[getItemViewType(position)].onBindViewHolder(holder, item, payloads)
        delegates[getItemViewType(position)].onPositionBy(holder, item, position, itemCount)
    }

    override fun onViewRecycled(holder: DelegateViewHolder<BaseUiModel>) {
        super.onViewRecycled(holder)
        tryNull {
            val delegate = delegates[getItemViewType(holder.adapterPosition)]
            delegate.onRecycled(holder)
            (delegate as? ItemPrefetchDelegate<BaseUiModel, DelegateViewHolder<BaseUiModel>>)?.clearPrefetch()
        }
    }

    class Builder {
        private val delegates: MutableList<ItemDelegateBase<BaseUiModel, DelegateViewHolder<BaseUiModel>>> =
            mutableListOf()
        private var prefetchPolicy = PrefetchPolicy.Enabled
        private var fragment: Fragment? = null

        fun setDelegates(vararg delegate: ItemDelegateBase<out BaseUiModel, out DelegateViewHolder<out BaseUiModel>>): Builder {
            delegate.forEach {
                delegates.add(it as ItemDelegateBase<BaseUiModel, DelegateViewHolder<BaseUiModel>>)
            }
            return this
        }

        fun setActionProcessor(processor: (Action) -> Unit): Builder {
            delegates.forEach {
                it.action = processor
            }
            return this
        }

        fun setPrefetchPolicy(prefetchPolicy: PrefetchPolicy): Builder {
            this.prefetchPolicy = prefetchPolicy
            return this
        }

        fun installFragmentReference(reference: Fragment): Builder {
            this.fragment = reference
            return this
        }

        fun buildIn() = lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DelegateAdapter(delegates, prefetchPolicy) }

        fun build() = DelegateAdapter(delegates, prefetchPolicy).apply {
            fragmentRef = WeakReference(fragment)
        }
    }

    private val prefetchedDelegates = delegates
        .filterIsInstance<ItemPrefetchDelegate<BaseUiModel, DelegateViewHolder<BaseUiModel>>>()

    private val prefetchedNestedDelegates = delegates
        .filterIsInstance<NestedPrefetchParent>()

    private val sharedPools: MutableMap<String, RecyclerView.RecycledViewPool> = mutableMapOf()

    enum class PrefetchPolicy {
        Enabled, Disabled
    }

}

interface ItemDelegateBase<T : BaseUiModel, H : DelegateViewHolder<T>> {
    fun getOrCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): H
    fun onBindViewHolder(holder: H, item: T, payloads: Any)
    fun onPositionBy(holder: H, item: T, position: Int, itemCount: Int)
    fun onRecycled(holder: H) {

    }

    val uiModelClass: KClass<T>
    val uiModelHelper: UIModelHelper<T>
    var action: (Action) -> Unit
    var fragmentRef: WeakReference<Fragment>
    val controllers: MutableList<RecyclerViewHoldersController>
    var sharedPools: MutableMap<String, RecyclerView.RecycledViewPool>

    fun retrieveSharePool(key: String): RecyclerView.RecycledViewPool? {
        return if (sharedPools[key] == null) {
            sharedPools[key] = RecyclerView.RecycledViewPool()
            sharedPools[key]
        } else {
            sharedPools[key]
        }
    }

    fun <F : Fragment> doWithFragment(on: F.() -> Unit) {
        val fragment = fragmentRef.get()
        if (fragment != null) {
            on(fragment as F)
        } else {
            Log.d("Deb", "You call fragment notification without installed reference")
        }
    }

    fun onRelease() {

    }

}


abstract class ItemSimpleDelegate<T : BaseUiModel, H : DelegateViewHolder<T>>(
    final override val uiModelClass: KClass<T>
) : ItemDelegateBase<T, H> {

    override val uiModelHelper: UIModelHelper<T> = Novalles.provideUiInterfaceFor(uiModelClass)
    private val inspector = Novalles.provideInspectorFromUiModelRaw(uiModelClass)
    override val controllers: MutableList<RecyclerViewHoldersController> = mutableListOf()
    override var sharedPools: MutableMap<String, RecyclerView.RecycledViewPool> = mutableMapOf()
    override var fragmentRef: WeakReference<Fragment> = WeakReference(null)

    override fun getOrCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): H {
        return createViewHolder(inflater, parent)
    }


    abstract fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): H

    override fun onBindViewHolder(holder: H, item: T, payloads: Any) {
        val instructor = provideInstructor(holder, item, payloads)
        inspector.inspectPayloads(payloads, instructor, holder) {
            bindHolder(item, holder, instructor)
        }
        holder.setOnClickListeners(item)
    }

    open fun bindHolder(model: T, holder: H, instructor: Instructor) {
        inspector.bind(model, holder, instructor)
    }

    abstract fun provideInstructor(holder: H, item: T, payload: Any): Instructor

    fun pushAction(action: Action) {
        this.action(action)
    }

    override fun onPositionBy(holder: H, item: T, position: Int, itemCount: Int) {

    }

    override var action: (Action) -> Unit = { }

    protected val viewPool by lazy { RecyclerView.RecycledViewPool() }

    fun setupNestedViewPool(recyclerView: RecyclerView) {
        recyclerView.setRecycledViewPool(viewPool)
    }

}


abstract class ItemPrefetchDelegate<T : BaseUiModel, H : DelegateViewHolder<T>>(
    final override val uiModelClass: KClass<T>,
    @LayoutRes val layout: Int
) : ItemDelegateBase<T, H> {

    override val uiModelHelper: UIModelHelper<T> = Novalles.provideUiInterfaceFor(uiModelClass)
    private val inspector = Novalles.provideInspectorFromUiModelRaw(uiModelClass)
    override val controllers: MutableList<RecyclerViewHoldersController> = mutableListOf()
    override var sharedPools: MutableMap<String, RecyclerView.RecycledViewPool> = mutableMapOf()
    override var fragmentRef: WeakReference<Fragment> = WeakReference(null)

    private val prefetchedViewsPool = ArrayDeque<View>()
    open val prefetchedViewsCount = 1
    protected var canPrefetchViews = true

    override fun getOrCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): H {
        val prefetched = prefetchedViewsPool.removeFirstOrNull()
        return prefetched?.let {
            createViewHolder(prefetched)
        } ?: run {
            val view = inflater.inflate(layout, parent, false)
            createViewHolder(view)
        }
    }

    open fun bindHolder(model: T, holder: H, instructor: Instructor) {
        inspector.bind(model, holder, instructor)
    }

    abstract fun createViewHolder(view: View): H

    override fun onBindViewHolder(holder: H, item: T, payloads: Any) {
        val instructor = provideInstructor(holder, item, payloads)
        inspector.inspectPayloads(payloads, instructor, holder) {
            bindHolder(item, holder, instructor)
        }
        holder.setOnClickListeners(item)
    }

    abstract fun provideInstructor(holder: H, item: T, payload: Any): Instructor

    fun pushAction(action: Action) {
        this.action(action)
    }

    fun prefetchViews(parent: ViewGroup) {
        val asyncInflater = AsyncFlexLayoutInflater(parent.context)
        tryNull {
            repeat(prefetchedViewsCount) {
                asyncInflater.inflateAsync(layout, parent) { view ->
                    if (canPrefetchViews) {
                        if (view != null) {
                            prefetchedViewsPool.add(view)
                        } else {
                            Log.d("Delegate", "prefetched failed")
                        }
                    }
                }
            }
        }
    }

    fun clearPrefetch() {
        //prefetchedViewsPool.clear()
        canPrefetchViews = false
    }

    override fun onPositionBy(holder: H, item: T, position: Int, itemCount: Int) {

    }

    override var action: (Action) -> Unit = { }

    private val viewPool by lazy { RecyclerView.RecycledViewPool() }

    fun setupNestedViewPool(recyclerView: RecyclerView) {
        recyclerView.setRecycledViewPool(viewPool)
    }

    fun retrieveNestedViewPool(pool: (recycledViewPool: RecyclerView.RecycledViewPool) -> Unit) {
        viewPool.let(pool)
    }

}

interface NestedPrefetchParent {
    fun onAttachParent(parent: ViewGroup)
}

interface Action {
    object None : Action
}