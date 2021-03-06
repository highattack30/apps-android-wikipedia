package org.wikipedia.history;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.wikipedia.page.PageTitle;

import java.util.Date;

public class HistoryEntry implements Parcelable {
    public static final HistoryEntryDatabaseTable DATABASE_TABLE = new HistoryEntryDatabaseTable();

    public static final int SOURCE_SEARCH = 1;
    public static final int SOURCE_INTERNAL_LINK = 2;
    public static final int SOURCE_EXTERNAL_LINK = 3;
    public static final int SOURCE_HISTORY = 4;
    public static final int SOURCE_SAVED_PAGE = 5;
    public static final int SOURCE_LANGUAGE_LINK = 6;
    public static final int SOURCE_RANDOM = 7;
    public static final int SOURCE_MAIN_PAGE = 8;
    public static final int SOURCE_NEARBY = 9;
    public static final int SOURCE_DISAMBIG = 10;
    public static final int SOURCE_READING_LIST = 11;
    public static final int SOURCE_FEED_CONTINUE_READING = 12;
    public static final int SOURCE_FEED_BECAUSE_YOU_READ = 13;
    public static final int SOURCE_FEED_MOST_READ = 14;
    public static final int SOURCE_FEED_FEATURED = 15;
    public static final int SOURCE_NEWS = 16;
    public static final int SOURCE_FEED_MAIN_PAGE = 17;
    public static final int SOURCE_FEED_RANDOM = 18;
    public static final int SOURCE_GALLERY = 19; // Or should we just use SOURCE_INTERNAL_LINK as before?  Some of these things seem not like the others.
    public static final int SOURCE_APP_SHORTCUT_RANDOM = 20;
    public static final int SOURCE_APP_SHORTCUT_CONTINUE_READING = 21;

    @NonNull private final PageTitle title;
    @NonNull private final Date timestamp;
    private final int source;
    private final int timeSpentSec;

    public HistoryEntry(@NonNull PageTitle title, @NonNull Date timestamp, int source,
                        int timeSpentSec) {
        this.title = title;
        this.timestamp = timestamp;
        this.source = source;
        this.timeSpentSec = timeSpentSec;
    }

    public HistoryEntry(@NonNull PageTitle title, @NonNull Date timestamp, int source) {
        this(title, timestamp, source, 0);
    }

    public HistoryEntry(@NonNull PageTitle title, int source) {
        this(title, new Date(), source);
    }

    @NonNull public PageTitle getTitle() {
        return title;
    }

    @NonNull public Date getTimestamp() {
        return timestamp;
    }

    public int getSource() {
        return source;
    }

    public int getTimeSpentSec() {
        return timeSpentSec;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HistoryEntry)) {
            return false;
        }
        HistoryEntry other = (HistoryEntry) o;
        return title.equals(other.title)
                && timestamp.equals(other.timestamp)
                && source == other.source;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + source;
        result = 31 * result + timestamp.hashCode();
        result = 31 * result + timeSpentSec;
        return result;
    }

    @Override
    public String toString() {
        return "HistoryEntry{"
                + "title=" + title
                + ", source=" + source
                + ", timestamp=" + timestamp.getTime()
                + ", timeSpentSec=" + timeSpentSec
                + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(getTitle(), flags);
        dest.writeLong(getTimestamp().getTime());
        dest.writeInt(getSource());
        dest.writeInt(getTimeSpentSec());
    }

    private HistoryEntry(Parcel in) {
        this.title = in.readParcelable(PageTitle.class.getClassLoader());
        this.timestamp = new Date(in.readLong());
        this.source = in.readInt();
        this.timeSpentSec = in.readInt();
    }

    public static final Parcelable.Creator<HistoryEntry> CREATOR
            = new Parcelable.Creator<HistoryEntry>() {
        @Override
        public HistoryEntry createFromParcel(Parcel in) {
            return new HistoryEntry(in);
        }

        @Override
        public HistoryEntry[] newArray(int size) {
            return new HistoryEntry[size];
        }
    };
}
