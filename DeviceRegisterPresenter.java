

import pocketgov.presenter.message.DeviceRegisterContract.Model;
import pocketgov.presenter.message.DeviceRegisterContract.View;

/**
 * Present layer implementation class for device registration module, responsible for interacting
 * with register Device state and view attaching, detaching.
 * </p>
 * @author Zhongwei Zhang
 */
public class DeviceRegisterPresenter implements DeviceRegisterContract.Presenter {
    private View view;
    private final Model model;

    public DeviceRegisterPresenter(DeviceRegisterContract.Model model, DeviceRegisterContract.View view) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void registerDevice() {
        if (view != null) {
            view.showLoading();
        }

        model.registerDevice(new DeviceRegisterContract.Model.OnRegisterListener() {
            @Override
            public void onSuccess(String deviceId) {
                if (view != null) {
                    view.hideLoading();
                    view.onRegisterSuccess(deviceId);
                }
            }

            @Override
            public void onFailure(String error) {
                if (view != null) {
                    view.hideLoading();
                    view.onRegisterFailed(error);
                }
            }
        });
    }

    @Override
    public void attachView(DeviceRegisterContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}