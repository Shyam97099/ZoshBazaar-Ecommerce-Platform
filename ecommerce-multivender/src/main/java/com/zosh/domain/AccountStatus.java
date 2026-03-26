package com.zosh.domain;

public enum AccountStatus {

    // The account has been created but not yet verified (e.g., email or mobile verification pending)
    PENDING_VERIFICATION,

    // The account is active and the user can fully access all features
    ACTIVE,

    // The account is temporarily suspended, usually due to policy violation or suspicious activity
    SUSPENDED,

    // The account has been voluntarily deactivated by the user and can be reactivated later
    DEACTIVATED,

    // The account is permanently banned due to severe violations (cannot be restored)
    BANNED,

    // The account is permanently closed by the user or administrator (all data may be removed)
    CLOSED
}

