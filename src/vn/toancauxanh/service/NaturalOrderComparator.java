package vn.toancauxanh.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NaturalOrderComparator implements Comparator{
	int compareRight(String a, String b)
    {
        int bias = 0;
        int ia = 0;
        int ib = 0;

        // The longest run of digits wins. That aside, the greatest
        // value wins, but we can't know that it will until we've scanned
        // both numbers to know that they have the same magnitude, so we
        // remember it in BIAS.
        for (;; ia++, ib++)
        {
            char ca = charAt(a, ia);
            char cb = charAt(b, ib);

            if (!Character.isDigit(ca) && !Character.isDigit(cb))
            {
                return bias;
            }
            else if (!Character.isDigit(ca))
            {
                return -1;
            }
            else if (!Character.isDigit(cb))
            {
                return +1;
            }
            else if (ca < cb)
            {
                if (bias == 0)
                {
                    bias = -1;
                }
            }
            else if (ca > cb)
            {
                if (bias == 0)
                    bias = +1;
            }
            else if (ca == 0 && cb == 0)
            {
                return bias;
            }
        }
    }

    @Override
	public int compare(Object o1, Object o2)
    {
        String a = "";
        String b = "";

        int ia = 0, ib = 0;
        int nza = 0, nzb = 0;
        char ca, cb;
        int result;

        while (true)
        {
            // only count the number of zeroes leading the last number compared
            nza = nzb = 0;

            ca = charAt(a, ia);
            cb = charAt(b, ib);

            // skip over leading spaces or zeros
            while (Character.isSpaceChar(ca) || ca == '0')
            {
                if (ca == '0')
                {
                    nza++;
                }
                else
                {
                    // only count consecutive zeroes
                    nza = 0;
                }

                ca = charAt(a, ++ia);
            }

            while (Character.isSpaceChar(cb) || cb == '0')
            {
                if (cb == '0')
                {
                    nzb++;
                }
                else
                {
                    // only count consecutive zeroes
                    nzb = 0;
                }

                cb = charAt(b, ++ib);
            }

            // process run of digits
            if (Character.isDigit(ca) && Character.isDigit(cb))
            {
                if ((result = compareRight(a.substring(ia), b.substring(ib))) != 0)
                {
                    return result;
                }
            }

            if (ca == 0 && cb == 0)
            {
                // The strings compare the same. Perhaps the caller
                // will want to call strcmp to break the tie.
                return nza - nzb;
            }

            if (ca < cb)
            {
                return -1;
            }
            else if (ca > cb)
            {
                return +1;
            }

            ++ia;
            ++ib;
        }
    }

    static char charAt(String s, int i)
    {
        if (i >= s.length())
        {
            return 0;
        }
        else
        {
            return s.charAt(i);
        }
    }    

    public static void main(String[] args)
    {
        String[] strings = new String[] { "Tổ 1", "Tổ 2", "Tổ 3", "Tổ 10", "Tổ 11", "Tổ 19", "Tổ 20", "Tổ 21", "Tổ 22", "Tổ 28", "Tổ 29", "Tổ 30", "Tổ 100" };
        List scrambled = Arrays.asList(strings);
        Collections.sort(scrambled, new NaturalOrderComparator());

        System.out.println("Sorted: " + scrambled);
    }

}
