package com.example.mutbooks.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

public class Ut {
    public static class date {

        // 해당 년, 월의 마지막 일자 구하기
        public static int getEndDay(int year, int month) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month - 1, 1);

            return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        // 해당 일자의 시작일시 구하기
        public static LocalDateTime getStartOfDay(int year, int month, int day) {
            LocalDate date = LocalDate.of(year, month, day);
            return date.atStartOfDay();
        }

        // 해당 일자의 종료일시 구하기
        public static LocalDateTime getEndOfDay(int year, int month, int day) {
            LocalDate date = LocalDate.of(year, month, day);
            return date.atTime(LocalTime.MAX);
        }
    }
}
