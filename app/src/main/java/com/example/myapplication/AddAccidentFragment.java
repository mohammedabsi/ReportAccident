package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAccidentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAccidentFragment extends Fragment {
    AppCompatButton addAccident;
    ImageButton scannerBtn;
    EditText idScanner, time, acident_address, carplate;
  //  private SharedViewModel viewModel;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String currentemail = firebaseAuth.getCurrentUser().getEmail();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddAccidentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddAccidentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddAccidentFragment newInstance(String param1, String param2) {
        AddAccidentFragment fragment = new AddAccidentFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.add_aacident, container, false);
        addAccident = v.findViewById(R.id.addAccident);
        scannerBtn = v.findViewById(R.id.scannerBtn);

        idScanner = v.findViewById(R.id.idScanner);
        time = v.findViewById(R.id.time);
        carplate = v.findViewById(R.id.carplate);
        acident_address = v.findViewById(R.id.acident_address);



        scannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QrScannerFragment fragmentB = new QrScannerFragment();

                Bundle args = new Bundle();
                args.putString("add", "1");
                fragmentB .setArguments(args);
                getParentFragmentManager().beginTransaction().replace(R.id.container,fragmentB).addToBackStack(null).commit();


            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date today = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd , hh:mm:ss a");
                String dateToStr = format.format(today);
                System.out.println(dateToStr);
                time.setText(dateToStr);
            }
        });

        addAccident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccident();


            }
        });


        return v;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
////        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
////        viewModel.getText().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
////            @Override
////            public void onChanged(@Nullable CharSequence charSequence) {
////               // idScanner.setText(charSequence);
////            }
////        });
//    }



    private void CreateAccident() {
        String acc_time = time.getText().toString();
        String acc_add = acident_address.getText().toString();
        String car_plt = carplate.getText().toString();
        String second_id = idScanner.getText().toString();

        AccidentDetails accidentDetails = new AccidentDetails(acc_time, acc_add, car_plt, second_id, currentemail);

        firestore.collection("Accident").document(currentemail + "," + second_id).collection(currentemail).document().set(accidentDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    //jump to next fragment
                    Bundle bundle = new Bundle();
                    bundle.putString("idkey", idScanner.getText().toString()); // Put anything what you want
                    bundle.putString("id", "1"); // Put anything what you want

                    ChatFragment fragment2 = new ChatFragment();
                    fragment2.setArguments(bundle);

                    getParentFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();

                } else {
                    Toast.makeText(getActivity(), "Something went wrong , try again later ...", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
          idScanner.setText(args.getString("qrscan"));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        idScanner.getText().clear();
        time.getText().clear();
    }
}