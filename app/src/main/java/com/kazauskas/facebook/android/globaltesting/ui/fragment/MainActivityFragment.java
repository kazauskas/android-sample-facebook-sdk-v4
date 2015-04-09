package com.kazauskas.facebook.android.globaltesting.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.ShareButton;
import com.kazauskas.facebook.android.globaltesting.R;

import org.json.JSONArray;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final String TAG = "TestsForFacebook4.0";

    AccessToken facebookAccessToken;
    CallbackManager callbackManager;

    final String appLinkUrl = "https://fb.me/826822027393881";
    final String previewImageUrl = "http://www.goodfellasmagazine.com/wp-content/uploads/2013/11/Most-famous-lol-face.jpg";

    @InjectView(R.id.btn_fb_share) ShareButton btnFacebookShare;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, v);
        initFacebook();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initFacebook(){
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, facebookLoginResultCallback);
        facebookAccessToken = AccessToken.getCurrentAccessToken();

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();
        btnFacebookShare.setShareContent(content);
    }

    @OnClick(R.id.btn_fb_login)
    public void loginWithFacebook(){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
    }

    @OnClick(R.id.btn_fb_invite_friends)
    public void inviteWithFacebook(){

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl)
                    .setPreviewImageUrl(previewImageUrl)
                    .build();
            AppInviteDialog.show(this, content);
        }
    }

    @OnClick(R.id.btn_fb_get_friends)
    public void getFriends(){
        if (facebookAccessToken == null || facebookAccessToken.isExpired()){
            loginWithFacebook();
        }
        if (facebookAccessToken.isExpired()){
            loginWithFacebook();
        }
        if (null != facebookAccessToken){
            GraphRequestBatch batch = new GraphRequestBatch(
                GraphRequest.newMyFriendsRequest(
                    facebookAccessToken,
                    new GraphRequest.GraphJSONArrayCallback() {
                        @Override public void onCompleted(JSONArray jsonArray, GraphResponse response) {
                            Log.i(TAG, "Batch request for facebook friends completed with result: " +jsonArray + ", response: " + response.getRawResponse() );
                        }
                    })
            );
            batch.executeAsync();
        }
        else{
            Log.e(TAG, "accessToken is null!");
        }
    }

    private final FacebookCallback<LoginResult> facebookLoginResultCallback = new FacebookCallback<LoginResult>() {
        @Override public void onSuccess(LoginResult loginResult) {
            facebookAccessToken = loginResult.getAccessToken();
            Log.i(TAG, "Login successfully finished");
        }
        @Override public void onCancel() {

        }
        @Override public void onError(FacebookException e) {

        }
    };
}
