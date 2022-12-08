package com.example.mutbooks.util;

import com.example.mutbooks.app.AppConfig;
import com.example.mutbooks.app.base.dto.RsData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Ut {
    private static final int TEMP_PASSWORD_LENGTH = 10;
    private static final int EMAIL_TOKEN_LENGTH = 20;

    // jwt 관련
    private static ObjectMapper getObjectMapper() {
        return (ObjectMapper) AppConfig.getContext().getBean("objectMapper");
    }

    public static class json {

        // map(json) -> String 변환
        public static Object toStr(Map<String, Object> map) {
            try {
                return getObjectMapper().writeValueAsString(map);
            } catch (JsonProcessingException e) {
                return null;
            }
        }

        // String -> map(json) 변환
        public static Map<String, Object> toMap(String jsonStr) {
            try {
                return getObjectMapper().readValue(jsonStr, LinkedHashMap.class);
            } catch (JsonProcessingException e) {
                return null;
            }
        }
    }
    // 추가 끝


    // 인자 값들을 map 형태로 반환
    public static<K, V> Map<K, V> mapOf(Object... args) {
        Map<K, V> map = new LinkedHashMap<>();

        int size = args.length / 2;

        for (int i = 0; i < size; i++) {
            int keyIndex = i * 2;
            int valueIndex = keyIndex + 1;

            K key = (K) args[keyIndex];
            V value = (V) args[valueIndex];

            map.put(key, value);
        }
        return map;
    }

    public static class spring {
        public static <T> ResponseEntity<RsData> responseEntityOf(RsData<T> rsData) {
            return responseEntityOf(rsData, null);
        }

        public static <T> ResponseEntity<RsData> responseEntityOf(RsData<T> rsData, HttpHeaders headers) {
            HttpStatus httpStatus = rsData.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
            // body, header, httpStatus
            return new ResponseEntity<>(rsData, headers, httpStatus);
        }

        // 들어온 인자를 (key, value) 형태의 HttpHeaders 로 반환
        public static HttpHeaders httpHeadersOf(String... args) {
            HttpHeaders headers = new HttpHeaders();

            // (key, value)
            Map<String, String> map = Ut.mapOf(args);

            for(String key : map.keySet()) {
                String value = map.get(key);
                headers.set(key, value);
            }

            return headers;
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

    // 랜덤 문자열 생성
    public static String genRandomUUID(int length) {
        return UUID.randomUUID().toString()
                .replace("-", "").substring(0, length);
    }

    // 임시 비밀번호 발급
    public static String genTempPassword() {
        return genRandomUUID(TEMP_PASSWORD_LENGTH);
    }

    // 이메일 인증키 발급
    public static String genEmailToken() {
        return genRandomUUID(EMAIL_TOKEN_LENGTH);
    }
}
