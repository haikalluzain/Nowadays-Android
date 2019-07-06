package com.example.haikalfluzain.nowadays.view;

import com.example.haikalfluzain.nowadays.base.BaseView;

public interface AuthView extends BaseView {
    void onSuccessLogin(String code, String message, String id, String token);
    void onSuccessLogout(String code, String message);
    void onSuccessShowUser(String id, String name, String email);
}
