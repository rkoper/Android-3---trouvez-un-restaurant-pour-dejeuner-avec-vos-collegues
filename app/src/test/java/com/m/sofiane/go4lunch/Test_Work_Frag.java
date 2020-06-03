package com.m.sofiane.go4lunch;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class Test_Work_Frag {
    @Test
    public void Test_Work_Frag() {
        String mListResultOne;
        String mDisplayRight;
        String mToDisplay = null;
        String mUserName;
        String mRestoName;

        mRestoName = "Café 80";
        mDisplayRight = "eat @ ";
        mListResultOne = "1";
        mUserName = "Marty Mc Fly ";


        if (mListResultOne.equals("1")) {
            mToDisplay = mUserName + mDisplayRight + mRestoName;
        }
        assertEquals("Marty Mc Fly eat @ Café 80", mToDisplay);

    }
}

