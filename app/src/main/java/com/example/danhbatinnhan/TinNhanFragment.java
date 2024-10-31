package com.example.danhbatinnhan;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.Manifest;

import com.example.danhbatinnhan.model.Message;

import java.util.ArrayList;

public class TinNhanFragment extends Fragment {

    private static final int REQUEST_CODE_READ_SMS = 101;
    ListView lvTinNhan;
    ArrayList<Message> dsTinNhan;
    ArrayAdapter<Message> adapterTinNhan;

    public TinNhanFragment() {
        // Required empty public constructor
    }

    public static TinNhanFragment newInstance() {
        return new TinNhanFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void showAllMessagesFromDevice() {
        Uri uriSms = Uri.parse("content://sms/inbox");

        Cursor cursor = getActivity().getContentResolver().query(uriSms, null, null, null, null);

        dsTinNhan.clear();

        if (cursor != null) {
            while (cursor.moveToNext()) {

                @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("body"));
                @SuppressLint("Range") String body = cursor.getString(cursor.getColumnIndex("address"));

                Message message = new Message(address, body);
                dsTinNhan.add(message);
            }
            cursor.close();
            adapterTinNhan.notifyDataSetChanged();
        }
    }

    private void addControl(View view) {
        lvTinNhan = view.findViewById(R.id.lvTinNhan);
        dsTinNhan = new ArrayList<>();
        adapterTinNhan = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, dsTinNhan
        );
        lvTinNhan.setAdapter(adapterTinNhan);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tin_nhan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addControl(view);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_SMS}, REQUEST_CODE_READ_SMS);
        } else {
            showAllMessagesFromDevice();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showAllMessagesFromDevice();
            } else {
                Toast.makeText(getContext(), "Permission Denied to read SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }
}