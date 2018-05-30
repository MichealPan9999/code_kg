package com.ktc.launcher.boardproperty;

import android.os.SystemProperties;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by xiacf on 2017/11/3.
 */

public class BoardProperty {

    public static boolean isT5c1() {
        try {
            Properties props = new Properties();
            InputStream in = new BufferedInputStream(new FileInputStream("/system/build.prop"));
            props.load(in);
            String boardType = props.getProperty("ktc.board.type");
            return ((boardType != null) && boardType.equals("T5C1"));

        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isT8c1() {
        try {
            Properties props = new Properties();
            InputStream in = new BufferedInputStream(new FileInputStream("/system/build.prop"));
            props.load(in);
            String boardType = props.getProperty("ktc.board.type");
            return ((boardType != null)  && boardType.equals("T8C1"));

        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isHasDvbsHasCi() {
        try {
            /*
            Properties props = new Properties();
            InputStream in = new BufferedInputStream(new FileInputStream("/system/build.prop"));
            props.load(in);
            String hasDvbs = props.getProperty("ktc.has.dvbs");
            String hasYpbpr = props.getProperty("ktc.has.ypbpr");*/
            String hasDvbs = SystemProperties.get("ktc.has.dvbs", "1");
            String hasYpbpr = SystemProperties.get("ktc.has.ypbpr", "1");

            return ( (hasDvbs != null) && (hasYpbpr != null)  && (hasDvbs.equals("1")) && (hasYpbpr.equals("0")));

        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isHasYpbpr() {
        try {
            /*
            Properties props = new Properties();
            InputStream in = new BufferedInputStream(new FileInputStream("/system/build.prop"));
            props.load(in);
            String hasDvbs = props.getProperty("ktc.has.dvbs");
            String hasYpbpr = props.getProperty("ktc.has.ypbpr");*/
            String hasYpbpr = SystemProperties.get("ktc.YPBPR.type", "true");
            Log.d("xsp","hasYpbpr==="+hasYpbpr);
            /*String hasDvbs = SystemProperties.get("ktc.has.dvbs", "1");
            String hasYpbpr = SystemProperties.get("ktc.has.ypbpr", "1");*/
            //Log.d("Maxs700","hasDvbs = " + hasDvbs +  " /hasYpbpr = " + hasYpbpr);
            return ((hasYpbpr != null) && (hasYpbpr.equals("true")));

        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isNonDvbsHasCi() {
        try {
            /*
            Properties props = new Properties();
            InputStream in = new BufferedInputStream(new FileInputStream("/system/build.prop"));
            props.load(in);
            String hasDvbs = props.getProperty("ktc.has.dvbs");
            String hasYpbpr = props.getProperty("ktc.has.ypbpr");*/
            String hasDvbs = SystemProperties.get("ktc.has.dvbs", "1");
            String hasYpbpr = SystemProperties.get("ktc.has.ypbpr", "1");
            return ((hasDvbs != null) && (hasYpbpr != null) && (hasDvbs.equals("0")) && (hasYpbpr.equals("0")));

        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isNonDvbsNonCi() {
        try {
           /*
            Properties props = new Properties();
            InputStream in = new BufferedInputStream(new FileInputStream("/system/build.prop"));
            props.load(in);
            String hasDvbs = props.getProperty("ktc.has.dvbs");
            String hasYpbpr = props.getProperty("ktc.has.ypbpr");*/
            String hasDvbs = SystemProperties.get("ktc.has.dvbs", "1");
            String hasYpbpr = SystemProperties.get("ktc.has.ypbpr", "1");
            return ((hasDvbs != null) && (hasYpbpr != null) && (hasDvbs.equals("0")) && (hasYpbpr.equals("1")));

        } catch (Exception e) {
        }
        return false;
    }
}
