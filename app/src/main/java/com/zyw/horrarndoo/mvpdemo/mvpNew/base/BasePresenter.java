package com.zyw.horrarndoo.mvpdemo.mvpNew.base;

/**
 * Created by Horrarndoo on 2017/4/25.
 */

public abstract class BasePresenter<M, V> {
    protected M mIModel;
    protected V mIView;

    /**
     * 返回presenter要持有的Model引用
     *
     * @return
     */
    protected abstract M getModel();

    /**
     * 绑定IModel和IView的引用
     *
     * @param v view
     */
    public void attachMV(V v) {
        this.mIModel = getModel();
        this.mIView = v;
        this.onStart();
    }

    /**
     * 解绑IModel和IView
     */
    public void detachMV() {
        mIView = null;
        mIModel = null;
    }

    /**
     * IView和IModel绑定完成立即执行
     * <p>
     * 实现类实现绑定完成后的逻辑,例如数据初始化等
     */
    public abstract void onStart();
}
