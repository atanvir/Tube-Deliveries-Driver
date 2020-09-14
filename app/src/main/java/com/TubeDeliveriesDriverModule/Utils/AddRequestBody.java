package com.TubeDeliveriesDriverModule.Utils;


import com.TubeDeliveriesDriverModule.Model.SignupModel;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AddRequestBody<T> {

    private MediaType mediaType = MediaType.parse("text/plain");

    private Map<String, RequestBody> requestBodyMap = new HashMap<>();

    public AddRequestBody(T data,String screen)
    {
        if(data instanceof SignupModel){
        if(screen.equalsIgnoreCase("Driver")) {
            requestBodyMap.put("type", RequestBody.create(mediaType, ((SignupModel) data).getType()));
            requestBodyMap.put("modal", RequestBody.create(mediaType, ((SignupModel) data).getModal()));
            requestBodyMap.put("vehicle_number", RequestBody.create(mediaType, ((SignupModel) data).getVehicle_number()));
            requestBodyMap.put("license_number", RequestBody.create(mediaType, ((SignupModel) data).getLicense_number()));
            requestBodyMap.put("insurance_number", RequestBody.create(mediaType, ((SignupModel) data).getInsurance_number()));
        }else if(screen.equalsIgnoreCase("Bank Details"))
        {
            //step one
            requestBodyMap.put("first_name", RequestBody.create(mediaType, ((SignupModel) data).getFirst_name()));
            requestBodyMap.put("last_name", RequestBody.create(mediaType, ((SignupModel) data).getLast_name()));
            requestBodyMap.put("email", RequestBody.create(mediaType, ((SignupModel) data).getEmail()));
            requestBodyMap.put("country_code", RequestBody.create(mediaType, ((SignupModel) data).getCountry_code()));
            requestBodyMap.put("phone_no", RequestBody.create(mediaType, ((SignupModel) data).getPhone_no()));
            requestBodyMap.put("password", RequestBody.create(mediaType, ((SignupModel) data).getPassword()));
            requestBodyMap.put("state", RequestBody.create(mediaType, ((SignupModel) data).getState()));
            requestBodyMap.put("device_type", RequestBody.create(mediaType, ((SignupModel) data).getDevice_type()));
            requestBodyMap.put("device_token", RequestBody.create(mediaType, ((SignupModel) data).getDevice_token()));
            requestBodyMap.put("city", RequestBody.create(mediaType, ((SignupModel) data).getCity()));
            requestBodyMap.put("longitude", RequestBody.create(mediaType, ((SignupModel) data).getLongitude()+""));
            requestBodyMap.put("latitude", RequestBody.create(mediaType, ((SignupModel) data).getLatitude()+""));

            //step two
            requestBodyMap.put("vehicle_type", RequestBody.create(mediaType, ((SignupModel) data).getType()));
            requestBodyMap.put("modal", RequestBody.create(mediaType, ((SignupModel) data).getModal()));
            requestBodyMap.put("vehicle_number", RequestBody.create(mediaType, ((SignupModel) data).getVehicle_number()));
            requestBodyMap.put("license_number", RequestBody.create(mediaType, ((SignupModel) data).getLicense_number()));
            requestBodyMap.put("insurance_number", RequestBody.create(mediaType, ((SignupModel) data).getInsurance_number()));

            //step three
            requestBodyMap.put("bank_type", RequestBody.create(mediaType, ((SignupModel) data).getBank_type()));
            requestBodyMap.put("name", RequestBody.create(mediaType, ((SignupModel) data).getName()));
            requestBodyMap.put("account_number", RequestBody.create(mediaType, ((SignupModel) data).getAccount_number()));
            requestBodyMap.put("iban", RequestBody.create(mediaType, ((SignupModel) data).getIban()));

        }else if(screen.equalsIgnoreCase("My Profile")) {
            //step one
            requestBodyMap.put("email", RequestBody.create(mediaType, ((SignupModel) data).getEmail()));
            requestBodyMap.put("country_code", RequestBody.create(mediaType, ((SignupModel) data).getCountry_code()));
            requestBodyMap.put("phone_no", RequestBody.create(mediaType, ((SignupModel) data).getPhone_no()));
            requestBodyMap.put("first_name", RequestBody.create(mediaType, ((SignupModel) data).getFirst_name()));
            requestBodyMap.put("last_name", RequestBody.create(mediaType, ((SignupModel) data).getLast_name()));
            requestBodyMap.put("state", RequestBody.create(mediaType, ((SignupModel) data).getState()));
            requestBodyMap.put("city", RequestBody.create(mediaType, ((SignupModel) data).getCity()));
            requestBodyMap.put("longitude", RequestBody.create(mediaType, ((SignupModel) data).getLongitude()+""));
            requestBodyMap.put("latitude", RequestBody.create(mediaType, ((SignupModel) data).getLatitude()+""));


            //step two
            requestBodyMap.put("vehicle_type", RequestBody.create(mediaType, ((SignupModel) data).getType()));
            requestBodyMap.put("modal", RequestBody.create(mediaType, ((SignupModel) data).getModal()));
            requestBodyMap.put("vehicle_number", RequestBody.create(mediaType, ((SignupModel) data).getVehicle_number()));
            requestBodyMap.put("license_number", RequestBody.create(mediaType, ((SignupModel) data).getLicense_number()));
            requestBodyMap.put("insurance_number", RequestBody.create(mediaType, ((SignupModel) data).getInsurance_number()));

            //step three
            requestBodyMap.put("bank_type", RequestBody.create(mediaType, ((SignupModel) data).getBank_type()));
            requestBodyMap.put("name", RequestBody.create(mediaType, ((SignupModel) data).getName()));
            requestBodyMap.put("account_number", RequestBody.create(mediaType, ((SignupModel) data).getAccount_number()));
            requestBodyMap.put("iban", RequestBody.create(mediaType, ((SignupModel) data).getIban()));
        }
        }

    }

    public Map<String, RequestBody> getBody()
    {
        return requestBodyMap;
    }
}
