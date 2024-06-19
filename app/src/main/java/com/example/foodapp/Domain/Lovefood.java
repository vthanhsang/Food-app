
package com.example.foodapp.Domain;

public class Lovefood {
    private String idfood;
    private String imgfood;
    private String pricefood;
    private String Titlefood;
    private String iduser;

    public Lovefood() {
    }

    public Lovefood(String idfood, String imgfood, String pricefood, String titlefood, String iduser) {
        this.idfood = idfood;
        this.imgfood = imgfood;
        this.pricefood = pricefood;
        Titlefood = titlefood;
        this.iduser = iduser;
    }

    public String getIdfood() {
        return idfood;
    }

    public void setIdfood(String idfood) {
        this.idfood = idfood;
    }

    public String getImgfood() {
        return imgfood;
    }

    public void setImgfood(String imgfood) {
        this.imgfood = imgfood;
    }

    public String getPricefood() {
        return pricefood;
    }

    public void setPricefood(String pricefood) {
        this.pricefood = pricefood;
    }

    public String getTitlefood() {
        return Titlefood;
    }

    public void setTitlefood(String titlefood) {
        Titlefood = titlefood;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }
}
