package com.example.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private TextView eUsername;
    private ProgressBar eBar;
    private TextView eLevel;
    private TextView eCursuri;
    private TextView eStreak;
    public ImageView eAvatar;

    private static User USER = null;
    Connection connect;


    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(User user) {
        ProfileFragment fragment = new ProfileFragment();
        USER = user;
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        eUsername = view.findViewById(R.id.tv_username);
        eUsername.setText(USER.getUsername());

        eStreak = view.findViewById(R.id.tv_streak);
        eLevel = view.findViewById(R.id.tv_level);
        eCursuri = view.findViewById(R.id.tv_cursuri);
        eBar = view.findViewById(R.id.pb_XP);
        eAvatar = view.findViewById(R.id.iv_avatar);

        setData();

        eAvatar.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
            View dialogLayout = inflater.inflate(R.layout.profilepic_popup, null);
            final AlertDialog dialog = builder.create();

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialog.setView(dialogLayout, 0, 0, 0, 0);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            WindowManager.LayoutParams wlmp = dialog.getWindow().getAttributes();
            wlmp.gravity = Gravity.BOTTOM;

            Button btnUpload = dialogLayout.findViewById(R.id.btnUpload);
            Button btnTake = dialogLayout.findViewById(R.id.btnTake);
            Button btnCancel = dialogLayout.findViewById(R.id.btnCancel);

            btnUpload.setOnClickListener(v3 -> {
                getImageFromAlbum();
                dialog.dismiss();
            });

            btnTake.setOnClickListener(v4 -> {
                dialog.dismiss();
            });

            btnCancel.setOnClickListener(v5 -> {
                dialog.dismiss();
            });

            builder.setView(dialogLayout);
            dialog.show();
        });

        return view;
    }

    private void setData() {
        int xp = USER.getXp();
        eCursuri.setText("Cursuri: " + USER.getCursuri());
        eStreak.setText(USER.getStreak() + "");
        eLevel.setText("Nivel " + xp /100);
        eBar.setProgress(xp %100);
        byte[] barray = USER.getBarray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(barray, 0, barray.length);
        eAvatar.setImageBitmap(bitmap);
    }

    private void getImageFromAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                case 101:
                    if (resultCode == getActivity().RESULT_OK && data != null) {
                        Bitmap Image = (Bitmap) data.getExtras().get("data");
                        eAvatar.setImageBitmap(Image);
                    }
                    break;
                case 100:
                    Uri selectedImageUri = data.getData();
                    if (selectedImageUri != null) {
                        eAvatar.setImageURI(selectedImageUri);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), selectedImageUri);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                            byte[] bArray = bos.toByteArray();
                            if (updateProfilePicture(bArray))
                                USER.setBarray(bArray);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "no data", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    private boolean updateProfilePicture(byte[] data) {
        try {
            SQLConnection connectionHelper = new SQLConnection();
            connect = connectionHelper.connectionclass();
            if (connect != null) {
                PreparedStatement stmt = connect.prepareStatement("UPDATE " + SQLConnection.profilesTable + " SET Image = ? WHERE Username = ?");
                stmt.setBytes(1, data);
                stmt.setString(2, USER.getUsername());
                stmt.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
