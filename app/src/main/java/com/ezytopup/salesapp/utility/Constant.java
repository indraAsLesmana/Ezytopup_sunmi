package com.ezytopup.salesapp.utility;

/**
 * Created by indraaguslesmana on 3/31/17.
 */

public class Constant {
    /********
     * Printer configuration
     * "width" count by each character
     * "algn" is font align
     ********/

    private static final int algn_left = 0;
    private static final int algn_center = 1;
    private static final int algn_right = 2;

    private static final int name_width = 12;
    private static final int quanity_width = 3;
    private static int price_width = 6;
    private static final int total_width = 9;

    /* header table*/
    public static final String ROW_NAME = "Name";
    public static final String ROW_QUANTITY = "Qty";
    public static final String ROW_PRICE = "Price";
    public static final String ROW_TOTAL_PRICE = "Total";

    /* default width for table*/
    public static final int[] width = new int[]{name_width, quanity_width, price_width, total_width};
    /*default align for header table*/
    public static final int[] align_header = new int[]{ algn_left, algn_center, algn_right, algn_right };
    /* default align for content table*/
    public static final int[] align = new int[]{algn_left, algn_right, algn_right, algn_right};

    /*****
     * end printer configuration
     *
     * ***/

    public static final String API_APIARY_ENDPOINT = "http://private-fc734-ezytopup.apiary-mock.com/";

    public static final String LOGIN_PROVIDER_EMAIL = "email";

    public static final  String API_ENDPOINT = "https://www.gsshop.co.id/";


}
