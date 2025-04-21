



public interface DeviceRegisterContract {

    interface View {
        void showLoading();
        void hideLoading();
        void onRegisterSuccess(String deviceId);
        void onRegisterFailed(String error);
    }
    interface Presenter {
        void registerDevice();

        void attachView(View view);

        void detachView();

    }

    interface Model {
        interface OnRegisterListener {
            void onSuccess(String deviceId);
            void onFailure(String error);
        }
        void registerDevice(OnRegisterListener listener);
    }
}