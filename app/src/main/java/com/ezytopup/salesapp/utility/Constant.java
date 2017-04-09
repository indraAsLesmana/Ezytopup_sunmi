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

    public static final String API_URL_PARAM1 = "H5c30S5aHa9c45J297a680beB0795f87W131556M9b82e471ee1c2baS82699e9eWa218G2c05fG3e259G971562052D03a78c81D6afdb5e92Gb=";
    public static final String API_URL_PARAM1_VALUE = "d61H266a2d1c5ca33475257S13fb490efba4f17ee0013596Dc03da1372e31c6a94f0eb608fbc834af00e4G6141715b4981c41f0L6d2959b43a71b4576fSa66285cf486423fa70fa5b66f1d28b95O24f3d7b8465b0Gf19122d75Ubbe828aF3Tdd8845cc1a7W9bS";
    public static final String API_URL_PARAM2 = "Kb0a176e5O7efbaf58OaKaFdfd0C79e353e1dbFCcf2328Ic6477922S5b4a47b79120D0U5WddV0d2b225W0fdf5Q809Vc=";
    public static final String API_URL_PARAM2_VALUE = "Ezy_Apps_WGS";
    public static final String API_URL_GENERALUSAGE = "H5c30S5aHa9c45J297a680beB0795f87W131556M9b82e471ee1c2baS82699e9eWa218G2c05fG3e259G971562052D03a78c81D6afdb5e92Gb=d61H266a2d1c5ca33475257S13fb490efba4f17ee0013596Dc03da1372e31c6a94f0eb608fbc834af00e4G6141715b4981c41f0L6d2959b43a71b4576fSa66285cf486423fa70fa5b66f1d28b95O24f3d7b8465b0Gf19122d75Ubbe828aF3Tdd8845cc1a7W9bS&Kb0a176e5O7efbaf58OaKaFdfd0C79e353e1dbFCcf2328Ic6477922S5b4a47b79120D0U5WddV0d2b225W0fdf5Q809Vc=Ezy_Apps_WGS";

    /**
     * slider option
     * */
    public static final long HEADER_DURATION = 5000;
    public static final long TUTORIAL_DURATION = 8000;

}
