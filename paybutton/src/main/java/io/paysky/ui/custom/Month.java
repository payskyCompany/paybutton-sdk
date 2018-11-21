package io.paysky.ui.custom;

/**
 * Created by Paysky-202 on 5/20/2018.
 */


/**
 * Created by MANGESH KADAM on 4/23/2015.
 */
public enum Month {
    JAN("01"),
    FEB("02"),
    MAR("03"),
    APR("04"),
    MAY("05"),
    JUN("06"),
    JUL("07"),
    AUG("08"),
    SEP("09"),
    OCT("10"),
    NOV("11"),
    DEC("12");

    private final String month;

    Month(String month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return month;
    }

    /**
     * Get month.
     *
     * @param month in MM format.
     * @return
     */
    public static Month getMonth(String month) {
        Month month1 = null;

        if ("01".equals(month) || "1".equals(month)) {
            month1 = JAN;
        } else if ("02".equals(month) || "2".equals(month)) {
            month1 = FEB;
        } else if ("03".equals(month) || "3".equals(month)) {
            month1 = MAR;
        } else if ("04".equals(month) || "4".equals(month)) {
            month1 = APR;
        } else if ("05".equals(month) || "5".equals(month)) {
            month1 = MAY;
        } else if ("06".equals(month) || "6".equals(month)) {
            month1 = JUN;
        } else if ("07".equals(month) || "7".equals(month)) {
            month1 = JUL;
        } else if ("08".equals(month) || "8".equals(month)) {
            month1 = AUG;
        } else if ("09".equals(month) || "9".equals(month)) {
            month1 = SEP;
        } else if ("10".equals(month)) {
            month1 = OCT;
        } else if ("11".equals(month)) {
            month1 = NOV;
        } else if ("12".equals(month)) {
            month1 = DEC;
        }

        return month1;
    }

}
