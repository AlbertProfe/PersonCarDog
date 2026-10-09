package org.example.utils;

public class resultUpdateOperation {

    private String type;
    private long timestamp;
    private boolean success;
    private String message;
    private Object updatedObject;

    public resultUpdateOperation(String type, long timestamp, boolean success, String message, Object updatedObject) {
        this.type = type;
        this.timestamp = timestamp;
        this.success = success;
        this.message = message;
        this.updatedObject = updatedObject;
    }

    public resultUpdateOperation(){}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getUpdatedObject() {
        return updatedObject;
    }

    public void setUpdatedObject(Object updatedObject) {
        this.updatedObject = updatedObject;
    }

    @Override
    public String toString() {
        return "resultUpdateOperation{" +
                "type='" + type + '\'' +
                ", timestamp=" + timestamp +
                ", success=" + success +
                ", message='" + message + '\'' +
                ", updatedObject=" + updatedObject +
                '}';
    }
}
