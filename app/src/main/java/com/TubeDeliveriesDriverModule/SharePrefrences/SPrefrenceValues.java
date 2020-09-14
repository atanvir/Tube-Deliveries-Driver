package com.TubeDeliveriesDriverModule.SharePrefrences;

import android.content.Context;
import android.util.Log;
import android.widget.Spinner;

import com.TubeDeliveriesDriverModule.Model.CommonModel;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;

public class SPrefrenceValues {

    public static void saveUserData(Context context, CommonModel model)
    {
        if(model.getRegisterResponse()!=null)
        {
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.TOKEN_TYPE, model.getRegisterResponse().getAuth().getTokenType());
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.ACCESS_TOKEN, model.getRegisterResponse().getAuth().getAccessToken());
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.REFRESH_TOKEN, model.getRegisterResponse().getAuth().getRefreshToken());
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.NAME, model.getRegisterResponse().getDriver().getFirstName() +"" +model.getRegisterResponse().getDriver().getFirstName()!=null?model.getRegisterResponse().getDriver().getFirstName():"");
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.MOBILE, model.getRegisterResponse().getDriver().getCountryCode()+" "+model.getRegisterResponse().getDriver().getPhone());
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.STATUS, model.getRegisterResponse().getDriver().getStatus());
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.ID,""+ model.getRegisterResponse().getDriver().getId());
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.EMAIL,""+ model.getRegisterResponse().getDriver().getEmail());
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.TOKEN,""+ model.getRegisterResponse().getDriver().getDeviceToken());
        }else if(model.getLoginResponse()!=null)
        {
            SharedPreferenceWriter.getInstance(context).writeBooleanValue(SPreferenceKey.IS_LOGIN, true);
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.TOKEN_TYPE, model.getLoginResponse().getAuth().getTokenType());
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.ACCESS_TOKEN, model.getLoginResponse().getAuth().getAccessToken());
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.REFRESH_TOKEN, model.getLoginResponse().getAuth().getRefreshToken());
            String name=model.getLoginResponse().getDriver().getFirstName() +" "+model.getLoginResponse().getDriver().getLastName();
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.NAME, name.trim());
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.MOBILE, model.getLoginResponse().getDriver().getCountryCode()+" "+model.getLoginResponse().getDriver().getPhone());
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.STATUS, model.getLoginResponse().getDriver().getStatus());
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.ID,""+ model.getLoginResponse().getDriver().getId());
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.EMAIL,""+ model.getLoginResponse().getDriver().getEmail());
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.TOKEN,""+ model.getLoginResponse().getDriver().getDeviceToken());

        }

    }

    public static void removeUserData(Context context)
    {
        SharedPreferenceWriter.getInstance(context).clearPreferenceValues();
        CommonUtils.getDeviceToken(context);
    }




}
