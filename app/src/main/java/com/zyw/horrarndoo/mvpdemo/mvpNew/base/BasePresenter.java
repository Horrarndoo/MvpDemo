package com.zyw.horrarndoo.mvpdemo.mvpNew.base;

/**
 * Created by Horrarndoo on 2017/4/25.
 */

public abstract class BasePresenter<M, V> {
    public M mIModel;
    public V mIView;

    /**
     * 返回presenter想持有的view引用
     *
     * @return
     */
    public abstract M getModel();

    /**
     * 绑定Model和View的引用
     *
     * @param m model
     * @param v view
     */
    public void attachMV(M m, V v) {
        this.mIModel = m;
        this.mIView = v;
        this.onStart();
    }

    /**
     * 解绑Model和View
     */
    public void detachMV() {
        mIView = null;
        mIModel = null;
    }

    /**
     * View和Model绑定完成立即执行
     * <p>
     * 实现类实现绑定完成后的逻辑,例如数据初始化等
     */
    public abstract void onStart();
}
