package com.example.haikalfluzain.nowadays.view;

import com.example.haikalfluzain.nowadays.base.BaseView;
import com.example.haikalfluzain.nowadays.model.Today;

import java.util.List;

public interface TodayView extends BaseView {
    void onSuccessLoadTodays(List<Today> todays);
    void onSuccessStore(String code, String message);
}
