package com.example.haikalfluzain.nowadays.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.haikalfluzain.nowadays.R;
import com.example.haikalfluzain.nowadays.activity.ChangePort;

public class NoConnectionFragment extends BottomSheetDialogFragment {
    Button changePort, retry;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_no_connection, container, false);

        changePort = view.findViewById(R.id.changePort);
        retry = view.findViewById(R.id.retry);

        changePort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChangePort.class));
                dismiss();
            }
        });

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}
