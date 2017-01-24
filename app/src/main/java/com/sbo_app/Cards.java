package com.sbo_app;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Cards {
    @SerializedName("getWithClient")
    @Expose
    private List<GetWithClient> getWithClient = null;

    public List<GetWithClient> getGetWithClient() {
        return getWithClient;
    }

    public void setGetWithClient(List<GetWithClient> getWithClient) {
        this.getWithClient = getWithClient;
    }
}
