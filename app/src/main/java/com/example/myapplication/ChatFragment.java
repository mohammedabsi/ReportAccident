package com.example.myapplication;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.util.FileUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.stream.IntStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    ImageButton mButtonSend;
    ImageView qr_imageButton , add_Accidentdetails2 ;
    private ListView mListView;
    private ArrayList<ChatMessage> chatMessageArrayList;
    private EditText mEditTextMessage;
    private ImageView mImageView;

    private ChatMessageAdapter mAdapter;
    private TextView user_id2;
    private SharedViewModel viewModel;
    private int counter = 0;
    ArrayList<String> firstList = new ArrayList<String>();
    List<String> secondList = new ArrayList<String>();
    List<String> thirdList = new ArrayList<String>();
    private UploadApis uploadApis;
    private ProgressDialog progressDialog;


    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int CAPTURE_REQUEST_CODE = 0;
    private static final int SELECT_REQUEST_CODE = 1;

    String path;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference qRef = db.collection("Questions");
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Dialog dialog , details_dialog;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String currentemail = firebaseAuth.getCurrentUser().getEmail();




    ImageView openCam_img;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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
        View v = inflater.inflate(R.layout.fragment_chat, container, false);


        mButtonSend = v.findViewById(R.id.send);
        mEditTextMessage = v.findViewById(R.id.emailEdt);
        mListView = v.findViewById(R.id.mListView);
        mImageView = v.findViewById(R.id.iv_image);
        user_id2 = v.findViewById(R.id.user_id2);
        add_Accidentdetails2 = v.findViewById(R.id.add_Accidentdetails2);
        qr_imageButton = v.findViewById(R.id.qr_imageButton);
        firstList.add("a");
        firstList.add("b");
        firstList.add("c");
        firstList.add("d");



        add_Accidentdetails2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetailsDialog();
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Image Upload....");

        chatMessageArrayList = new ArrayList<ChatMessage>();

        showDialog();

        mAdapter = new ChatMessageAdapter(getActivity(), chatMessageArrayList);
        mListView.setAdapter(mAdapter);


        openCam_img = dialog.findViewById(R.id.openCam_img);

        qr_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                QrScannerFragment fragmentB = new QrScannerFragment();

                Bundle args = new Bundle();
                args.putString("add", "2");
                fragmentB.setArguments(args);
                getParentFragmentManager().beginTransaction().replace(R.id.container, fragmentB).addToBackStack(null).commit();

            }
        });


        // camera open
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }


        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mEditTextMessage.getText().toString().trim();
                mEditTextMessage.getText().clear();

                if (!user_id2.getText().toString().trim().isEmpty()) {
                    mListView.setSelection(mAdapter.getCount() - 1);
                    dialog.dismiss();
                    if (message.contains("ok") || message.contains("yes") || message.contains("a") || message.contains("b") || message.contains("c") || message.contains("d")) {
                        counter++;


                    } else {
                        mimicOtherMessage("Didn't recognize your input , try to write as requested !! ");

                    }
                    sendMessage(message, counter);

                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("You Should Scan Qr code First ");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });
                    alertDialog.show();

                }
            }
        });




        return v;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        viewModel.getText().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
            @Override
            public void onChanged(@Nullable CharSequence charSequence) {
                user_id2.setText(charSequence);

            }
        });
    }

    private void sendMessage(String message, int count) {
        ChatMessage chatMessage = new ChatMessage(message, true, false);
        mAdapter.add(chatMessage);
        //respond as Helloworld


        if (message.trim().equalsIgnoreCase("yes") || message.trim().contains("done")) {
            mimicOtherMessage("Choose the answer that includes your status by typing the code of your answer only \n to start the questions type ok");

        }

        if (message.trim().equalsIgnoreCase("ok")) {
            mimicOtherMessage("Question 1 : are you riding on right side or left side of the road ? \n a- left \n b-right ");

        }

        if (count == 3) {

            mimicOtherMessage("Question 2 : where did you get the hit ? \n a- bottom of the car \n b- front of the car \n c- left side \n  d- right side ");
            secondList.add(message);

        }
        if (count == 4) {

            mimicOtherMessage("Question 3 : whats your speed ?? \n a- 40 km< \n b- 40 km> \n c- 120 km>");
            secondList.add(message);


        }
        if (count == 5) {

            mimicOtherMessage("Question 4 : whats your speed ?? \n a- 40 km< \n b- 40 km> \n c- 120 km>");
            secondList.add(message);


        }
        if (count == 6) {
            secondList.add(message);
            // chatMessageArrayList.clear();
            IntStream.range(0, firstList.size()).forEach(i -> {
                double x = 1;

                if (firstList.get(i).equals(secondList.get(i))) {
                    thirdList.add(secondList.get(i));
                    System.out.println(thirdList);





                }


            });
            double q = (double) thirdList.size();
            double w = (double) firstList.size();
            double z = (double) (q / w) * 100;
            mimicOtherMessage("your result is : " + z + " %");
            mButtonSend.setEnabled(false);
            mEditTextMessage.setEnabled(false);


        }

    }

    private void mimicOtherMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false, false);
        mAdapter.add(chatMessage);
    }

    private void sendMessage() {
        ChatMessage chatMessage = new ChatMessage(null, true, true);
        mAdapter.add(chatMessage);

        mimicOtherMessage();
    }

    private void mimicOtherMessage() {
        ChatMessage chatMessage = new ChatMessage(null, false, true);
        mAdapter.add(chatMessage);
    }

    private void showDetailsDialog() {
        details_dialog = new Dialog(getActivity());
        details_dialog.setContentView(R.layout.adddetails_dialog);

        details_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        details_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        details_dialog.setCancelable(false);
        details_dialog.show();

        TextView generate_time_dialog = details_dialog.findViewById(R.id.generate_time_dialog);
        EditText acident_address_dialog = details_dialog.findViewById(R.id.acident_address_dialog);
        EditText carplate_dialog = details_dialog.findViewById(R.id.carplate_dialog);
        EditText time_dialog = details_dialog.findViewById(R.id.time_dialog);
        Button uploadAcc_btn_dialog = details_dialog.findViewById(R.id.uploadAcc_btn_dialog);
        Button cancel_dialog = details_dialog.findViewById(R.id.cancel_dialog);


        generate_time_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date today = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd , hh:mm:ss a");
                String dateToStr = format.format(today);
                time_dialog.setText(dateToStr);
            }
        });
        uploadAcc_btn_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!acident_address_dialog.getText().toString().trim().isEmpty()){
                    if (!time_dialog.getText().toString().trim().isEmpty()){
                        if (!carplate_dialog.getText().toString().trim().isEmpty()){
                            AccidentDetails accidentDetails = new AccidentDetails(time_dialog.getText().toString().trim(), acident_address_dialog.getText().toString().trim(), carplate_dialog.getText().toString().trim(), user_id2.getText().toString().trim(), currentemail);


                            //Todo : add accident details for second part

                            firestore.collection("Accident").document(user_id2.getText().toString().trim()+ "," +  currentemail ).collection(currentemail).document().set(accidentDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                        details_dialog.dismiss();
                                        add_Accidentdetails2.setVisibility(View.GONE);



                                    } else {
                                        Toast.makeText(getActivity(), "Something went wrong , try again later ...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }else {
                            carplate_dialog.setError("Empty field !");
                        }
                    }else {
                        time_dialog.setError("Empty field , generate time");
                    }
                }else {
                    acident_address_dialog.setError("Empty Field");
                }


            }
        });
        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                details_dialog.dismiss();
            }
        });





    }

    public void showDialog() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.uploadimg_dialog);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(false);
        dialog.show();
        AppCompatButton cancel = dialog.findViewById(R.id.cancel);
        ImageView openCam_img = dialog.findViewById(R.id.openCam_img);
        TextView img_toimgview = dialog.findViewById(R.id.img_toimgview);
        AppCompatButton uploadAcc_btn = dialog.findViewById(R.id.uploadAcc_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (openCam_img.getDrawable() == null) {
                    dialog.dismiss();
                //  getParentFragmentManager().beginTransaction().replace(R.id.container,new MainFragment()).commit();

                    Toast.makeText(getActivity(), "You cant start chatbot before uploading image", Toast.LENGTH_SHORT).show();

                }else {
                    dialog.dismiss();
               //     getParentFragmentManager().beginTransaction().replace(R.id.container,new MainFragment()).commit();
                }


            }
        });
        uploadAcc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                mimicOtherMessage("hello this is your assistant bot \n if you didnt get the other part Id please scan his code with the button shown down \n if you get the id then it must be shown at bottom of the chats \n type yes to start the chat bot");


            }
        });

        img_toimgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open camera
                if (CheckPermission()) {
                    Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(capture, CAPTURE_REQUEST_CODE);
                }
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAPTURE_REQUEST_CODE: {
                if (resultCode == RESULT_OK) {


                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    openCam_img.setImageBitmap(bitmap);


                }
            }
            break;

        }
    }

    private void ImageUpload() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://165.232.90.241/api/").addConverterFactory(GsonConverterFactory.create()).build();
        uploadApis = retrofit.create(UploadApis.class);


        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);


        Call<AddPhoto> call = uploadApis.uploadImage(body);

        call.enqueue(new Callback<AddPhoto>() {
            @Override
            public void onResponse(Call<AddPhoto> call, Response<AddPhoto> response) {

            }

            @Override
            public void onFailure(Call<AddPhoto> call, Throwable t) {
                Log.d("TAG", "failedresponse: " + t.toString());

            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();
        mButtonSend.setEnabled(true);
        mEditTextMessage.setEnabled(true);

        Bundle args = getArguments();


        if (args != null) {
            if (args.getString("id").equalsIgnoreCase("1")) {
                user_id2.setText(args.getString("idkey"));
                dialog.dismiss();
                add_Accidentdetails2.setVisibility(View.VISIBLE);




            } else if (args.getString("id").equalsIgnoreCase("2")) {
                user_id2.setText(args.getString("qrscan"));
                dialog.dismiss();
                add_Accidentdetails2.setVisibility(View.VISIBLE);

                Log.d("moham", "onCreateView: " + args.getString("qrscan"));
            }
        }


        if (user_id2.getText().toString().equals("")) {
            qr_imageButton.setVisibility(View.VISIBLE);

        } else {
            qr_imageButton.setVisibility(View.GONE);

        }


    }

    public boolean CheckPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Permission")
                        .setMessage("Please accept the permissions")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_LOCATION);


                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().overridePendingTransition(0, 0);
                            }
                        })
                        .create()
                       .show();


            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

            return false;
        } else {

            return true;

        }
    }
}