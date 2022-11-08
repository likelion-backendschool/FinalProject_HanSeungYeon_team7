package com.example.mutbooks.util;

import com.example.mutbooks.app.base.dto.RsData;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Ut {
    public static class spring {
        public static <T> ResponseEntity<RsData> responseEntityOf(RsData<T> rsData) {
            return responseEntityOf(rsData, null);
        }

        public static <T> ResponseEntity<RsData> responseEntityOf(RsData<T> rsData, HttpHeaders headers) {
            HttpStatus httpStatus = rsData.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
            // body, header, httpStatus
            return new ResponseEntity<>(rsData, headers, httpStatus);
        }
    }

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

        // 날짜 문자열 -> 해당 패턴의 LocalDateTime 변환
        public static LocalDateTime parse(String pattern, String dateText) {
            return LocalDateTime.parse(dateText, DateTimeFormatter.ofPattern(pattern));
        }

        // 날짜 문자열 -> 디폴트 패턴의 LocalDateTime 변환
        public static LocalDateTime parse(String dateText) {
            return parse("yyyy-MM-dd HH:mm:ss.SSSSSS", dateText);
        }

        // 해당 패턴의 LocalDateTime -> 날짜 문자열 변환
        public static String format(String pattern, LocalDateTime datetime) {
            return datetime.format(DateTimeFormatter.ofPattern(pattern));
        }

        // 디폴트 패턴의 LocalDateTime -> 날짜 문자열 변환
        public static String format(LocalDateTime datetime) {
            return format("yyyy-MM-dd HH:mm:ss.SSSSSS", datetime);
        }
    }
}
