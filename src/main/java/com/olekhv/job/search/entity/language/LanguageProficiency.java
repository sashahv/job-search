package com.olekhv.job.search.entity.language;

public enum LanguageProficiency {
    ELEMENTARY("Elementary"),
    LIMITED_WORKING("Limited working"),
    PROFESSIONAL_WORKING("Professional working"),
    FULL_PROFESSIONAL("Full professional"),
    NATIVE("Native");

    private String name;

    LanguageProficiency(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}