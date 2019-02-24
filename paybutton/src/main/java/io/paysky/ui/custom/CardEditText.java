package io.paysky.ui.custom;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.ImageView;


import com.paysky.paybutton.R;

import java.util.ArrayList;
import java.util.LinkedList;

import io.paysky.util.CardsValidation;


public class CardEditText extends AppCompatEditText implements TextWatcher {

    private static final char SPACE_CHAR = ' ';
    private static final String SPACE_STRING = String.valueOf(SPACE_CHAR);
    private static final int GROUPSIZE = 4;
    private boolean isUpdating = false;
    private ImageView cardTypeImage;

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public CardEditText(Context context) {
        super(context);
        addTextWatcher();
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public CardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTextWatcher();
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public CardEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addTextWatcher();
    }


    public void setCardTypeImage(ImageView cardTypeImage) {
        this.cardTypeImage = cardTypeImage;
        changeIcon();
    }


    private void addTextWatcher() {
        addTextChangedListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void changeIcon() {
        String s = getText().toString().replace(" ", "").trim();
        int cardIcon;
        if (s.startsWith("4") || s.matches(CardPattern.VISA)) {
            cardIcon = R.drawable.vi;
        } else if (s.startsWith("5") || s.matches(CardPattern.MASTERCARD_SHORTER) || s.matches(CardPattern.MASTERCARD_SHORT)
                || s.matches(CardPattern.MASTERCARD)) {
            cardIcon = R.drawable.mc;
        } else if (s.matches(CardPattern.AMERICAN_EXPRESS)) {
            cardIcon = R.drawable.am;
        } else if (s.matches(CardPattern.DISCOVER_SHORT) || s.matches(CardPattern.DISCOVER)) {
            cardIcon = R.drawable.ds;
        } else if (s.matches(CardPattern.JCB_SHORT) || s.matches(CardPattern.JCB)) {
            cardIcon = R.drawable.jcb;
        } else if (s.matches(CardPattern.DINERS_CLUB_SHORT) || s.matches(CardPattern.DINERS_CLUB)) {
            cardIcon = R.drawable.dc;
        } else if (s.matches(CardPattern.MEZA_VALID)
                || s.matches(CardPattern.MASTER_MEZA_VALID)
                || s.matches(CardPattern.SHORT_MEZA_VALID)
                || s.matches(CardPattern.SHORT_MASTER_MEZA_VALID)
                || s.startsWith("9818")
                || s.startsWith("50")) {
            cardIcon = R.drawable.meeza_logo;
        } else {
            cardIcon = R.drawable.card_icon;
        }

        if (cardTypeImage != null) {
            cardTypeImage.setImageResource(cardIcon);
        }

        if (s.length() > 15 && !CardsValidation.isCardValid(s)) {
            // check card validation.
            this.setError(this.getContext().getString(R.string.invalid_card_number_length));
        }

    }

    public String getCardNumber() {
        return getText().toString().replace(" ", "").trim();
    }

    public boolean isValid() {
        final ArrayList<String> listOfPattern = new ArrayList<>();
        String ptVisa = "^4[0-9]{6,}$";
        listOfPattern.add(ptVisa);
        String ptMasterCard = "^5[1-5][0-9]{5,}$";
        listOfPattern.add(ptMasterCard);
        String ptAmeExp = "^3[47][0-9]{5,}$";
        listOfPattern.add(ptAmeExp);
        String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
        listOfPattern.add(ptDinClb);
        String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
        listOfPattern.add(ptDiscover);
        String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
        listOfPattern.add(ptJcb);
        String cardNumber = getCardNumber();
        for (String pattern : listOfPattern) {
            if (cardNumber.matches(pattern)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        changeIcon();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String originalString = s.toString();

        // Check if we are already updating, to avoid infinite loop.
        // Also check if the string is already in a valid format.
        String regexp = "^(\\d{4}\\s)*\\d{0,4}(?<!\\s)$";
        if (isUpdating || originalString.matches(regexp)) {
            return;
        }

        // Set flag to indicate that we are updating the Editable.
        isUpdating = true;

        // First all whitespaces must be removed. Find the index of all whitespace.
        LinkedList<Integer> spaceIndices = new LinkedList<>();
        for (int index = originalString.indexOf(SPACE_CHAR); index >= 0; index = originalString.indexOf(SPACE_CHAR, index + 1)) {
            spaceIndices.offerLast(index);
        }

        // Delete the whitespace, starting from the end of the string and working towards the beginning.
        Integer spaceIndex;
        while (!spaceIndices.isEmpty()) {
            spaceIndex = spaceIndices.removeLast();
            s.delete(spaceIndex, spaceIndex + 1);
        }

        // Loop through the string again and add whitespaces in the correct positions
        for (int i = 0; ((i + 1) * GROUPSIZE + i) < s.length(); i++) {
            s.insert((i + 1) * GROUPSIZE + i, SPACE_STRING);
        }

        int cursorPos = getSelectionStart();
        if (cursorPos > 0 && s.charAt(cursorPos - 1) == SPACE_CHAR) {
            setSelection(cursorPos - 1);
        }

        isUpdating = false;
    }
}
