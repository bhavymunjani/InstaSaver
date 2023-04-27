package com.example.instasaver;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.StringUtils;

import java.util.zip.Inflater;

public class profilePicFragment extends Fragment{

    String URL="NULL";
    ImageView mparticulerprofilepic;
    EditText getprofilepiclink;
    Button getprofilepic;
    Button downloadprofilepic;
    String profilepicurl="1";
    private Uri uri2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.profilepic_fragment,null);



        getprofilepiclink=v.findViewById(R.id.getprofilepiclink);
        getprofilepic=v.findViewById(R.id.getprofilepic);
        downloadprofilepic=v.findViewById(R.id.downloadprofilepic);
        mparticulerprofilepic=v.findViewById(R.id.particulerprofilepic);

        getprofilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL=getprofilepic.getText().toString().trim();
                if (URL.isEmpty()) {
                    new CommonMethod(getContext(),"first enter link");
                } else {
                    String result2= StringUtils.substringBefore(URL,"/?");
                    URL=result2+"/?__a=1&__d=dis";
                    processData();
                }
            }
        });


        downloadprofilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!profilepicurl.equals(1))
                {
                    DownloadManager.Request request=new DownloadManager.Request(uri2);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI|DownloadManager.Request.NETWORK_MOBILE);
                    request.setTitle("Download");
                    request.setDescription("................");
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DCIM,""+System.currentTimeMillis()+".jpeg");
                    DownloadManager manager=(DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);
                    new CommonMethod(getContext(),"Download done");
                }
                else
                {
                    new CommonMethod(getContext(),"no video to download");
                }
            }
        });


        return v;

    }

    private void processData() {

        StringRequest request=new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();

                MainURL mainURL = gson.fromJson(response, MainURL.class);
                profilepicurl = mainURL.getGraphql().getShortcode_media().getDisplay_url();
                uri2 = Uri.parse(profilepicurl);
                Glide.with(getContext()).load(uri2).into(mparticulerprofilepic);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new CommonMethod(getContext(),"Not Able To Fatch");
            }
        });
        RequestQueue queue= Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
