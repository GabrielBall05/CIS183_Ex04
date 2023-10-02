package com.example.databaseexample;

public class User
{
    private String fname;
    private String lname;
    private String uname;

    public User()
    {

    }

    public User(String u, String l, String f)
    {
        fname = f;
        lname = l;
        uname = u;
    }

    //Get/set fname
    public String getFname()
    {
        return fname;
    }
    public void setFname(String f)
    {
        fname = f;
    }

    //Get/set lname
    public String getLname()
    {
        return lname;
    }
    public void setLname(String l)
    {
        lname = l;
    }

    //Get/set uname
    public String getUname()
    {
        return uname;
    }
    public void setUname(String u)
    {
        uname = u;
    }
}
