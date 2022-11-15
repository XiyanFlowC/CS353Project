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
     * @return A new {@code Result}.
     */
    public static Result succ (Object object) {
        return new Result(0, "Operation success", object);
    }

    /**
     * Create a new failed message with -1 as it's error code and a specified description.
     * Note the object will be left as null.
     * 
     * @param message The message needs to pass to frontend.
     * @return New {@code Result}.
     */
    public static Result fail (String message) {
        return new Result(-1, message, null);
    }
}
