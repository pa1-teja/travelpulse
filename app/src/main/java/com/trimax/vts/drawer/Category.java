package com.trimax.vts.drawer;

public class Category 
{
    private String cat_name;
    private int cat_code;
    private String img_uri;
    private int imageid;

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }





public String getCat_name()
{
	return cat_name;
}
public void setCat_name(String cat_name)
{
	this.cat_name = cat_name;
}
public int getCat_code()
{
	return cat_code;
}
public void setCat_code(int cat_code) 
{
	this.cat_code = cat_code;
}
public String getImg_uri() {
	return img_uri;
}
public void setImg_uri(String img_uri) {
	this.img_uri = img_uri;
}


    @Override
    public boolean equals(Object obj) {

    if(obj != null && obj instanceof Category){

        return cat_name.equalsIgnoreCase(((Category) obj).getCat_name());

    }
    return false;
    }
}
