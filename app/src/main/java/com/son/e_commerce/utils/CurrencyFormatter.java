package com.son.e_commerce.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Utility class to format currency prices for Vietnamese Dong (VND)
 * Format: 1.234.567 VND (with dot separators every 3 digits)
 */
public class CurrencyFormatter {

    /**
     * Format price to VND with dot separators (e.g., 1.234.567 VND)
     */
    public static String formatVND(double price) {
        // Use Vietnamese locale for number formatting
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');  // Use dot as thousands separator
        symbols.setDecimalSeparator(',');   // Use comma as decimal separator

        DecimalFormat decimalFormat = new DecimalFormat("#,##0", symbols);
        decimalFormat.setGroupingUsed(true);

        return decimalFormat.format((long) price) + " VND";
    }

    /**
     * Format price to VND without currency symbol (e.g., 1.234.567)
     */
    public static String formatVNDNumber(double price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        DecimalFormat decimalFormat = new DecimalFormat("#,##0", symbols);
        decimalFormat.setGroupingUsed(true);

        return decimalFormat.format((long) price);
    }

    /**
     * Format price for display (e.g., "1.234.567 đ" using Vietnamese dong symbol)
     */
    public static String formatVNDWithSymbol(double price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        DecimalFormat decimalFormat = new DecimalFormat("#,##0", symbols);
        decimalFormat.setGroupingUsed(true);

        return decimalFormat.format((long) price) + " đ";
    }

    /**
     * Format total price for cart/order display
     */
    public static String formatTotalVND(double total) {
        return formatVND(total);
    }
}
