package io.paysky.ui.custom;

/**
 * Created by Paysky-202 on 5/20/2018.
 */


/**
 * Created by MANGESH KADAM on 4/23/2015.
 */
public enum Year {
    _2015("2015"),
    _2016("2016"),
    _2017("2017"),
    _2018("2018"),
    _2019("2019"),
    _2020("2020"),
    _2021("2021"),
    _2022("2022"),
    _2023("2023"),
    _2024("2024"),
    _2025("2025"),
    _2026("2026"),
    _2027("2027"),
    _2028("2028"),
    _2029("2029"),
    _2030("2030"),
    _2031("2031"),
    _2032("2032"),
    _2033("2033"),
    _2034("2034"),
    _2035("2035"),
    _2036("2036"),
    _2037("2037"),
    _2038("2038"),
    _2039("2039"),
    _2040("2040"),
    _2041("2041"),
    _2042("2042"),
    _2043("2043"),
    _2044("2044"),
    _2045("2045"),
    _2046("2046"),
    _2047("2047"),
    _2048("2048"),
    _2049("2049"),
    _2050("2050");

    private final String year;

    Year(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return year;
    }

    /**
     * Get an object of Year
     *
     * @param year - year in yyyy or yy format
     * @return
     */
    public static Year getYear(String year) {
        Year year1 = null;

        if ("15".equals(year) || "2015".equals(year)) {
            year1 = _2015;
        } else if ("16".equals(year) || "2016".equals(year)) {
            year1 = _2016;
        } else if ("17".equals(year) || "2017".equals(year)) {
            year1 = _2017;
        } else if ("18".equals(year) || "2018".equals(year)) {
            year1 = _2018;
        } else if ("19".equals(year) || "2019".equals(year)) {
            year1 = _2019;
        } else if ("20".equals(year) || "2020".equals(year)) {
            year1 = _2020;
        } else if ("21".equals(year) || "2021".equals(year)) {
            year1 = _2021;
        } else if ("22".equals(year) || "2022".equals(year)) {
            year1 = _2022;
        } else if ("23".equals(year) || "2023".equals(year)) {
            year1 = _2023;
        } else if ("24".equals(year) || "2024".equals(year)) {
            year1 = _2024;
        } else if ("25".equals(year) || "2025".equals(year)) {
            year1 = _2025;
        } else if ("26".equals(year) || "2026".equals(year)) {
            year1 = _2026;
        } else if ("27".equals(year) || "2027".equals(year)) {
            year1 = _2027;
        } else if ("28".equals(year) || "2028".equals(year)) {
            year1 = _2028;
        } else if ("29".equals(year) || "2029".equals(year)) {
            year1 = _2029;
        } else if ("30".equals(year) || "2030".equals(year)) {
            year1 = _2030;
        } else if ("31".equals(year) || "2031".equals(year)) {
            year1 = _2031;
        } else if ("32".equals(year) || "2032".equals(year)) {
            year1 = _2032;
        } else if ("33".equals(year) || "2033".equals(year)) {
            year1 = _2033;
        } else if ("34".equals(year) || "2034".equals(year)) {
            year1 = _2034;
        } else if ("35".equals(year) || "2035".equals(year)) {
            year1 = _2035;
        } else if ("36".equals(year) || "2036".equals(year)) {
            year1 = _2036;
        } else if ("37".equals(year) || "2037".equals(year)) {
            year1 = _2037;
        } else if ("38".equals(year) || "2038".equals(year)) {
            year1 = _2038;
        } else if ("39".equals(year) || "2039".equals(year)) {
            year1 = _2039;
        } else if ("40".equals(year) || "2040".equals(year)) {
            year1 = _2040;
        } else if ("41".equals(year) || "2041".equals(year)) {
            year1 = _2041;
        } else if ("42".equals(year) || "2042".equals(year)) {
            year1 = _2042;
        } else if ("43".equals(year) || "2043".equals(year)) {
            year1 = _2043;
        } else if ("44".equals(year) || "2044".equals(year)) {
            year1 = _2044;
        } else if ("45".equals(year) || "2045".equals(year)) {
            year1 = _2045;
        } else if ("46".equals(year) || "2046".equals(year)) {
            year1 = _2046;
        } else if ("47".equals(year) || "2047".equals(year)) {
            year1 = _2047;
        } else if ("48".equals(year) || "2048".equals(year)) {
            year1 = _2048;
        } else if ("49".equals(year) || "2049".equals(year)) {
            year1 = _2049;
        } else if ("50".equals(year) || "2050".equals(year)) {
            year1 = _2050;
        }

        return year1;
    }
}