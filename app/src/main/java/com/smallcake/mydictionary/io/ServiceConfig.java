package com.smallcake.mydictionary.io;

import android.content.Context;

public class ServiceConfig extends BaseConfig {
    public ServiceConfig(Context context)
    {
        super(context, "service.properties");
    }

    public boolean isClose(String serviceName) {
        String att = super.getProperty(serviceName);
        if (att == null)
        {
            return false;
        }
        return att.equals("close");
    }

    public void setOpenStatus(String serviceName, boolean status) {
        super.setProperty(serviceName, status?"open":"close");
        super.save();
    }

}
