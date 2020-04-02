package com.trimax.vts.view.master.model.documents;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadDocumentTypeList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("insurance_company_name")
    @Expose
    private String insuranceCompanyName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInsuranceCompanyName() {
        return insuranceCompanyName;
    }

    public void setInsuranceCompanyName(String insuranceCompanyName) {
        this.insuranceCompanyName = insuranceCompanyName;
    }

}
