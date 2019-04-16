package io.pivotal.pal.tracker;

import java.util.*;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long, TimeEntry> timeEntryMap = new HashMap<>();
    private Long index = 1L;

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(index);
        timeEntryMap.put(index, timeEntry);
        index += 1;
        return timeEntry;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return timeEntryMap.get(timeEntryId);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(timeEntryMap.values());
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        if (!timeEntryMap.containsKey(id)) {
            return null;
        }
        timeEntry.setId(id);
        timeEntryMap.put(id, timeEntry);
        return timeEntry;
    }

    @Override
    public void delete(long timeEntryId) {
        timeEntryMap.remove(timeEntryId);
    }


}
