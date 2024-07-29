package com.example.finalproj;

public class Commit {
    private final String commit;
    private final String author;
    private final String date;
    private final String message;

    public Commit(String commit, String author, String date, String message) {
        this.commit = commit;
        this.author = author;
        this.date = date;
        this.message = message;
    }

    public String getCommit() { return commit; }
    public String getAuthor() { return author; }
    public String getDate() { return date; }
    public String getMessage() { return message; }
}
