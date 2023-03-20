package com.olekhv.job.search.entity.application;

public enum AttachmentCategory {
    CV("CV"),
    COVER_LETTER("Cover Letter"),
    CERTIFICATE("Certificate"),
    OTHER("Other");

    private String name;

    AttachmentCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
