package com.olekhv.job.search.company;

public enum JobType {
    FULL_TIME("Full time"),
    CONTRACT("Contract"),
    INTERNSHIP("Internship"),
    OTHER("Other");

    private String name;

    JobType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
