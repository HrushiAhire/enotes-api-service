package com.enotes.enums;

import java.util.Map;
import java.util.HashMap;

public class TodoStatusConstants {
    public static final int NOT_STARTED = 1;
    public static final int IN_PROGRESS = 2;
    public static final int COMPLETED = 3;

    public static final Map<Integer, String> STATUS_MAP = new HashMap<>() {{
        put(NOT_STARTED, "Not Started");
        put(IN_PROGRESS, "In Progress");
        put(COMPLETED, "Completed");
    }};

    public static String getStatusName(int id) {
        return STATUS_MAP.getOrDefault(id, "Unknown Status");
    }
}
