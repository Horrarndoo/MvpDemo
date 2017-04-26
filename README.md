# MvpDemo
关于MVP的概念，或者MVP相对传统MVC的好处，这些这里就不多讲了，网上的资料随便一搜就是一大把。最近刚好项目重构，参考网上一些文章之后，结合自身的理解，本次简单的总结一下我个人对MVP的一些理解。

为了最直观的比较，本次通过三个demo示例实现一个登陆demo逻辑，来简单的演示一下MVC、MVP以及实际MVP使用的异同。

先看一下布局代码，就是很简单的一个textView，用来显示登陆结果，三个demo的布局代码都是一样的。

```
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
             android:layout_width="match_parent"
             android:layout_height="match_parent"
             android:orientation="vertical">

    <TextView
        android:id="@+id/tv_login_result"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center"
        android:text="Loading Success"
        android:textSize="50sp"
        android:visibility="invisible"/>
</FrameLayout>
```
由于只是一个demo，这里稍微偷了下懒，默认一打开demo就会自动的“登陆”，然后两秒后返回登陆结果。

```
/**
 * Created by Horrarndoo on 2017/4/25.
 * 模拟登陆请求
 */

public class LoginRequest {
    public static void login(String name, String password, final LoginListener loginListener) {
        Log.w("tag", "name = " + name);
        Log.w("tag", "password = " + password);
        //假设下面是正常的登陆请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    loginListener.onFailed();
                }
                loginListener.onSuccess();
            }
        }).start();
    }

    public interface LoginListener {
        void onSuccess();

        void onFailed();
    }
}
```
实现效果如下：

