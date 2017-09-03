package com.simaskuprelis.kag_androidapp.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.text.Collator;
import java.util.Calendar;
import java.util.Locale;

public class Node extends NodeListItem implements Parcelable, Comparable<Node> {
    public static final int TEACHER = 1;
    public static final int ROOM = 2;
    public static final int STUDENT = 3;

    private String id;
    private int cat;
    private String name;

    @SuppressWarnings("unused")
    public Node() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCat() {
        return cat;
    }

    public void setCat(int cat) {
        this.cat = cat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassId() {
        if (id.length() < 3) return null;

        int index;
        try {
            index = Integer.parseInt(id.substring(0, 2));
        } catch (Exception e) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        if (cal.get(Calendar.MONTH) < Calendar.SEPTEMBER) year--;

        char letter = Character.toUpperCase(id.charAt(2));
        if (!Character.isLetter(letter)) return null;

        String number = Integer.toString(year - 1996 - (index - 1));  // pirmoji laida 1997

        return number + letter;
    }

    @Override
    public int getType() {
        switch (cat) {
            case TEACHER: return NodeListItem.TYPE_TEACHER;
            case ROOM: return NodeListItem.TYPE_ROOM;
            case STUDENT: return NodeListItem.TYPE_STUDENT;
            default: return -1;
        }
    }

    @Override
    public int compareTo(@NonNull Node o) {
        if (getCat() != o.getCat()) return getCat() - o.getCat();
        Collator collator = Collator.getInstance(new Locale("lt","LT"));
        return collator.compare(getName(), o.getName());
    }

    protected Node(Parcel in) {
        id = in.readString();
        cat = in.readInt();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(cat);
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Node> CREATOR = new Parcelable.Creator<Node>() {
        @Override
        public Node createFromParcel(Parcel in) {
            return new Node(in);
        }

        @Override
        public Node[] newArray(int size) {
            return new Node[size];
        }
    };
}
