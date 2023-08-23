package ru.parma.filesdistr.utils;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    static final int ONE_MB_SIZE_IN_BYTES = 1048576;
    static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    public static Date getDateWithoutTime() throws ParseException {
        return formatter.parse(formatter.format(new Date()));
    }

    public static Double convertByteToMb(byte @NotNull [] bytes){
        return ((double)bytes.length/(double) ONE_MB_SIZE_IN_BYTES);
    }

}
