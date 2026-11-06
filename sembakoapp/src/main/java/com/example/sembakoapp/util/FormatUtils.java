package com.example.sembakoapp.util;

import java.text.NumberFormat;
import java.util.Locale;

public final class FormatUtils {

  private static final Locale ID = Locale.forLanguageTag("id-ID");

  private FormatUtils() {}

  public static String rupiah(long value) {
    NumberFormat format = NumberFormat.getNumberInstance(ID);
    return "Rp " + format.format(value);
  }
}
