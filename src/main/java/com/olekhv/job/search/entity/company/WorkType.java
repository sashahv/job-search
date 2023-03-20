package com.olekhv.job.search.entity.company;

public enum WorkType {
    ON_SITE("On-site"),
    REMOTE("Remote"),
    HYBRID("Hybrid");

    private String name;

    WorkType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
