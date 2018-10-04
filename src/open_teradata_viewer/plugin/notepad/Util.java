/*
 * Open Teradata Viewer ( notepad plugin )
 * Copyright (C) 2012, D. Campione
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package open_teradata_viewer.plugin.notepad;

import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class Util {

    private static String hexChars = "0123456789ABCDEF";

    public static String pad(String text, int chars) {
        String aux = text;

        for (int i = 0; i < chars - text.length(); i++) {
            aux = aux + " ";
        }
        return aux;
    }

    public static String replicate(String text, int numTimes) {
        String aux = "";

        for (int i = 0; i < numTimes; i++) {
            aux = aux + text;
        }
        return aux;
    }

    public static String replaceStr(String s, String pattern, String newPattern) {
        String r = "";
        int i;
        while ((i = s.indexOf(pattern)) != -1) {
            r = r + s.substring(0, i) + newPattern;
            s = s.substring(i + pattern.length());
        }
        return r + s;
    }

    public static String firstCharUpperCase(String str) {
        if (str == null)
            return null;
        if (str.equals(""))
            return "";

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String firstCharLowerCase(String str) {
        if (str == null)
            return null;
        if (str.equals(""))
            return "";

        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static String convertToHex(long n, int digits) {
        String res = "";

        for (int i = 0; i < digits; i++) {
            res = hexChars.charAt((int) (n & 0xF)) + res;
            n >>= 4;
        }

        return res;
    }

    public static long convertFromHex(String s) {
        long n = 0L;

        for (int i = 0; i < s.length(); i++) {
            n <<= 4;

            int c = s.charAt(i);

            if ((c >= 48) && (c <= 57))
                c -= 48;
            if ((c >= 97) && (c <= 102))
                c -= 97;
            if ((c >= 65) && (c <= 70))
                c = c - 65 + 10;

            n += c;
        }

        return n;
    }

    public static int getIntValue(String text, int defValue) {
        if (text == null)
            return defValue;

        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
        }
        return defValue;
    }

    public static int getIntValueMinMax(String text, int defValue,
            int minValue, int maxValue) {
        int v = getIntValue(text, defValue);

        if ((v < minValue) || (v > maxValue))
            return defValue;

        return v;
    }

    public static float getFloatValue(String text, float defValue) {
        if (text == null)
            return defValue;

        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
        }
        return defValue;
    }

    public static boolean getBooleanValue(String text, boolean defValue) {
        if (text == null)
            return defValue;

        String s = text.toLowerCase();

        if (s.equals("true"))
            return true;
        if (s.equals("false"))
            return false;

        return defValue;
    }

    public static String getStringValue(String text, String defValue) {
        return text == null ? defValue : text;
    }

    public static String getCurrentDate() {
        return getCurrentDay() + " " + getCurrentTime();
    }

    public static String getCurrentDay() {
        Calendar c = Calendar.getInstance();

        String year = "" + c.get(1);
        String month = "" + (c.get(2) + 1);
        String day = "" + c.get(5);

        if (month.length() == 1)
            month = "0" + month;
        if (day.length() == 1)
            day = "0" + day;

        return year + "-" + month + "-" + day;
    }

    public static String getCurrentTime() {
        Calendar c = Calendar.getInstance();

        String hour = "" + c.get(11);
        String min = "" + c.get(12);
        String sec = "" + c.get(13);

        if (hour.length() == 1)
            hour = "0" + hour;
        if (min.length() == 1)
            min = "0" + min;
        if (sec.length() == 1)
            sec = "0" + sec;

        return hour + ":" + min + ":" + sec;
    }

    /**
     * Take the given string and chop it up into a series of strings on
     * whitespace boundaries.
     */
    public static String[] tokenize(String input) {
        Vector<String> v = new Vector<String>();
        StringTokenizer t = new StringTokenizer(input);
        String cmd[];

        while (t.hasMoreTokens())
            v.addElement(t.nextToken());
        cmd = new String[v.size()];
        for (int i = 0; i < cmd.length; i++)
            cmd[i] = (String) v.elementAt(i);

        return cmd;
    }

    public static String replaceAll(String text, String regex,
            String replacement) {
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher;
        matcher = pattern.matcher(text);

        /* The "found" variable is true if a "regex" occurence has
         * been found.
         */
        if (matcher.find()) {
            text = matcher.replaceAll(replacement);
        }

        return text;
    }

    public static String extractFileName(String path) {
        return path.substring(path.lastIndexOf("\\") + 1);
    }
}