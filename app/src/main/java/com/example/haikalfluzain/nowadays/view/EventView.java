package com.example.haikalfluzain.nowadays.view;

import com.example.haikalfluzain.nowadays.base.BaseView;
import com.example.haikalfluzain.nowadays.model.Event;

import java.util.List;

public interface EventView extends BaseView {
    void onSuccessLoadEvent(List<Event> events);
    void onSuccessStoreEvent(String code, String message);
    void onSuccessDeleteEvent(String code, String message);
    void onSuccessUpdateEvent(String code, String message);
}
