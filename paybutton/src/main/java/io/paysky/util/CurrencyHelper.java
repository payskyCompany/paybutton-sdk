package io.paysky.util;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

import io.paysky.data.model.CurrencyCode;

public class CurrencyHelper {


    public static CurrencyCode getCurrencyCode(Context context, String currencyCode) {
        try {
            XmlPullParser xmlPullParser = parseXML(context);
            return processParsing(xmlPullParser, currencyCode);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static XmlPullParser parseXML(Context context) throws XmlPullParserException, IOException {
        XmlPullParserFactory parserFactory;
        parserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = parserFactory.newPullParser();
        InputStream is = context.getAssets().open("currency_iso.xml");
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(is, null);
        return parser;
    }

    private static CurrencyCode processParsing(XmlPullParser parser, String currencyCode) throws
            IOException, XmlPullParserException {

        int eventType = parser.getEventType();
        CurrencyCode currencyItem = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String eltName = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (eltName != null) {
                        if ("CcyNtry".equals(eltName)) {
                            currencyItem = new CurrencyCode();
                        } else if (currencyItem != null) {
                            if ("CtryNm".equals(eltName)) {
                                currencyItem.cityName = parser.nextText();
                            } else if ("CcyNm".equals(eltName)) {
                                currencyItem.currencyName = parser.nextText();
                            } else if ("Ccy".equals(eltName)) {
                                currencyItem.currencyShortName = parser.nextText();
                            } else if ("CcyNbr".equals(eltName)) {
                                currencyItem.currencyNumber = parser.nextText();
                            } else if ("CcyMnrUnts".equals(eltName)) {
                                currencyItem.currencyUnit = parser.nextText();
                            }
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (eltName != null && "CcyNtry".equals(eltName)) {
                        boolean objectNotNull = currencyItem != null;
                        boolean numberNotNull = objectNotNull && currencyItem.currencyNumber != null;
                        if (numberNotNull && currencyItem.currencyNumber.equals(currencyCode)) {
                            return currencyItem;
                        }
                    }
            }
            eventType = parser.next();
        }
        return null;
    }
}
