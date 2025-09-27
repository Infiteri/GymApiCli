package org.example.utils;

public class Utils
{
        public static boolean ValidatePhoneNumber(String phoneNumber)
        {
                if (!phoneNumber.startsWith("+")) return false;

                if (!phoneNumber.matches("^\\+[0-9]+$")) return false;

                return true;
        }

        public static String FormatPhoneNumberForQuery(String phoneNumber) {
                return phoneNumber.replaceFirst("\\+", "%2B");
        }
}
