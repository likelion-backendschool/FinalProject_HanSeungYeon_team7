package com.example.mutbooks.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UtTest {
    @Test
    @DisplayName("Ut.mapOf() 인자값을 map 으로 변환")
     void t1() {
        Map<String, Integer> ages = Ut.mapOf("영수", 22, "철수", 33, "영희", 44, "민수", 55);

        assertThat(ages.get("영수")).isEqualTo(22);
        assertThat(ages.get("철수")).isEqualTo(33);
        assertThat(ages.get("영희")).isEqualTo(44);
        assertThat(ages.get("민수")).isEqualTo(55);
    }
}