package org.example.utils;

public class ResultOperation {

    private String type;
    private long timestamp;
    private boolean success;
    private String message;
    private Object updatedObject;

    public ResultOperation(String type, long timestamp, boolean success, String message, Object updatedObject) {
        this.type = type;
        this.timestamp = timestamp;
        this.success = success;
        this.message = message;
        this.updatedObject = updatedObject;
    }

    public ResultOperation(){}


     // Convenience setter to set all fields at once.
    public ResultOperation setter(boolean success, String message, String type, long timestamp, Object updatedObject) {
        this.success = success;
        this.message = message;
        this.type = type;
        this.timestamp = timestamp;
        this.updatedObject = updatedObject;

        return this;
    }


     // Convenience setter to set all fields at once (updatedObject defaults to null).
    public ResultOperation setter(boolean success, String message, String type, long timestamp) {
        setter(success, message, type, timestamp, null);
        return this;
    }

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
