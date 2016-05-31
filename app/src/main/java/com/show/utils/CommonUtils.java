package com.show.utils;

import com.show.MyApplication;
import com.show.R;
import com.show.MyApplication;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by abhijeet on 05/01/16.
 */
public class CommonUtils {
    static NumberFormat currencyFormat;
    static DecimalFormatSymbols decimalFormatSymbols,currencydecimalFormatSymbols;

    public CommonUtils(){


    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = MyApplication.mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = MyApplication.mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static NumberFormat getCurrencyFormatEmpty(){
        currencyFormat = NumberFormat.getCurrencyInstance(
                new Locale(MyApplication.mContext.getResources().getString(R.string.language),
                        MyApplication.mContext.getResources().getString(R.string.country)));
        currencyFormat.setMaximumFractionDigits(0);
        decimalFormatSymbols = ((DecimalFormat)currencyFormat).getDecimalFormatSymbols();
        currencydecimalFormatSymbols = ((DecimalFormat)currencyFormat).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat)currencyFormat).setDecimalFormatSymbols(decimalFormatSymbols);

        return currencyFormat;
    }

    public static NumberFormat getCurrencyFormat(){
        currencyFormat = NumberFormat.getCurrencyInstance(
                new Locale(MyApplication.mContext.getResources().getString(R.string.language),
                        MyApplication.mContext.getResources().getString(R.string.country)));
        currencyFormat.setMaximumFractionDigits(0);
        decimalFormatSymbols = ((DecimalFormat)currencyFormat).getDecimalFormatSymbols();
        currencydecimalFormatSymbols = ((DecimalFormat)currencyFormat).getDecimalFormatSymbols();
        //decimalFormatSymbols.setCurrencySymbol("");
        //((DecimalFormat)currencyFormat).setDecimalFormatSymbols(decimalFormatSymbols);

        return  currencyFormat;
    }

    public static SimpleDateFormat getDateFormat(){
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd");
        return formatter;
    }
    public static SimpleDateFormat getDateFormatWY(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM-yy");
        return formatter;
    }
    public static int getData(){
        return 10;
    }
}
