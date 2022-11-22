package org.cstp.meowtool.utils;

import lombok.Data;

@Data
public class Result {
    private final Integer error;
    private final String desc;
    private final Object data;

    /**
     * Create a success message with specified description and error code.
     * 
     * @param error
     * @param desc
     * @return A new {@code Result}.
     */
    public static Result succ (Integer error, String desc) {
        return new Result(error, desc, null);
    }

    /**
     * Create a success message with specified object.
     * 
     * @param object The object needs to pass to frontend.
     * @return A new {@code Result} object.
     */
    public static Result succ (Object object) {
        return new Result(0, "Operation success", object);
    }

    /**
     * Create a new failed message with -1 as it's error code and a specified description.
     * Note the object will be left as null.
     * 
     * @param message The message needs to pass to frontend.
     * @return New {@code Result} object.
     */
    public static Result fail (String message) {
        return new Result(-1, message, null);
    }

    /**
     * Create a new failed message to announce the client some error occured. Note this
     * method allow you to set the error code. Set it reasonable. (with null data field)
     * 
     * @param error The error code to mark the error type.
     * @param message The message to describe the error. Leave it empty if it is a secret.
     * @return New {@code Result} object.
     */
    public static Result fail (Integer error, String message) {
        return new Result(error, message, null);
    }
}
