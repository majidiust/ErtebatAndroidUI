package org.ertebat.ui;

import org.ertebat.R;

public enum ContactStatus {
    Offline(R.drawable.ic_contact_offline),
    Idle(R.drawable.ic_contact_idle),
    Online(R.drawable.ic_contact_online);

    private int mIconResource;

    private ContactStatus(int resource) {
        mIconResource = resource;
    }

    public int getIcon() {
        return mIconResource;
    }
}
