package com.trimax.vts.view.master.pojo.documents;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UploadDocument implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("vehicle_id")
    @Expose
    private String vehicleId;
    @SerializedName("atribute_id")
    @Expose
    private String atributeId;
    @SerializedName("other_name")
    @Expose
    private String otherName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("expiry_date")
    @Expose
    private String expiryDate;
    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("file_path")
    @Expose
    private String filePath;
    @SerializedName("insurance_company")
    @Expose
    private String insuranceCompany;
    @SerializedName("insurance_policy_no")
    @Expose
    private String insurancePolicyNo;
    @SerializedName("insurance_amount")
    @Expose
    private String insuranceAmount;
    @SerializedName("insurance_company_name")
    @Expose
    private String insuranceCompanyName;

    public UploadDocument(String atributeId) {
        this.id = "";
        this.atributeId = atributeId;
        this.vehicleId = "";
        this.otherName = "";
        this.description = "";
        this.expiryDate = "";
        this.fileName = "";
        this.filePath = "";
        this.insuranceCompany = "";
        this.insurancePolicyNo = "";
        this.insuranceAmount = "";
        this.insuranceCompanyName = "";
    }

    public void UpdateModelValue(String id, String vehicleId, String otherName, String description, String expiryDate, String fileName, String filePath, String insuranceCompany, String insurancePolicyNo, String insuranceAmount, String insuranceCompanyName) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.otherName = otherName;
        this.description = description;
        this.expiryDate = expiryDate;
        this.fileName = fileName;
        this.filePath = filePath;
        this.insuranceCompany = insuranceCompany;
        this.insurancePolicyNo = insurancePolicyNo;
        this.insuranceAmount = insuranceAmount;
        this.insuranceCompanyName = insuranceCompanyName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getAtributeId() {
        return atributeId;
    }

    public void setAtributeId(String atributeId) {
        this.atributeId = atributeId;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getInsurancePolicyNo() {
        return insurancePolicyNo;
    }

    public void setInsurancePolicyNo(String insurancePolicyNo) {
        this.insurancePolicyNo = insurancePolicyNo;
    }

    public String getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(String insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public String getInsuranceCompanyName() {
        return insuranceCompanyName;
    }

    public void setInsuranceCompanyName(String insuranceCompanyName) {
        this.insuranceCompanyName = insuranceCompanyName;
    }


    public boolean checkModelValue(String description, String expiryDate, String fileName, String filePath, String insuranceCompany, String insurancePolicyNo, String insuranceAmount, String insuranceCompanyName) {


        if((description !=null && !TextUtils.isEmpty(description))
        ||(expiryDate !=null && !TextUtils.isEmpty(expiryDate))
            ||(fileName !=null && !TextUtils.isEmpty(fileName))
            ||(filePath !=null && !TextUtils.isEmpty(filePath))
            ||(insuranceCompany !=null && !TextUtils.isEmpty(insuranceCompany))
            ||(insurancePolicyNo !=null && !TextUtils.isEmpty(insurancePolicyNo))
            ||(insuranceAmount !=null && !TextUtils.isEmpty(insuranceAmount))
            ||(insuranceCompanyName !=null && !TextUtils.isEmpty(insuranceCompanyName))){

            return true;
        }

        return false;
    }




}

