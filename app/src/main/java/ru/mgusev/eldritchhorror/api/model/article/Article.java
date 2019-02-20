package ru.mgusev.eldritchhorror.api.model.article;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article {

    @SerializedName("err_msg")
    @Expose
    private String errMsg;
    @SerializedName("err_code")
    @Expose
    private String errCode;
    @SerializedName("response_id")
    @Expose
    private Integer responseId;
    @SerializedName("api")
    @Expose
    private String api;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public Integer getResponseId() {
        return responseId;
    }

    public void setResponseId(Integer responseId) {
        this.responseId = responseId;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}