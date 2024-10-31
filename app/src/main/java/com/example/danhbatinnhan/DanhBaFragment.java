package com.example.danhbatinnhan;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.Manifest;

import com.example.danhbatinnhan.model.Contact;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DanhBaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DanhBaFragment extends Fragment {

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1001;
    ListView lvDanhBa ;
    ArrayList<Contact> dsDanhBa;
    ArrayAdapter<Contact> adapterDanhBa;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DanhBaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DanhBaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DanhBaFragment newInstance(String param1, String param2) {
        DanhBaFragment fragment = new DanhBaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private void showAllContactFromDevice() {

        Uri uri  = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        Cursor cursor = getActivity().getContentResolver().query(uri,null,null,null,null);

        dsDanhBa.clear();

        if (cursor != null) {
            dsDanhBa.clear();
            while (cursor.moveToNext()) {

                String tenCotName = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
                String tenCotPhone = ContactsContract.CommonDataKinds.Phone.NUMBER;


                int viTriCotName = cursor.getColumnIndex(tenCotName);
                int viTriCotPhone = cursor.getColumnIndex(tenCotPhone);


                String name = cursor.getString(viTriCotName);
                String phone = cursor.getString(viTriCotPhone);


                Contact contact = new Contact(name, phone);
                dsDanhBa.add(contact);
            }
            cursor.close();
            adapterDanhBa.notifyDataSetChanged();
        }

    }

    private void addControl(View view) {
        lvDanhBa = view.findViewById(R.id.lvDanhBa);
        dsDanhBa = new ArrayList<>();
        adapterDanhBa = new ArrayAdapter<>(
          getActivity(), android.R.layout.simple_list_item_1,dsDanhBa
        );
        lvDanhBa.setAdapter(adapterDanhBa);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_danh_ba, container, false);
    }

//    @Override
//    public void onViewCreated(View view,Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        addControl(view); // Gọi phương thức addControls
//        showAllContactFromDevice(); // Gọi phương thức lấy danh bạ
//    }

    private static final int REQUEST_CODE_READ_CONTACTS = 100;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addControl(view);


        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        } else {

            showAllContactFromDevice();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                showAllContactFromDevice();
            } else {

                Toast.makeText(getContext(), "Permission Denied to read contacts", Toast.LENGTH_SHORT).show();
            }
        }
    }
}