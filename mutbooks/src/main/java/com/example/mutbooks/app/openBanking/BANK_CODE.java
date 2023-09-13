package com.example.mutbooks.app.openBanking;

public enum BANK_CODE {
    KDBBANK("002", "KDB산업은행"), IBK_BC("003", "IBK기업은행");

    private final String bankCode;
    private final String bankName;

    BANK_CODE(String bankCode, String bankName) {
        this.bankCode = bankCode;
        this.bankName = bankName;
    }
}
