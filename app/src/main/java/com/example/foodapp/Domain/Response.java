package com.example.foodapp.Domain;

public class Response {
    private String iduser;
    private String infor;
    private String datetime;
    private String idrespon; // Thay thế trường id bằng idrespon

    public Response() {
        // Empty constructor required for Firebase
    }

    public Response(String iduser, String infor, String datetime, String idrespon) {
        this.iduser = iduser;
        this.infor = infor;
        this.datetime = datetime;
        this.idrespon = idrespon;
    }

    public String getIdrespon() {
        return idrespon;
    }

    public void setIdrespon(String idrespon) {
        this.idrespon = idrespon;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getInfor() {
        return infor;
    }

    public void setInfor(String infor) {
        this.infor = infor;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
