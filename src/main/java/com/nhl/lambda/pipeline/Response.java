package com.nhl.lambda.pipeline;

import com.amazonaws.util.json.Jackson;

public enum Response {
    PASS("1000", "Pipeline activated."), FAIL("1001", "Pipeline activation failed."),
    NOT_FOUND("1002", "Pipeline not found.");
    String id;
    String message;

    Response(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return Jackson.toJsonPrettyString(this);
    }
}
