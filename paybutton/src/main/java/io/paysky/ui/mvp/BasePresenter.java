package io.paysky.ui.mvp;

public class BasePresenter<View extends BaseView> {

    public View view;

    public BasePresenter(View view) {

        this.view = view;
    }

    public BasePresenter() {

    }

    public void attachView(View view) {

        this.view = view;

    }

    public void detachView() {
        this.view = null;
    }

    public boolean isViewAttached() {
        return view != null;
    }

    public boolean isViewDetached() {
        return !isViewAttached();
    }
}