![这里写图片描述](http://img.blog.csdn.net/20170426092850602?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvb1FpbllvdQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

## **MVC-demo**
先看看我们最熟悉的MVC方式如何实现。
```
public class MvcActivity extends AppCompatActivity {
    @BindView(R.id.tv_login_result)
    TextView tvLoginResult;

    private ProgressDialog mWaitPorgressDialog;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvc);
        ButterKnife.bind(this);
        mWaitPorgressDialog = new ProgressDialog(this);
        login();
    }

    private void login() {
        showProgressDialog("login...");
        LoginRequest.login("Horrarndoo", "mvczzzzzz", new LoginRequest.LoginListener() {
            @Override
            public void onSuccess() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvLoginResult.setText("login success.");
                        tvLoginResult.setVisibility(View.VISIBLE);
                        hideProgressDialog();
                    }
                });
            }

            @Override
            public void onFailed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvLoginResult.setText("login failed.");
                        tvLoginResult.setVisibility(View.VISIBLE);
                        hideProgressDialog();
                    }
                });
            }
        });
    }

    private void showProgressDialog(String msg) {
        mWaitPorgressDialog.setMessage(msg);
        mWaitPorgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mWaitPorgressDialog != null) {
            mWaitPorgressDialog.dismiss();
        }
    }
}
```
这么看MVC框架其实也没什么问题，代码逻辑也很清晰。但是实际的项目中自然不可能说只有这么几句简单的逻辑，往往一个页面，十几个甚至几十个“登陆”逻辑。这时候我们再想想如果还是按照MVC模式来写的话，所有的界面处理、逻辑等等，全部都堆在Activity，Activity的代码量会有多大，维护起来会多么痛苦。

## **MVP-Simple-demo**
出现了问题，自然就要想着如何解决这个问题，下面我们看看一个MVP框架的一个简单demo示例。
先看看代码结构：

![这里写图片描述](http://img.blog.csdn.net/20170426100011927?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvb1FpbllvdQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

### Model层
Model层有一个接口ILoginModel，还有一个实现了ILoginModel接口的实现类。

```
public interface ILoginModel {
    void requestLogin(String userName, String userPassword, LoginModel.LoginListener loginListener);
}
```
ILoginModel接口很简单，只有一个方法，就是请求登陆的方法。

```
public class LoginModel implements ILoginModel {
    @Override
    public void requestLogin(String userName, String userPassword, final LoginListener loginListener) {
        LoginRequest.login(userName, userPassword, new LoginRequest.LoginListener() {
            @Override
            public void onSuccess() {
                loginListener.loginSuccess();
            }

            @Override
            public void onFailed() {
                loginListener.loginFailed();
            }
        });
    }

    public interface LoginListener {
        void loginSuccess();

        void loginFailed();
    }
}
```
LoginModel实现ILoginModel的接口方法，并且通过一个接口对外回调登陆结果。

### View层
View包下，只有一个view的接口，根据前面MVC框架写法，我们抽出了几个接口。其实严格意义上来讲，Activity应该也是归到View包下。

```
public interface ILoginView {
    void showLoginSuccess();
    void showLoginFailed();
    void showDialog();
    void hideDialog();
    String getUserName();
    String getUserPassword();
}
```
接下来呢，我们让Activity实现ILoginView 的接口方法。

```
public class MvpActivity extends AppCompatActivity implements ILoginView{
    @BindView(R.id.tv_login_result)
    TextView tvLoginResult;

    LoginPresenter mPresenter;
    private ProgressDialog mWaitPorgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);
        ButterKnife.bind(this);
        mWaitPorgressDialog = new ProgressDialog(this);
    }

    @Override
    public void showLoginSuccess() {
        tvLoginResult.setText("login success.");
        tvLoginResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoginFailed() {
        tvLoginResult.setText("login failed.");
        tvLoginResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDialog() {
        showProgressDialog("login...");
    }

    @Override
    public void hideDialog() {
        hideProgressDialog();
    }

    @Override
    public String getUserName() {
        return "Horrarndoo";
    }

    @Override
    public String getUserPassword() {
        return "mvpzzzzzz";
    }

    private void showProgressDialog(String msg) {
        mWaitPorgressDialog.setMessage(msg);
        mWaitPorgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mWaitPorgressDialog != null) {
            mWaitPorgressDialog.dismiss();
        }
    }
}
```
### Presenter层
Presenter层相当于沟通Model层和View层的一个中间层，主要做一些业务逻辑的判断处理。Presenter层持有Model和View的引用，Presenter将Model层处理完的数据通过接口回调给View层，View层做界面上的显示处理。也就是说View和Model的交互，全部通过presenter。

```
public class LoginPresenter {
    ILoginModel mILoginModel;
    ILoginView mILoginView;
    Handler mHandler = new Handler(Looper.getMainLooper());
    public LoginPresenter(ILoginView iLoginView){
        mILoginModel = new LoginModel();
        mILoginView = iLoginView;
    }

    public void login(){
        String userName = mILoginView.getUserName();
        String userPassword = mILoginView.getUserPassword();
        mILoginView.showDialog();
        mILoginModel.requestLogin(userName, userPassword, new LoginModel.LoginListener() {
            @Override
            public void loginSuccess() {
                if(mILoginView != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mILoginView.hideDialog();
                            mILoginView.showLoginSuccess();
                        }
                    });
                }
            }

            @Override
            public void loginFailed() {
                if (mILoginView != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mILoginView.hideDialog();
                            mILoginView.showLoginFailed();
                        }
                    });
                }
            }
        });
    }
}
```
最后Activity通过一个LoginPresenter对象，调用登陆方法。

```
LoginPresenter mPresenter;
mPresenter = new LoginPresenter(this);
mPresenter.login();
```
最终实现的效果和MVC实现的效果是一样的，这时候就有人要问了，这代码量比MVC还要大啊，而且一堆接口乱起八糟的好烦啊。确实很烦，但是实际上我们这样做将所有的业务逻辑和数据处理都从View层剥离开来，View只用做最简单的显示工作，数据上面的工作都有Model层去处理，业务逻辑也都丢给Presenter去处理，不管是从测试还是说维护的方面来讲，好处都是很大的。
##**MVP-demo**
以上是简单的MVPdemo示例，考虑到实际应用场景，我们来对这个简单的MVP-Simple进行一些修改和封装。先看看我们的代码结构：
![这里写图片描述](http://img.blog.csdn.net/20170426102649871?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvb1FpbllvdQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
和上面的MVP-Simple相比，这里将Model，View，Presenter都抽取了一个基类，针对IView和IModel，也抽取了一个基类。
### Base
#### BasePresneter
首先看看BasePresneter，考虑到Presenter需要获取IModel和IView的引用，所以做如下封装。

```
public abstract class BasePresenter<M, V> {
    public M mIModel;
    public V mIView;

    /**
     * 返回presenter要持有的Model引用
     *
     * @return
     */
    public abstract M getModel();

    /**
     * 绑定IModel和IView的引用
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
```
#### BaseModel&IBaseModel
由于我们这里并没有公共方法和变量可以抽取出来，所以这里先空着，如果有实际需求，可以在这里做一些处理。

```
public class BaseModel {
    //初始化一些公共数据,声明全局变量等
}

public interface IBaseModel {
}
```

#### IBaseView
IBaseView接口主要定义了2个方法，showToast提供给所有BaseView（BaseActivity或者BaseFragment）实现，继承自BaseActivity或者BaseFragment的子类就不用一个个去实现showToast接口。而考虑到每个Activity或者Fragment需要持有一个presenter引用，所有再定义一个setPresenter，由继承BaseActivity或者BaseFragment的子类实现presenter的初始化。
```
public interface IBaseView {
    void showToast(String msg);

    void setPresenter();
}
```
#### BaseMvpActivity
BaseMvpActivity主要是做了一些基本的封装，由于具体的presenter和IModel由子类决定，所以这里定义2个继承BasePresenter和IBaseModel的泛型。
```
public abstract class BaseMvpActivity<P extends BasePresenter, M extends IBaseModel> extends
        AppCompatActivity implements IBaseView {

    protected ProgressDialog mWaitPorgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getlayoutId());
        ButterKnife.bind(this);
        initData();
    }

    protected abstract int getlayoutId();

    /**
     * presenter 具体的presenter由子类确定
     */
    protected P mPresenter;

    /**
     * model 具体的model由子类确定
     */
    private M mIModel;

    /**
     * 初始化数据
     * <p>
     * 子类可以复写此方法初始化子类数据
     */
    protected void initData() {
        mWaitPorgressDialog = new ProgressDialog(this);
        if (this instanceof IBaseView) {
            this.setPresenter();
            if (mPresenter != null) {
                mIModel = (M) mPresenter.getModel();
                mPresenter.attachMV(mIModel, this);
                Log.d("tag", "attach M V success.");
            }
        } else {
            Log.e("tag", "attach M V failed.");
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachMV();
            Log.d("tag", "detach M V success.");
        }
        super.onDestroy();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showProgressDialog(String msg) {
        mWaitPorgressDialog.setMessage(msg);
        mWaitPorgressDialog.show();
    }

    protected void hideProgressDialog() {
        if (mWaitPorgressDialog != null) {
            mWaitPorgressDialog.dismiss();
        }
    }
}
```

### contract
上面是Base封装的一些封装思路介绍，看看MyContract，前面我们的ILoginView和ILoginModel是单独定义的，这里我们通过一个协议接口MyContract来定义ILoginView和ILoginModel，这样也确定了LoginPresenter的IModel和IView的具体类型。由于Presenter、IView和IModel都定义在MyContract，这样一来，presenter方法、view接口方法、model接口方法全部都一目了然。后面如果有功能拓展的话，直接按照这个模式往后面添加就可以了。
```
public interface MyContract {
    abstract class LoginPresenter extends BasePresenter<ILoginModel, ILoginView> {
        public abstract void login();
    }

    interface ILoginModel extends IBaseModel {
        void requestLogin(String userName, String userPassword, LoginModel.LoginListener
                loginListener);
    }

    interface ILoginView extends IBaseView {
        void showLoginSuccess();
        void showLoginFailed();
        void showDialog();
        void hideDialog();
        String getUserName();
        String getUserPassword();
    }
}
```
### **实现类**
接口和接口方法都定义好了，实现类的话和前面的MVP-Simple大体是一样的。

#### LoginModel
先看看LoginModel，对比前面的LoginModel，这里的LoginModel继承了BaseModel，然后实现的MyContract中定义的ILoginModel 接口方法。
```
public class LoginModel extends BaseModel implements MyContract.ILoginModel {
    @Override
    public void requestLogin(String userName, String userPassword, final LoginListener loginListener) {
        LoginRequest.login(userName, userPassword, new LoginRequest.LoginListener() {
            @Override
            public void onSuccess() {
                loginListener.loginSuccess();
            }

            @Override
            public void onFailed() {
                loginListener.loginFailed();
            }
        });
    }

    public interface LoginListener {
        void loginSuccess();

        void loginFailed();
    }
}

```
#### LoginPresenter
再看看最后的LoginPresenter，和前面MVP-Simple不同的地方在于LoginPresenter继承MyContract.LoginPresenter，也就是说所有presenter要实现的方法，在MyContract中就已经确定好了。
```
public class LoginPresenter extends MyContract.LoginPresenter {
    Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void login() {
        String userName = mIView.getUserName();
        String userPassword = mIView.getUserPassword();
        mIView.showDialog();
        mIModel.requestLogin(userName, userPassword, new LoginModel.LoginListener() {
            @Override
            public void loginSuccess() {
                if(mIView != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mIView.hideDialog();
                            mIView.showLoginSuccess();
                            mIView.showToast("hahaha, this is new mvp.");
                        }
                    });
                }
            }

            @Override
            public void loginFailed() {
                if (mIView != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mIView.hideDialog();
                            mIView.showLoginFailed();
                        }
                    });
                }
            }
        });
    }

    @Override
    public LoginModel getModel() {
        return new LoginModel();
    }

    @Override
    public void onStart() {
        Log.w("tag", "LoginPresenter onStart.");
    }
}

```
### **Activity**
最后看看我们的Activity实现，由于继承BaseMvpActivity，所有butterKnife的绑定，onCreate等方法都不用实现了，直接实现抽象方法getLayoutId()确定Activity要显示的布局就可以了。

```
public class MvpNewActivity extends BaseMvpActivity<MyContract.LoginPresenter, MyContract
        .ILoginModel> implements MyContract.ILoginView {
    @BindView(R.id.tv_login_result)
    TextView tvLoginResult;

    @Override
    protected int getlayoutId() {
        return R.layout.activity_mvp_new;
    }

    @Override
    public void setPresenter() {
        mPresenter = new LoginPresenter();
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.login();
    }

    @Override
    public void showLoginSuccess() {
        tvLoginResult.setText("login success.");
        tvLoginResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoginFailed() {
        tvLoginResult.setText("login failed.");
        tvLoginResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDialog() {
        showProgressDialog("login...");
    }

    @Override
    public void hideDialog() {
        hideProgressDialog();
    }

    @Override
    public String getUserName() {
        return "Horrarndoo";
    }

    @Override
    public String getUserPassword() {
        return "mvpzzzzzz";
    }
}

```
为了区别前面的mvp和验证IBaseView的实际效果，我们这个最终的demo在登陆完成后加了一条toast提示。

![这里写图片描述](http://img.blog.csdn.net/20170426111114365?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvb1FpbllvdQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

三个demo对比看一下的话，还是能看出挺大差别的，个中好处，还是需要自己去领会。
OK，话不多说，最后附上完整demo地址：[https://github.com/Horrarndoo/MvpDemo](https://github.com/Horrarndoo/MvpDemo)
