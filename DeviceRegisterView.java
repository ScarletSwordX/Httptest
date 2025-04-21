

import pocketgov.presenter.message.DeviceRegisterContract;

public class DeviceRegisterView implements DeviceRegisterContract.View {
    @Override
    public void showLoading() {
        // Show loading indicator
    }

    @Override
    public void hideLoading() {
        // Hide loading indicator
    }

    @Override
    public void onRegisterSuccess(String deviceId) {
        // Handle successful registration
    }

    @Override
    public void onRegisterFailed(String error) {
        // Handle registration failure
    }


}
